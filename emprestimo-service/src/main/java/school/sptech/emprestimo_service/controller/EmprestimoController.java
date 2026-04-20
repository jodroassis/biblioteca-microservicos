package school.sptech.emprestimo_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import school.sptech.emprestimo_service.model.Emprestimo;
import school.sptech.emprestimo_service.service.EmprestimoService;

@RestController
@RequestMapping("/emprestimos")
public class EmprestimoController {

    private final EmprestimoService emprestimoService;

    public EmprestimoController(EmprestimoService emprestimoService) {
        this.emprestimoService = emprestimoService;
    }

    @PostMapping
    public ResponseEntity<Emprestimo> realizarEmprestimo(
            @RequestParam String isbn,
            @RequestParam String emailUsuario) {
        Emprestimo emprestimo = emprestimoService.realizarEmprestimo(isbn, emailUsuario);
        return ResponseEntity.status(201).body(emprestimo);
    }
}
