package school.sptech.emprestimo_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import school.sptech.emprestimo_service.model.Emprestimo;

import java.util.List;

public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {
    List<Emprestimo> findByEmailUsuario(String emailUsuario);
    List<Emprestimo> findByIsbn(String isbn);
}
