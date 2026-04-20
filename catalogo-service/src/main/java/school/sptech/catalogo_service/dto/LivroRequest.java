package school.sptech.catalogo_service.dto;

import school.sptech.catalogo_service.model.Livro;

public record LivroRequest(String isbn, String titulo, String autor, int ano) {

    public Livro toEntity() {
        return new Livro(isbn, titulo, autor, ano);
    }
}
