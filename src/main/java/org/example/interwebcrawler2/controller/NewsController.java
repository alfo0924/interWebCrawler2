package org.example.interwebcrawler2.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.interwebcrawler2.model.NewsResponse;
import org.example.interwebcrawler2.service.NewsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/news")
@RequiredArgsConstructor
@Slf4j
public class NewsController {

    private final NewsService newsService;

    @GetMapping("/category/{category}")
    public ResponseEntity<NewsResponse> getNewsByCategory(@PathVariable String category) {
        log.info("Received request for news in category: {}", category);
        NewsResponse response = newsService.getNewsByCategory(category);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<NewsResponse> searchNews(@RequestParam String query) {
        log.info("Received search request with query: {}", query);
        NewsResponse response = newsService.searchNews(query);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/top-headlines")
    public ResponseEntity<NewsResponse> getTopHeadlines() {
        log.info("Received request for top headlines");
        NewsResponse response = newsService.getTopHeadlines();
        return ResponseEntity.ok(response);
    }
}
