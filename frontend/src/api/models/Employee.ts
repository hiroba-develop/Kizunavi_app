/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { Role } from './Role';
export type Employee = {
    employeeId?: string;
    displayName?: string;
    email?: string;
    role?: Role;
    divisionId?: string | null;
    divisionName?: string | null;
    sectionId?: string | null;
    sectionName?: string | null;
    kizunaLevel?: number;
    roleLabel?: string;
    hireDate?: string | null;
};

