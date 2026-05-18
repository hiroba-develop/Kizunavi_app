/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { FieldError } from './FieldError';
/**
 * エラーレスポンス
 */
export type ErrorResponse = {
    /**
     * エラー発生日時
     */
    timestamp: string;
    /**
     * HTTPステータスコード
     */
    status: number;
    /**
     * エラーコード
     */
    error: string;
    /**
     * エラーメッセージ
     */
    message: string;
    /**
     * リクエストパス
     */
    path: string;
    /**
     * フィールドごとのバリデーションエラー（バリデーションエラー時のみ）
     */
    fieldErrors?: Array<FieldError> | null;
};

