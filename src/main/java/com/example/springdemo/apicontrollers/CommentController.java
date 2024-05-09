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

    @PostMapping("/api/comment/quantity")
    public ResponseEntity getQuantity (@RequestBody Map<String, String> request) {
        CommentService commentService = new CommentService();
        int articleId = Integer.parseInt(request.get("articleId"));
        Map<String,Integer> quantity = commentService.getQuantity(articleId);
        if(!quantity.containsKey("quantity")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(CommentService.createMessage("NOT_FOUND"));
        }
        return ResponseEntity.status(HttpStatus.OK).body(quantity);
    }

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
