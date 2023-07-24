package touch.baton.fixture.vo;

import touch.baton.domain.supporter.vo.StarCount;

public abstract class StarCountFixture {

    private StarCountFixture() {
    }

    public static StarCount starCount(final int value) {
        return new StarCount(value);
    }
}
