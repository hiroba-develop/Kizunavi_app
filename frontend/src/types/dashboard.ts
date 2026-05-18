export type AlertSeverity = "high" | "middle" | "low";
export type AlertStatus = "warning" | "ok";

export interface AlertItem {
  id: string;
  category: string;
  priority: number;
  status: AlertStatus;
  severity: AlertSeverity;
  title: string;
  description: string;
}

export const ALERT_CATEGORIES: string[] = ["すべて"];
export const ALERT_CATEGORY_DISPLAY_ORDER: string[] = ["すべて"];
export const DISPLAY_ALERT_LIMIT = 50;

export type ScoreCardColor =
  | "gray"
  | "orange"
  | "blue"
  | "green"
  | "amber"
  | "red";

export interface ScoreCardData {
  key: string;
  label: string;
  value: string;
  color: ScoreCardColor;
  delta?: string;
}

export interface SurveyOption {
  id: string;
  name: string;
  executedAt: string;
}

export interface SurveySnapshot {
  overallScore: number;
  scoreCards: ScoreCardData[];
  previousDelta: number;
}

export type ValueMapPhaseTone = "danger" | "warning" | "good";

export interface ValueMapPoint {
  id: string;
  name: string;
  subtitle: string;
  x: number;
  y: number;
  kizunaScore: number;
  phase: string;
  phaseTone: ValueMapPhaseTone;
  description: string;
}
