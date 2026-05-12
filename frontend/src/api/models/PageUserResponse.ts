/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { Pageable } from './Pageable';
import type { Sort } from './Sort';
import type { UserResponse } from './UserResponse';
/**
 * ページネーション付きユーザー一覧レスポンス（Spring Data Page形式）
 */
export type PageUserResponse = {
    /**
     * ユーザー一覧
     */
    content?: Array<UserResponse>;
    pageable?: Pageable;
    /**
     * 全件数
     */
    totalElements?: number;
    /**
     * 総ページ数
     */
    totalPages?: number;
    /**
     * 1ページあたりの件数
     */
    size?: number;
    /**
     * 現在のページ番号（0始まり）
     */
    number?: number;
    sort?: Sort;
    /**
     * 最初のページかどうか
     */
    first?: boolean;
    /**
     * 最後のページかどうか
     */
    last?: boolean;
    /**
     * 現在のページの要素数
     */
    numberOfElements?: number;
    /**
     * 空かどうか
     */
    empty?: boolean;
};

