# Google 國際新聞爬蟲系統 (SpringBoot )

## 項目結構

### 後端項目 (interWebCrawler2)

```
interWebCrawler2/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── webcrawler/
│   │   │           ├── InterWebCrawler2Application.java
│   │   │           ├── config/
│   │   │           │   ├── WebConfig.java
│   │   │           │   └── RestTemplateConfig.java
│   │   │           ├── controller/
│   │   │           │   └── NewsController.java
│   │   │           ├── service/
│   │   │           │   ├── NewsService.java
│   │   │           │   └── NewsServiceImpl.java
│   │   │           ├── model/
│   │   │           │   ├── NewsItem.java
│   │   │           │   ├── NewsSource.java
│   │   │           │   └── NewsResponse.java
│   │   │           └── util/
│   │   │               ├── RssParser.java
│   │   │               └── HtmlParser.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── static/
│   └── test/
│       └── java/
│           └── com/
│               └── webcrawler/
│                   └── InterWebCrawler2ApplicationTests.java
├── pom.xml
└── README.md
```

## 技術選型

### 後端技術

- **框架**: Spring Boot 2.7.x
- **構建工具**: Maven
- **Java 版本**: Java 11
- **主要依賴**:
    - spring-boot-starter-web: 提供 Web 相關功能
    - spring-boot-starter-webflux: 用於非阻塞 HTTP 請求
    - rome/rometools: 用於解析 RSS feed
    - jsoup: 用於 HTML 解析
    - lombok: 減少樣板代碼
    - spring-boot-starter-cache: 提供緩存功能

## 系統設計

### 後端設計

1. **Controller 層**: 提供 RESTful API 接口，接收前端請求
2. **Service 層**: 實現業務邏輯，包括獲取和解析新聞數據
3. **Model 層**: 定義數據模型
4. **Util 層**: 提供工具類，如 RSS 解析器和 HTML 解析器



### 數據流

1. 用戶在前端選擇新聞分類或搜索關鍵詞
2. 前端發送請求到後端 API
3. 後端從 Google News 獲取數據
4. 後端解析數據並返回給前端
5. 前端渲染新聞卡片

### 特色功能

1. **分類瀏覽**: 支持多種新聞分類
2. **關鍵詞搜索**: 支持搜索特定關鍵詞的新聞
3. **響應式設計**: 適應不同屏幕尺寸
4. **動態背景**: 橘色到黑色的漸變背景，每12秒變換一次
5. **緩存機制**: 後端實現緩存，減少對 Google News 的請求
6. **錯誤處理**: 完善的錯誤處理機制
7. **跨域處理**: 後端處理跨域問題，無需前端代理

## 實現細節

### 後端實現

1. **獲取新聞數據**: 使用 RestTemplate 或 WebClient 從 Google News RSS feed 獲取數據
2. **解析 RSS**: 使用 Rome/RomeTools 解析 RSS feed
3. **提取圖片**: 從新聞描述或鏈接中提取圖片 URL
4. **緩存機制**: 使用 Spring Cache 緩存新聞數據，減少請求次數
5. **跨域配置**: 配置 CORS 允許前端跨域請求

