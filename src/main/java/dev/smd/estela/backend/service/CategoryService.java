package dev.smd.estela.backend.service;

import dev.smd.estela.backend.dao.CategoryDAO;
import dev.smd.estela.backend.dto.category.ReponseCategoryDTO;
import dev.smd.estela.backend.model.Category;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class CategoryService {

    private static final CategoryDAO categoryDao = new CategoryDAO();

    public ArrayList<ReponseCategoryDTO> listAllCategory() {
        var categories = categoryDao.findAll();
        if (categories == null || categories.isEmpty()) {
            return new ArrayList<>();
        }
        return categories.stream()
                .map(category -> new ReponseCategoryDTO(category.getCategoryId(), category.getName()))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public void saveCategory(String name) throws Exception {
        Category category = new Category();
        category.setName(name);

        categoryDao.save(category);
    }

    public void updateCategory(Long id, String name) throws Exception {
        Category category = new Category();
        category.setCategoryId(id);
        category.setName(name);

        categoryDao.update(category);
    }

    public void deleteCategory(Long id) throws Exception {
        if (id == null || id <= 0) {
            throw new Exception("ID inválido para exclusão.");
        }
        categoryDao.deleteById(id);
    }
}
