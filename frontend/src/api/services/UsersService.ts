/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { PageUserResponse } from '../models/PageUserResponse';
import type { UpdateUserRequest } from '../models/UpdateUserRequest';
import type { UserResponse } from '../models/UserResponse';
import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';
export class UsersService {
    /**
     * ログインユーザー情報取得
     * 認証済みユーザー自身の情報を取得する
     * @returns UserResponse 取得成功
     * @throws ApiError
     */
    public static getCurrentUser(): CancelablePromise<UserResponse> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/users/me',
            errors: {
                401: `未認証`,
            },
        });
    }
    /**
     * ログインユーザー情報更新
     * 認証済みユーザー自身の情報を更新する
     * @param requestBody
     * @returns UserResponse 更新成功
     * @throws ApiError
     */
    public static updateCurrentUser(
        requestBody: UpdateUserRequest,
    ): CancelablePromise<UserResponse> {
        return __request(OpenAPI, {
            method: 'PUT',
            url: '/api/users/me',
            body: requestBody,
            mediaType: 'application/json',
            errors: {
                400: `バリデーションエラー`,
                401: `未認証`,
            },
        });
    }
    /**
     * ユーザー一覧取得（管理者専用）
     * ページネーション付きでユーザー一覧を取得する。管理者権限が必要。
     * @param page ページ番号（0始まり）
     * @param size 1ページあたりの件数
     * @param sort ソート条件（例: createdAt,desc）
     * @returns PageUserResponse 取得成功
     * @throws ApiError
     */
    public static getUsers(
        page?: number,
        size: number = 20,
        sort?: string,
    ): CancelablePromise<PageUserResponse> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/users',
            query: {
                'page': page,
                'size': size,
                'sort': sort,
            },
            errors: {
                401: `未認証`,
                403: `権限不足（管理者権限が必要）`,
            },
        });
    }
    /**
     * ユーザー詳細取得（管理者専用）
     * 指定したIDのユーザー情報を取得する。管理者権限が必要。
     * @param id ユーザーID
     * @returns UserResponse 取得成功
     * @throws ApiError
     */
    public static getUserById(
        id: number,
    ): CancelablePromise<UserResponse> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/users/{id}',
            path: {
                'id': id,
            },
            errors: {
                401: `未認証`,
                403: `権限不足（管理者権限が必要）`,
                404: `ユーザーが見つからない`,
            },
        });
    }
    /**
     * ユーザー削除（管理者専用）
     * 指定したIDのユーザーを削除する。管理者権限が必要。
     * @param id ユーザーID
     * @returns void
     * @throws ApiError
     */
    public static deleteUser(
        id: number,
    ): CancelablePromise<void> {
        return __request(OpenAPI, {
            method: 'DELETE',
            url: '/api/users/{id}',
            path: {
                'id': id,
            },
            errors: {
                401: `未認証`,
                403: `権限不足（管理者権限が必要）`,
                404: `ユーザーが見つからない`,
            },
        });
    }
}
