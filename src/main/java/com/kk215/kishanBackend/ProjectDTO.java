package com.kk215.kishanBackend;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * ProjectDTO - Data Transfer Object for API responses
 * Converts comma-separated strings into lists for cleaner JSON
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDTO {

    private String id;
    private String name;
    private String category;
    private List<String> tech;  // Converted from comma-separated string
    private String description;
    private List<String> screenshots;  // Converted from comma-separated string
    private String liveLink;
    private String githubLink;
    private String apkLink;
    private Long createdAt;
}
