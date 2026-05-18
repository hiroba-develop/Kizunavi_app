/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { CreateSurveyRequest } from '../models/CreateSurveyRequest';
import type { CreateSurveyResponse } from '../models/CreateSurveyResponse';
import type { SurveyDetailListResponse } from '../models/SurveyDetailListResponse';
import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';
export class SurveysService {
    /**
     * サーベイ一覧取得
     * customerIdはJWTから取得。createdAt降順で返却。
     * ※従業員が多い場合は将来的にrespondentsを別APIに分離を検討（2026/05/13）
     *
     * @returns SurveyDetailListResponse サーベイ一覧
     * @throws ApiError
     */
    public static getSurveys(): CancelablePromise<SurveyDetailListResponse> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/surveys',
            errors: {
                401: `認証エラー（トークン未指定・無効・期限切れ）`,
            },
        });
    }
    /**
     * 新規サーベイ配信
     * サーベイを作成し、全従業員にメールを配信する。
     * SURVEY_ANSWER_SESSIONSに全従業員分のセッションをansweredAt=NULLで事前登録。
     * 【メールリンクからのアクセス】未ログイン状態でリンクをクリックした場合、ログイン画面にリダイレクトし、ログイン成功後は元のURL（/survey/answer/{surveyId}）に戻すこと。
     *
     * @param requestBody
     * @returns CreateSurveyResponse サーベイ配信成功
     * @throws ApiError
     */
    public static createSurvey(
        requestBody: CreateSurveyRequest,
    ): CancelablePromise<CreateSurveyResponse> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/surveys',
            body: requestBody,
            mediaType: 'application/json',
            errors: {
                401: `認証エラー（トークン未指定・無効・期限切れ）`,
            },
        });
    }
}
