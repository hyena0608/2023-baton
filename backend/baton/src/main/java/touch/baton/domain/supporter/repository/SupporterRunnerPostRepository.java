package touch.baton.domain.supporter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import touch.baton.domain.supporter.SupporterRunnerPost;

import java.util.List;
import java.util.Optional;

public interface SupporterRunnerPostRepository extends JpaRepository<SupporterRunnerPost, Long> {

    @Query("""
            select count(1)
            from SupporterRunnerPost srp
            group by srp.runnerPost.id
            having srp.runnerPost.id in (:runnerPostIds)
            """)
    List<Long> countByRunnerPostIdIn(@Param("runnerPostIds") final List<Long> runnerPostIds);

    @Query("""
        select count(1)
        from SupporterRunnerPost srp
        group by srp.runnerPost.id
        having srp.runnerPost.id = :runnerPostId
        """)
    Optional<Integer> countByRunnerPostId(@Param("runnerPostId") final Long runnerPostId);

    void deleteBySupporterIdAndRunnerPostId(final Long supporterId, final Long runnerPostId);

    boolean existsByRunnerPostIdAndSupporterId(final Long runnerPostId, final Long supporterId);
}
