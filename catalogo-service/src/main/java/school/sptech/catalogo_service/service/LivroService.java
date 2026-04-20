package school.sptech.catalogo_service.service;

import org.springframework.stereotype.Service;
import school.sptech.catalogo_service.enums.StatusLivro;
import school.sptech.catalogo_service.exception.LivroException;
import school.sptech.catalogo_service.model.Livro;
import school.sptech.catalogo_service.repository.LivroRepository;

import java.util.List;

@Service
public class LivroService {
    private final LivroRepository livroRepository;

    public LivroService(LivroRepository livroRepository) {
        this.livroRepository = livroRepository;
    }

    public Livro cadastrarLivro(Livro livro){
        if (livroRepository.existsById(livro.getIsbn())){
            throw new LivroException("ISBN " + livro.getIsbn() + " já cadastrado");
        }
        return livroRepository.save(livro);
    }

    public Livro buscarPorIsbn(String isbn) {
        return livroRepository.findById(isbn).orElseThrow(
                () -> new LivroException("Livro não encontrado")
        );
    }

    public List<Livro> buscarTodos() {
        return livroRepository.findAll();
    }

    public Livro emprestar(String isbn) {
        Livro livro = buscarPorIsbn(isbn);
        if (livro.getStatus().equals(StatusLivro.EMPRESTADO)) {
            throw new LivroException("Livro já está emprestado");
        }
        livro.setStatus(StatusLivro.EMPRESTADO);
        return livroRepository.save(livro);
    }

    public Livro devolver(String isbn) {
        Livro livro = buscarPorIsbn(isbn);
        if (livro.getStatus().equals(StatusLivro.DISPONIVEL)) {
            throw new LivroException("Livro já estava disponível");
        }
        livro.setStatus(StatusLivro.DISPONIVEL);
        return livroRepository.save(livro);
    }

    public void deletar(String isbn) {
        Livro livro = buscarPorIsbn(isbn);
        if (livro.getStatus().equals(StatusLivro.EMPRESTADO)) {
            throw new LivroException("Não é possível remover um livro emprestado");
        }
        livroRepository.delete(livro);
    }

    public List<Livro> buscarPorTitulo(String titulo){
        List<Livro> livros = livroRepository.findByTituloContainingIgnoreCase(titulo);
        if (livros.isEmpty()) {
            throw new LivroException("Título não existe");
        }
        return livros;
    }

    public List<Livro> buscarPorAutor(String autor) {
        List<Livro> livros = livroRepository.findByAutorContainingIgnoreCase(autor);
        if (livros.isEmpty()) {
            throw new LivroException("Autor não existe");
        }
        return livros;
    }
}
