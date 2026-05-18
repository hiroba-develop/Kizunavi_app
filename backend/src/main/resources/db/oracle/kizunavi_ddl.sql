-- =============================================================================
--  KizuNavi DB DDL (Oracle)
--  Encoding   : UTF-8 (NLS_LANG=AMERICAN_AMERICA.AL32UTF8 を推奨)
--  Tables     : 全17テーブル (SURVEY_QUESTION_MAPPINGS は今回スコープ外)
--
--  方針
--   - 認証系 (USERS / REFRESH_TOKENS / LOGIN_ATTEMPTS / PASSWORD_RESET_TOKENS)
--     のみ物理FKを定義。業務系は概念FKのみ (アプリケーションで整合性を担保)。
--   - 識別子はPL/SQLの30byte制限内に収め、Oracle 12.2+ の128byte制限にも対応。
--   - 日時カラムは TIMESTAMP(6)。デフォルトは SYSTIMESTAMP。
--   - 論理削除は CHAR(1) ('0':有効 / '1':削除)。
--   - 命名規約 : PK_<table> / FK_<table>_<ref> / UK_<table>_<col>
--                IDX_<table>_<col> / CK_<table>_<col>
--   - 表領域   : テーブル本体     -> KIZUNAVI_TABLE
--                インデックス全般 -> KIZUNAVI_INDEX
--                (PRIMARY KEY / UNIQUE 制約の裏インデックスも KIZUNAVI_INDEX)
--
--  事前準備 (DBA作業) : 本DDLを実行する前に下記の表領域を作成しておくこと
--    CREATE TABLESPACE KIZUNAVI_TABLE
--      DATAFILE '<path>' SIZE 100M AUTOEXTEND ON NEXT 50M MAXSIZE UNLIMITED;
--    CREATE TABLESPACE KIZUNAVI_INDEX
--      DATAFILE '<path>' SIZE 50M  AUTOEXTEND ON NEXT 25M MAXSIZE UNLIMITED;
--    ※ AWS RDS for Oracle の場合は DATAFILE 句を省略可
-- =============================================================================


-- =============================================================================
-- 01. CUSTOMERS  (利用企業テーブル)
-- =============================================================================
CREATE TABLE CUSTOMERS (
    customer_id          VARCHAR2(36)   NOT NULL,
    customer_name        VARCHAR2(255)  NOT NULL,
    customer_name_kana   VARCHAR2(255),
    employee_cnt         NUMBER(10),
    industry             NUMBER(2),
    postal_num           CHAR(8),
    address              VARCHAR2(255),
    tel_num              VARCHAR2(20),
    mail                 VARCHAR2(255),
    del_flg              CHAR(1)        DEFAULT '0',
    created_at           TIMESTAMP(6)   DEFAULT SYSTIMESTAMP NOT NULL,
    updated_at           TIMESTAMP(6)   DEFAULT SYSTIMESTAMP NOT NULL,
    CONSTRAINT PK_CUSTOMERS PRIMARY KEY (customer_id) USING INDEX TABLESPACE KIZUNAVI_INDEX,
    CONSTRAINT CK_CUSTOMERS_DEL_FLG CHECK (del_flg IN ('0','1'))
) TABLESPACE KIZUNAVI_TABLE;

COMMENT ON TABLE  CUSTOMERS                     IS '利用企業テーブル: テナントとなる利用企業の基本情報・契約情報';
COMMENT ON COLUMN CUSTOMERS.customer_id         IS '顧客ID (UUID, 全テーブルのテナントキー)';
COMMENT ON COLUMN CUSTOMERS.customer_name       IS '顧客名 (利用企業名)';
COMMENT ON COLUMN CUSTOMERS.customer_name_kana  IS '顧客名カナ';
COMMENT ON COLUMN CUSTOMERS.employee_cnt        IS '従業員数';
COMMENT ON COLUMN CUSTOMERS.industry            IS '業界コード (アプリ側固定コードで管理)';
COMMENT ON COLUMN CUSTOMERS.postal_num          IS '郵便番号 (ハイフン含む xxx-xxxx 形式)';
COMMENT ON COLUMN CUSTOMERS.address             IS '住所';
COMMENT ON COLUMN CUSTOMERS.tel_num             IS '電話番号';
COMMENT ON COLUMN CUSTOMERS.mail                IS '利用企業の連絡先メール';
COMMENT ON COLUMN CUSTOMERS.del_flg             IS '削除フラグ (0:有効 1:削除)';
COMMENT ON COLUMN CUSTOMERS.created_at          IS '作成日時';
COMMENT ON COLUMN CUSTOMERS.updated_at          IS '更新日時';


-- =============================================================================
-- 02. DIVISIONS  (部テーブル)
-- =============================================================================
CREATE TABLE DIVISIONS (
    division_id    VARCHAR2(36)   NOT NULL,
    customer_id    VARCHAR2(36)   NOT NULL,
    display_name   VARCHAR2(255)  NOT NULL,
    del_flg        CHAR(1)        DEFAULT '0',
    created_at     TIMESTAMP(6)   DEFAULT SYSTIMESTAMP NOT NULL,
    updated_at     TIMESTAMP(6)   DEFAULT SYSTIMESTAMP NOT NULL,
    CONSTRAINT PK_DIVISIONS PRIMARY KEY (division_id) USING INDEX TABLESPACE KIZUNAVI_INDEX,
    CONSTRAINT CK_DIVISIONS_DEL_FLG CHECK (del_flg IN ('0','1'))
) TABLESPACE KIZUNAVI_TABLE;

CREATE INDEX IDX_DIVISIONS_CUSTOMER_ID ON DIVISIONS (customer_id) TABLESPACE KIZUNAVI_INDEX;

COMMENT ON TABLE  DIVISIONS              IS '部テーブル: 企業ごとの部を管理';
COMMENT ON COLUMN DIVISIONS.division_id  IS '部ID (UUID)';
COMMENT ON COLUMN DIVISIONS.customer_id  IS '顧客ID (概念FK→CUSTOMERS.customer_id)';
COMMENT ON COLUMN DIVISIONS.display_name IS '部の名称 (例: 営業部、開発部)';
COMMENT ON COLUMN DIVISIONS.del_flg      IS '削除フラグ (0:有効 1:削除)';
COMMENT ON COLUMN DIVISIONS.created_at   IS '作成日時';
COMMENT ON COLUMN DIVISIONS.updated_at   IS '更新日時';


-- =============================================================================
-- 03. SECTIONS  (課テーブル)
-- =============================================================================
CREATE TABLE SECTIONS (
    section_id     VARCHAR2(36)   NOT NULL,
    customer_id    VARCHAR2(36)   NOT NULL,
    division_id    VARCHAR2(36)   NOT NULL,
    display_name   VARCHAR2(255)  NOT NULL,
    del_flg        CHAR(1)        DEFAULT '0',
    created_at     TIMESTAMP(6)   DEFAULT SYSTIMESTAMP NOT NULL,
    updated_at     TIMESTAMP(6)   DEFAULT SYSTIMESTAMP NOT NULL,
    CONSTRAINT PK_SECTIONS PRIMARY KEY (section_id) USING INDEX TABLESPACE KIZUNAVI_INDEX,
    CONSTRAINT CK_SECTIONS_DEL_FLG CHECK (del_flg IN ('0','1'))
) TABLESPACE KIZUNAVI_TABLE;

CREATE INDEX IDX_SECTIONS_CUSTOMER_ID ON SECTIONS (customer_id) TABLESPACE KIZUNAVI_INDEX;
CREATE INDEX IDX_SECTIONS_DIVISION_ID ON SECTIONS (division_id) TABLESPACE KIZUNAVI_INDEX;

COMMENT ON TABLE  SECTIONS              IS '課テーブル: 企業ごとの課を管理。division_idで親の部と紐付く';
COMMENT ON COLUMN SECTIONS.section_id   IS '課ID (UUID)';
COMMENT ON COLUMN SECTIONS.customer_id  IS '顧客ID (概念FK→CUSTOMERS.customer_id)';
COMMENT ON COLUMN SECTIONS.division_id  IS '部ID (概念FK→DIVISIONS.division_id)';
COMMENT ON COLUMN SECTIONS.display_name IS '課の名称 (例: 第一営業課、第二開発課)';
COMMENT ON COLUMN SECTIONS.del_flg      IS '削除フラグ (0:有効 1:削除)';
COMMENT ON COLUMN SECTIONS.created_at   IS '作成日時';
COMMENT ON COLUMN SECTIONS.updated_at   IS '更新日時';


-- =============================================================================
-- 04. EMPLOYEES  (従業員テーブル)
--   - 設計書のレビュー結果に基づき del_flg を追加
--   - employee_name は USERS.name を利用する方針のため追加しない
-- =============================================================================
CREATE TABLE EMPLOYEES (
    employee_id    VARCHAR2(36)   NOT NULL,
    customer_id    VARCHAR2(36)   NOT NULL,
    division_id    VARCHAR2(36),
    section_id     VARCHAR2(36),
    kizuna_level   NUMBER(1),
    hire_date      DATE,
    del_flg        CHAR(1)        DEFAULT '0',
    created_at     TIMESTAMP(6)   DEFAULT SYSTIMESTAMP NOT NULL,
    updated_at     TIMESTAMP(6)   DEFAULT SYSTIMESTAMP NOT NULL,
    CONSTRAINT PK_EMPLOYEES PRIMARY KEY (employee_id) USING INDEX TABLESPACE KIZUNAVI_INDEX,
    CONSTRAINT CK_EMPLOYEES_KIZUNA_LEVEL CHECK (kizuna_level IN (1,2,3,4,5)),
    CONSTRAINT CK_EMPLOYEES_DEL_FLG      CHECK (del_flg IN ('0','1'))
) TABLESPACE KIZUNAVI_TABLE;

CREATE INDEX IDX_EMPLOYEES_CUSTOMER_ID ON EMPLOYEES (customer_id) TABLESPACE KIZUNAVI_INDEX;
CREATE INDEX IDX_EMPLOYEES_DIVISION_ID ON EMPLOYEES (division_id) TABLESPACE KIZUNAVI_INDEX;
CREATE INDEX IDX_EMPLOYEES_SECTION_ID  ON EMPLOYEES (section_id) TABLESPACE KIZUNAVI_INDEX;

COMMENT ON TABLE  EMPLOYEES              IS '従業員テーブル: 経営陣(kizuna_level=2)はdivision_id/section_idともNULLで運用';
COMMENT ON COLUMN EMPLOYEES.employee_id  IS '従業員ID (UUID, USERSテーブルと1:1)';
COMMENT ON COLUMN EMPLOYEES.customer_id  IS '顧客ID (概念FK→CUSTOMERS.customer_id)';
COMMENT ON COLUMN EMPLOYEES.division_id  IS '部ID (概念FK→DIVISIONS.division_id, 経営陣はNULL)';
COMMENT ON COLUMN EMPLOYEES.section_id   IS '課ID (概念FK→SECTIONS.section_id, 部長以上または課なし社員はNULL)';
COMMENT ON COLUMN EMPLOYEES.kizuna_level IS 'キズナ診断役職レベル (1:社長 2:役員 3:部長 4:課長 5:社員)';
COMMENT ON COLUMN EMPLOYEES.hire_date    IS '入社年月日';
COMMENT ON COLUMN EMPLOYEES.del_flg      IS '削除フラグ (0:有効 1:削除, 退職者用)';
COMMENT ON COLUMN EMPLOYEES.created_at   IS '作成日時';
COMMENT ON COLUMN EMPLOYEES.updated_at   IS '更新日時';


-- =============================================================================
-- 05. USERS  (認証ユーザーテーブル)
--   - 物理FK: employee_id → EMPLOYEES, customer_id → CUSTOMERS
-- =============================================================================
CREATE TABLE USERS (
    user_id                    NUMBER(19)     GENERATED ALWAYS AS IDENTITY NOT NULL,
    employee_id                VARCHAR2(36),
    customer_id                VARCHAR2(36),
    email                      VARCHAR2(255)  NOT NULL,
    password_hash              VARCHAR2(72)   NOT NULL,
    name                       VARCHAR2(100)  NOT NULL,
    role                       VARCHAR2(20)   NOT NULL,
    enabled                    NUMBER(1)      DEFAULT 1 NOT NULL,
    failed_login_count         NUMBER(3)      DEFAULT 0 NOT NULL,
    locked_until               TIMESTAMP(6),
    last_login_at              TIMESTAMP(6),
    last_password_changed_at   TIMESTAMP(6)   NOT NULL,
    created_at                 TIMESTAMP(6)   DEFAULT SYSTIMESTAMP NOT NULL,
    updated_at                 TIMESTAMP(6)   DEFAULT SYSTIMESTAMP NOT NULL,
    CONSTRAINT PK_USERS PRIMARY KEY (user_id) USING INDEX TABLESPACE KIZUNAVI_INDEX,
    CONSTRAINT UK_USERS_EMAIL       UNIQUE (email) USING INDEX TABLESPACE KIZUNAVI_INDEX,
    CONSTRAINT UK_USERS_EMPLOYEE_ID UNIQUE (employee_id) USING INDEX TABLESPACE KIZUNAVI_INDEX,
    CONSTRAINT FK_USERS_EMPLOYEE FOREIGN KEY (employee_id) REFERENCES EMPLOYEES (employee_id),
    CONSTRAINT FK_USERS_CUSTOMER FOREIGN KEY (customer_id) REFERENCES CUSTOMERS (customer_id),
    CONSTRAINT CK_USERS_ROLE    CHECK (role IN ('ROLE_USER','ROLE_ADMIN')),
    CONSTRAINT CK_USERS_ENABLED CHECK (enabled IN (0,1))
) TABLESPACE KIZUNAVI_TABLE;

COMMENT ON TABLE  USERS                            IS '認証ユーザーテーブル: ログイン認証情報・アカウント状態を管理';
COMMENT ON COLUMN USERS.user_id                    IS 'ユーザーID (IDENTITY)';
COMMENT ON COLUMN USERS.employee_id                IS '従業員ID (FK→EMPLOYEES, 1:1, NULL=管理者アカウント等)';
COMMENT ON COLUMN USERS.customer_id                IS '顧客ID (FK→CUSTOMERS, テナント特定用)';
COMMENT ON COLUMN USERS.email                      IS 'メールアドレス (ログインID, 一意)';
COMMENT ON COLUMN USERS.password_hash              IS 'パスワードハッシュ (BCrypt 72文字固定)';
COMMENT ON COLUMN USERS.name                       IS '表示名 (人事氏名としても利用)';
COMMENT ON COLUMN USERS.role                       IS 'ロール (ROLE_USER / ROLE_ADMIN)';
COMMENT ON COLUMN USERS.enabled                    IS '有効フラグ (1:有効 0:無効)';
COMMENT ON COLUMN USERS.failed_login_count         IS '連続ログイン失敗回数';
COMMENT ON COLUMN USERS.locked_until               IS 'ロック解除時刻 (NULL=ロックなし)';
COMMENT ON COLUMN USERS.last_login_at              IS '最終ログイン日時';
COMMENT ON COLUMN USERS.last_password_changed_at   IS '最終パスワード変更日時';
COMMENT ON COLUMN USERS.created_at                 IS '作成日時';
COMMENT ON COLUMN USERS.updated_at                 IS '更新日時';


-- =============================================================================
-- 06. REFRESH_TOKENS  (リフレッシュトークンテーブル)
--   - 物理FK: user_id → USERS (ON DELETE CASCADE)
-- =============================================================================
CREATE TABLE REFRESH_TOKENS (
    token_id     NUMBER(19)     GENERATED ALWAYS AS IDENTITY NOT NULL,
    user_id      NUMBER(19)     NOT NULL,
    token_hash   VARCHAR2(64)   NOT NULL,
    issued_at    TIMESTAMP(6)   NOT NULL,
    expires_at   TIMESTAMP(6)   NOT NULL,
    revoked_at   TIMESTAMP(6),
    user_agent   VARCHAR2(512),
    ip_address   VARCHAR2(64),
    CONSTRAINT PK_REFRESH_TOKENS PRIMARY KEY (token_id) USING INDEX TABLESPACE KIZUNAVI_INDEX,
    CONSTRAINT UK_REFRESH_TOKENS_TOKEN_HASH UNIQUE (token_hash) USING INDEX TABLESPACE KIZUNAVI_INDEX,
    CONSTRAINT FK_REFRESH_TOKENS_USER
        FOREIGN KEY (user_id) REFERENCES USERS (user_id) ON DELETE CASCADE
) TABLESPACE KIZUNAVI_TABLE;

CREATE INDEX IDX_REFRESH_TOKENS_EXPIRES_AT  ON REFRESH_TOKENS (expires_at) TABLESPACE KIZUNAVI_INDEX;
CREATE INDEX IDX_REFRESH_TOKENS_USER_REVOKED ON REFRESH_TOKENS (user_id, revoked_at) TABLESPACE KIZUNAVI_INDEX;

COMMENT ON TABLE  REFRESH_TOKENS              IS 'リフレッシュトークンテーブル: デバイス単位でSHA-256ハッシュを保持。ローテーション方式';
COMMENT ON COLUMN REFRESH_TOKENS.token_id     IS 'トークンID (IDENTITY)';
COMMENT ON COLUMN REFRESH_TOKENS.user_id      IS 'ユーザーID (FK→USERS.user_id, ON DELETE CASCADE)';
COMMENT ON COLUMN REFRESH_TOKENS.token_hash   IS 'トークンハッシュ (SHA-256 hex文字列)';
COMMENT ON COLUMN REFRESH_TOKENS.issued_at    IS '発行日時';
COMMENT ON COLUMN REFRESH_TOKENS.expires_at   IS '失効日時';
COMMENT ON COLUMN REFRESH_TOKENS.revoked_at   IS '明示失効日時 (NULL=有効)';
COMMENT ON COLUMN REFRESH_TOKENS.user_agent   IS 'ユーザーエージェント';
COMMENT ON COLUMN REFRESH_TOKENS.ip_address   IS 'IPアドレス (IPv4/IPv6両対応)';


-- =============================================================================
-- 07. LOGIN_ATTEMPTS  (ログイン試行ログテーブル)
--   - 物理FK: user_id → USERS (ON DELETE SET NULL)
--   - CK_LOGIN_ATTEMPTS_REASON: succeeded と failure_reason の整合性
-- =============================================================================
CREATE TABLE LOGIN_ATTEMPTS (
    attempt_id       NUMBER(19)     GENERATED ALWAYS AS IDENTITY NOT NULL,
    user_id          NUMBER(19),
    email            VARCHAR2(255)  NOT NULL,
    succeeded        NUMBER(1)      NOT NULL,
    failure_reason   VARCHAR2(30),
    ip_address       VARCHAR2(64),
    attempted_at     TIMESTAMP(6)   NOT NULL,
    CONSTRAINT PK_LOGIN_ATTEMPTS PRIMARY KEY (attempt_id) USING INDEX TABLESPACE KIZUNAVI_INDEX,
    CONSTRAINT FK_LOGIN_ATTEMPTS_USER
        FOREIGN KEY (user_id) REFERENCES USERS (user_id) ON DELETE SET NULL,
    CONSTRAINT CK_LOGIN_ATTEMPTS_SUCCEEDED CHECK (succeeded IN (0,1)),
    CONSTRAINT CK_LOGIN_ATTEMPTS_REASON CHECK (
        (succeeded = 1 AND failure_reason IS NULL)
     OR (succeeded = 0 AND failure_reason IS NOT NULL)
    )
) TABLESPACE KIZUNAVI_TABLE;

CREATE INDEX IDX_LOGIN_ATTEMPTS_EMAIL_ATTEMPTED ON LOGIN_ATTEMPTS (email, attempted_at) TABLESPACE KIZUNAVI_INDEX;
CREATE INDEX IDX_LOGIN_ATTEMPTS_ATTEMPTED_AT    ON LOGIN_ATTEMPTS (attempted_at) TABLESPACE KIZUNAVI_INDEX;

COMMENT ON TABLE  LOGIN_ATTEMPTS                IS 'ログイン試行ログテーブル: 成功・失敗の監査ログ';
COMMENT ON COLUMN LOGIN_ATTEMPTS.attempt_id     IS '試行ID (IDENTITY)';
COMMENT ON COLUMN LOGIN_ATTEMPTS.user_id        IS 'ユーザーID (FK→USERS, ON DELETE SET NULL, NULL=存在しないメールでの試行)';
COMMENT ON COLUMN LOGIN_ATTEMPTS.email          IS 'メールアドレス (入力された値をそのまま保存)';
COMMENT ON COLUMN LOGIN_ATTEMPTS.succeeded      IS '成功フラグ (1:成功 0:失敗)';
COMMENT ON COLUMN LOGIN_ATTEMPTS.failure_reason IS '失敗理由 (成功時はNULL)';
COMMENT ON COLUMN LOGIN_ATTEMPTS.ip_address     IS 'IPアドレス (IPv4/IPv6両対応)';
COMMENT ON COLUMN LOGIN_ATTEMPTS.attempted_at   IS '試行日時';


-- =============================================================================
-- 08. PASSWORD_RESET_TOKENS  (パスワードリセットトークンテーブル)
--   - 物理FK: user_id → USERS (ON DELETE CASCADE)
-- =============================================================================
CREATE TABLE PASSWORD_RESET_TOKENS (
    token_id       NUMBER(19)     GENERATED ALWAYS AS IDENTITY NOT NULL,
    user_id        NUMBER(19)     NOT NULL,
    token_hash     VARCHAR2(64)   NOT NULL,
    expires_at     TIMESTAMP(6)   NOT NULL,
    used_at        TIMESTAMP(6),
    created_at     TIMESTAMP(6)   DEFAULT SYSTIMESTAMP NOT NULL,
    requester_ip   VARCHAR2(64),
    CONSTRAINT PK_PASSWORD_RESET_TOKENS PRIMARY KEY (token_id) USING INDEX TABLESPACE KIZUNAVI_INDEX,
    CONSTRAINT UK_PASSWORD_RESET_TOKEN_HASH UNIQUE (token_hash) USING INDEX TABLESPACE KIZUNAVI_INDEX,
    CONSTRAINT FK_PASSWORD_RESET_TOKENS_USER
        FOREIGN KEY (user_id) REFERENCES USERS (user_id) ON DELETE CASCADE
) TABLESPACE KIZUNAVI_TABLE;

CREATE INDEX IDX_PASSWORD_RESET_TOKENS_USER_EXPIRES ON PASSWORD_RESET_TOKENS (user_id, expires_at) TABLESPACE KIZUNAVI_INDEX;

COMMENT ON TABLE  PASSWORD_RESET_TOKENS              IS 'パスワードリセットトークンテーブル: ワンタイムトークン (SHA-256ハッシュ保存)';
COMMENT ON COLUMN PASSWORD_RESET_TOKENS.token_id     IS 'トークンID (IDENTITY)';
COMMENT ON COLUMN PASSWORD_RESET_TOKENS.user_id      IS 'ユーザーID (FK→USERS, ON DELETE CASCADE)';
COMMENT ON COLUMN PASSWORD_RESET_TOKENS.token_hash   IS 'トークンハッシュ (SHA-256 hex文字列)';
COMMENT ON COLUMN PASSWORD_RESET_TOKENS.expires_at   IS '失効日時';
COMMENT ON COLUMN PASSWORD_RESET_TOKENS.used_at      IS '使用日時 (NULL=未使用)';
COMMENT ON COLUMN PASSWORD_RESET_TOKENS.created_at   IS '作成日時';
COMMENT ON COLUMN PASSWORD_RESET_TOKENS.requester_ip IS 'リクエスト元IP (監査用)';


-- =============================================================================
-- 09. ALERT_RULES  (アラートルールマスタ)
--   - rule_no をPKに採用
-- =============================================================================
CREATE TABLE ALERT_RULES (
    rule_no          NUMBER(2)      NOT NULL,
    category         VARCHAR2(50)   NOT NULL,
    item_name        VARCHAR2(100)  NOT NULL,
    alert_comment    CLOB           NOT NULL,
    importance       VARCHAR2(10)   NOT NULL,
    priority_order   NUMBER(3)      NOT NULL,
    created_at       TIMESTAMP(6)   DEFAULT SYSTIMESTAMP NOT NULL,
    updated_at       TIMESTAMP(6)   DEFAULT SYSTIMESTAMP NOT NULL,
    CONSTRAINT PK_ALERT_RULES PRIMARY KEY (rule_no) USING INDEX TABLESPACE KIZUNAVI_INDEX,
    CONSTRAINT CK_ALERT_RULES_IMPORTANCE CHECK (importance IN ('高','中','低'))
) TABLESPACE KIZUNAVI_TABLE;

COMMENT ON TABLE  ALERT_RULES                IS 'アラートルールマスタ: ダッシュボードのアラートチェック32項目をDB管理';
COMMENT ON COLUMN ALERT_RULES.rule_no        IS 'ルールNo (1〜32, PK)';
COMMENT ON COLUMN ALERT_RULES.category       IS 'カテゴリ (従業員エンゲージメント / キズナ度 / 組織温度 等)';
COMMENT ON COLUMN ALERT_RULES.item_name      IS '項目名';
COMMENT ON COLUMN ALERT_RULES.alert_comment  IS 'コメント (プレースホルダー含む文言)';
COMMENT ON COLUMN ALERT_RULES.importance     IS '重要度 (高 / 中 / 低)';
COMMENT ON COLUMN ALERT_RULES.priority_order IS '優先順位 (1が最高優先)';
COMMENT ON COLUMN ALERT_RULES.created_at     IS '作成日時';
COMMENT ON COLUMN ALERT_RULES.updated_at     IS '更新日時';


-- =============================================================================
-- 10. ROLE_LABEL_SETTINGS  (役職名称設定テーブル)
--   - PK: (customer_id, kizuna_level) 企業全体で役職表示名を統一
-- =============================================================================
CREATE TABLE ROLE_LABEL_SETTINGS (
    customer_id    VARCHAR2(36)   NOT NULL,
    division_id    VARCHAR2(36),
    section_id     VARCHAR2(36),
    kizuna_level   NUMBER(1)      NOT NULL,
    display_name   VARCHAR2(100)  NOT NULL,
    created_at     TIMESTAMP(6)   DEFAULT SYSTIMESTAMP NOT NULL,
    updated_at     TIMESTAMP(6)   DEFAULT SYSTIMESTAMP NOT NULL,
    del_flg        CHAR(1)        DEFAULT '0',
    CONSTRAINT PK_ROLE_LABEL_SETTINGS PRIMARY KEY (customer_id, kizuna_level) USING INDEX TABLESPACE KIZUNAVI_INDEX,
    CONSTRAINT CK_ROLE_LABEL_KIZUNA_LEVEL CHECK (kizuna_level IN (1,2,3,4,5)),
    CONSTRAINT CK_ROLE_LABEL_DEL_FLG      CHECK (del_flg IN ('0','1'))
) TABLESPACE KIZUNAVI_TABLE;

COMMENT ON TABLE  ROLE_LABEL_SETTINGS               IS '役職名称設定テーブル: 企業ごとの役職表示名 (レコードなし=デフォルト名)';
COMMENT ON COLUMN ROLE_LABEL_SETTINGS.customer_id   IS '顧客ID (PK①, 概念FK→CUSTOMERS.customer_id)';
COMMENT ON COLUMN ROLE_LABEL_SETTINGS.division_id   IS '部ID (概念FK→DIVISIONS.division_id, 経営陣はNULL, 将来拡張用)';
COMMENT ON COLUMN ROLE_LABEL_SETTINGS.section_id    IS '課ID (概念FK→SECTIONS.section_id, 部長以上はNULL, 将来拡張用)';
COMMENT ON COLUMN ROLE_LABEL_SETTINGS.kizuna_level  IS 'キズナ診断役職レベル (PK②, 1:社長 2:役員 3:部長 4:課長 5:社員)';
COMMENT ON COLUMN ROLE_LABEL_SETTINGS.display_name  IS '表示名 (例: CEO、取締役、GM)';
COMMENT ON COLUMN ROLE_LABEL_SETTINGS.created_at    IS '作成日時';
COMMENT ON COLUMN ROLE_LABEL_SETTINGS.updated_at    IS '更新日時';
COMMENT ON COLUMN ROLE_LABEL_SETTINGS.del_flg       IS '削除フラグ (0:有効 1:削除)';


-- =============================================================================
-- 11. SURVEY_DETAILS  (サーベイ詳細テーブル)
--   - status カラムは追加しない方針
-- =============================================================================
CREATE TABLE SURVEY_DETAILS (
    survey_id            VARCHAR2(36)    NOT NULL,
    customer_id          VARCHAR2(36)    NOT NULL,
    survey_name          VARCHAR2(255),
    survey_description   VARCHAR2(1000),
    answer_deadline      DATE,
    created_by           VARCHAR2(36),
    created_at           TIMESTAMP(6)    DEFAULT SYSTIMESTAMP NOT NULL,
    updated_at           TIMESTAMP(6)    DEFAULT SYSTIMESTAMP NOT NULL,
    CONSTRAINT PK_SURVEY_DETAILS PRIMARY KEY (survey_id) USING INDEX TABLESPACE KIZUNAVI_INDEX
) TABLESPACE KIZUNAVI_TABLE;

CREATE INDEX IDX_SURVEY_DETAILS_CUSTOMER_ID ON SURVEY_DETAILS (customer_id) TABLESPACE KIZUNAVI_INDEX;

COMMENT ON TABLE  SURVEY_DETAILS                    IS 'サーベイ詳細テーブル: 診断回ごとの設定・回答期限を管理';
COMMENT ON COLUMN SURVEY_DETAILS.survey_id          IS 'サーベイID (UUID)';
COMMENT ON COLUMN SURVEY_DETAILS.customer_id        IS '顧客ID (概念FK→CUSTOMERS.customer_id)';
COMMENT ON COLUMN SURVEY_DETAILS.survey_name        IS 'サーベイ名 (例: 2025年第1回キズナ診断)';
COMMENT ON COLUMN SURVEY_DETAILS.survey_description IS 'サーベイ説明文';
COMMENT ON COLUMN SURVEY_DETAILS.answer_deadline    IS '回答期限';
COMMENT ON COLUMN SURVEY_DETAILS.created_by         IS '作成者ID (概念FK→EMPLOYEES.employee_id)';
COMMENT ON COLUMN SURVEY_DETAILS.created_at         IS '作成日時';
COMMENT ON COLUMN SURVEY_DETAILS.updated_at         IS '更新日時';


-- =============================================================================
-- 12. SURVEY_MAIL_LOGS  (サーベイメール送信ログ)
-- =============================================================================
CREATE TABLE SURVEY_MAIL_LOGS (
    mail_log_id     VARCHAR2(36)    NOT NULL,
    survey_id       VARCHAR2(36)    NOT NULL,
    employee_id     VARCHAR2(36)    NOT NULL,
    customer_id     VARCHAR2(36)    NOT NULL,
    mail_type       VARCHAR2(20)    NOT NULL,
    sent_to         VARCHAR2(255)   NOT NULL,
    sent_at         TIMESTAMP(6)    DEFAULT SYSTIMESTAMP NOT NULL,
    status          VARCHAR2(10)    DEFAULT 'SUCCESS' NOT NULL,
    error_message   VARCHAR2(500),
    created_at      TIMESTAMP(6)    DEFAULT SYSTIMESTAMP NOT NULL,
    CONSTRAINT PK_SURVEY_MAIL_LOGS PRIMARY KEY (mail_log_id) USING INDEX TABLESPACE KIZUNAVI_INDEX,
    CONSTRAINT CK_SURVEY_MAIL_LOGS_TYPE   CHECK (mail_type IN ('INVITE','REMIND','CLOSE')),
    CONSTRAINT CK_SURVEY_MAIL_LOGS_STATUS CHECK (status    IN ('SUCCESS','FAILED','BOUNCED'))
) TABLESPACE KIZUNAVI_TABLE;

CREATE INDEX IDX_SURVEY_MAIL_LOGS_SURVEY_ID   ON SURVEY_MAIL_LOGS (survey_id) TABLESPACE KIZUNAVI_INDEX;
CREATE INDEX IDX_SURVEY_MAIL_LOGS_EMPLOYEE_ID ON SURVEY_MAIL_LOGS (employee_id) TABLESPACE KIZUNAVI_INDEX;
CREATE INDEX IDX_SURVEY_MAIL_LOGS_CUSTOMER_ID ON SURVEY_MAIL_LOGS (customer_id) TABLESPACE KIZUNAVI_INDEX;

COMMENT ON TABLE  SURVEY_MAIL_LOGS               IS 'サーベイメール送信ログ: 案内メールの送信履歴';
COMMENT ON COLUMN SURVEY_MAIL_LOGS.mail_log_id   IS 'メールログID (UUID)';
COMMENT ON COLUMN SURVEY_MAIL_LOGS.survey_id     IS 'サーベイID (概念FK→SURVEY_DETAILS.survey_id)';
COMMENT ON COLUMN SURVEY_MAIL_LOGS.employee_id   IS '従業員ID (概念FK→EMPLOYEES.employee_id)';
COMMENT ON COLUMN SURVEY_MAIL_LOGS.customer_id   IS '顧客ID (概念FK→CUSTOMERS.customer_id, 検索用)';
COMMENT ON COLUMN SURVEY_MAIL_LOGS.mail_type     IS 'メール種別 (INVITE=案内 / REMIND=リマインド / CLOSE=締切通知)';
COMMENT ON COLUMN SURVEY_MAIL_LOGS.sent_to       IS '送信先メールアドレス (送信時点のスナップショット)';
COMMENT ON COLUMN SURVEY_MAIL_LOGS.sent_at       IS '送信日時';
COMMENT ON COLUMN SURVEY_MAIL_LOGS.status        IS '送信ステータス (SUCCESS / FAILED / BOUNCED)';
COMMENT ON COLUMN SURVEY_MAIL_LOGS.error_message IS 'エラーメッセージ (status=FAILED時)';
COMMENT ON COLUMN SURVEY_MAIL_LOGS.created_at    IS '作成日時';


-- =============================================================================
-- 13. SURVEY_QUESTIONS  (サーベイ設問テーブル)
--   - evaluation_type は持たず, score_direction NULL/NOT NULL で判別
--   - target_kizuna_level / target_dept_type は少なくとも一方が NOT NULL
-- =============================================================================
CREATE TABLE SURVEY_QUESTIONS (
    question_id               VARCHAR2(50)    NOT NULL,
    customer_id               VARCHAR2(36)    NOT NULL,
    respondent_kizuna_level   NUMBER(1),
    question_no               NUMBER(3)       NOT NULL,
    question_text             VARCHAR2(4000)  NOT NULL,
    target_kizuna_level       NUMBER(1),
    target_dept_type          NUMBER(1),
    element_code              VARCHAR2(10)    NOT NULL,
    score_direction           CHAR(1),
    created_at                TIMESTAMP(6)    DEFAULT SYSTIMESTAMP NOT NULL,
    updated_at                TIMESTAMP(6)    DEFAULT SYSTIMESTAMP NOT NULL,
    CONSTRAINT PK_SURVEY_QUESTIONS PRIMARY KEY (question_id) USING INDEX TABLESPACE KIZUNAVI_INDEX,
    CONSTRAINT CK_SURVEY_QUESTIONS_RESPONDENT CHECK (respondent_kizuna_level IS NULL OR respondent_kizuna_level IN (1,2,3,4,5)),
    CONSTRAINT CK_SURVEY_QUESTIONS_TGT_LEVEL  CHECK (target_kizuna_level   IS NULL OR target_kizuna_level   IN (1,2,3,4,5)),
    CONSTRAINT CK_SURVEY_QUESTIONS_TGT_DEPT   CHECK (target_dept_type      IS NULL OR target_dept_type      IN (1,2,3,4)),
    CONSTRAINT CK_SURVEY_QUESTIONS_TARGET     CHECK (target_kizuna_level IS NOT NULL OR target_dept_type IS NOT NULL),
    CONSTRAINT CK_SURVEY_QUESTIONS_ELEMENT    CHECK (element_code IN (
        'U','A','T','P','C',
        'O1','O2','O3','O4','O5','O6',
        'CL','CH','EX','M','PH',
        'V1','V2','S1','S2'
    )),
    CONSTRAINT CK_SURVEY_QUESTIONS_DIRECTION  CHECK (score_direction IS NULL OR score_direction IN ('F','R'))
) TABLESPACE KIZUNAVI_TABLE;

CREATE INDEX IDX_SURVEY_QUESTIONS_CUSTOMER_ID ON SURVEY_QUESTIONS (customer_id) TABLESPACE KIZUNAVI_INDEX;

COMMENT ON TABLE  SURVEY_QUESTIONS                          IS 'サーベイ設問テーブル: 設問定義マスタ。SURVEY_QUESTION_MAPPINGSで割当管理';
COMMENT ON COLUMN SURVEY_QUESTIONS.question_id              IS '設問ID (例: Q_CEO_001)';
COMMENT ON COLUMN SURVEY_QUESTIONS.customer_id              IS '顧客ID (概念FK→CUSTOMERS.customer_id)';
COMMENT ON COLUMN SURVEY_QUESTIONS.respondent_kizuna_level  IS '回答者役職レベル (1〜5, NULL=全役職共通)';
COMMENT ON COLUMN SURVEY_QUESTIONS.question_no              IS '設問番号 (企業内での管理番号)';
COMMENT ON COLUMN SURVEY_QUESTIONS.question_text            IS '設問文言';
COMMENT ON COLUMN SURVEY_QUESTIONS.target_kizuna_level      IS '対象役職レベル (1〜5, NULL=役職以外が対象)';
COMMENT ON COLUMN SURVEY_QUESTIONS.target_dept_type         IS '対象部門種別 (1:会社 2:経営陣 3:部署 4:課)';
COMMENT ON COLUMN SURVEY_QUESTIONS.element_code             IS '指標コード (U/A/T/P/C/O1〜O6/CL/CH/EX/M/PH/V1/V2/S1/S2)';
COMMENT ON COLUMN SURVEY_QUESTIONS.score_direction          IS 'スコア方向 (F=順方向 / R=逆方向 / NULL=フェーズ評価)';
COMMENT ON COLUMN SURVEY_QUESTIONS.created_at               IS '作成日時';
COMMENT ON COLUMN SURVEY_QUESTIONS.updated_at               IS '更新日時';


-- =============================================================================
-- 14. SURVEY_ANSWER_SESSIONS  (サーベイ回答セッションテーブル)
-- =============================================================================
CREATE TABLE SURVEY_ANSWER_SESSIONS (
    survey_answer_id   VARCHAR2(36)   NOT NULL,
    survey_id          VARCHAR2(36)   NOT NULL,
    employee_id        VARCHAR2(36)   NOT NULL,
    answered_at        TIMESTAMP(6),
    created_at         TIMESTAMP(6)   DEFAULT SYSTIMESTAMP NOT NULL,
    updated_at         TIMESTAMP(6)   DEFAULT SYSTIMESTAMP NOT NULL,
    CONSTRAINT PK_SURVEY_ANSWER_SESSIONS PRIMARY KEY (survey_answer_id) USING INDEX TABLESPACE KIZUNAVI_INDEX
) TABLESPACE KIZUNAVI_TABLE;

CREATE INDEX IDX_SURVEY_ANSWER_SESSIONS_SURVEY_ID   ON SURVEY_ANSWER_SESSIONS (survey_id) TABLESPACE KIZUNAVI_INDEX;
CREATE INDEX IDX_SURVEY_ANSWER_SESSIONS_EMPLOYEE_ID ON SURVEY_ANSWER_SESSIONS (employee_id) TABLESPACE KIZUNAVI_INDEX;

COMMENT ON TABLE  SURVEY_ANSWER_SESSIONS                  IS 'サーベイ回答セッションテーブル: SURVEY_ANSWER_DETAILSの親テーブル';
COMMENT ON COLUMN SURVEY_ANSWER_SESSIONS.survey_answer_id IS 'サーベイ回答ID (UUID)';
COMMENT ON COLUMN SURVEY_ANSWER_SESSIONS.survey_id        IS 'サーベイID (概念FK→SURVEY_DETAILS.survey_id)';
COMMENT ON COLUMN SURVEY_ANSWER_SESSIONS.employee_id      IS '従業員ID (概念FK→EMPLOYEES.employee_id)';
COMMENT ON COLUMN SURVEY_ANSWER_SESSIONS.answered_at      IS '回答完了日時 (NULL=未完了)';
COMMENT ON COLUMN SURVEY_ANSWER_SESSIONS.created_at       IS '作成日時';
COMMENT ON COLUMN SURVEY_ANSWER_SESSIONS.updated_at       IS '更新日時';


-- =============================================================================
-- 15. SURVEY_ANSWER_DETAILS  (サーベイ回答明細テーブル)
--   - PK: (survey_answer_id, question_id) の複合キー
--   - calculated_score は持たない方針
-- =============================================================================
CREATE TABLE SURVEY_ANSWER_DETAILS (
    survey_answer_id   VARCHAR2(36)   NOT NULL,
    question_id        VARCHAR2(50)   NOT NULL,
    answer_value       NUMBER(2),
    created_at         TIMESTAMP(6)   DEFAULT SYSTIMESTAMP NOT NULL,
    updated_at         TIMESTAMP(6)   DEFAULT SYSTIMESTAMP NOT NULL,
    CONSTRAINT PK_SURVEY_ANSWER_DETAILS PRIMARY KEY (survey_answer_id, question_id) USING INDEX TABLESPACE KIZUNAVI_INDEX,
    CONSTRAINT CK_SURVEY_ANSWER_DETAILS_VALUE CHECK (answer_value IS NULL OR answer_value BETWEEN 1 AND 7)
) TABLESPACE KIZUNAVI_TABLE;

COMMENT ON TABLE  SURVEY_ANSWER_DETAILS                  IS 'サーベイ回答明細テーブル: 設問ごとの回答値を格納';
COMMENT ON COLUMN SURVEY_ANSWER_DETAILS.survey_answer_id IS 'サーベイ回答ID (PK①, 概念FK→SURVEY_ANSWER_SESSIONS.survey_answer_id)';
COMMENT ON COLUMN SURVEY_ANSWER_DETAILS.question_id      IS '設問ID (PK②, 概念FK→SURVEY_QUESTIONS.question_id)';
COMMENT ON COLUMN SURVEY_ANSWER_DETAILS.answer_value     IS '回答値 (1〜7, フェーズ評価はフェーズ番号1〜7)';
COMMENT ON COLUMN SURVEY_ANSWER_DETAILS.created_at       IS '作成日時';
COMMENT ON COLUMN SURVEY_ANSWER_DETAILS.updated_at       IS '更新日時';


-- =============================================================================
-- 16. DASHBOARD_SUMMARIES  (ダッシュボードテーブル)
-- =============================================================================
CREATE TABLE DASHBOARD_SUMMARIES (
    survey_id              VARCHAR2(36)    NOT NULL,
    customer_id            VARCHAR2(36)    NOT NULL,
    condition              NUMBER(1),
    total_kizuna_score     NUMBER(6,2),
    kizuna_score           NUMBER(6,2),
    kizuna_change_score    NUMBER(6,2),
    role_expectation_score NUMBER(6,2),
    engagement_score       NUMBER(6,2),
    climate_score          NUMBER(6,2),
    accuracy_score         NUMBER(6,2),
    alert_top10            VARCHAR2(2000),
    calculated_at          TIMESTAMP(6)    DEFAULT SYSTIMESTAMP NOT NULL,
    updated_at             TIMESTAMP(6)    DEFAULT SYSTIMESTAMP NOT NULL,
    CONSTRAINT PK_DASHBOARD_SUMMARIES PRIMARY KEY (survey_id) USING INDEX TABLESPACE KIZUNAVI_INDEX,
    CONSTRAINT CK_DASHBOARD_CONDITION CHECK (condition IS NULL OR condition IN (1,2,3))
) TABLESPACE KIZUNAVI_TABLE;

CREATE INDEX IDX_DASHBOARD_SUMMARIES_CUSTOMER_ID ON DASHBOARD_SUMMARIES (customer_id) TABLESPACE KIZUNAVI_INDEX;

COMMENT ON TABLE  DASHBOARD_SUMMARIES                        IS 'ダッシュボードテーブル: 1サーベイ1レコードの集計結果';
COMMENT ON COLUMN DASHBOARD_SUMMARIES.survey_id              IS 'サーベイID (PK, 概念FK→SURVEY_DETAILS.survey_id)';
COMMENT ON COLUMN DASHBOARD_SUMMARIES.customer_id            IS '顧客ID (概念FK→CUSTOMERS.customer_id, 検索高速化用)';
COMMENT ON COLUMN DASHBOARD_SUMMARIES.condition              IS 'コンディション (1:良好 2:普通 3:要注意)';
COMMENT ON COLUMN DASHBOARD_SUMMARIES.total_kizuna_score     IS 'トータルキズナスコア (アラートチェック達成割合%)';
COMMENT ON COLUMN DASHBOARD_SUMMARIES.kizuna_score           IS '対個人間キズナスコア (個人間U/A/T/P/C合計の正規化)';
COMMENT ON COLUMN DASHBOARD_SUMMARIES.kizuna_change_score    IS '直近キズナ認識変化 (CHスコア集計, プラス=改善)';
COMMENT ON COLUMN DASHBOARD_SUMMARIES.role_expectation_score IS '役割期待値スコア (EXスコアを正規化)';
COMMENT ON COLUMN DASHBOARD_SUMMARIES.engagement_score       IS '従業員エンゲージメントスコア (O1〜O6を正規化)';
COMMENT ON COLUMN DASHBOARD_SUMMARIES.climate_score          IS '組織温度スコア (CLスコアを正規化)';
COMMENT ON COLUMN DASHBOARD_SUMMARIES.accuracy_score         IS '回答正確性スコア (Mスコアを正規化)';
COMMENT ON COLUMN DASHBOARD_SUMMARIES.alert_top10            IS 'アラート上位10件 (JSON文字列)';
COMMENT ON COLUMN DASHBOARD_SUMMARIES.calculated_at          IS '集計日時 (バックエンドが集計・保存した日時)';
COMMENT ON COLUMN DASHBOARD_SUMMARIES.updated_at             IS '更新日時 (再集計時に更新)';


-- =============================================================================
-- 17. VALUE_DISTRIBUTIONS  (価値観分布テーブル)
--   - PK: (survey_id, employee_id) の複合キー
-- =============================================================================
CREATE TABLE VALUE_DISTRIBUTIONS (
    survey_id                VARCHAR2(36)   NOT NULL,
    employee_id              VARCHAR2(36)   NOT NULL,
    customer_id              VARCHAR2(36)   NOT NULL,
    score_long_short         NUMBER(5,2),
    score_explore_exploit    NUMBER(5,2),
    score_assert_listen      NUMBER(5,2),
    score_express_suppress   NUMBER(5,2),
    kizuna_human_score       NUMBER(6,2),
    kizuna_org_score         NUMBER(6,2),
    phase_score              NUMBER(3,1),
    calculated_at            TIMESTAMP(6)   DEFAULT SYSTIMESTAMP NOT NULL,
    updated_at               TIMESTAMP(6)   DEFAULT SYSTIMESTAMP NOT NULL,
    CONSTRAINT PK_VALUE_DISTRIBUTIONS PRIMARY KEY (survey_id, employee_id) USING INDEX TABLESPACE KIZUNAVI_INDEX,
    CONSTRAINT CK_VALUE_DIST_PHASE_SCORE CHECK (phase_score IS NULL OR phase_score BETWEEN 1 AND 7)
) TABLESPACE KIZUNAVI_TABLE;

CREATE INDEX IDX_VALUE_DISTRIBUTIONS_CUSTOMER_ID ON VALUE_DISTRIBUTIONS (customer_id) TABLESPACE KIZUNAVI_INDEX;
CREATE INDEX IDX_VALUE_DISTRIBUTIONS_EMPLOYEE_ID ON VALUE_DISTRIBUTIONS (employee_id) TABLESPACE KIZUNAVI_INDEX;

COMMENT ON TABLE  VALUE_DISTRIBUTIONS                        IS '価値観分布テーブル: 従業員ごとの価値観・スタイル・キズナスコア・フェーズスコア';
COMMENT ON COLUMN VALUE_DISTRIBUTIONS.survey_id              IS 'サーベイID (PK①, 概念FK→SURVEY_DETAILS.survey_id)';
COMMENT ON COLUMN VALUE_DISTRIBUTIONS.employee_id            IS '従業員ID (PK②, 概念FK→EMPLOYEES.employee_id)';
COMMENT ON COLUMN VALUE_DISTRIBUTIONS.customer_id            IS '顧客ID (概念FK→CUSTOMERS.customer_id, 検索高速化用)';
COMMENT ON COLUMN VALUE_DISTRIBUTIONS.score_long_short       IS '長期/短期スコア (V1: + 長期 / - 短期)';
COMMENT ON COLUMN VALUE_DISTRIBUTIONS.score_explore_exploit  IS '探索/活用スコア (V2: + 探索 / - 活用)';
COMMENT ON COLUMN VALUE_DISTRIBUTIONS.score_assert_listen    IS '主張/傾聴スコア (S1: + 主張 / - 傾聴)';
COMMENT ON COLUMN VALUE_DISTRIBUTIONS.score_express_suppress IS '表現/抑制スコア (S2: + 表現 / - 抑制)';
COMMENT ON COLUMN VALUE_DISTRIBUTIONS.kizuna_human_score     IS '対人キズナスコア (0〜100点)';
COMMENT ON COLUMN VALUE_DISTRIBUTIONS.kizuna_org_score       IS '対組織キズナスコア (0〜100点)';
COMMENT ON COLUMN VALUE_DISTRIBUTIONS.phase_score            IS 'フェーズスコア (1〜7)';
COMMENT ON COLUMN VALUE_DISTRIBUTIONS.calculated_at          IS '集計日時';
COMMENT ON COLUMN VALUE_DISTRIBUTIONS.updated_at             IS '更新日時 (再集計時に更新)';


-- =============================================================================
-- 補足: updated_at 自動更新トリガ (任意導入, 全テーブル分のサンプル)
-- =============================================================================
-- アプリ側 (Spring Data JPA の @UpdateTimestamp 等) で updated_at を更新する
-- 場合は不要。DB側で自動更新したい場合は以下のようなトリガを各テーブルに作成する。
--
-- CREATE OR REPLACE TRIGGER TR_CUSTOMERS_UPDATED_AT
-- BEFORE UPDATE ON CUSTOMERS FOR EACH ROW
-- BEGIN
--     :NEW.updated_at := SYSTIMESTAMP;
-- END;
-- /
-- =============================================================================
--  End of DDL
-- =============================================================================
