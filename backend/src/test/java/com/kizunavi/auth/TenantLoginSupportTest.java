package com.kizunavi.auth;

import com.kizunavi.entity.Customer;
import com.kizunavi.entity.Employee;
import com.kizunavi.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TenantLoginSupportTest {

    @Test
    @DisplayName("従業員・顧客未紐づけユーザーはログイン可能")
    void allowsAdminWithoutTenant() {
        User user = User.builder().build();
        assertThat(TenantLoginSupport.isLoginAllowed(user)).isTrue();
    }

    @Test
    @DisplayName("顧客・従業員とも del_flg=0 ならログイン可能")
    void allowsWhenBothActive() {
        User user = User.builder()
            .customer(Customer.builder().delFlg("0").build())
            .employee(Employee.builder().delFlg("0").build())
            .build();
        assertThat(TenantLoginSupport.isLoginAllowed(user)).isTrue();
    }

    @Test
    @DisplayName("顧客 del_flg=1 ならログイン不可")
    void rejectsDeletedCustomer() {
        User user = User.builder()
            .customer(Customer.builder().delFlg("1").build())
            .employee(Employee.builder().delFlg("0").build())
            .build();
        assertThat(TenantLoginSupport.isLoginAllowed(user)).isFalse();
    }

    @Test
    @DisplayName("従業員 del_flg=1 ならログイン不可")
    void rejectsDeletedEmployee() {
        User user = User.builder()
            .customer(Customer.builder().delFlg("0").build())
            .employee(Employee.builder().delFlg("1").build())
            .build();
        assertThat(TenantLoginSupport.isLoginAllowed(user)).isFalse();
    }
}
