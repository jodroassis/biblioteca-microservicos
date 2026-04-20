package school.sptech.catalogo_service.dto;

import school.sptech.catalogo_service.model.Livro;

public record LivroResponse(String isbn, String titulo, String autor, int ano, String status) {

    // converte Entidade em DTO de resposta
    public static LivroResponse fromEntity(Livro livro) {
        return new LivroResponse(
                livro.getIsbn(),
                livro.getTitulo(),
                livro.getAutor(),
                livro.getAno(),
                livro.getStatus().name()
        );
    }
}
