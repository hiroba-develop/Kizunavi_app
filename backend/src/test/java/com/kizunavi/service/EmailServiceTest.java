package com.kizunavi.service;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.SendEmailRequest;
import software.amazon.awssdk.services.ses.model.SendEmailResponse;
import software.amazon.awssdk.services.ses.model.SesException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("null")
class EmailServiceTest {

    @Mock
    private SesClient sesClient;

    @Test
    @DisplayName("enabled=false のとき送信せず return する")
    void sendEmailWhenDisabled() {
        EmailService service =
            new EmailService(Optional.of(sesClient), "noreply@test.com", false, "http://localhost:5175");

        service.sendEmail("to@test.com", "件名", "<p>html</p>", "text");

        verify(sesClient, never()).sendEmail(any(SendEmailRequest.class));
    }

    @Test
    @DisplayName("enabled=true かつ SesClient なしのとき warn して return する")
    void sendEmailWhenClientMissing() {
        EmailService service =
            new EmailService(Optional.empty(), "noreply@test.com", true, "http://localhost:5175/");

        service.sendEmail("to@test.com", "件名", "<p>html</p>", "text");

        verify(sesClient, never()).sendEmail(any(SendEmailRequest.class));
    }

    @Test
    @DisplayName("enabled=true かつ送信成功")
    void sendEmailSuccess() {
        when(sesClient.sendEmail(any(SendEmailRequest.class)))
            .thenReturn(SendEmailResponse.builder().messageId("msg-1").build());

        EmailService service =
            new EmailService(Optional.of(sesClient), "noreply@test.com", true, "http://localhost:5175");

        service.sendEmail("to@test.com", "件名", "<p>html</p>", "text");

        ArgumentCaptor<SendEmailRequest> captor = ArgumentCaptor.forClass(SendEmailRequest.class);
        verify(sesClient).sendEmail(captor.capture());
        assertThat(captor.getValue().source()).isEqualTo("noreply@test.com");
        assertThat(captor.getValue().destination().toAddresses()).containsExactly("to@test.com");
    }

    @Test
    @DisplayName("SesException 時は RuntimeException をスローする")
    void sendEmailThrowsWhenSesFails() {
        SesException sesException = mock(SesException.class);
        when(sesClient.sendEmail(any(SendEmailRequest.class))).thenThrow(sesException);

        EmailService service =
            new EmailService(Optional.of(sesClient), "noreply@test.com", true, "http://localhost:5175");

        assertThatThrownBy(
                () -> service.sendEmail("to@test.com", "件名", "<p>html</p>", "text"))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("メール送信に失敗しました");
    }

    @Test
    @DisplayName("sendWelcomeEmail は sendEmail を呼び出す")
    void sendWelcomeEmailDelegatesToSendEmail() {
        when(sesClient.sendEmail(any(SendEmailRequest.class)))
            .thenReturn(SendEmailResponse.builder().messageId("msg-1").build());

        EmailService service =
            new EmailService(Optional.of(sesClient), "noreply@test.com", true, "http://localhost:5175");

        service.sendWelcomeEmail("to@test.com", "田中太郎");

        verify(sesClient).sendEmail(any(SendEmailRequest.class));
    }

    @Test
    @DisplayName("sendPasswordResetEmail は sendEmail を呼び出す")
    void sendPasswordResetEmailDelegatesToSendEmail() {
        when(sesClient.sendEmail(any(SendEmailRequest.class)))
            .thenReturn(SendEmailResponse.builder().messageId("msg-1").build());

        EmailService service =
            new EmailService(Optional.of(sesClient), "noreply@test.com", true, "http://localhost:5175");

        service.sendPasswordResetEmail("to@test.com", "reset-token-abc");

        ArgumentCaptor<SendEmailRequest> captor = ArgumentCaptor.forClass(SendEmailRequest.class);
        verify(sesClient).sendEmail(captor.capture());
        assertThat(captor.getValue().message().subject().data()).contains("パスワードリセット");
    }

    @Test
    @DisplayName("コンストラクタ: enabled=true かつ client 空で warn")
    void constructorWarnsWhenEnabledButNoClient() {
        new EmailService(Optional.empty(), "noreply@test.com", true, "http://example.com/");
    }
}
