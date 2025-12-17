package dev.smd.estela.backend.service;


import dev.smd.estela.backend.dao.CategoryDAO;
import dev.smd.estela.backend.dto.ReponseCategoryDTO;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class CategoryService {

    private static final CategoryDAO categoryDao = new CategoryDAO();

    public ArrayList<ReponseCategoryDTO> listAllCategory() {
        ArrayList<ReponseCategoryDTO> categoryList = null;

        categoryList = categoryDao.findAll().stream().map(category
                -> new ReponseCategoryDTO(category.getCategoryId(), category.getName())
        ).collect(Collectors.toCollection(ArrayList::new));

        return categoryList;
    }
}
