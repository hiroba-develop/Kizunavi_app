export type EmployeeAppRole = "一般ユーザー" | "管理者";

export type EmployeeRole =
  | "president"
  | "executive"
  | "division_head"
  | "section_head"
  | "staff";

export const EMPLOYEE_ROLES_IN_ORDER: EmployeeRole[] = [
  "president",
  "executive",
  "division_head",
  "section_head",
  "staff",
];

export interface Employee {
  id: string;
  displayName: string;
  email: string;
  appRole: EmployeeAppRole;
  departmentDivision: string;
  departmentSection: string;
  role: EmployeeRole;
  joinedAt: string;
}

export interface DivisionWithSections {
  name: string;
  sections: string[];
}

export type EmployeeRoleLabels = Record<EmployeeRole, string>;

export const DEFAULT_EMPLOYEE_ROLE_LABELS: EmployeeRoleLabels = {
  president: "社長",
  executive: "役員",
  division_head: "部長",
  section_head: "課長",
  staff: "一般社員",
};

export const APP_ROLE_OPTIONS: EmployeeAppRole[] = ["一般ユーザー", "管理者"];
