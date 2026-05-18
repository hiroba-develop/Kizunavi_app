package com.kizunavi.gradle.ddl

import java.util.regex.Matcher
import java.util.regex.Pattern

class DdlParser {

    private static final Pattern CREATE_TABLE = ~/(?i)CREATE\s+TABLE\s+(\w+)\s*\(/
    private static final Pattern TABLE_END = ~/(?i)^\s*\)\s*TABLESPACE/
    private static final Pattern PK = ~/(?i)PRIMARY\s+KEY\s*\(([^)]+)\)/
    private static final Pattern FK = ~/(?i)FOREIGN\s+KEY\s*\((\w+)\)\s+REFERENCES\s+(\w+)\s*\((\w+)\)(?:\s+ON\s+DELETE\s+(\w+))?/
    private static final Pattern UK = ~/(?i)UNIQUE\s*\(([^)]+)\)/
    private static final Pattern CREATE_INDEX = ~/(?i)CREATE\s+INDEX\s+(\w+)\s+ON\s+(\w+)\s*\(([^)]+)\)/
    private static final Pattern COMMENT_TABLE = ~/(?i)COMMENT\s+ON\s+TABLE\s+(\w+)\s+IS\s+'((?:[^']|'')*)'/
    private static final Pattern COMMENT_COLUMN = ~/(?i)COMMENT\s+ON\s+COLUMN\s+(\w+)\.(\w+)\s+IS\s+'((?:[^']|'')*)'/

    List<TableDef> parse(File ddlFile) {
        def lines = ddlFile.readLines('UTF-8')
        def tables = [] as List<TableDef>
        def indexes = [] as List<IndexDef>

        TableDef current = null
        for (def rawLine in lines) {
            def line = rawLine.trim()
            if (line.isEmpty() || line.startsWith('--')) {
                continue
            }

            def createMatcher = CREATE_TABLE.matcher(line)
            if (createMatcher.find()) {
                current = new TableDef(name: createMatcher.group(1).toUpperCase())
                tables << current
                continue
            }

            if (current != null) {
                if (TABLE_END.matcher(line).find()) {
                    current = null
                    continue
                }
                parseTableBodyLine(current, line)
            }

            def indexMatcher = CREATE_INDEX.matcher(line)
            if (indexMatcher.find()) {
                def idx = new IndexDef(
                    name: indexMatcher.group(1).toLowerCase(),
                    table: indexMatcher.group(2).toUpperCase()
                )
                idx.columns = indexMatcher.group(3).split(',').collect { it.trim().toLowerCase() }
                indexes << idx
            }
        }

        attachIndexes(tables, indexes)
        attachComments(tables, lines)
        return tables
    }

    private static void parseTableBodyLine(TableDef table, String line) {
        if (parseConstraintLine(table, line)) {
            return
        }

        if (line.startsWith('COMMENT') || line.startsWith('CREATE ')) {
            return
        }

        def column = parseColumn(line)
        if (column != null) {
            table.columns << column
        }
    }

    private static boolean parseConstraintLine(TableDef table, String line) {
        def pkMatcher = PK.matcher(line)
        if (pkMatcher.find()) {
            table.primaryKeyColumns = splitColumns(pkMatcher.group(1))
            return true
        }
        def fkMatcher = FK.matcher(line)
        if (fkMatcher.find()) {
            table.foreignKeys << new ForeignKeyDef(
                column: fkMatcher.group(1).toLowerCase(),
                refTable: fkMatcher.group(2).toUpperCase(),
                refColumn: fkMatcher.group(3).toLowerCase(),
                onDelete: fkMatcher.group(4)?.toUpperCase()
            )
            return true
        }
        def ukMatcher = UK.matcher(line)
        if (ukMatcher.find()) {
            table.uniqueConstraints << splitColumns(ukMatcher.group(1))
            return true
        }
        return false
    }

    private static ColumnDef parseColumn(String line) {
        def cleaned = line.replaceAll(/,\s*$/, '').trim()
        if (!cleaned || cleaned.startsWith('CONSTRAINT')) {
            return null
        }

        def parts = cleaned.split(/\s+/, 2)
        if (parts.length < 2) {
            return null
        }

        def col = new ColumnDef(name: parts[0].toLowerCase())
        def rest = parts[1].toUpperCase()

        if (rest.contains('GENERATED ALWAYS AS IDENTITY')) {
            col.identity = true
        }
        col.nullable = !rest.contains('NOT NULL')

        if (rest.startsWith('VARCHAR2')) {
            col.oracleType = 'VARCHAR2'
            col.varcharLength = extractParenInt(rest, 'VARCHAR2')
        } else if (rest.startsWith('CHAR')) {
            col.oracleType = 'CHAR'
            col.varcharLength = extractParenInt(rest, 'CHAR')
        } else if (rest.startsWith('NUMBER')) {
            col.oracleType = 'NUMBER'
            def m = (rest =~ /NUMBER\s*\(\s*(\d+)\s*(?:,\s*(\d+)\s*)?\)/)
            if (m.find()) {
                col.numberPrecision = m.group(1) as Integer
                if (m.group(2) != null) {
                    col.numberScale = m.group(2) as Integer
                }
            }
        } else if (rest.startsWith('TIMESTAMP')) {
            col.oracleType = 'TIMESTAMP'
        } else if (rest.startsWith('DATE')) {
            col.oracleType = 'DATE'
        } else if (rest.startsWith('CLOB')) {
            col.oracleType = 'CLOB'
        } else {
            return null
        }

        return col
    }

    private static Integer extractParenInt(String text, String typeName) {
        def m = (text =~ /${typeName}\s*\(\s*(\d+)\s*\)/)
        return m.find() ? (m.group(1) as Integer) : null
    }

    private static List<String> splitColumns(String csv) {
        csv.split(',').collect { it.trim().toLowerCase() }
    }

    private static void attachIndexes(List<TableDef> tables, List<IndexDef> indexes) {
        def byName = tables.collectEntries { [(it.name): it] }
        indexes.each { idx ->
            def table = byName[idx.table]
            if (table != null) {
                table.indexes << idx
            }
        }
    }

    private static void attachComments(List<TableDef> tables, List<String> lines) {
        def byName = tables.collectEntries { [(it.name): it] }
        lines.each { raw ->
            def line = raw.trim()
            def tableMatcher = COMMENT_TABLE.matcher(line)
            if (tableMatcher.find()) {
                def table = byName[tableMatcher.group(1).toUpperCase()]
                if (table != null) {
                    table.tableComment = unescapeComment(tableMatcher.group(2))
                }
                return
            }
            def columnMatcher = COMMENT_COLUMN.matcher(line)
            if (columnMatcher.find()) {
                def table = byName[columnMatcher.group(1).toUpperCase()]
                if (table != null) {
                    table.columnComments[columnMatcher.group(2).toLowerCase()] =
                        unescapeComment(columnMatcher.group(3))
                }
            }
        }
    }

    private static String unescapeComment(String value) {
        value.replace("''", "'")
    }
}
