/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { CreateDivisionRequest } from '../models/CreateDivisionRequest';
import type { CreateDivisionResponse } from '../models/CreateDivisionResponse';
import type { CreateEmployeeRequest } from '../models/CreateEmployeeRequest';
import type { CreateEmployeeResponse } from '../models/CreateEmployeeResponse';
import type { CreateSectionRequest } from '../models/CreateSectionRequest';
import type { CreateSectionResponse } from '../models/CreateSectionResponse';
import type { DivisionListResponse } from '../models/DivisionListResponse';
import type { EmployeeListResponse } from '../models/EmployeeListResponse';
import type { RoleLabelListResponse } from '../models/RoleLabelListResponse';
import type { SectionListResponse } from '../models/SectionListResponse';
import type { SimpleStatusResponse } from '../models/SimpleStatusResponse';
import type { StatusMessage } from '../models/StatusMessage';
import type { UpdatedAtResponse } from '../models/UpdatedAtResponse';
import type { UpdateDivisionRequest } from '../models/UpdateDivisionRequest';
import type { UpdateEmployeeRequest } from '../models/UpdateEmployeeRequest';
import type { UpdateRoleLabelRequest } from '../models/UpdateRoleLabelRequest';
import type { UpdateRoleLabelResponse } from '../models/UpdateRoleLabelResponse';
import type { UpdateSectionRequest } from '../models/UpdateSectionRequest';
import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';
export class EmployeesService {
    /**
     * 従業員一覧取得
     * customerIdはJWTから取得。
     * USERS.enabled=0（論理削除済み）の従業員は返さない。
     *
     * @returns EmployeeListResponse 従業員一覧
     * @throws ApiError
     */
    public static getEmployees(): CancelablePromise<EmployeeListResponse> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/employees',
            errors: {
                401: `認証エラー（トークン未指定・無効・期限切れ）`,
            },
        });
    }
    /**
     * 従業員登録
     * EMPLOYEESとUSERSを同一トランザクションで登録する。
     * kizunaLevel=2（役員）はdivisionId / sectionId=NULLで登録。
     * 登録完了後に仮パスワードをメールで従業員に送信。
     *
     * @param requestBody
     * @returns CreateEmployeeResponse 従業員登録成功
     * @throws ApiError
     */
    public static createEmployee(
        requestBody: CreateEmployeeRequest,
    ): CancelablePromise<CreateEmployeeResponse> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/employees',
            body: requestBody,
            mediaType: 'application/json',
            errors: {
                401: `認証エラー（トークン未指定・無効・期限切れ）`,
                409: `メールアドレス重複`,
            },
        });
    }
    /**
     * 従業員情報更新
     * EMPLOYEESとUSERSを同一トランザクションで更新。
     * employeeId + customerIdで対象を特定。
     * kizunaLevel=2に変更する場合はdivisionId / sectionId=NULLにセット。
     *
     * @param employeeId 従業員ID（UUID）
     * @param requestBody
     * @returns UpdatedAtResponse 更新成功
     * @throws ApiError
     */
    public static updateEmployee(
        employeeId: string,
        requestBody: UpdateEmployeeRequest,
    ): CancelablePromise<UpdatedAtResponse> {
        return __request(OpenAPI, {
            method: 'PUT',
            url: '/api/employees/{employeeId}',
            path: {
                'employeeId': employeeId,
            },
            body: requestBody,
            mediaType: 'application/json',
            errors: {
                401: `認証エラー（トークン未指定・無効・期限切れ）`,
                404: `リソースが存在しない`,
            },
        });
    }
    /**
     * 従業員削除（論理削除）
     * USERS.enabled=0に更新（論理削除）。EMPLOYEESは削除しない。
     * REFRESH_TOKENSの該当ユーザーの全行をrevokedAtに現在時刻をセット（強制ログアウト）。
     *
     * @param employeeId 従業員ID（UUID）
     * @returns StatusMessage 削除成功
     * @throws ApiError
     */
    public static deleteEmployee(
        employeeId: string,
    ): CancelablePromise<StatusMessage> {
        return __request(OpenAPI, {
            method: 'DELETE',
            url: '/api/employees/{employeeId}',
            path: {
                'employeeId': employeeId,
            },
            errors: {
                401: `認証エラー（トークン未指定・無効・期限切れ）`,
                404: `リソースが存在しない`,
            },
        });
    }
    /**
     * 部一覧取得
     * customerIdで絞り込み。delFlg=0のみ・displayOrder昇順で返却。
     * @returns DivisionListResponse 部一覧
     * @throws ApiError
     */
    public static getDivisions(): CancelablePromise<DivisionListResponse> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/divisions',
            errors: {
                401: `認証エラー（トークン未指定・無効・期限切れ）`,
            },
        });
    }
    /**
     * 部門登録
     * displayOrder省略時は既存の最大displayOrder+1を設定。
     * @param requestBody
     * @returns CreateDivisionResponse 登録成功
     * @throws ApiError
     */
    public static createDivision(
        requestBody: CreateDivisionRequest,
    ): CancelablePromise<CreateDivisionResponse> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/divisions',
            body: requestBody,
            mediaType: 'application/json',
            errors: {
                401: `認証エラー（トークン未指定・無効・期限切れ）`,
            },
        });
    }
    /**
     * 部門情報更新
     * @param divisionId 部ID（UUID）
     * @param requestBody
     * @returns UpdatedAtResponse 更新成功
     * @throws ApiError
     */
    public static updateDivision(
        divisionId: string,
        requestBody: UpdateDivisionRequest,
    ): CancelablePromise<UpdatedAtResponse> {
        return __request(OpenAPI, {
            method: 'PUT',
            url: '/api/divisions/{divisionId}',
            path: {
                'divisionId': divisionId,
            },
            body: requestBody,
            mediaType: 'application/json',
            errors: {
                401: `認証エラー（トークン未指定・無効・期限切れ）`,
                404: `リソースが存在しない`,
            },
        });
    }
    /**
     * 部門削除（論理削除）
     * DIVISIONS.delFlg=1に更新。
     * 配下のSECTIONSも合わせてdelFlg=1に更新。
     * 該当divisionIdを持つEMPLOYEESのdivisionId=NULLに更新。
     *
     * @param divisionId 部ID（UUID）
     * @returns SimpleStatusResponse 削除成功
     * @throws ApiError
     */
    public static deleteDivision(
        divisionId: string,
    ): CancelablePromise<SimpleStatusResponse> {
        return __request(OpenAPI, {
            method: 'DELETE',
            url: '/api/divisions/{divisionId}',
            path: {
                'divisionId': divisionId,
            },
            errors: {
                401: `認証エラー（トークン未指定・無効・期限切れ）`,
                404: `リソースが存在しない`,
            },
        });
    }
    /**
     * 課一覧取得
     * divisionId指定時はその部の課のみ返す。delFlg=0・displayOrder昇順。
     * @param divisionId 部ID（省略時は全課）
     * @returns SectionListResponse 課一覧
     * @throws ApiError
     */
    public static getSections(
        divisionId?: string,
    ): CancelablePromise<SectionListResponse> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/sections',
            query: {
                'divisionId': divisionId,
            },
            errors: {
                401: `認証エラー（トークン未指定・無効・期限切れ）`,
            },
        });
    }
    /**
     * 課登録
     * divisionIdの存在確認を行い、存在しない場合は404エラー。
     * @param requestBody
     * @returns CreateSectionResponse 登録成功
     * @throws ApiError
     */
    public static createSection(
        requestBody: CreateSectionRequest,
    ): CancelablePromise<CreateSectionResponse> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/sections',
            body: requestBody,
            mediaType: 'application/json',
            errors: {
                401: `認証エラー（トークン未指定・無効・期限切れ）`,
                404: `リソースが存在しない`,
            },
        });
    }
    /**
     * 課情報更新
     * @param sectionId 課ID（UUID）
     * @param requestBody
     * @returns UpdatedAtResponse 更新成功
     * @throws ApiError
     */
    public static updateSection(
        sectionId: string,
        requestBody: UpdateSectionRequest,
    ): CancelablePromise<UpdatedAtResponse> {
        return __request(OpenAPI, {
            method: 'PUT',
            url: '/api/sections/{sectionId}',
            path: {
                'sectionId': sectionId,
            },
            body: requestBody,
            mediaType: 'application/json',
            errors: {
                401: `認証エラー（トークン未指定・無効・期限切れ）`,
                404: `リソースが存在しない`,
            },
        });
    }
    /**
     * 課削除（論理削除）
     * SECTIONS.delFlg=1に更新。
     * 該当sectionIdを持つEMPLOYEESのsectionId=NULLに更新。
     *
     * @param sectionId 課ID（UUID）
     * @returns SimpleStatusResponse 削除成功
     * @throws ApiError
     */
    public static deleteSection(
        sectionId: string,
    ): CancelablePromise<SimpleStatusResponse> {
        return __request(OpenAPI, {
            method: 'DELETE',
            url: '/api/sections/{sectionId}',
            path: {
                'sectionId': sectionId,
            },
            errors: {
                401: `認証エラー（トークン未指定・無効・期限切れ）`,
                404: `リソースが存在しない`,
            },
        });
    }
    /**
     * 役職一覧取得
     * ROLE_LABEL_SETTINGSをcustomerIdで絞り込み。
     * レコードがないkizunaLevelはデフォルト名で補完（1=社長 2=役員 3=部長 4=課長 5=社員）。
     * kizunaLevel昇順で返却。
     *
     * @returns RoleLabelListResponse 役職一覧
     * @throws ApiError
     */
    public static getRoleLabels(): CancelablePromise<RoleLabelListResponse> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/role-labels',
            errors: {
                401: `認証エラー（トークン未指定・無効・期限切れ）`,
            },
        });
    }
    /**
     * 役職情報更新（UPSERT）
     * レコードがある場合はUPDATE、ない場合はINSERT（UPSERT）。kizunaLevelは1〜5のみ許可。
     * @param kizunaLevel 役職レベル（1〜5）
     * @param requestBody
     * @returns UpdateRoleLabelResponse 更新成功
     * @throws ApiError
     */
    public static updateRoleLabel(
        kizunaLevel: number,
        requestBody: UpdateRoleLabelRequest,
    ): CancelablePromise<UpdateRoleLabelResponse> {
        return __request(OpenAPI, {
            method: 'PUT',
            url: '/api/role-labels/{kizunaLevel}',
            path: {
                'kizunaLevel': kizunaLevel,
            },
            body: requestBody,
            mediaType: 'application/json',
            errors: {
                400: `リクエストパラメータ不正`,
                401: `認証エラー（トークン未指定・無効・期限切れ）`,
            },
        });
    }
}
