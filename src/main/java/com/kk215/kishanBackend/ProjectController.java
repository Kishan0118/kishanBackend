package com.kk215.kishanBackend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

/**
 * ProjectController - REST API endpoints for project operations
 * Handles requests from frontend for both projects page and view_project page
 */
@CrossOrigin(origins = "*")  // Allow requests from any origin (can be restricted later)
@RestController
@RequestMapping("/api")
public class ProjectController {

    @Autowired
    private FirebaseService firebaseService;

    /**
     * GET /api/projects
     * Fetch all projects (for Projects Page)
     *
     * @return List of all projects in DTO format
     */
    @GetMapping("/projects")
    public ResponseEntity<List<ProjectDTO>> getAllProjects()
            throws ExecutionException, InterruptedException {

        List<ProjectDTO> projects = firebaseService.getAllProjects();
        return ResponseEntity.ok(projects);
    }

    /**
     * GET /api/projects/{id}
     * Fetch a single project by ID (for View Project Page)
     *
     * @param id The project ID from Firestore
     * @return Project details or 404 if not found
     */
    @GetMapping("/projects/{id}")
    public ResponseEntity<ProjectDTO> getProjectById(@PathVariable String id)
            throws ExecutionException, InterruptedException {

        Optional<ProjectDTO> project = firebaseService.getProjectById(id);

        if (project.isPresent()) {
            return ResponseEntity.ok(project.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
    }

    /**
     * GET /api/projects/category/{category}
     * Fetch projects by category (for filtering)
     *
     * @param category The category filter (e.g., "websites", "android", "ai")
     * @return List of projects in the specified category
     */
    @GetMapping("/projects/category/{category}")
    public ResponseEntity<List<ProjectDTO>> getProjectsByCategory(@PathVariable String category)
            throws ExecutionException, InterruptedException {

        List<ProjectDTO> projects = firebaseService.getProjectsByCategory(category);
        return ResponseEntity.ok(projects);
    }
}
