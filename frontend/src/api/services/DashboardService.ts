/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { DashboardListResponse } from '../models/DashboardListResponse';
import type { DashboardResponse } from '../models/DashboardResponse';
import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';
export class DashboardService {
    /**
     * サーベイ一覧取得
     * 全サーベイ一覧を取得する。サイドバー・ドロップダウンのサーベイ切替用。
     * customerIdはJWTから取得するためクエリパラメータへの指定は不要。
     * SURVEY_DETAILSをcustomerIdで絞り込み、createdAt降順で返却。
     * サーベイが0件の場合はsurveysを空配列で返す。
     *
     * @returns DashboardListResponse サーベイ一覧
     * @throws ApiError
     */
    public static getDashboardList(): CancelablePromise<DashboardListResponse> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/dashboard/list',
            errors: {
                401: `認証エラー（トークン未指定・無効・期限切れ）`,
            },
        });
    }
    /**
     * ダッシュボード取得
     * 指定したsurveyIdのサーベイについて、サーベイ一覧・サマリー・従業員バリュー分布を一括取得する。
     * customerIdはJWTから取得するためクエリパラメータへの指定は不要。
     * ※レスポンスが大きい場合は将来的にAPIの分離を検討（2026/05/13）
     *
     * @param surveyId サーベイID
     * @returns DashboardResponse ダッシュボードデータ
     * @throws ApiError
     */
    public static getDashboard(
        surveyId: string,
    ): CancelablePromise<DashboardResponse> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/dashboard/{surveyId}',
            path: {
                'surveyId': surveyId,
            },
            errors: {
                401: `認証エラー（トークン未指定・無効・期限切れ）`,
                404: `リソースが存在しない`,
            },
        });
    }
}
