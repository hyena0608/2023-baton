package touch.baton.document.notification.delete;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import touch.baton.config.RestdocsConfig;
import touch.baton.domain.member.command.Member;
import touch.baton.domain.notification.command.Notification;
import touch.baton.domain.notification.command.controller.NotificationCommandController;
import touch.baton.domain.notification.command.service.NotificationCommandService;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.fixture.domain.NotificationFixture;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static touch.baton.fixture.vo.NotificationReferencedIdFixture.notificationReferencedId;

@WebMvcTest(NotificationCommandController.class)
class NotificationDeleteApiTest extends RestdocsConfig {

    @MockBean
    private NotificationCommandService notificationCommandService;

    @BeforeEach
    void setUp() {
        restdocsSetUp(new NotificationCommandController(notificationCommandService));
    }

    @DisplayName("알림 삭제 API")
    @Test
    void deleteNotificationByNotificationId() throws Exception {
        // given
        final Member memberHyena = MemberFixture.createHyena();
        final String token = getAccessTokenBySocialId(memberHyena.getSocialId().getValue());
        final Notification notification = NotificationFixture.create(memberHyena, notificationReferencedId(1L));

        // when
        doNothing().when(notificationCommandService).deleteNotificationByMember(any(Member.class), anyLong());
        when(oauthMemberCommandRepository.findBySocialId(any())).thenReturn(Optional.ofNullable(memberHyena));

        final Notification spyNotification = spy(notification);
        when(spyNotification.getId()).thenReturn(1L);

        // then
        mockMvc.perform(delete("/api/v1/notifications/{notificationId}", spyNotification.getId())
                        .header(AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isNoContent())
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("notificationId").description("알림 식별자값")
                        ),
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description("Bearer JWT")
                        )
                ));
    }
}
