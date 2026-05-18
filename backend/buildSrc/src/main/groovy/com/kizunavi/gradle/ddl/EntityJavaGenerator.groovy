package com.kizunavi.gradle.ddl

class EntityJavaGenerator {

    private final String javaPackage
    private final Map<String, String> entityByTable

    EntityJavaGenerator(String javaPackage, List<TableDef> tables) {
        this.javaPackage = javaPackage
        this.entityByTable = tables.collectEntries {
            [(it.name): NamingUtils.entityClassName(it.name)]
        }
    }

    void generateAll(List<TableDef> tables, File outputDir) {
        outputDir.mkdirs()
        tables.each { table ->
            def entityName = entityByTable[table.name]
            def file = new File(outputDir, "${entityName}.java")
            file.text = generateEntity(table, entityName)
        }
    }

    private String generateEntity(TableDef table, String entityName) {
        def fkColumns = table.foreignKeys.collect { it.column } as Set
        def compositePk = table.primaryKeyColumns.size() > 1
        def embeddableName = compositePk ? NamingUtils.embeddableIdClassName(entityName) : null

        def imports = new LinkedHashSet<String>()
        imports << 'jakarta.persistence.*'
        imports << 'lombok.*'

        def fields = [] as List<String>

        if (compositePk) {
            imports << 'java.io.Serializable'
            fields << generateEmbeddableId(embeddableName, table)
            fields << ''
            fields << "    @EmbeddedId"
            fields << "    private ${embeddableName} id;"
            fields << ''
        }

        table.foreignKeys.each { fk ->
            def refEntity = entityByTable[fk.refTable]
            def fieldName = NamingUtils.fkFieldName(fk, refEntity)
            def optional = table.columns.find { it.name == fk.column }?.nullable ?: false
            def nullableAttr = optional ? '' : ', optional = false'
            def columnNullable = optional ? '' : ', nullable = false'

            fields << ''
            fields << fieldJavadoc(table, fk.column)
            fields << "    @ManyToOne(fetch = FetchType.LAZY${nullableAttr})"
            fields << "    @JoinColumn(name = \"${fk.column}\"${columnNullable})"
            fields << "    private ${refEntity} ${fieldName};"
        }

        table.columns.each { col ->
            if (compositePk && table.primaryKeyColumns.contains(col.name)) {
                return
            }
            if (fkColumns.contains(col.name)) {
                return
            }

            def fieldBlock = generateScalarField(table, col)
            if (fieldBlock != null) {
                fields << ''
                fields.addAll(fieldBlock)
            }
        }

        collectImports(table, imports, compositePk)

        def tableAnnotation = generateTableAnnotation(table)
        def body = fields.join('\n')

        def classDoc = formatClassJavadoc(table.tableComment)

        return """package ${javaPackage};

${imports.collect { "import ${it};" }.sort().join('\n')}

${classDoc}
@Entity
${tableAnnotation}
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ${entityName} {

${body}
}
"""
    }

    private static String formatClassJavadoc(String comment) {
        def desc = comment ? escapeJavadoc(comment) : 'DDL から自動生成された JPA エンティティ'
        return """/**
 * ${desc}
 *
 * <p>このファイルは {@code kizunavi_ddl.sql} から自動生成されています。手編集しないでください。
 * 再生成: {@code ./gradlew generateEntities copyGeneratedEntities}</p>
 */"""
    }

    private static String fieldJavadoc(TableDef table, String columnName) {
        def comment = table.columnComments[columnName]
        if (!comment) {
            return ''
        }
        return "    /** ${escapeJavadoc(comment)} */"
    }

    private static String escapeJavadoc(String text) {
        return text.replace('*/', '* /')
    }

    private String generateEmbeddableId(String className, TableDef table) {
        def idFields = table.primaryKeyColumns.collect { pkCol ->
            def col = table.columns.find { it.name == pkCol }
            def javaType = javaTypeForColumn(table.name, col)
            def fieldName = NamingUtils.fieldName(pkCol)
            def annotations = columnAnnotations(table, col, true)
            return """        ${annotations.join('\n        ')}
        private ${javaType} ${fieldName};"""
        }.join('\n\n')

        return """    @Embeddable
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class ${className} implements Serializable {

${idFields}
    }"""
    }

    private List<String> generateScalarField(TableDef table, ColumnDef col) {
        def lines = [] as List<String>
        def javadoc = fieldJavadoc(table, col.name)
        if (javadoc) {
            lines << javadoc
        }

        def isPk = table.primaryKeyColumns.size() == 1 && table.primaryKeyColumns[0] == col.name
        if (isPk) {
            if (col.identity) {
                lines << '    @Id'
                lines << '    @GeneratedValue(strategy = GenerationType.IDENTITY)'
            } else {
                lines << '    @Id'
            }
        }

        lines.addAll(columnAnnotations(table, col, isPk))

        def javaType = javaTypeForColumn(table.name, col)
        def fieldName = javaFieldName(table.name, col.name)
        def defaultSuffix = defaultValueSuffix(table.name, col)

        lines << "    private ${javaType} ${fieldName}${defaultSuffix};"
        return lines
    }

    private static List<String> columnAnnotations(TableDef table, ColumnDef col, boolean isPk) {
        def attrs = [] as List<String>
        attrs << "name = \"${col.name}\""
        if (!col.nullable && !isPk) {
            attrs << 'nullable = false'
        }
        if (col.varcharLength != null && col.oracleType in ['VARCHAR2', 'CHAR']) {
            attrs << "length = ${col.varcharLength}"
        }
        if (col.oracleType == 'CLOB') {
            return ['    @Lob', "    @Column(${attrs.join(', ')})"]
        }
        if (col.oracleType == 'CHAR') {
            return [
                '    @JdbcTypeCode(SqlTypes.CHAR)',
                "    @Column(${attrs.join(', ')})"
            ]
        }
        if (col.name == 'created_at' && col.oracleType == 'TIMESTAMP' && hasUpdatedAtColumn(table)) {
            def lines = ['    @CreationTimestamp', '    @Column(name = "created_at", nullable = false, updatable = false)']
            return lines
        }
        if (col.name == 'updated_at' && col.oracleType == 'TIMESTAMP' && hasUpdatedAtColumn(table)) {
            def lines = ['    @UpdateTimestamp', '    @Column(name = "updated_at", nullable = false)']
            return lines
        }
        if (isRoleColumn(table.name, col.name)) {
            return [
                '    @Enumerated(EnumType.STRING)',
                "    @Column(${attrs.join(', ')})"
            ]
        }
        return ["    @Column(${attrs.join(', ')})"]
    }

    private static boolean hasUpdatedAtColumn(TableDef table) {
        table.columns.any { it.name == 'updated_at' && it.oracleType == 'TIMESTAMP' }
    }

    private static boolean isRoleColumn(String tableName, String columnName) {
        tableName == 'USERS' && columnName == 'role'
    }

    private static String defaultValueSuffix(String tableName, ColumnDef col) {
        if (col.name == 'del_flg') {
            return ''
        }
        if (tableName == 'USERS' && col.name == 'failed_login_count') {
            return ''
        }
        return ''
    }

    private String javaTypeForColumn(String tableName, ColumnDef col) {
        if (isRoleColumn(tableName, col.name)) {
            return 'Role'
        }
        if (isBooleanNumberColumn(tableName, col)) {
            return 'boolean'
        }
        switch (col.oracleType) {
            case 'VARCHAR2':
            case 'CHAR':
            case 'CLOB':
                return 'String'
            case 'DATE':
                return 'LocalDate'
            case 'TIMESTAMP':
                return 'LocalDateTime'
            case 'NUMBER':
                if (col.numberScale != null && col.numberScale > 0) {
                    return 'BigDecimal'
                }
                if (col.numberPrecision != null && col.numberPrecision >= 19) {
                    return 'Long'
                }
                return 'Integer'
            default:
                return 'String'
        }
    }

    private static boolean isBooleanNumberColumn(String tableName, ColumnDef col) {
        if (col.oracleType != 'NUMBER' || col.numberPrecision != 1) {
            return false
        }
        return col.name in ['enabled', 'succeeded']
    }

    private static String javaFieldName(String tableName, String columnName) {
        if (tableName == 'DASHBOARD_SUMMARIES' && columnName == 'condition') {
            return 'conditionCode'
        }
        return NamingUtils.fieldName(columnName)
    }

    private void collectImports(TableDef table, Set<String> imports, boolean compositePk) {
        def needsBigDecimal = table.columns.any { col ->
            col.oracleType == 'NUMBER' && col.numberScale != null && col.numberScale > 0
        }
        def needsLocalDate = table.columns.any { it.oracleType == 'DATE' }
        def needsLocalDateTime = table.columns.any { it.oracleType == 'TIMESTAMP' }
        def needsRole = table.name == 'USERS'
        def hasCreatedAt = table.columns.any { it.name == 'created_at' && it.oracleType == 'TIMESTAMP' }
        def hasUpdatedAt = hasUpdatedAtColumn(table)
        def needsCreationTimestamp = hasCreatedAt && hasUpdatedAt
        def needsUpdateTimestamp = hasUpdatedAt
        def needsJdbcTypeCode = table.columns.any { it.oracleType == 'CHAR' }

        if (needsBigDecimal) {
            imports << 'java.math.BigDecimal'
        }
        if (needsLocalDate) {
            imports << 'java.time.LocalDate'
        }
        if (needsLocalDateTime) {
            imports << 'java.time.LocalDateTime'
        }
        if (needsRole) {
            imports << 'com.kizunavi.dto.Role'
        }
        if (needsCreationTimestamp) {
            imports << 'org.hibernate.annotations.CreationTimestamp'
        }
        if (needsUpdateTimestamp) {
            imports << 'org.hibernate.annotations.UpdateTimestamp'
        }
        if (needsJdbcTypeCode) {
            imports << 'org.hibernate.annotations.JdbcTypeCode'
            imports << 'org.hibernate.type.SqlTypes'
        }
    }

    private static String generateTableAnnotation(TableDef table) {
        def tableName = NamingUtils.tableNameForJpa(table.name)
        def parts = [] as List<String>
        parts << "name = \"${tableName}\""

        if (!table.indexes.isEmpty()) {
            def indexEntries = table.indexes.collect { idx ->
                def cols = idx.columns.collect { "\"${it}\"" }.join(', ')
                return "@Index(name = \"${idx.name}\", columnList = \"${idx.columns.join(', ')}\")"
            }
            parts << "indexes = { ${indexEntries.join(', ')} }"
        }

        if (!table.uniqueConstraints.isEmpty()) {
            def ukEntries = table.uniqueConstraints.collect { cols ->
                def ukName = "uk_${table.name.toLowerCase()}_${cols.join('_')}"
                return "@UniqueConstraint(name = \"${ukName}\", columnNames = { ${cols.collect { "\"${it}\"" }.join(', ')} })"
            }
            if (ukEntries.size() == 1) {
                parts << "uniqueConstraints = ${ukEntries[0]}"
            } else {
                parts << "uniqueConstraints = { ${ukEntries.join(', ')} }"
            }
        }

        return "@Table(${parts.join(', ')})"
    }
}
