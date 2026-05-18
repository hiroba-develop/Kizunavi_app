/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { FirstLoginRequest } from '../models/FirstLoginRequest';
import type { ForgotPasswordRequest } from '../models/ForgotPasswordRequest';
import type { LoginRequest } from '../models/LoginRequest';
import type { LoginResponse } from '../models/LoginResponse';
import type { PasswordResetVerifyResponse } from '../models/PasswordResetVerifyResponse';
import type { ResetPasswordRequest } from '../models/ResetPasswordRequest';
import type { SimpleStatusResponse } from '../models/SimpleStatusResponse';
import type { StatusMessage } from '../models/StatusMessage';
import type { TokenRefreshResponse } from '../models/TokenRefreshResponse';
import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';
export class AuthService {
    /**
     * ログイン
     * メールアドレスとパスワードで認証し、アクセストークンを取得する。
     * @param requestBody
     * @returns LoginResponse ログイン成功
     * @throws ApiError
     */
    public static login(
        requestBody: LoginRequest,
    ): CancelablePromise<LoginResponse> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/auth/login',
            body: requestBody,
            mediaType: 'application/json',
            errors: {
                401: `認証エラー（トークン未指定・無効・期限切れ）`,
                423: `アカウントロック中`,
            },
        });
    }
    /**
     * ログアウト
     * リフレッシュトークンを失効させる。アクセストークンはステートレスのためサーバー側での無効化は不要。
     * @param refreshToken リフレッシュトークン（HttpOnly Cookieとしてブラウザが自動送信）
     * @returns StatusMessage ログアウト成功
     * @throws ApiError
     */
    public static logout(
        refreshToken: string,
    ): CancelablePromise<StatusMessage> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/auth/logout',
            cookies: {
                'refreshToken': refreshToken,
            },
            errors: {
                401: `認証エラー（トークン未指定・無効・期限切れ）`,
            },
        });
    }
    /**
     * トークンリフレッシュ
     * HttpOnly CookieのrefreshTokenを使って新しいアクセストークンを発行する。
     * refreshTokenはブラウザが自動送信するためリクエストボディへの指定は不要。
     * ローテーション処理により旧refreshTokenは失効し、新しいrefreshTokenがSet-Cookieで返される。
     *
     * @param refreshToken リフレッシュトークン（HttpOnly Cookieとしてブラウザが自動送信）
     * @returns TokenRefreshResponse トークン発行成功
     * @throws ApiError
     */
    public static refreshToken(
        refreshToken: string,
    ): CancelablePromise<TokenRefreshResponse> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/auth/refresh',
            cookies: {
                'refreshToken': refreshToken,
            },
            errors: {
                401: `認証エラー（トークン未指定・無効・期限切れ）`,
            },
        });
    }
    /**
     * パスワード再発行メール送信
     * 指定メールアドレス宛にパスワードリセット用URLを送信する。
     * セキュリティのため、メールアドレスが存在しない場合も同じレスポンスを返す。
     *
     * @param requestBody
     * @returns SimpleStatusResponse 送信完了（メールアドレス不存在の場合も同じレスポンス）
     * @throws ApiError
     */
    public static forgotPassword(
        requestBody: ForgotPasswordRequest,
    ): CancelablePromise<SimpleStatusResponse> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/auth/password/forgot',
            body: requestBody,
            mediaType: 'application/json',
        });
    }
    /**
     * パスワードリセットトークン検証
     * メールのリンククリック時に呼び出し、トークンの有効性を検証する。
     * @param token リセットトークン（PASSWORD_RESET_TOKENS.tokenHashの元値）
     * @returns PasswordResetVerifyResponse 検証結果
     * @throws ApiError
     */
    public static verifyResetToken(
        token: string,
    ): CancelablePromise<PasswordResetVerifyResponse> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/auth/password/reset/verify/{token}',
            path: {
                'token': token,
            },
        });
    }
    /**
     * パスワード再設定
     * トークンを検証し、新しいパスワードを設定する。
     * @param requestBody
     * @returns SimpleStatusResponse パスワード更新成功
     * @throws ApiError
     */
    public static resetPassword(
        requestBody: ResetPasswordRequest,
    ): CancelablePromise<SimpleStatusResponse> {
        return __request(OpenAPI, {
            method: 'PUT',
            url: '/api/auth/password/reset',
            body: requestBody,
            mediaType: 'application/json',
            errors: {
                400: `リクエストパラメータ不正`,
                401: `認証エラー（トークン未指定・無効・期限切れ）`,
            },
        });
    }
    /**
     * 初回パスワード設定
     * 仮パスワードを検証し、新しいパスワードを設定する。
     * 【前提】本APIはtempPassword（仮パスワード）をRequest Bodyに含むため、HTTPS必須。HTTP通信は許可しないこと。
     *
     * @param requestBody
     * @returns SimpleStatusResponse パスワード設定成功
     * @throws ApiError
     */
    public static firstLogin(
        requestBody: FirstLoginRequest,
    ): CancelablePromise<SimpleStatusResponse> {
        return __request(OpenAPI, {
            method: 'PUT',
            url: '/api/auth/firstlogin',
            body: requestBody,
            mediaType: 'application/json',
            errors: {
                401: `認証エラー（トークン未指定・無効・期限切れ）`,
            },
        });
    }
}
