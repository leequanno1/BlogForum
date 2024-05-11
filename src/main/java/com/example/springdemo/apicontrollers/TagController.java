package com.example.springdemo.apicontrollers;

import com.example.springdemo.services.StatusBodyMessageService;
import com.example.springdemo.services.TagService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.PublicKey;
import java.util.List;
import java.util.Map;

@RestController
public class TagController {

    @GetMapping("/api/tag/getall")
    public ResponseEntity getAll () {
        TagService tagService = new TagService();
        List<Object> res = tagService.getAll();
        if(res.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(StatusBodyMessageService.statusNotFound());
        }
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @GetMapping("/api/tag/getsimilar")
    public ResponseEntity getSimilar(@RequestParam Map<String,String> request) {
        String name = "";
        if(request.containsKey("name")) {
            name = request.get("name");
        }
        TagService tagService = new TagService();
        List<Object> res = tagService.getSimilar(name);
        if(res.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(StatusBodyMessageService.statusNotFound());
        }
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @PutMapping("/api/tag/add")
    public ResponseEntity addTag(@RequestBody Map<String, String> request) {
        if(!request.containsKey("name")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(StatusBodyMessageService.statusBadRequest());
        }
        String name = request.get("name");
        TagService tagService = new TagService();
        boolean res = tagService.addNewTag(name);
        if(res) {
            return ResponseEntity.status(HttpStatus.OK).body(StatusBodyMessageService.statusOk());
        }
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(StatusBodyMessageService.statusNotAcceptable());
    }

    @PutMapping("/api/tag/update")
    public ResponseEntity updateTag(@RequestBody Map<String, String> request) {
        if(!request.containsKey("currentName") || !request.containsKey("newName")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(StatusBodyMessageService.statusBadRequest());
        }
        String currentName = request.get("currentName");
        String newName = request.get("newName");
        TagService tagService = new TagService();
        boolean res = tagService.updateTag(currentName, newName);
        if(res) {
            return ResponseEntity.status(HttpStatus.OK).body(StatusBodyMessageService.statusOk());
        }
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(StatusBodyMessageService.statusNotAcceptable());
    }
}
