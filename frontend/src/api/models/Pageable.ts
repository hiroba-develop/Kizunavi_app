/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { Sort } from './Sort';
export type Pageable = {
    /**
     * ページ番号
     */
    pageNumber?: number;
    /**
     * ページサイズ
     */
    pageSize?: number;
    sort?: Sort;
    /**
     * オフセット
     */
    offset?: number;
    paged?: boolean;
    unpaged?: boolean;
};

