package org.example.interwebcrawler2.service;

import org.example.interwebcrawler2.model.NewsResponse;

public interface NewsService {

    /**
     * Get news by category
     * @param category the news category
     * @return NewsResponse containing articles for the specified category
     */
    NewsResponse getNewsByCategory(String category);

    /**
     * Search news by query
     * @param query the search query
     * @return NewsResponse containing articles matching the query
     */
    NewsResponse searchNews(String query);

    /**
     * Get top headlines
     * @return NewsResponse containing top headline articles
     */
    NewsResponse getTopHeadlines();
}
