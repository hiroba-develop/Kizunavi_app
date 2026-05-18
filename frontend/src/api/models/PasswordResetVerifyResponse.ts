/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
export type PasswordResetVerifyResponse = {
    responseStatus?: string;
    /**
     * true=有効 / false=無効
     */
    valid?: boolean;
    /**
     * 無効時の理由（期限切れ / 使用済み）
     */
    message?: string | null;
};

