import type {
  DivisionWithSections,
  Employee,
  EmployeeAppRole,
  EmployeeRole,
  EmployeeRoleLabels,
} from "../types/employee";

export const EMPLOYEE_ROLES_IN_ORDER: EmployeeRole[] = [
  "president",
  "executive",
  "division_head",
  "section_head",
  "staff",
];

export const DEFAULT_EMPLOYEE_ROLE_LABELS: EmployeeRoleLabels = {
  president: "社長",
  executive: "役員",
  division_head: "部長",
  section_head: "課長",
  staff: "一般社員",
};

export const APP_ROLE_OPTIONS: EmployeeAppRole[] = ["一般ユーザー", "管理者"];

export const fetchEmployees = async (): Promise<Employee[]> => {
  // TODO: API 実装後に apiClient + 生成コードへ差し替える
  return [];
};

export const fetchDepartmentHierarchy = async (): Promise<
  DivisionWithSections[]
> => {
  // TODO: API 実装後に apiClient + 生成コードへ差し替える
  return [];
};

export const fetchEmployeeRoleLabels = async (): Promise<EmployeeRoleLabels> => {
  // TODO: API 実装後に apiClient + 生成コードへ差し替える
  return DEFAULT_EMPLOYEE_ROLE_LABELS;
};

export const createEmployee = async (employee: Employee): Promise<Employee> => {
  // TODO: API 実装後に useMutation + apiClient へ差し替える
  return employee;
};

export const updateEmployee = async (employee: Employee): Promise<Employee> => {
  // TODO: API 実装後に useMutation + apiClient へ差し替える
  return employee;
};

export const deleteEmployee = async (_employeeId: string): Promise<void> => {
  // TODO: API 実装後に useMutation + apiClient へ差し替える
};

export const addDivision = async (_name: string): Promise<void> => {
  // TODO: API 実装後に useMutation + apiClient へ差し替える
};

export const renameDivision = async (
  _from: string,
  _to: string
): Promise<void> => {
  // TODO: API 実装後に useMutation + apiClient へ差し替える
};

export const deleteDivision = async (_name: string): Promise<void> => {
  // TODO: API 実装後に useMutation + apiClient へ差し替える
};

export const addSection = async (
  _divisionName: string,
  _sectionName: string
): Promise<void> => {
  // TODO: API 実装後に useMutation + apiClient へ差し替える
};

export const renameSection = async (
  _divisionName: string,
  _from: string,
  _to: string
): Promise<void> => {
  // TODO: API 実装後に useMutation + apiClient へ差し替える
};

export const deleteSection = async (
  _divisionName: string,
  _sectionName: string
): Promise<void> => {
  // TODO: API 実装後に useMutation + apiClient へ差し替える
};

export const updateEmployeeRoleLabels = async (
  _labels: EmployeeRoleLabels
): Promise<void> => {
  // TODO: API 実装後に useMutation + apiClient へ差し替える
};
