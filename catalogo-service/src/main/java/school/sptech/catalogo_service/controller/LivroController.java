package school.sptech.catalogo_service.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import school.sptech.catalogo_service.dto.LivroRequest;
import school.sptech.catalogo_service.dto.LivroResponse;
import school.sptech.catalogo_service.model.Livro;
import school.sptech.catalogo_service.service.LivroService;

import java.util.List;

@RestController
@RequestMapping("/livros")
public class LivroController {

    private final LivroService livroService;

    public LivroController(LivroService livroService) {
        this.livroService = livroService;
    }

    @PostMapping
    public ResponseEntity<LivroResponse> cadastrarLivro(@Valid @RequestBody LivroRequest request) {
        Livro livro = request.toEntity();  // DTO -> entidade
        Livro cadastrado = livroService.cadastrarLivro(livro);
        return ResponseEntity.status(201).body(LivroResponse.fromEntity(cadastrado)); // entidade -> DTO
    }

    @GetMapping("/{isbn}")
    public ResponseEntity<LivroResponse> buscarPorIsbn(@PathVariable String isbn) {
        Livro buscado = livroService.buscarPorIsbn(isbn);
        return ResponseEntity.status(200).body(LivroResponse.fromEntity(buscado));
    }

    @GetMapping
    public ResponseEntity<List<LivroResponse>> buscar(
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) String autor
    ) {
        List<Livro> livros;

        if (titulo != null){
            livros = livroService.buscarPorTitulo(titulo);
        } else if (autor != null) {
            livros = livroService.buscarPorAutor(autor);
        } else {
            livros = livroService.buscarTodos();
        }

        List<LivroResponse> resposta = livros.stream()
                .map(LivroResponse::fromEntity)
                .toList();
        return ResponseEntity.status(200).body(resposta);
    }

    @PatchMapping("/{isbn}/emprestar")
    public ResponseEntity<LivroResponse> emprestar(@PathVariable String isbn) {
        Livro emprestado = livroService.emprestar(isbn);
        return ResponseEntity.status(200).body(LivroResponse.fromEntity(emprestado));
    }

    @PatchMapping("/{isbn}/devolver")
    public ResponseEntity<LivroResponse> devolver(@PathVariable String isbn) {
        Livro devolvido = livroService.devolver(isbn);
        return ResponseEntity.status(200).body(LivroResponse.fromEntity(devolvido));
    }

    @DeleteMapping("/{isbn}")
    public ResponseEntity<Void> deletar(@PathVariable String isbn) {
        livroService.deletar(isbn);
        return ResponseEntity.status(204).build();
    }
}
