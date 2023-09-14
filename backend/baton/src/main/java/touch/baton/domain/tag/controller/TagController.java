package touch.baton.domain.tag.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import touch.baton.domain.tag.controller.response.TagSearchResponse;
import touch.baton.domain.tag.controller.response.TagSearchResponses;
import touch.baton.domain.tag.service.TagService;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/tags")
@RestController
public class TagController {

    private final TagService tagService;

    @GetMapping("/search")
    public ResponseEntity<TagSearchResponses.Detail> readTagsByTagName(@RequestParam String tagName) {
        final List<TagSearchResponse.TagResponse> tagSearchResponses = tagService.readTagsByReducedName(tagName).stream()
                .map(TagSearchResponse.TagResponse::from)
                .toList();

        return ResponseEntity.ok(TagSearchResponses.Detail.from(tagSearchResponses));
    }
}