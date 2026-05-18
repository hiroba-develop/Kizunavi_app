/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { SurveyRespondent } from './SurveyRespondent';
export type SurveyDetail = {
    surveyId?: string;
    surveyName?: string | null;
    answerDeadline?: string;
    createdAt?: string;
    totalEmployees?: number;
    responseCount?: number;
    noResponseCount?: number;
    respondents?: Array<SurveyRespondent>;
};

