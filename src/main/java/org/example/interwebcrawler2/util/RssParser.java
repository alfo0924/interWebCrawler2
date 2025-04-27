package org.example.interwebcrawler2.util;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.interwebcrawler2.model.NewsItem;
import org.example.interwebcrawler2.model.NewsSource;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
@Slf4j
public class RssParser {

    private final HtmlParser htmlParser;

    private static final Pattern SOURCE_PATTERN = Pattern.compile("<font color=\"#6f6f6f\">(.*?)</font>");
    private static final Pattern IMG_PATTERN = Pattern.compile("<img src=\"(.*?)\"");

    public List<NewsItem> parseRssFeed(String rssContent, String category) {
        List<NewsItem> newsItems = new ArrayList<>();

        try {
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(
                    new ByteArrayInputStream(rssContent.getBytes(StandardCharsets.UTF_8))
            ));

            for (SyndEntry entry : feed.getEntries()) {
                NewsItem newsItem = convertToNewsItem(entry, category);
                newsItems.add(newsItem);
            }

        } catch (Exception e) {
            log.error("Error parsing RSS feed: {}", e.getMessage());
        }

        return newsItems;
    }

    private NewsItem convertToNewsItem(SyndEntry entry, String category) {
        String title = entry.getTitle();
        String link = entry.getLink();
        Date publishedDate = entry.getPublishedDate();
        String description = entry.getDescription() != null ? entry.getDescription().getValue() : "";

        // Extract source from description
        String sourceName = extractSource(description);

        // Extract image URL from description or fetch it from the article
        String imageUrl = extractImageUrl(description);
        if (imageUrl == null || imageUrl.isEmpty()) {
            try {
                imageUrl = htmlParser.extractImageFromArticle(link);
            } catch (Exception e) {
                log.warn("Failed to extract image from article {}: {}", link, e.getMessage());
                imageUrl = "https://via.placeholder.com/350x200?text=No+Image";
            }
        }

        // Clean description text
        String cleanDescription = description.replaceAll("<[^>]*>", "").trim();

        return NewsItem.builder()
                .title(title)
                .description(cleanDescription)
                .url(link)
                .urlToImage(imageUrl)
                .publishedAt(publishedDate)
                .source(NewsSource.builder().name(sourceName).build())
                .category(category)
                .build();
    }

    private String extractSource(String description) {
        Matcher matcher = SOURCE_PATTERN.matcher(description);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "Google News";
    }

    private String extractImageUrl(String description) {
        Matcher matcher = IMG_PATTERN.matcher(description);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
}
