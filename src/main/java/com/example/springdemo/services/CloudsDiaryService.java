package com.example.springdemo.services;

import com.cloudinary.utils.ObjectUtils;
import com.example.springdemo.models.SingletonCloudinary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.*;

@Service
public class CloudsDiaryService {

    /**
     * This function handle upload 1 image into cloud.
     * @param image the image MultipartFile
     * @param savedName String name that you want the image can be saved.
     * @return the image URL to access cloud.
     * */
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

    /**
     * This function handle upload 1 image into cloud.
     * @param image the image File
     * @param savedName String name that you want the image can be saved.
     * @return the image URL to access cloud.
     * */
    public String uploadImage(File image, String savedName) {
        String endURL = "";
        Map params = ObjectUtils.asMap(
                "folder", "SpringForum/Images",
                "resource_type", "image",
                "public_id", savedName
        );
        try {
            Map result = SingletonCloudinary.getInstance().uploader().upload(image, params);
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
        endURL = "https://res.cloudinary.com/ddczpe6gq/image/upload/SpringForum/Images/"+savedName+".png";
        return endURL;
    }

    /**
     * This function handle upload multiple image into cloud.
     * @param images list MultipartFile of images.
     * @return list string of image URL.
     * */
    public List<String> uploadImages (List<MultipartFile> images) {
        List<String> imageURL = new ArrayList<>();
        for(MultipartFile image : images) {
            String imageName = UUID.randomUUID().toString();
            imageURL.add(uploadImage(image, imageName));
        }
        return imageURL;
    }

    /**
     * This function handle upload 1 image into cloud.
     * @param image the image data string encode by base64
     * @param savedName String name that you want the image can be saved.
     * @return the image URL to access cloud.
     * */
    public String uploadImageBase64(String image, String savedName) {
        image = image.split(",")[1];
        String endURL = "";
        Map params = ObjectUtils.asMap(
                "folder", "SpringForum/Images",
                "resource_type", "image",
                "public_id", savedName
        );
        try {
            Map result = SingletonCloudinary.getInstance().uploader().upload(Base64.getDecoder().decode(image.getBytes(StandardCharsets.UTF_8)), params);
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
        endURL = "https://res.cloudinary.com/ddczpe6gq/image/upload/SpringForum/Images/"+savedName+".png";
        return endURL;
    }

    /**
     * This function handle upload multiple image into cloud.
     * @param images list string of image data encode by base64.
     * @return list string of image URL.
     * */
    public List<String> uploadImagesBase64 (List<String> images) {
        List<String> imageURL = new ArrayList<>();
        for(String image : images) {
            String imageName = UUID.randomUUID().toString();
            imageURL.add(uploadImageBase64(image, imageName));
        }
        return imageURL;
    }
}
