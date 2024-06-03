package com.example.springdemo.apicontrollers;

import com.example.springdemo.services.ArticleService;
import com.example.springdemo.services.CloudsDiaryService;
import com.example.springdemo.services.StatusBodyMessageService;
import com.example.springdemo.services.CookieService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@SuppressWarnings("ALL")
public class ArticleController {

    private CloudsDiaryService cloudsDiaryService = new CloudsDiaryService();
    private final int DEFAULT_QUANTITY = 6;

    private final int DEFAULT_PAGE = 1;

    /**
     * 1 - Get all articles
     * This function handle getting a list of article.
     * If success, return status code OK with a list of article.
     * If the list is empty, return status code NOT_FOUND
     * @param request Request body contain 2 key:
     *                "quantity" (allow null, default = 6),
     *                "page"allow null, default = 1
     * */
    @GetMapping("/api/article/getall")
    public ResponseEntity getAll(@RequestParam Map<String, String> request) {
        int quantity = DEFAULT_QUANTITY;
        int page = DEFAULT_PAGE;
        if (request.containsKey("quantity")) {
            quantity = Integer.parseInt(request.get("quantity"));
        }
        if (request.containsKey("page")) {
            page = Integer.parseInt(request.get("page"));
        }
        ArticleService articleService = new ArticleService();
        List<Object> res = articleService.getAll(quantity, page);
        if (!res.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(res);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(StatusBodyMessageService.statusNotFound());
    }

    /**
     * 2 - Get all articles by using user id
     * This function handle getting a list of article by using user id.
     * If success, return status code OK with a list of article.
     * If the list is empty, return status code NOT_FOUND.
     * Otherwise, return status code BAD_REQUEST
     * @param request Request body contain 2 key:
     *                "userId" (not null),
     *                "quantity" (allow null, default = 6),
     *                "page"allow null, default = 1
     * */
    @PostMapping("/api/article/byuserid")
    public ResponseEntity getAllByUserId(@RequestBody Map<String, String> request) {
        if (!request.containsKey("userId")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(StatusBodyMessageService.statusBadRequest());
        }
        int userID = Integer.parseInt(request.get("userId"));
        int quantity = DEFAULT_QUANTITY;
        int page = DEFAULT_PAGE;
        if (request.containsKey("quantity")) {
            quantity = Integer.parseInt(request.get("quantity"));
        }
        if (request.containsKey("page")) {
            page = Integer.parseInt(request.get("page"));
        }
        ArticleService articleService = new ArticleService();
        List<Object> res = articleService.getAllByUserID(userID,quantity, page);
        if (!res.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(res);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(StatusBodyMessageService.statusNotFound());
    }

    /**
     * 3 - Get all articles by using article title
     * This function handle getting a list of article by using article title.
     * If success, return status code OK with a list of article.
     * If the list is empty, return status code NOT_FOUND.
     * Otherwise, return status code BAD_REQUEST
     * @param request Request body contain 2 key:
     *                "title" (not null),
     *                "quantity" (allow null, default = 6),
     *                "page"allow null, default = 1
     * */
    @GetMapping("/api/article/bytitle")
    public ResponseEntity getAllByTitle(@RequestParam Map<String, String> request) {
        if (!request.containsKey("title")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(StatusBodyMessageService.statusBadRequest());
        }
        String title = request.get("title");
        int quantity = DEFAULT_QUANTITY;
        int page = DEFAULT_PAGE;
        if (request.containsKey("quantity")) {
            quantity = Integer.parseInt(request.get("quantity"));
        }
        if (request.containsKey("page")) {
            page = Integer.parseInt(request.get("page"));
        }
        ArticleService articleService = new ArticleService();
        List<Object> res = articleService.getAllByTitle(title,quantity, page);
        if (!res.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(res);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(StatusBodyMessageService.statusNotFound());
    }

    /**
     * 4 - Get all article by using tag name
     * This function handle getting a list of article by using tag name.
     * If success, return status code OK with a list of article.
     * If the list is empty, return status code NOT_FOUND.
     * Otherwise, return status code BAD_REQUEST
     * @param request Request body contain 2 key:
     *                "tagName" (not null),
     *                "quantity" (allow null, default = 6),
     *                "page"allow null, default = 1
     * */
    @GetMapping("/api/article/bytagname")
    public ResponseEntity getAllByTagName(@RequestParam Map<String, String> request) {
        if (!request.containsKey("tagName")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(StatusBodyMessageService.statusBadRequest());
        }
        String tagName = request.get("tagName");
        int quantity = DEFAULT_QUANTITY;
        int page = DEFAULT_PAGE;
        if (request.containsKey("quantity")) {
            quantity = Integer.parseInt(request.get("quantity"));
        }
        if (request.containsKey("page")) {
            page = Integer.parseInt(request.get("page"));
        }
        ArticleService articleService = new ArticleService();
        List<Object> res = articleService.getAllByTagName(tagName,quantity, page);
        if (!res.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(res);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(StatusBodyMessageService.statusNotFound());
    }

    /**
     * 5 - Get all articles from date to now
     * This function handle getting a list of article by using specific date.
     * If success, return status code OK with a list of article.
     * If the list is empty, return status code NOT_FOUND.
     * Otherwise, return status code BAD_REQUEST
     * @param request Request body contain 2 key:
     *                "date" (not null),
     *                "quantity" (allow null, default = 6),
     *                "page"allow null, default = 1
     * */
    @GetMapping("/api/article/fromdate")
    public ResponseEntity getAllFromDate(@RequestParam Map<String, String> request) {
        if (!request.containsKey("date")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(StatusBodyMessageService.statusBadRequest());
        }
        String date = request.get("date");
        int quantity = DEFAULT_QUANTITY;
        int page = DEFAULT_PAGE;
        if (request.containsKey("quantity")) {
            quantity = Integer.parseInt(request.get("quantity"));
        }
        if (request.containsKey("page")) {
            page = Integer.parseInt(request.get("page"));
        }
        ArticleService articleService = new ArticleService();
        List<Object> res = articleService.getFromDate(date,quantity, page);
        if (!res.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(res);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(StatusBodyMessageService.statusNotFound());
    }

    /**
     * 6 - Get all articles bookmark by user
     * This function handle getting a list of article by using user's bookmark.
     * If success, return status code OK with a list of article.
     * If the list is empty, return status code NOT_FOUND.
     * Otherwise, return status code BAD_REQUEST
     * @param request Request body contain 2 key:
     *                "userId" (not null),
     *                "quantity" (allow null, default = 6),
     *                "page"allow null, default = 1
     * */
    @PostMapping("/api/article/bookmark")
    public ResponseEntity getAllBookmark(@RequestBody Map<String, String> request) {
        if (!request.containsKey("userId")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(StatusBodyMessageService.statusBadRequest());
        }
        int userID = Integer.parseInt(request.get("userId"));
        int quantity = DEFAULT_QUANTITY;
        int page = DEFAULT_PAGE;
        if (request.containsKey("quantity")) {
            quantity = Integer.parseInt(request.get("quantity"));
        }
        if (request.containsKey("page")) {
            page = Integer.parseInt(request.get("page"));
        }
        ArticleService articleService = new ArticleService();
        List<Object> res = articleService.getAllBookmark(userID,quantity, page);
        if (!res.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(res);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(StatusBodyMessageService.statusNotFound());
    }

    /**
     * 7 - Get article content by using article id
     * This function handle getting information of an article by using article id.
     * If success, return status code OK with article information.
     * If the article is empty, return status code NOT_FOUND.
     * Otherwise, return status code BAD_REQUEST
     * @param request Request body contain 2 key:
     *                "userId" (allow null, default = null),
     *                "articleId" (not null)
     * */
    @PostMapping("/api/article/articleid")
    public ResponseEntity getByArticleId(@RequestBody Map<String, String> request) {
        if (!request.containsKey("articleId")) {
            return ResponseEntity.status((HttpStatus.BAD_REQUEST)).body(StatusBodyMessageService.statusBadRequest());
        }
        Integer articleId = Integer.parseInt(request.get("articleId"));
        Integer userId = null;
        if(request.containsKey("userId")){
            if (request.get("userId") != null) {
                userId =  Integer.parseInt(request.get("userId"));
            }
        }
        ArticleService articleService = new ArticleService();
        Map<String, Object> res = articleService.getByArticleId(userId, articleId);
        if(!res.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(res);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(StatusBodyMessageService.statusNotFound());
    }

//    @PostMapping("/api/article/new")
//    public ResponseEntity newArticle(@RequestParam("title") String title,
//                                     @RequestParam("content") String content,
//                                     @RequestParam("tags") String tags,
//                                     @RequestParam("images") List<MultipartFile> images,
//                                     HttpServletRequest request) {
//        ArticleService articleService = new ArticleService();
//        Integer userId = Integer.parseInt(CookieService.getCookieValue(request, CookieService.cookieUserIdKey));
//        boolean result = articleService.handelAddNewArticle(userId, title, content, tags.split(" "), images);
//        if(result) {
//            return ResponseEntity.status(HttpStatus.OK).body(StatusBodyMessageService.statusOk());
//        }
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(StatusBodyMessageService.statusInternalServerError());
//    }

    /**
     * This function handle upload article content to database and it's image to cloud.
     * @param param This param is a map contain 4 key: title, content, tags, images.
     * @param request This is the client request info, we use it to get infomation of session and cookie.
     * @return ResponEntity that is a message Object.
     * */
    @PostMapping("/api/article/newbase64")
    public ResponseEntity newArticleBase64(@RequestBody Map<String, Object> param,
                                     HttpServletRequest request) {
        String title = (String) param.get("title");
        String content = (String) param.get("content");
        String tags = (String) param.get("tags");
        List<String> images = (List<String>) param.get("images");
        ArticleService articleService = new ArticleService();
        Integer userId = Integer.parseInt(CookieService.getCookieValue(request, CookieService.cookieUserIdKey));
        boolean result = articleService.handelAddNewArticleBase64(userId, title, content, tags.split(" "), images);
        if(result) {
            return ResponseEntity.status(HttpStatus.OK).body(StatusBodyMessageService.statusOk());
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(StatusBodyMessageService.statusInternalServerError());
    }

    /**
     * Endpoint to upvote an article
     *
     * @param request the request body containing user ID and article ID
     * @return ResponseEntity indicating the status of the upvote operation
     */
    @PostMapping("/api/article/upvote")
    public ResponseEntity<?> upVoteArticle(@RequestBody Map<String, Integer> request) {
        ArticleService articleService = new ArticleService();

        int userId = request.get("userId");
        int articleId = request.get("articleId");
        int checked = request.get("checked");

        boolean success = articleService.upVoteArticle(userId, articleId, checked);
        if (success) {
            return ResponseEntity.ok("Article upvoted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upvote article");
        }
    }

    /**
     * Endpoint to downvote an article
     *
     * @param request the request body containing user ID and article ID
     * @return ResponseEntity indicating the status of the downvote operation
     */
    @PostMapping("/api/article/downvote")
    public ResponseEntity<?> downVoteArticle(@RequestBody Map<String, Integer> request) {
        ArticleService articleService = new ArticleService();

        int userId = request.get("userId");
        int articleId = request.get("articleId");
        int checked = request.get("checked");

        boolean success = articleService.downVoteArticle(userId, articleId, checked);
        if (success) {
            return ResponseEntity.ok("Article downvoted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to downvote article");
        }
    }

    /**
     * Endpoint to bookmark an article
     *
     * @param request the request body containing user ID and article ID
     * @return ResponseEntity indicating the status of the bookmark operation
     */
    @PostMapping("/api/article/addBookmark")
    public ResponseEntity<?> bookmarkArticle(@RequestBody Map<String, Integer> request) {
        ArticleService articleService = new ArticleService();

        int userId = request.get("userId");
        int articleId = request.get("articleId");
        boolean success = articleService.bookmarkArticle(userId, articleId);
        if (success) {
            return ResponseEntity.ok("Article bookmarked successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to bookmark article");
        }
    }

    /**
     * Endpoint to unbookmark an article
     *
     * @param request the request body containing user ID and article ID
     * @return ResponseEntity indicating the status of the unbookmark operation
     */
    @PostMapping("/api/article/unbookmark")
    public ResponseEntity<?> unbookmarkArticle(@RequestBody Map<String, Integer> request) {
        ArticleService articleService = new ArticleService();

        int userId = request.get("userId");
        int articleId = request.get("articleId");
        boolean success = articleService.unbookmarkArticle(userId, articleId);
        if (success) {
            return ResponseEntity.ok("Article unbookmarked successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to unbookmark article");
        }
    }

}
