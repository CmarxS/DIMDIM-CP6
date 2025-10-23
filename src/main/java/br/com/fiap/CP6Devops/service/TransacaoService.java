package br.com.fiap.CP6Devops.service;

import br.com.fiap.CP6Devops.model.Transacao;
import br.com.fiap.CP6Devops.repository.TransacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TransacaoService {

    @Autowired
    private TransacaoRepository repository;

    public List<Transacao> listarTodas() {
        return repository.findAll();
    }

    public Optional<Transacao> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public List<Transacao> buscarPorTipo(Transacao.TipoTransacao tipo) {
        return repository.findByTipo(tipo);
    }

    public List<Transacao> buscarPorPeriodo(LocalDate inicio, LocalDate fim) {
        return repository.findByDataBetween(inicio, fim);
    }

    public Transacao salvar(Transacao transacao) {
        return repository.save(transacao);
    }

    public Transacao atualizar(Long id, Transacao transacao) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Transação não encontrada");
        }
        transacao.setId(id);
        return repository.save(transacao);
    }

    public void deletar(Long id) {
        repository.deleteById(id);
    }
}