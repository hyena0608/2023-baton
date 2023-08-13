package touch.baton.fixture.domain;

import touch.baton.domain.runnerpost.RunnerPost;
import touch.baton.domain.supporter.Supporter;
import touch.baton.domain.supporter.SupporterRunnerPost;
import touch.baton.domain.supporter.vo.Message;

public abstract class SupporterRunnerPostFixture {

    private SupporterRunnerPostFixture() {
    }

    public static SupporterRunnerPost create(final RunnerPost runnerPost, final Supporter supporter) {
        return SupporterRunnerPost.builder()
                .runnerPost(runnerPost)
                .supporter(supporter)
                .message(new Message("안녕하세요. 테스트용 서포터입니다."))
                .build();
    }
}
