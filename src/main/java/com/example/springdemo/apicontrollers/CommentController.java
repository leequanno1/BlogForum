package com.example.springdemo.apicontrollers;

import com.example.springdemo.services.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@SuppressWarnings("ALL")
public class CommentController {

    /**
     * 1 - Get all comments by using user Id and article id.
     * This function return all comments of an article.
     * If article has no comment, return status code: NOT_FOUND.
     * Otherwise, return status code: OK.
     * @param request Request body contain 1 key: "articleId".
     * */
    @PostMapping("/api/comment/getall")
    public ResponseEntity getAll (@RequestBody Map<String, String> request) {
        CommentService commentService = new CommentService();
        int articleId = Integer.parseInt(request.get("articleId"));
        List<Object> comments = commentService.getAll(articleId);
        if(comments.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(CommentService.createMessage("NOT_FOUND"));
        }
        return ResponseEntity.status(HttpStatus.OK).body(comments);
    }

    /**
     * 2 - Get comment’s quantity by using article id.
     * This function return the comment's quantity of an article.
     * If article has no comment (zero), return status code: NOT_FOUND.
     * Otherwise, return status code: OK.
     * @param request Request body contain 1 key: "articleId".
     * */
    @PostMapping("/api/comment/quantity")
    public ResponseEntity getQuantity (@RequestBody Map<String, String> request) {
        CommentService commentService = new CommentService();
        int articleId = Integer.parseInt(request.get("articleId"));
        Map<String,Integer> quantity = commentService.getQuantity(articleId);
        if(quantity.get("quantity") == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(CommentService.createMessage("NOT_FOUND"));
        }
        return ResponseEntity.status(HttpStatus.OK).body(quantity);
    }

    /**
     * 3 - Get latest comment by user in article.
     * This function return the latest comment write
     * by user in a article.
     * If article has no comment (zero), return status code: NOT_FOUND.
     * Otherwise, return status code: OK.
     * @param request Request body contain 2 key: "userId", "articleId".
     * */
    @PostMapping("/api/comment/latest")
    public ResponseEntity getLatestComment (@RequestBody Map<String, String> request) {
        CommentService commentService = new CommentService();
        int userId = Integer.parseInt(request.get("userId"));
        int articleId = Integer.parseInt(request.get("articleId"));
        Map<String, Object> comments = commentService.getLatestComment(userId,articleId);
        if(comments.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(CommentService.createMessage("NOT_FOUND"));
        }
        return ResponseEntity.status(HttpStatus.OK).body(comments);
    }

    /**
     * 4 - Add new comment
     * This function handle adding a new comment.
     * If adding comment success, return status code: OK.
     * Otherwise, return status code: INTERNAL_SERVER_ERROR.
     * @param request Request body contain 3 key: "userId", "articleId", "content".
     * */
    @PutMapping("/api/comment/add")
    public ResponseEntity addNewComment(@RequestBody Map<String, String> request) {
        CommentService commentService = new CommentService();
        int userId = Integer.parseInt(request.get("userId"));
        int articleId = Integer.parseInt(request.get("articleId"));
        String content = request.get("content");
        boolean isInserted = commentService.createNewComment(userId,articleId,content);
        if(isInserted) {
            return ResponseEntity.status(HttpStatus.OK).body(CommentService.createMessage("OK"));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(CommentService.createMessage("INTERNAL_SERVER_ERROR"));
    }

    /**
     * 5 - Update comments (if user have authorization)
     * This function handle updating a comment's content
     * writen by this user.
     * If adding comment success, return status code: OK.
     * Otherwise, return status code: INTERNAL_SERVER_ERROR.
     * @param request Request body contain 3 key: "userId", "commentId", "content".
     * */
    @PutMapping("/api/comment/update")
    public ResponseEntity updateComment(@RequestBody Map<String, String> request) {
        CommentService commentService = new CommentService();
        int userId = Integer.parseInt(request.get("userId"));
        int commentId = Integer.parseInt(request.get("commentId"));
        String content = request.get("content");
        boolean isUpdated = commentService.updateComment(userId,commentId,content);
        if(isUpdated) {
            return ResponseEntity.status(HttpStatus.OK).body(CommentService.createMessage("OK"));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(CommentService.createMessage("INTERNAL_SERVER_ERROR"));
    }

    /**
     * 6 - Delete comments (if user have authorization)
     * This function handle deleting a comment's content
     * writen by this user.
     * If adding comment success, return status code: OK.
     * Otherwise, return status code: INTERNAL_SERVER_ERROR.
     * @param request Request body contain 2 key: "userId", "commentId".
     * */
    @PutMapping("/api/comment/delete")
    public ResponseEntity deleteComment(@RequestBody Map<String, String> request) {
        CommentService commentService = new CommentService();
        int userId = Integer.parseInt(request.get("userId"));
        int commentId = Integer.parseInt(request.get("commentId"));
        boolean isUpdated = commentService.deleteComment(userId,commentId);
        if(isUpdated) {
            return ResponseEntity.status(HttpStatus.OK).body(CommentService.createMessage("OK"));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(CommentService.createMessage("INTERNAL_SERVER_ERROR"));
    }
}
