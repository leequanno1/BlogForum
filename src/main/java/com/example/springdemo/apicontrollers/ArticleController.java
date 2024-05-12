package com.example.springdemo.apicontrollers;

import com.example.springdemo.services.ArticleService;
import com.example.springdemo.services.StatusBodyMessageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class ArticleController {

    private final int DEFAULT_QUANTITY = 6;

    private final int DEFAULT_PAGE = 1;

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


}
