package touch.baton.domain.alarm.command.event;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;
import touch.baton.domain.alarm.command.Alarm;
import touch.baton.domain.alarm.exception.AlarmBusinessException;
import touch.baton.domain.alarm.command.repository.AlarmCommandRepository;
import touch.baton.domain.alarm.command.vo.AlarmMessage;
import touch.baton.domain.alarm.command.vo.AlarmReferencedId;
import touch.baton.domain.alarm.command.vo.AlarmTitle;
import touch.baton.domain.alarm.command.vo.AlarmType;
import touch.baton.domain.alarm.command.vo.IsRead;
import touch.baton.domain.runnerpost.command.RunnerPost;
import touch.baton.domain.runnerpost.command.event.RunnerPostApplySupporterEvent;
import touch.baton.domain.runnerpost.command.event.RunnerPostAssignSupporterEvent;
import touch.baton.domain.runnerpost.command.event.RunnerPostReviewStatusDoneEvent;
import touch.baton.domain.runnerpost.query.repository.RunnerPostQueryRepository;

import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;
import static org.springframework.transaction.event.TransactionPhase.AFTER_COMMIT;
import static touch.baton.domain.alarm.command.vo.AlarmText.MESSAGE_REFERENCED_BY_RUNNER_POST;
import static touch.baton.domain.alarm.command.vo.AlarmText.TITLE_RUNNER_POST_APPLICANT;
import static touch.baton.domain.alarm.command.vo.AlarmText.TITLE_RUNNER_POST_ASSIGN_SUPPORTER;
import static touch.baton.domain.alarm.command.vo.AlarmText.TITLE_RUNNER_POST_REVIEW_STATUS_DONE;

@RequiredArgsConstructor
@Component
public class AlarmEventListener {

    private final AlarmCommandRepository alarmCommandRepository;
    private final RunnerPostQueryRepository runnerPostQueryRepository;

    @TransactionalEventListener(phase = AFTER_COMMIT)
    @Transactional(propagation = REQUIRES_NEW)
    public void subscribeRunnerPostApplySupporterEvent(final RunnerPostApplySupporterEvent event) {
        createRunnerPostAlarm(event.runnerPostId(), TITLE_RUNNER_POST_APPLICANT.getText());
    }

    private void createRunnerPostAlarm(final Long runnerPostId, final String alarmTitle) {
        final RunnerPost foundRunnerPost = runnerPostQueryRepository.joinMemberByRunnerPostId(runnerPostId)
                .orElseThrow(() -> new AlarmBusinessException("러너 게시글 식별자값으로 러너 게시글과 러너(작성자)를 조회하던 도중에 오류가 발생하였습니다."));

        final Alarm newAlarm = Alarm.builder()
                .alarmTitle(new AlarmTitle(alarmTitle))
                .alarmMessage(new AlarmMessage(String.format(MESSAGE_REFERENCED_BY_RUNNER_POST.getText(), foundRunnerPost.getTitle().getValue())))
                .alarmType(AlarmType.RUNNER_POST)
                .alarmReferencedId(new AlarmReferencedId(foundRunnerPost.getId()))
                .isRead(new IsRead(false))
                .member(foundRunnerPost.getRunner().getMember())
                .build();

        alarmCommandRepository.save(newAlarm);
    }

    @TransactionalEventListener(phase = AFTER_COMMIT)
    @Transactional(propagation = REQUIRES_NEW)
    public void subscribeRunnerPostReviewStatusDoneEvent(final RunnerPostReviewStatusDoneEvent event) {
        createRunnerPostAlarm(event.runnerPostId(), TITLE_RUNNER_POST_REVIEW_STATUS_DONE.getText());
    }

    @TransactionalEventListener(phase = AFTER_COMMIT)
    @Transactional(propagation = REQUIRES_NEW)
    public void subscribeRunnerPostAssignSupporterEvent(final RunnerPostAssignSupporterEvent event) {
        final RunnerPost foundRunnerPost = runnerPostQueryRepository.joinSupporterByRunnerPostId(event.runnerPostId())
                .orElseThrow(() -> new AlarmBusinessException("러너 게시글 식별자값으로 러너 게시글과 서포터(지원자)를 조회하던 도중에 오류가 발생하였습니다."));

        final Alarm newAlarm = Alarm.builder()
                .alarmTitle(new AlarmTitle(TITLE_RUNNER_POST_ASSIGN_SUPPORTER.getText()))
                .alarmMessage(new AlarmMessage(String.format(MESSAGE_REFERENCED_BY_RUNNER_POST.getText(), foundRunnerPost.getTitle().getValue())))
                .alarmType(AlarmType.RUNNER_POST)
                .alarmReferencedId(new AlarmReferencedId(foundRunnerPost.getId()))
                .isRead(new IsRead(false))
                .member(foundRunnerPost.getSupporter().getMember())
                .build();

        alarmCommandRepository.save(newAlarm);
    }
}
