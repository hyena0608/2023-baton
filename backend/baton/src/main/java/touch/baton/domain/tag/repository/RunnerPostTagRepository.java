package touch.baton.domain.tag.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import touch.baton.domain.tag.RunnerPostTag;

import java.util.List;

public interface RunnerPostTagRepository extends JpaRepository<RunnerPostTag, Long> {

    @Query("""
            select rpt
            from RunnerPostTag rpt
            join fetch Tag tag on rpt.tag.id = tag.id
            where rpt.runnerPost.id = :runnerPostId
            """)
    List<RunnerPostTag> joinTagsByRunnerPostId(final Long runnerPostId);
}