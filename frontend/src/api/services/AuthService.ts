/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { LoginRequest } from '../models/LoginRequest';
import type { RefreshTokenRequest } from '../models/RefreshTokenRequest';
import type { SignupRequest } from '../models/SignupRequest';
import type { TokenResponse } from '../models/TokenResponse';
import type { UserResponse } from '../models/UserResponse';
import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';
export class AuthService {
    /**
     * ログイン
     * メールアドレスとパスワードで認証し、アクセストークンとリフレッシュトークンを取得する
     * @param requestBody
     * @returns TokenResponse 認証成功
     * @throws ApiError
     */
    public static login(
        requestBody: LoginRequest,
    ): CancelablePromise<TokenResponse> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/auth/login',
            body: requestBody,
            mediaType: 'application/json',
            errors: {
                400: `バリデーションエラー`,
                401: `認証失敗（メールアドレスまたはパスワードが不正）`,
            },
        });
    }
    /**
     * ユーザー登録
     * 新規ユーザーを登録する
     * @param requestBody
     * @returns UserResponse ユーザー登録成功
     * @throws ApiError
     */
    public static signup(
        requestBody: SignupRequest,
    ): CancelablePromise<UserResponse> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/auth/signup',
            body: requestBody,
            mediaType: 'application/json',
            errors: {
                400: `バリデーションエラー`,
                409: `メールアドレスが既に登録済み`,
            },
        });
    }
    /**
     * トークンリフレッシュ
     * リフレッシュトークンを使用して新しいアクセストークンを取得する
     * @param requestBody
     * @returns TokenResponse トークンリフレッシュ成功
     * @throws ApiError
     */
    public static refreshToken(
        requestBody: RefreshTokenRequest,
    ): CancelablePromise<TokenResponse> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/auth/refresh',
            body: requestBody,
            mediaType: 'application/json',
            errors: {
                400: `バリデーションエラー`,
                401: `リフレッシュトークンが無効または期限切れ`,
            },
        });
    }
    /**
     * ログアウト
     * 現在のユーザーのリフレッシュトークンを無効化する
     * @returns void
     * @throws ApiError
     */
    public static logout(): CancelablePromise<void> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/auth/logout',
            errors: {
                401: `未認証`,
            },
        });
    }
}
