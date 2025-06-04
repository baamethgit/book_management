package ba.ameth.projects.bookManagement.controllers;

import ba.ameth.projects.bookManagement.dto.BookDto;
import ba.ameth.projects.bookManagement.dto.ReadingSessionDto;
import ba.ameth.projects.bookManagement.dto.StatsDto;
import ba.ameth.projects.bookManagement.entities.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ba.ameth.projects.bookManagement.service.StatsService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class StatsController {

    private StatsService statsService;

    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    @GetMapping("/stats")
    public ResponseEntity<StatsDto> getUserStats(Authentication auth) {
        User user = (User) auth.getPrincipal();
        StatsDto stats = statsService.getUserStats(user.getId());
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/stats/sessions")
    public ResponseEntity<List<ReadingSessionDto>> getRecentSessions(
            @RequestParam(defaultValue = "10") int limit,
            Authentication auth) {
        User user = (User) auth.getPrincipal();
        List<ReadingSessionDto> sessions = statsService.getRecentSessions(user.getId(), limit);
        return ResponseEntity.ok(sessions);
    }
}