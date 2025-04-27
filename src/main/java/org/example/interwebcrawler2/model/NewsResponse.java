package org.example.interwebcrawler2.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsResponse {
    private String status;
    private int totalResults;
    private List<NewsItem> articles;
    private String category;
    private String query;
    private String errorMessage;
}
