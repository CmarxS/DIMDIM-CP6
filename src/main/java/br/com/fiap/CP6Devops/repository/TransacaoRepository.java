package br.com.fiap.CP6Devops.repository;

import br.com.fiap.CP6Devops.model.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransacaoRepository extends JpaRepository<Transacao, Long> {
    List<Transacao> findByTipo(Transacao.TipoTransacao tipo);
    List<Transacao> findByDataBetween(LocalDate inicio, LocalDate fim);
}