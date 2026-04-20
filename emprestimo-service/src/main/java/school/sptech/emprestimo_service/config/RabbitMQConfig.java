package school.sptech.emprestimo_service.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitMQConfig {

    public static final String FILA_EMPRESTIMO = "fila.emprestimo";
    public static final String EXCHANGE_EMPRESTIMO = "exchange.emprestimo";
    public static final String ROUTING_KEY_EMPRESTIMO = "emprestimo.solicitado";

    @Bean
    public Queue filaEmprestimo() {
        return new Queue(FILA_EMPRESTIMO, true);
    }

    @Bean
    public DirectExchange exchangeEmprestimo() {
        return new DirectExchange(EXCHANGE_EMPRESTIMO);
    }

    @Bean
    public Binding binding(Queue filaEmprestimo, DirectExchange exchangeEmprestimo) {
        return BindingBuilder
                .bind(filaEmprestimo)
                .to(exchangeEmprestimo)
                .with(ROUTING_KEY_EMPRESTIMO);
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public ApplicationRunner initRabbit(RabbitAdmin rabbitAdmin) {
        return args -> rabbitAdmin.initialize();
    }

    @Bean
    public MessageConverter messageConverter() {
        return new SimpleMessageConverter();
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory =
                new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(new SimpleMessageConverter());
        factory.setDefaultRequeueRejected(false);
        return factory;
    }
}
