/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { Role } from './Role';
export type CreateEmployeeRequest = {
    /**
     * 表示名（USERS.name）
     */
    displayName: string;
    /**
     * メールアドレス（USERS.email・一意）
     */
    email: string;
    role: Role;
    /**
     * 役職レベル
     */
    kizunaLevel: number;
    /**
     * 部ID（kizunaLevel=2はNULL必須）
     */
    divisionId?: string | null;
    /**
     * 課ID
     */
    sectionId?: string | null;
    /**
     * 入社日（YYYY-MM-DD）
     */
    hireDate?: string | null;
    /**
     * 仮パスワード（BCryptハッシュ化してUSERS.passwordHashに保存）
     */
    tempPassword: string;
};

