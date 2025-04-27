package org.example.interwebcrawler2.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class HtmlParser {

    private static final String DEFAULT_USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36";
    private static final int TIMEOUT = 10000; // 10 seconds

    /**
     * Extract the main image from an article URL
     *
     * @param url the article URL
     * @return the URL of the main image, or null if not found
     * @throws IOException if there's an error connecting to the URL
     */
    public String extractImageFromArticle(String url) throws IOException {
        log.debug("Extracting image from article: {}", url);

        try {
            Document doc = Jsoup.connect(url)
                    .userAgent(DEFAULT_USER_AGENT)
                    .timeout(TIMEOUT)
                    .get();

            // Try to find Open Graph image tag first (most reliable)
            Elements ogImage = doc.select("meta[property=og:image]");
            if (!ogImage.isEmpty()) {
                String imageUrl = ogImage.attr("content");
                if (!imageUrl.isEmpty()) {
                    log.debug("Found Open Graph image: {}", imageUrl);
                    return imageUrl;
                }
            }

            // Try Twitter card image
            Elements twitterImage = doc.select("meta[name=twitter:image]");
            if (!twitterImage.isEmpty()) {
                String imageUrl = twitterImage.attr("content");
                if (!imageUrl.isEmpty()) {
                    log.debug("Found Twitter card image: {}", imageUrl);
                    return imageUrl;
                }
            }

            // Try to find the first large image in the article
            Elements images = doc.select("article img, .article img, .content img, .main img");
            if (images.isEmpty()) {
                // If no images found in specific containers, try all images
                images = doc.select("img");
            }

            for (Element img : images) {
                // Skip small images, icons, etc.
                if (isLikelyArticleImage(img)) {
                    String imageUrl = img.absUrl("src");
                    if (!imageUrl.isEmpty()) {
                        log.debug("Found article image: {}", imageUrl);
                        return imageUrl;
                    }
                }
            }

            log.debug("No suitable image found for article: {}", url);
            return null;

        } catch (IOException e) {
            log.error("Error extracting image from article {}: {}", url, e.getMessage());
            throw e;
        }
    }

    private boolean isLikelyArticleImage(Element img) {
        // Check if image has width/height attributes
        String width = img.attr("width");
        String height = img.attr("height");

        // Try to parse width/height
        int widthValue = 0;
        int heightValue = 0;

        try {
            if (!width.isEmpty()) widthValue = Integer.parseInt(width);
            if (!height.isEmpty()) heightValue = Integer.parseInt(height);
        } catch (NumberFormatException e) {
            // Ignore parsing errors
        }

        // Check if image has reasonable dimensions
        if (widthValue > 300 && heightValue > 200) {
            return true;
        }

        // Check if image is in a figure element or has article-image class
        if (img.parents().is("figure") ||
                img.hasClass("article-image") ||
                img.hasClass("featured-image") ||
                img.hasClass("main-image")) {
            return true;
        }

        // Check if image has alt text (more likely to be content image)
        String alt = img.attr("alt");
        return !alt.isEmpty() && alt.length() > 10;
    }
}
