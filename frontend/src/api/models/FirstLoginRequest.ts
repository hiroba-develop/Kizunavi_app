/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
export type FirstLoginRequest = {
    /**
     * メールアドレス
     */
    email: string;
    /**
     * 仮パスワード（BCrypt検証）
     */
    tempPassword: string;
    /**
     * 新パスワード（BCryptハッシュ化）
     */
    newPassword: string;
};

