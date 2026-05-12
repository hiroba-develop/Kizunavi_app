package com.product.template.controller;

import com.product.template.dto.UpdateUserRequest;
import com.product.template.dto.UserResponse;
import com.product.template.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * ユーザー情報の参照・更新および管理者向け一覧・削除 API。
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    /** ユーザー業務ロジック。 */
    private final UserService userService;

    /**
     * ログイン中のユーザー自身の情報を取得する。
     *
     * @param userDetails Spring Security の認証プリンシパル（ユーザー名はメール）
     * @return {@link UserResponse}
     */
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        UserResponse response = userService.getCurrentUser(userDetails.getUsername());
        return ResponseEntity.ok(response);
    }

    /**
     * ログイン中のユーザー自身のプロフィール（名前・パスワード）を更新する。
     *
     * @param userDetails 認証済みユーザー
     * @param request 更新内容
     * @return 更新後の {@link UserResponse}
     */
    @PutMapping("/me")
    public ResponseEntity<UserResponse> updateCurrentUser(
        @AuthenticationPrincipal UserDetails userDetails,
        @Valid @RequestBody UpdateUserRequest request
    ) {
        UserResponse response = userService.updateUser(userDetails.getUsername(), request);
        return ResponseEntity.ok(response);
    }

    /**
     * 全ユーザーをページング取得する（管理者のみ）。
     *
     * @param pageable ページパラメータ（デフォルトサイズ 20）
     * @return ユーザーページ
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<UserResponse>> getAllUsers(
        @PageableDefault(size = 20) Pageable pageable
    ) {
        Page<UserResponse> response = userService.getAllUsers(pageable);
        return ResponseEntity.ok(response);
    }

    /**
     * 指定 ID のユーザーを取得する（管理者のみ）。
     *
     * @param id ユーザー ID
     * @return {@link UserResponse}
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        UserResponse response = userService.getUserById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * 指定 ID のユーザーを削除する（管理者のみ）。
     *
     * @param id ユーザー ID
     * @return 本文なし（HTTP 204）
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
