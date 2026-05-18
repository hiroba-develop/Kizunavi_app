/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
export type CreateSurveyRequest = {
    /**
     * サーベイ名
     */
    surveyName?: string | null;
    /**
     * 説明文
     */
    description?: string | null;
    /**
     * 回答締め切り（YYYY-MM-DD）
     */
    answerDeadline: string;
};

