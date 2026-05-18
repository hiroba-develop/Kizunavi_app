package com.kizunavi.gradle.ddl

class NamingUtils {

    private static final Map<String, String> ENTITY_NAME_OVERRIDES = [
        'USERS'                  : 'User',
        'CUSTOMERS'              : 'Customer',
        'DIVISIONS'              : 'Division',
        'SECTIONS'               : 'Section',
        'EMPLOYEES'              : 'Employee',
        'REFRESH_TOKENS'         : 'RefreshToken',
        'LOGIN_ATTEMPTS'         : 'LoginAttempt',
        'PASSWORD_RESET_TOKENS'  : 'PasswordResetToken',
        'ALERT_RULES'            : 'AlertRule',
        'ROLE_LABEL_SETTINGS'    : 'RoleLabelSettings',
        'SURVEY_DETAILS'         : 'SurveyDetail',
        'SURVEY_MAIL_LOGS'       : 'SurveyMailLog',
        'SURVEY_QUESTIONS'       : 'SurveyQuestion',
        'SURVEY_ANSWER_SESSIONS' : 'SurveyAnswerSession',
        'SURVEY_ANSWER_DETAILS'  : 'SurveyAnswerDetail',
        'DASHBOARD_SUMMARIES'    : 'DashboardSummary',
        'VALUE_DISTRIBUTIONS'    : 'ValueDistribution',
    ]

    static String entityClassName(String tableName) {
        def upper = tableName.toUpperCase()
        if (ENTITY_NAME_OVERRIDES.containsKey(upper)) {
            return ENTITY_NAME_OVERRIDES[upper]
        }
        return toPascalCase(singularize(upper))
    }

    static String embeddableIdClassName(String entityClassName) {
        return "${entityClassName}Id"
    }

    static String tableNameForJpa(String tableName) {
        return tableName.toLowerCase()
    }

    static String fieldName(String columnName) {
        def parts = columnName.toLowerCase().split('_')
        if (parts.length == 1) {
            return parts[0]
        }
        return parts[0] + parts.drop(1).collect { it.capitalize() }.join('')
    }

    static String fkFieldName(ForeignKeyDef fk, String refEntityClass) {
        def base = refEntityClass.substring(0, 1).toLowerCase() + refEntityClass.substring(1)
        if (fk.column == 'user_id') {
            return 'user'
        }
        if (fk.column == 'employee_id') {
            return 'employee'
        }
        if (fk.column == 'customer_id') {
            return 'customer'
        }
        return base
    }

    private static String singularize(String snakeUpper) {
        if (snakeUpper.endsWith('_TOKENS')) {
            return snakeUpper.substring(0, snakeUpper.length() - 1)
        }
        if (snakeUpper.endsWith('_ATTEMPTS')) {
            return snakeUpper.substring(0, snakeUpper.length() - 1)
        }
        if (snakeUpper.endsWith('_DETAILS')) {
            return snakeUpper.substring(0, snakeUpper.length() - 1)
        }
        if (snakeUpper.endsWith('_SESSIONS')) {
            return snakeUpper.substring(0, snakeUpper.length() - 1)
        }
        if (snakeUpper.endsWith('_LOGS')) {
            return snakeUpper.substring(0, snakeUpper.length() - 1)
        }
        if (snakeUpper.endsWith('_RULES')) {
            return snakeUpper.substring(0, snakeUpper.length() - 1)
        }
        if (snakeUpper.endsWith('_SETTINGS')) {
            return snakeUpper
        }
        if (snakeUpper.endsWith('_SUMMARIES')) {
            return snakeUpper.substring(0, snakeUpper.length() - 1)
        }
        if (snakeUpper.endsWith('_DISTRIBUTIONS')) {
            return snakeUpper.substring(0, snakeUpper.length() - 1)
        }
        if (snakeUpper.endsWith('S')) {
            return snakeUpper.substring(0, snakeUpper.length() - 1)
        }
        return snakeUpper
    }

    private static String toPascalCase(String snakeUpper) {
        snakeUpper.toLowerCase().split('_').collect { it.capitalize() }.join('')
    }
}
