/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
export type LoginResponse = {
    /**
     * アクセストークン（JWT）
     */
    token: string;
    /**
     * ユーザー表示名（USERS.name。取得後フロントでlocalStorageに保存）
     */
    name: string;
};

