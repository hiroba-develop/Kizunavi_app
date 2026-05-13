import type { EmployeeRole } from "../types/employee";
import type { SurveyDistribution, SurveyQuestion } from "../types/survey";

type CreateSurveyInput = {
  title: string;
  description: string;
  expirationDate: string;
  targetRoles: EmployeeRole[];
  recipients: { employeeId: string; hasResponded: boolean }[];
};

const today = () => new Date().toISOString().slice(0, 10);

export const fetchSurveys = async (): Promise<SurveyDistribution[]> => {
  // TODO: API 実装後に apiClient + 生成コードへ差し替える
  return [];
};

export const createSurvey = async (
  input: CreateSurveyInput
): Promise<SurveyDistribution> => {
  // TODO: API 実装後に useMutation + apiClient へ差し替える
  return {
    id: `srv-${Date.now()}`,
    title: input.title,
    description: input.description,
    targetRoles: input.targetRoles,
    startDate: today(),
    expirationDate: input.expirationDate,
    status: "active",
    createdAt: today(),
    recipients: input.recipients,
  };
};

export const fetchSurveyQuestions = async (
  _surveyId: string
): Promise<SurveyQuestion[]> => {
  // TODO: API 実装後に apiClient + 生成コードへ差し替える
  return [];
};

export const submitSurveyAnswers = async (
  _surveyId: string,
  _answers: Record<number, number>
): Promise<void> => {
  // TODO: API 実装後に useMutation + apiClient へ差し替える
};
