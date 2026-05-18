import type {
  AlertItem,
  SurveyOption,
  SurveySnapshot,
  ValueMapPoint,
} from "../types/dashboard";

export const fetchSurveyOptions = async (): Promise<SurveyOption[]> => {
  // TODO: API 実装後に apiClient + 生成コードへ差し替える
  return [];
};

export const fetchSurveySnapshot = async (
  _surveyId: string
): Promise<SurveySnapshot | null> => {
  // TODO: API 実装後に apiClient + 生成コードへ差し替える
  return null;
};

export const fetchAlerts = async (): Promise<AlertItem[]> => {
  // TODO: API 実装後に apiClient + 生成コードへ差し替える
  return [];
};

export const fetchValueMapPoints = async (
  _axis: "explore-use" | "express-restrain"
): Promise<ValueMapPoint[]> => {
  // TODO: API 実装後に apiClient + 生成コードへ差し替える
  return [];
};
