package com.example.springdemo.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

public class SingletonCloudinary {
    private static Cloudinary cloudinary = null;

    /**
     * This function follow the singleton pattern.
     * This function handle get 1 instance of Cloudinary.
     * @return cloudinary instance to interact with cloud.
     * */
    public static Cloudinary getInstance() {
        if (cloudinary == null) {
            cloudinary = new Cloudinary(ObjectUtils.asMap(
                    "cloud_name", "ddczpe6gq",
                    "api_key", "568155832217452",
                    "api_secret", "J8ITlIPkYy0oSzIw77YeH132hNQ",
                    "secure", true
            ));
        }
        return cloudinary;
    }
}
