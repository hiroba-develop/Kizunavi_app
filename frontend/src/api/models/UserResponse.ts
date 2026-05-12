/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { Role } from './Role';
export type UserResponse = {
    /**
     * ユーザーID
     */
    id?: number;
    /**
     * メールアドレス
     */
    email?: string;
    /**
     * ユーザー名
     */
    name?: string;
    role?: Role;
    /**
     * 作成日時
     */
    createdAt?: string;
    /**
     * 更新日時
     */
    updatedAt?: string;
};

