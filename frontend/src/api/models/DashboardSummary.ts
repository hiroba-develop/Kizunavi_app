/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
export type DashboardSummary = {
    kizunaScore?: number;
    kizunaChangeScore?: number;
    roleExpectationScore?: number;
    engagementScore?: number;
    climateScore?: number;
    accuracyScore?: number;
    /**
     * 1=良好 2=普通 3=要注意
     */
    condition?: number;
    /**
     * アラート上位10件（DASHBOARD_SUMMARIES.alertTop10）。
     * ※要素の shape は別途設計予定。現時点では未定義のため any 扱い。
     * フロント・バックともに実装前に型定義の合意を取ること。
     *
     */
    alertTop10?: Array<any>;
};

