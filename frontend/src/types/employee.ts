export type EmployeeAppRole = "一般ユーザー" | "管理者";

export type EmployeeRole =
  | "president"
  | "executive"
  | "division_head"
  | "section_head"
  | "staff";

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
