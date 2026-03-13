package com.example.ticket.infrastructure.adapter.in.web;

import com.example.ticket.domain.model.EstadoTicket;
import com.example.ticket.domain.model.Ticket;
import com.example.ticket.domain.port.in.ConsultarEstadoTicketUseCase;
import com.example.ticket.domain.port.in.RegistrarTicketUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    private final RegistrarTicketUseCase registrarTicketUseCase;
    private final ConsultarEstadoTicketUseCase consultarEstadoTicketUseCase;

    public TicketController(RegistrarTicketUseCase registrarTicketUseCase,
                            ConsultarEstadoTicketUseCase consultarEstadoTicketUseCase) {
        this.registrarTicketUseCase = registrarTicketUseCase;
        this.consultarEstadoTicketUseCase = consultarEstadoTicketUseCase;
    }

    @PostMapping
    public ResponseEntity<TicketResponse> registrar(@RequestBody CrearTicketRequest request) {
        Ticket ticket = registrarTicketUseCase.registrar(request.getTitulo(), request.getDescripcion());

        TicketResponse response = new TicketResponse(
                ticket.getId(),
                ticket.getTitulo(),
                ticket.getDescripcion(),
                ticket.getEstado()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/estado")
    public ResponseEntity<String> consultarEstado(@PathVariable Long id) {
        EstadoTicket estado = consultarEstadoTicketUseCase.consultarEstado(id);
        return ResponseEntity.ok(estado.name());
    }
}