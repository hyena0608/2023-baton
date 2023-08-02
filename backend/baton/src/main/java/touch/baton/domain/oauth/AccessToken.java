package touch.baton.domain.oauth;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
@Getter
public final class AccessToken {

    private final String value;

    public AccessToken(final String value) {
        this.value = value;
    }
}
