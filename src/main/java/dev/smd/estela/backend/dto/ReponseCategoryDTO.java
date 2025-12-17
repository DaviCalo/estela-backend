package dev.smd.estela.backend.dto;

public class ReponseCategoryDTO {
        private Long categoryId;
        private String name;

        public ReponseCategoryDTO(Long categoryId, String name) {
                this.categoryId = categoryId;
                this.name = name;
        }

    @Override
    public String toString() {
        return "ReponseCategoryDTO{" + "tagcategoryId=" + categoryId + ", name=" + name + '}';
    }
}
