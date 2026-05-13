package com.kizunavi.service;

import com.kizunavi.dto.UpdateUserRequest;
import com.kizunavi.dto.UserResponse;
import com.kizunavi.entity.User;
import com.kizunavi.exception.BadRequestException;
import com.kizunavi.exception.ResourceNotFoundException;
import com.kizunavi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

import static com.kizunavi.service.AuthService.toUserResponse;

/**
 * ログインユーザーおよび管理者向けのユーザー CRUD・プロフィール更新を提供する。
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    /** ユーザーの照会・更新・削除。 */
    private final UserRepository userRepository;
    /** パスワード変更時の検証および再ハッシュ化に使用。 */
    private final PasswordEncoder passwordEncoder;

    /**
     * メールアドレス（認証プリンシパル）に紐づく現在のユーザーを取得する。
     *
     * @param email ログインユーザーのメールアドレス
     * @return ユーザー公開情報
     * @throws ResourceNotFoundException ユーザーが存在しない場合
     */
    @Transactional(readOnly = true)
    public UserResponse getCurrentUser(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("ユーザーが見つかりません"));
        return toUserResponse(user);
    }

    /**
     * 主キーでユーザーを取得する（管理者向け）。
     *
     * @param id ユーザー ID
     * @return ユーザー公開情報
     * @throws ResourceNotFoundException ユーザーが存在しない場合
     */
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("ユーザーが見つかりません"));
        return toUserResponse(user);
    }

    /**
     * ユーザーをページング取得する（管理者向け）。
     *
     * @param pageable ページ番号・サイズ・ソート
     * @return {@link UserResponse} のページ
     */
    @Transactional(readOnly = true)
    public Page<UserResponse> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
            .map(AuthService::toUserResponse);
    }

    /**
     * ログインユーザーの名前および任意でパスワードを更新する。
     *
     * <p>パスワード変更時は現在のパスワードの一致が必須。</p>
     *
     * @param email 更新対象ユーザーのメールアドレス
     * @param request 更新内容（名前・新パスワード・現在パスワード）
     * @return 更新後のユーザー公開情報
     * @throws ResourceNotFoundException ユーザーが存在しない場合
     * @throws BadRequestException パスワード変更時に現在パスワードが未入力または不一致の場合
     */
    @Transactional
    public UserResponse updateUser(String email, UpdateUserRequest request) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("ユーザーが見つかりません"));

        if (StringUtils.hasText(request.getName())) {
            user.setName(request.getName());
        }

        if (StringUtils.hasText(request.getPassword())) {
            if (!StringUtils.hasText(request.getCurrentPassword())) {
                throw new BadRequestException("現在のパスワードを入力してください");
            }
            if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPasswordHash())) {
                throw new BadRequestException("現在のパスワードが正しくありません");
            }
            user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
            user.setLastPasswordChangedAt(LocalDateTime.now());
        }

        User updatedUser = userRepository.save(user);
        log.info("User updated: {}", updatedUser.getEmail());

        return toUserResponse(updatedUser);
    }

    /**
     * 指定 ID のユーザーを削除する（管理者向け）。
     *
     * @param id 削除するユーザー ID
     * @throws ResourceNotFoundException ユーザーが存在しない場合
     */
    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("ユーザーが見つかりません");
        }
        userRepository.deleteById(id);
        log.info("User deleted: {}", id);
    }
}
