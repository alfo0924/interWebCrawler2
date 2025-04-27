package org.example.interwebcrawler2.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.interwebcrawler2.model.NewsItem;
import org.example.interwebcrawler2.model.NewsResponse;
import org.example.interwebcrawler2.util.RssParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class NewsServiceImpl implements NewsService {

    private final RestTemplate restTemplate;
    private final RssParser rssParser;

    @Value("${news.default.language:zh-TW}")
    private String defaultLanguage;

    @Value("${news.default.country:TW}")
    private String defaultCountry;

    private static final Map<String, String> CATEGORY_TOPICS = new HashMap<>();

    static {
        CATEGORY_TOPICS.put("world", "CAAqJggKIiBDQkFTRWdvSUwyMHZNRGx1YlY4U0FtVnVHZ0pWVXlnQVAB");
        CATEGORY_TOPICS.put("business", "CAAqJggKIiBDQkFTRWdvSUwyMHZNRGRqTVhZU0FtVnVHZ0pWVXlnQVAB");
        CATEGORY_TOPICS.put("technology", "CAAqJggKIiBDQkFTRWdvSUwyMHZNRGRqTVhZU0FtVnVHZ0pWVXlnQVAB");
        CATEGORY_TOPICS.put("science", "CAAqJggKIiBDQkFTRWdvSUwyMHZNRFp0Y1RjU0FtVnVHZ0pWVXlnQVAB");
        CATEGORY_TOPICS.put("health", "CAAqIQgKIhtDQkFTRGdvSUwyMHZNR3QwTlRFU0FtVnVLQUFQAQ");
        CATEGORY_TOPICS.put("entertainment", "CAAqJggKIiBDQkFTRWdvSUwyMHZNREpxYW5RU0FtVnVHZ0pWVXlnQVAB");
        CATEGORY_TOPICS.put("sports", "CAAqJggKIiBDQkFTRWdvSUwyMHZNRFp1ZEdvU0FtVnVHZ0pWVXlnQVAB");
    }

    @Override
    @Cacheable(value = "newsCategory", key = "#category")
    public NewsResponse getNewsByCategory(String category) {
        log.debug("Fetching news for category: {}", category);

        try {
            String topicId = CATEGORY_TOPICS.getOrDefault(category.toLowerCase(), CATEGORY_TOPICS.get("world"));
            String rssUrl = String.format(
                    "https://news.google.com/rss/topics/%s?hl=%s&gl=%s&ceid=%s:%s",
                    topicId, defaultLanguage, defaultCountry, defaultCountry, defaultLanguage
            );

            log.debug("RSS URL: {}", rssUrl);

            String rssContent = restTemplate.getForObject(rssUrl, String.class);
            List<NewsItem> articles = rssParser.parseRssFeed(rssContent, category);

            return NewsResponse.builder()
                    .status("ok")
                    .totalResults(articles.size())
                    .articles(articles)
                    .category(category)
                    .build();

        } catch (RestClientException e) {
            log.error("Error fetching news for category {}: {}", category, e.getMessage());
            return createErrorResponse("Error fetching news: " + e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error fetching news for category {}: {}", category, e.getMessage());
            return createErrorResponse("Unexpected error: " + e.getMessage());
        }
    }

    @Override
    @Cacheable(value = "newsSearch", key = "#query")
    public NewsResponse searchNews(String query) {
        log.debug("Searching news with query: {}", query);

        try {
            String rssUrl = String.format(
                    "https://news.google.com/rss/search?q=%s&hl=%s&gl=%s&ceid=%s:%s",
                    query, defaultLanguage, defaultCountry, defaultCountry, defaultLanguage
            );

            log.debug("RSS URL: {}", rssUrl);

            String rssContent = restTemplate.getForObject(rssUrl, String.class);
            List<NewsItem> articles = rssParser.parseRssFeed(rssContent, "search");

            return NewsResponse.builder()
                    .status("ok")
                    .totalResults(articles.size())
                    .articles(articles)
                    .query(query)
                    .build();

        } catch (RestClientException e) {
            log.error("Error searching news with query {}: {}", query, e.getMessage());
            return createErrorResponse("Error searching news: " + e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error searching news with query {}: {}", query, e.getMessage());
            return createErrorResponse("Unexpected error: " + e.getMessage());
        }
    }

    @Override
    @Cacheable(value = "topHeadlines")
    public NewsResponse getTopHeadlines() {
        log.debug("Fetching top headlines");

        try {
            String rssUrl = String.format(
                    "https://news.google.com/rss?hl=%s&gl=%s&ceid=%s:%s",
                    defaultLanguage, defaultCountry, defaultCountry, defaultLanguage
            );

            log.debug("RSS URL: {}", rssUrl);

            String rssContent = restTemplate.getForObject(rssUrl, String.class);
            List<NewsItem> articles = rssParser.parseRssFeed(rssContent, "top");

            return NewsResponse.builder()
                    .status("ok")
                    .totalResults(articles.size())
                    .articles(articles)
                    .category("top")
                    .build();

        } catch (RestClientException e) {
            log.error("Error fetching top headlines: {}", e.getMessage());
            return createErrorResponse("Error fetching top headlines: " + e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error fetching top headlines: {}", e.getMessage());
            return createErrorResponse("Unexpected error: " + e.getMessage());
        }
    }

    private NewsResponse createErrorResponse(String errorMessage) {
        return NewsResponse.builder()
                .status("error")
                .totalResults(0)
                .articles(new ArrayList<>())
                .errorMessage(errorMessage)
                .build();
    }
}
