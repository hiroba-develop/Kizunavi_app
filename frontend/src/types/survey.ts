import type { EmployeeRole } from "./employee";

export type SurveyStatus = "draft" | "active" | "expired";

export interface SurveyRecipientProgress {
  employeeId: string;
  hasResponded: boolean;
  respondedAt?: string;
}

export interface SurveyDistribution {
  id: string;
  title: string;
  description?: string;
  targetRoles: EmployeeRole[];
  startDate: string;
  expirationDate: string;
  status: SurveyStatus;
  createdAt: string;
  recipients: SurveyRecipientProgress[];
}

export interface SurveyQuestion {
  id: number;
  category: string;
  text: string;
}

export const SCALE_LABELS = [
  "まったくそう思わない",
  "そう思わない",
  "ややそう思わない",
  "どちらともいえない",
  "ややそう思う",
  "そう思う",
  "とてもそう思う",
] as const;
