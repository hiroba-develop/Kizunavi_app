/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
export type ResetPasswordRequest = {
    /**
     * リセットトークン（PASSWORD_RESET_TOKENS.tokenHashの元値）
     */
    token: string;
    /**
     * 新パスワード（BCryptハッシュ化してUSERS.passwordHashへ）
     */
    newPassword: string;
};

