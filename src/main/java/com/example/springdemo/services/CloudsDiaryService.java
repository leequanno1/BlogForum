package com.example.springdemo.services;

import com.cloudinary.utils.ObjectUtils;
import com.example.springdemo.models.SingletonCloudinary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class CloudsDiaryService {
    public String uploadImage(MultipartFile image, String savedName) {
        String endURL = "";
        Map params = ObjectUtils.asMap(
                "folder", "SpringForum/Images",
                "resource_type", "image",
                "public_id", savedName
        );
        try {
            Map result = SingletonCloudinary.getInstance().uploader().upload(image.getBytes(), params);
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
        endURL = "https://res.cloudinary.com/ddczpe6gq/image/upload/SpringForum/Images/"+savedName+".png";
        return endURL;
    }

    public List<String> uploadImages (List<MultipartFile> images) {
        List<String> imageURL = new ArrayList<>();
        for(MultipartFile image : images) {
            String imageName = UUID.randomUUID().toString();
            imageURL.add(uploadImage(image, imageName));
        }
        return imageURL;
    }
}
