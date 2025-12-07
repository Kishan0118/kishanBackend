package com.kk215.kishanBackend;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Project Entity - Represents a portfolio project
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Project {

    private String id;
    private String name;
    private String category;  // "websites", "android", "ai", "automation"
    private String description;
    private String tech;  // Comma-separated: "Java,Spring Boot,MySQL"
    private String screenshots;  // Comma-separated: "/image/pic1.jpg,/image/pic2.jpg"
    private String liveLink;
    private String githubLink;
    private String apkLink;  // For Android projects
    private Long createdAt;  // Timestamp
}
