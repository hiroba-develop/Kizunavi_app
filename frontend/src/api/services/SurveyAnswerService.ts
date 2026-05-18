/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { SimpleStatusResponse } from '../models/SimpleStatusResponse';
import type { SubmitAnswerRequest } from '../models/SubmitAnswerRequest';
import type { SurveyAnswerFormResponse } from '../models/SurveyAnswerFormResponse';
import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';
export class SurveyAnswerService {
    /**
     * サーベイ回答画面表示
     * employeeIdはJWTから取得。
     * EMPLOYEESテーブルからkizunaLevel, divisionId, sectionIdを毎回取得（JWTには含めない）。
     * SURVEY_QUESTIONS.respondentKizunaLevelでkizunaLevelに一致する設問のみ返す。
     *
     * @param surveyId サーベイID（UUID）
     * @returns SurveyAnswerFormResponse サーベイ情報と設問一覧
     * @throws ApiError
     */
    public static getSurveyAnswerForm(
        surveyId: string,
    ): CancelablePromise<SurveyAnswerFormResponse> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/survey-answer/{surveyId}',
            path: {
                'surveyId': surveyId,
            },
            errors: {
                401: `認証エラー（トークン未指定・無効・期限切れ）`,
                404: `リソースが存在しない`,
            },
        });
    }
    /**
     * サーベイ回答送信
     * employeeIdはJWTから取得。
     * 【バリデーション】answerValueは1〜7の整数のみ許可。範囲外の場合は400エラー。
     * 【二重送信防止】SURVEY_ANSWER_SESSIONS.answeredAt IS NOT NULLの場合は409エラー。
     *
     * @param surveyId サーベイID（UUID）
     * @param requestBody
     * @returns SimpleStatusResponse 回答送信成功
     * @throws ApiError
     */
    public static submitSurveyAnswer(
        surveyId: string,
        requestBody: SubmitAnswerRequest,
    ): CancelablePromise<SimpleStatusResponse> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/survey-answer/{surveyId}',
            path: {
                'surveyId': surveyId,
            },
            body: requestBody,
            mediaType: 'application/json',
            errors: {
                400: `リクエストパラメータ不正`,
                401: `認証エラー（トークン未指定・無効・期限切れ）`,
                409: `回答済み（二重送信）`,
            },
        });
    }
}
