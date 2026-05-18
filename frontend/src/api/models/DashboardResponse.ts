/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { DashboardSummary } from './DashboardSummary';
import type { EmployeeValue } from './EmployeeValue';
import type { SurveySummary } from './SurveySummary';
export type DashboardResponse = {
    /**
     * 全サーベイ一覧（サイドバー・ドロップダウン用）
     */
    surveys: Array<SurveySummary>;
    survey: SurveySummary;
    summary: DashboardSummary;
    /**
     * 従業員バリュー分布
     */
    values: Array<EmployeeValue>;
};

