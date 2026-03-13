package com.example.ticket.infrastructure.config;

import com.example.ticket.application.TicketService;
import com.example.ticket.domain.port.in.ConsultarEstadoTicketUseCase;
import com.example.ticket.domain.port.in.RegistrarTicketUseCase;
import com.example.ticket.domain.port.out.NotificarTicketPort;
import com.example.ticket.domain.port.out.TicketRepositoryPort;
import com.example.ticket.infrastructure.adapter.out.notification.TicketNotificationAdapter;
import com.example.ticket.infrastructure.adapter.out.persistence.SpringDataTicketRepository;
import com.example.ticket.infrastructure.adapter.out.persistence.TicketPersistenceAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public TicketRepositoryPort ticketRepositoryPort(SpringDataTicketRepository repository) {
        return new TicketPersistenceAdapter(repository);
    }

    @Bean
    public NotificarTicketPort notificarTicketPort() {
        return new TicketNotificationAdapter();
    }

    @Bean
    public TicketService ticketService(TicketRepositoryPort ticketRepositoryPort,
                                       NotificarTicketPort notificarTicketPort) {
        return new TicketService(ticketRepositoryPort, notificarTicketPort);
    }

    @Bean
    public RegistrarTicketUseCase registrarTicketUseCase(TicketService ticketService) {
        return ticketService;
    }

    @Bean
    public ConsultarEstadoTicketUseCase consultarEstadoTicketUseCase(TicketService ticketService) {
        return ticketService;
    }
}
