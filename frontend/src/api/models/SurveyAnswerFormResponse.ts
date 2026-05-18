/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { SurveyQuestion } from './SurveyQuestion';
export type SurveyAnswerFormResponse = {
    surveyId?: string;
    surveyName?: string | null;
    answerDeadline?: string;
    /**
     * true=回答済み（フロントが回答済み画面を表示）
     */
    alreadyAnswered?: boolean;
    questions?: Array<SurveyQuestion>;
};

