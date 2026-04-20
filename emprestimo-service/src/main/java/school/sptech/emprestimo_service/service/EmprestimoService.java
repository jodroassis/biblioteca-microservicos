package school.sptech.emprestimo_service.service;

import org.springframework.stereotype.Service;
import school.sptech.emprestimo_service.messaging.EmprestimoProducer;
import school.sptech.emprestimo_service.model.Emprestimo;
import school.sptech.emprestimo_service.repository.EmprestimoRepository;

@Service
public class EmprestimoService {

    private final EmprestimoRepository repository;
    private final EmprestimoProducer producer;

    public EmprestimoService(EmprestimoRepository repository,
                             EmprestimoProducer producer) {
        this.repository = repository;
        this.producer = producer;
    }

    public Emprestimo realizarEmprestimo(String isbn, String emailUsuario) {
        Emprestimo emprestimo = new Emprestimo(isbn, emailUsuario);
        repository.save(emprestimo);
        producer.publicarEmprestimo(isbn);
        return emprestimo;
    }
}
