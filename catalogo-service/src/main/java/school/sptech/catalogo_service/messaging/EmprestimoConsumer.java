package school.sptech.catalogo_service.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import school.sptech.catalogo_service.config.RabbitMQConfig;
import school.sptech.catalogo_service.service.LivroService;

@Component
public class EmprestimoConsumer {

    private final LivroService livroService;

    public EmprestimoConsumer(LivroService livroService) {
        this.livroService = livroService;
        System.out.println("EmprestimoConsumer criado!");
    }

    @RabbitListener(queues = RabbitMQConfig.FILA_EMPRESTIMO)
    public void processarEmprestimo(String isbn) {
        System.out.println("Mensagem recebida: emprestimo do ISBN " + isbn);
        livroService.emprestar(isbn);
    }
}
