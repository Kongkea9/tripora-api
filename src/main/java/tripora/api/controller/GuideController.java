package tripora.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tripora.api.dto.GuideRequest;
import tripora.api.dto.GuideResponse;
import tripora.api.service.guide.GuideService;

import java.util.List;

@RestController
@RequestMapping("v1/api/guides")
@RequiredArgsConstructor
public class GuideController {

    private final GuideService guideService;

    @GetMapping
    public List<GuideResponse> getAll() {
        return guideService.getAll();
    }

    @PostMapping
    public GuideResponse create(@Valid @RequestBody GuideRequest req) {
        return guideService.create(req);
    }

    @PutMapping("/{id}")
    public GuideResponse update(
            @PathVariable Integer id,
            @Valid @RequestBody GuideRequest req
    ) {
        return guideService.update(id, req);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        guideService.delete(id);
    }
}