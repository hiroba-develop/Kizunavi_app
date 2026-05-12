/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
export type UpdateUserRequest = {
    /**
     * ユーザー名（100文字以下）
     */
    name?: string;
    /**
     * 新しいパスワード（8文字以上100文字以下）
     */
    password?: string;
    /**
     * 現在のパスワード（パスワード変更時に必要）
     */
    currentPassword?: string;
};

