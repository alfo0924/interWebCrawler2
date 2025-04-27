# interWebCrawler2

## 國際新聞爬蟲系統 (後端)

這是一個使用 Spring Boot 開發的 Google News 國際新聞爬蟲系統後端。系統通過解析 Google News RSS Feed 獲取最新國際新聞，並提供 RESTful API 接口供前端調用。

### 技術棧

- **Spring Boot 2.7.x**: 核心框架
- **Spring WebFlux**: 非阻塞 HTTP 請求
- **Rome/RomeTools**: RSS 解析
- **JSoup**: HTML 解析
- **Lombok**: 減少樣板代碼
- **Spring Cache (Caffeine)**: 緩存功能

### 主要功能

1. **分類新聞獲取**: 支持獲取不同分類的新聞 (國際、商業、科技、科學、健康、娛樂、體育)
2. **關鍵詞搜索**: 支持搜索特定關鍵詞的新聞
3. **頭條新聞**: 獲取熱門頭條新聞
4. **緩存機制**: 使用 Caffeine 實現高效緩存，減少對 Google News 的請求
5. **圖片提取**: 自動從新聞文章中提取圖片

### API 接口

1. **獲取分類新聞**
  - URL: `/api/news/category/{category}`
  - Method: GET
  - 參數: `category` - 新聞分類 (world, business, technology, science, health, entertainment, sports)
  - 返回: 新聞列表

2. **搜索新聞**
  - URL: `/api/news/search`
  - Method: GET
  - 參數: `query` - 搜索關鍵詞
  - 返回: 新聞列表

3. **獲取頭條新聞**
  - URL: `/api/news/top-headlines`
  - Method: GET
  - 返回: 頭條新聞列表

### 運行方式

1. 確保已安裝 Java 11 或更高版本
2. 克隆項目到本地
3. 在項目根目錄執行: `./mvnw spring-boot:run`
4. 服務將在 `http://localhost:8080` 啟動

### 配置說明

可在 `application.properties` 中修改以下配置:

- `server.port`: 服務端口
- `news.default.language`: 默認語言
- `news.default.country`: 默認國家
- `news.cache.ttl`: 緩存過期時間 (秒)

### 注意事項

- 本項目僅用於學習和研究目的
- 請遵守 Google 的服務條款和使用政策
- 過於頻繁的請求可能會導致 IP 被臨時封鎖
