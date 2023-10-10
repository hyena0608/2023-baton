package touch.baton.domain.notification.command.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

import static lombok.AccessLevel.PROTECTED;

@EqualsAndHashCode
@Getter
@NoArgsConstructor(access = PROTECTED)
@Embeddable
public class NotificationMessage {

    @Column(name = "message", nullable = false)
    private String value;

    public NotificationMessage(final String value) {
        validateNotNull(value);
        this.value = value;
    }

    private void validateNotNull(final String value) {
        if (Objects.isNull(value)) {
            throw new IllegalArgumentException("NotificationMessage 객체 내부에 message 는 null 일 수 없습니다.");
        }
    }
}
