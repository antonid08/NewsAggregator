# Requirements

This is single screen application. On the screen user can see list of news from different sources, sorted by date (from new to old).
Every news is represented by card containing this information: image, title, source.

#### Cache
The application should use offline-first approach. When user open the app, they see cached news (or placeholder) and loader. Cache size is restricted by 200 news.
#### Fresh news
When the application decide that fresh news appeared, corresponding button should be shown. After clicking this button new items shown, old news cleared. Application should automatically check for fresh news.
#### Pagination
User should be able to load older news. Page size - 20. Pagination to newer news is not available.
