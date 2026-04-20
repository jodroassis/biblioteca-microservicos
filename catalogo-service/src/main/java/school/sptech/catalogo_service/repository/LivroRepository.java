package school.sptech.catalogo_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import school.sptech.catalogo_service.model.Livro;

import java.util.List;

public interface LivroRepository extends JpaRepository<Livro, String> {
    List<Livro> findByTituloContainingIgnoreCase(String titulo);
    List<Livro> findByAutorContainingIgnoreCase(String autor);
}
