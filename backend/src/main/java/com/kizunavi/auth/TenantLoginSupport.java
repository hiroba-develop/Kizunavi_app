package com.kizunavi.auth;

import com.kizunavi.entity.Customer;
import com.kizunavi.entity.Employee;
import com.kizunavi.entity.User;

/**
 * ログイン可否に関するテナント（利用企業・従業員）の有効性判定。
 *
 * <p>{@code CUSTOMERS.del_flg} および {@code EMPLOYEES.del_flg} がいずれか {@code 1} の場合、
 * 紐づくユーザーはログインできない。</p>
 */
public final class TenantLoginSupport {

    /** 有効（未削除）を示す削除フラグ */
    public static final String DEL_FLG_ACTIVE = "0";

    private TenantLoginSupport() {}

    /**
     * ユーザーに紐づく利用企業・従業員がログイン可能な状態かどうか。
     *
     * <p>従業員・顧客のいずれも未紐づけ（管理者アカウント等）の場合は {@code true}。
     * 紐づけがある場合は、それぞれの {@code del_flg} が {@value #DEL_FLG_ACTIVE} であること。</p>
     *
     * @param user 判定対象（{@code employee} / {@code customer} が初期化済みであること）
     * @return ログイン可能なら {@code true}
     */
    public static boolean isLoginAllowed(User user) {
        if (user == null) {
            return false;
        }
        Customer customer = user.getCustomer();
        Employee employee = user.getEmployee();
        if (customer == null && employee == null) {
            return true;
        }
        if (customer != null && !isDelFlagActive(customer.getDelFlg())) {
            return false;
        }
        if (employee != null && !isDelFlagActive(employee.getDelFlg())) {
            return false;
        }
        return true;
    }

    /**
     * 削除フラグが有効（未削除）かどうか。
     *
     * @param delFlg DB の {@code del_flg} 列
     * @return {@code 0} のとき {@code true}
     */
    public static boolean isDelFlagActive(String delFlg) {
        return DEL_FLG_ACTIVE.equals(delFlg);
    }
}
