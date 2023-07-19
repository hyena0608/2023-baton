package touch.baton.assure.runnerpost;

import org.junit.jupiter.api.Test;
import touch.baton.config.AssuredTestConfig;
import touch.baton.domain.common.vo.*;
import touch.baton.domain.member.Member;
import touch.baton.domain.member.vo.*;
import touch.baton.domain.runner.Runner;
import touch.baton.domain.runnerpost.RunnerPost;
import touch.baton.domain.runnerpost.controller.response.RunnerPostResponse;
import touch.baton.domain.runnerpost.vo.Deadline;
import touch.baton.domain.runnerpost.vo.PullRequestUrl;
import touch.baton.domain.tag.RunnerPostTags;

import java.time.LocalDateTime;
import java.util.ArrayList;

class RunnerPostAssuredReadTest extends AssuredTestConfig {

    @Test
    void 러너의_게시글_식별자값으로_러너_게시글_상세_정보_조회에_성공한다() {
        final Member member = Member.builder()
                .memberName(new MemberName("헤에디주"))
                .email(new Email("test@test.co.kr"))
                .oauthId(new OauthId("dsigjh98gh230gn2oinv913bcuo23nqovbvu93b12voi3bc31j"))
                .githubUrl(new GithubUrl("github.com/hyena0608"))
                .company(new Company("우아한형제들"))
                .imageUrl(new ImageUrl("https://"))
                .build();
        memberRepository.save(member);

        final Runner runner = Runner.builder()
                .totalRating(new TotalRating(100))
                .grade(Grade.BARE_FOOT)
                .member(member)
                .build();
        runnerRepository.save(runner);

        final LocalDateTime deadline = LocalDateTime.now();
        final RunnerPost runnerPost = RunnerPost.builder()
                .title(new Title("제 코드 리뷰 좀 해주세요!!"))
                .contents(new Contents("제 코드는 클린코드가 맞을까요?"))
                .deadline(new Deadline(deadline))
                .pullRequestUrl(new PullRequestUrl("https://"))
                .watchedCount(new WatchedCount(0))
                .chattingRoomCount(new ChattingRoomCount(0))
                .runnerPostTags(new RunnerPostTags(new ArrayList<>()))
                .runner(runner)
                .supporter(null)
                .build();
        runnerPostRepository.save(runnerPost);

        RunnerPostAssuredSupport
                .클라이언트_요청().러너_게시글_식별자값으로_러너_게시글을_조회한다(runnerPost.getId())
                .서버_응답().러너_게시글_단건_조회_성공을_검증한다(RunnerPostResponse.SingleRunnerPost.from(runnerPost));
    }
}
