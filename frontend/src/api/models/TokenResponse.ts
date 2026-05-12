/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
export type TokenResponse = {
    /**
     * アクセストークン（JWT）
     */
    accessToken?: string;
    /**
     * リフレッシュトークン
     */
    refreshToken?: string;
    /**
     * トークンタイプ
     */
    tokenType?: string;
    /**
     * アクセストークンの有効期限（秒）
     */
    expiresIn?: number;
};

