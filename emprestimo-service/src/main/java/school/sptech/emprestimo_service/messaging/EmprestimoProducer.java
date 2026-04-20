package school.sptech.emprestimo_service.messaging;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Component;

@Component
public class EmprestimoProducer {

    private final AmqpTemplate amqpTemplate;

    public EmprestimoProducer(AmqpTemplate amqpTemplate) {
        this.amqpTemplate = amqpTemplate;
    }

    public void publicarEmprestimo(String isbn) {
        amqpTemplate.convertAndSend(
                "exchange.emprestimo",
                "emprestimo.solicitado",
                isbn
        );
        System.out.println("Evento publicado: emprestimo do ISBN " + isbn);
    }
}
