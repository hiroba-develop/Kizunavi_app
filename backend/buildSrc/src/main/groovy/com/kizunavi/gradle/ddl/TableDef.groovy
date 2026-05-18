package com.kizunavi.gradle.ddl

class TableDef {
    String name
    List<ColumnDef> columns = []
    List<String> primaryKeyColumns = []
    List<ForeignKeyDef> foreignKeys = []
    List<List<String>> uniqueConstraints = []
    List<IndexDef> indexes = []
    String tableComment
    Map<String, String> columnComments = [:]
}
