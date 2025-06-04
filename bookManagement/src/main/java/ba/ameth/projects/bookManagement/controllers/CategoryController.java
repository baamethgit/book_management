package ba.ameth.projects.bookManagement.controllers;

import ba.ameth.projects.bookManagement.dto.CategoryDto;
import ba.ameth.projects.bookManagement.entities.User;
import ba.ameth.projects.bookManagement.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getMyCategories(Authentication auth) {
        User user = (User) auth.getPrincipal();
        List<CategoryDto> categories = categoryService.getUserCategories(user.getId());
        return ResponseEntity.ok(categories);
    }

    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody Map<String, String> request, Authentication auth) {
        try {
            User user = (User) auth.getPrincipal();
            String name = request.get("name");
            String description = request.get("description");

            if (name == null || name.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Le nom est obligatoire"));
            }

            CategoryDto category = categoryService.createCategory(name.trim(), description, user.getId(), user);
            return ResponseEntity.ok(category);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable Long id, @RequestBody Map<String, String> request, Authentication auth) {
        try {
            User user = (User) auth.getPrincipal();
            String name = request.get("name");
            String description = request.get("description");

            if (name == null || name.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Le nom est obligatoire"));
            }

            CategoryDto category = categoryService.updateCategory(id, name.trim(), description, user.getId());
            return ResponseEntity.ok(category);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id, Authentication auth) {
        try {
            User user = (User) auth.getPrincipal();
            categoryService.deleteCategory(id, user.getId());
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
