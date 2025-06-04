package ba.ameth.projects.bookManagement.service;

import ba.ameth.projects.bookManagement.dto.CategoryDto;
import ba.ameth.projects.bookManagement.entities.Category;
import ba.ameth.projects.bookManagement.entities.User;
import ba.ameth.projects.bookManagement.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<CategoryDto> getUserCategories(Long userId) {
        return categoryRepository.findByUserId(userId)
                .stream()
                .map(CategoryDto::new)
                .collect(Collectors.toList());
    }

    public CategoryDto createCategory(String name, String description, Long userId, User user) {
        // Vérifier si le nom existe déjà pour cet utilisateur
        if (categoryRepository.existsByNameAndUserId(name, userId)) {
            throw new RuntimeException("Une catégorie avec ce nom existe déjà");
        }

        Category category = new Category(name, description, user);
        Category saved = categoryRepository.save(category);
        return new CategoryDto(saved);
    }

    public CategoryDto updateCategory(Long categoryId, String name, String description, Long userId) {
        Category category = categoryRepository.findByIdAndUserId(categoryId, userId)
                .orElseThrow(() -> new RuntimeException("Catégorie non trouvée"));

        // Vérifier le nom uniquement si il a changé
        if (!category.getName().equals(name) && categoryRepository.existsByNameAndUserId(name, userId)) {
            throw new RuntimeException("Une catégorie avec ce nom existe déjà");
        }

        category.setName(name);
        category.setDescription(description);
        Category saved = categoryRepository.save(category);
        return new CategoryDto(saved);
    }

    public void deleteCategory(Long categoryId, Long userId) {
        Category category = categoryRepository.findByIdAndUserId(categoryId, userId)
                .orElseThrow(() -> new RuntimeException("Catégorie non trouvée"));


        categoryRepository.delete(category);
    }
}

