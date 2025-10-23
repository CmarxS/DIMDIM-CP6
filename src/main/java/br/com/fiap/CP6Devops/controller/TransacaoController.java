package br.com.fiap.CP6Devops.controller;

import br.com.fiap.CP6Devops.model.Transacao;
import br.com.fiap.CP6Devops.service.TransacaoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/transacoes")
public class TransacaoController {

    @Autowired
    private TransacaoService service;

    @GetMapping
    public ResponseEntity<List<Transacao>> listarTodas() {
        return ResponseEntity.ok(service.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transacao> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<Transacao>> buscarPorTipo(@PathVariable Transacao.TipoTransacao tipo) {
        return ResponseEntity.ok(service.buscarPorTipo(tipo));
    }

    @GetMapping("/periodo")
    public ResponseEntity<List<Transacao>> buscarPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        return ResponseEntity.ok(service.buscarPorPeriodo(inicio, fim));
    }

    @PostMapping
    public ResponseEntity<Transacao> criar(@Valid @RequestBody Transacao transacao) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(transacao));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Transacao> atualizar(@PathVariable Long id, @Valid @RequestBody Transacao transacao) {
        try {
            return ResponseEntity.ok(service.atualizar(id, transacao));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}