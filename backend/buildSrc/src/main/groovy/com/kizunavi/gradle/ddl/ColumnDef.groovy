package com.kizunavi.gradle.ddl

class ColumnDef {
    String name
    String oracleType
    boolean nullable = true
    boolean identity = false
    Integer varcharLength
    Integer numberPrecision
    Integer numberScale
}
