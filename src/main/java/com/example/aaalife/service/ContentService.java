package com.example.aaalife.service;

import org.springframework.stereotype.Service;

@Service
public class ContentService {
    public static String getContent(String fileLocation) {
        // Placeholder implementation. In a real application, this would be a Strategy object to retrieve the content from the specified location/URI.
        return "Content for file at location: " + fileLocation;
    }
}