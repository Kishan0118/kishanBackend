package com.kk215.kishanBackend;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.FirebaseApp;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

/**
 * FirebaseService - Business logic for Firebase Firestore operations
 * Handles fetching projects from Firestore and converting them to DTOs
 */
@Service
public class FirebaseService {

    private final Firestore db;

    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    /**
     * Constructor: Dependency Injection for FirebaseApp
     */
    public FirebaseService(FirebaseApp firebaseApp) {
        if (firebaseApp == null) {
            throw new IllegalStateException("Firebase App is not initialized.");
        }
        this.db = FirestoreClient.getFirestore(firebaseApp);
    }

    /**
     * Get all projects from Firestore
     * Endpoint: GET /api/projects
     */
    public List<ProjectDTO> getAllProjects() throws ExecutionException, InterruptedException {

        ApiFuture<QuerySnapshot> future = db.collection("projects").get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        List<ProjectDTO> projectDTOs = new ArrayList<>();

        for (QueryDocumentSnapshot document : documents) {
            Project project = document.toObject(Project.class);
            project.setId(document.getId());
            projectDTOs.add(convertToDTO(project));
        }

        return projectDTOs;
    }

    /**
     * Get a single project by ID from Firestore
     * Endpoint: GET /api/projects/{id}
     * Used for View Project page
     */
    public Optional<ProjectDTO> getProjectById(String id) throws ExecutionException, InterruptedException {

        ApiFuture<DocumentSnapshot> future = db.collection("projects").document(id).get();
        DocumentSnapshot document = future.get();

        if (document.exists()) {
            Project project = document.toObject(Project.class);
            project.setId(document.getId());
            return Optional.of(convertToDTO(project));
        }

        return Optional.empty();
    }

    /**
     * Get projects by category
     * Endpoint: GET /api/projects/category/{category}
     */
    public List<ProjectDTO> getProjectsByCategory(String category) throws ExecutionException, InterruptedException {

        ApiFuture<QuerySnapshot> future = db.collection("projects")
                .whereEqualTo("category", category)
                .get();

        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        List<ProjectDTO> projectDTOs = new ArrayList<>();

        for (QueryDocumentSnapshot document : documents) {
            Project project = document.toObject(Project.class);
            project.setId(document.getId());
            projectDTOs.add(convertToDTO(project));
        }

        return projectDTOs;
    }

    /**
     * Helper method: Convert Project entity to ProjectDTO
     * Handles tech and screenshots as either String (comma-separated) or List (from Firestore)
     * Supports both 'screenshot' (singular) and 'screenshots' (plural) field names
     */
    private ProjectDTO convertToDTO(Project project) {
        ProjectDTO dto = new ProjectDTO();
        dto.setId(project.getId());
        dto.setName(project.getName());
        dto.setCategory(project.getCategory());

        // Convert tech: handle both String and List formats
        dto.setTech(convertToList(project.getTech()));

        dto.setDescription(project.getDescription());

        // Convert screenshots: try 'screenshots' first, then fall back to 'screenshot' (singular)
        Object screenshotField = project.getScreenshots();
        if (screenshotField == null || (screenshotField instanceof List && ((List<?>) screenshotField).isEmpty())) {
            // If screenshots is empty, try the singular field name
            screenshotField = project.getScreenshots();
        }

        List<String> rawScreenshots = convertToList(screenshotField);
        List<String> normalizedScreens = new ArrayList<>();

        for (String s : rawScreenshots) {
            String trimmed = s.trim();
            // Remove surrounding quotes if present (Firestore sometimes adds them)
            if (trimmed.startsWith("\"") && trimmed.endsWith("\"")) {
                trimmed = trimmed.substring(1, trimmed.length() - 1).trim();
            }
            if (trimmed.isEmpty()) continue;

            // If already absolute URL, keep as-is
            if (trimmed.startsWith("http://") || trimmed.startsWith("https://")) {
                normalizedScreens.add(trimmed);
            } else if (trimmed.startsWith("/")) {
                // relative path starting with / -> prepend baseUrl
                normalizedScreens.add(baseUrl + trimmed);
            } else {
                // no leading slash -> join with baseUrl
                normalizedScreens.add(baseUrl + "/" + trimmed);
            }
        }
        dto.setScreenshots(normalizedScreens);

        dto.setLiveLink(project.getLiveLink());
        dto.setGithubLink(project.getGithubLink());
        dto.setApkLink(project.getApkLink());
        dto.setCreatedAt(project.getCreatedAt());

        return dto;
    }

    /**
     * Helper method: Convert Object (String or List) to List<String>
     * Handles both comma-separated strings and Firestore arrays
     */
    private List<String> convertToList(Object obj) {
        if (obj == null) {
            return List.of();
        }

        // If already a List, return it as List<String>
        if (obj instanceof List) {
            List<?> list = (List<?>) obj;
            List<String> result = new ArrayList<>();
            for (Object item : list) {
                if (item != null) {
                    result.add(item.toString().trim());
                }
            }
            return result;
        }

        // If a String, split by comma
        if (obj instanceof String) {
            String str = (String) obj;
            if (str.isBlank()) {
                return List.of();
            }
            List<String> result = new ArrayList<>();
            for (String item : str.split(",")) {
                String trimmed = item.trim();
                if (!trimmed.isEmpty()) {
                    result.add(trimmed);
                }
            }
            return result;
        }

        return List.of();
    }
}
