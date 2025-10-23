package br.com.fiap.CP6Devops.service;

import br.com.fiap.CP6Devops.model.Categoria;
import br.com.fiap.CP6Devops.model.Transacao;
import br.com.fiap.CP6Devops.repository.CategoriaRepository;
import br.com.fiap.CP6Devops.repository.TransacaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class TransacaoServiceTest {

    @Autowired
    private TransacaoService service;

    @Autowired
    private TransacaoRepository repository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    private Transacao transacao;
    private Categoria categoria;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
        categoriaRepository.deleteAll();

        categoria = new Categoria();
        categoria.setNome("Alimentação");
        categoria.setDescricao("Despesas com alimentação");
        categoria.setAtiva(true);
        categoria = categoriaRepository.save(categoria);

        transacao = new Transacao();
        transacao.setDescricao("Almoço");
        transacao.setValor(new BigDecimal("50.00"));
        transacao.setData(LocalDate.now());
        transacao.setTipo(Transacao.TipoTransacao.DESPESA);
        transacao.setCategoria(categoria);
    }

    @Test
    void deveSalvarTransacao() {
        Transacao salva = service.salvar(transacao);

        assertNotNull(salva.getId());
        assertEquals("Almoço", salva.getDescricao());
        assertEquals(0, new BigDecimal("50.00").compareTo(salva.getValor()));
        assertEquals(Transacao.TipoTransacao.DESPESA, salva.getTipo());
    }

    @Test
    void deveListarTodasTransacoes() {
        service.salvar(transacao);

        Transacao transacao2 = new Transacao();
        transacao2.setDescricao("Salário");
        transacao2.setValor(new BigDecimal("3000.00"));
        transacao2.setData(LocalDate.now());
        transacao2.setTipo(Transacao.TipoTransacao.RECEITA);
        service.salvar(transacao2);

        List<Transacao> transacoes = service.listarTodas();

        assertEquals(2, transacoes.size());
    }

    @Test
    void deveBuscarTransacaoPorId() {
        Transacao salva = service.salvar(transacao);

        Optional<Transacao> encontrada = service.buscarPorId(salva.getId());

        assertTrue(encontrada.isPresent());
        assertEquals("Almoço", encontrada.get().getDescricao());
    }

    @Test
    void deveBuscarTransacoesPorTipo() {
        service.salvar(transacao);

        Transacao receita = new Transacao();
        receita.setDescricao("Salário");
        receita.setValor(new BigDecimal("3000.00"));
        receita.setData(LocalDate.now());
        receita.setTipo(Transacao.TipoTransacao.RECEITA);
        service.salvar(receita);

        List<Transacao> despesas = service.buscarPorTipo(Transacao.TipoTransacao.DESPESA);
        List<Transacao> receitas = service.buscarPorTipo(Transacao.TipoTransacao.RECEITA);

        assertEquals(1, despesas.size());
        assertEquals(1, receitas.size());
        assertEquals(Transacao.TipoTransacao.DESPESA, despesas.get(0).getTipo());
        assertEquals(Transacao.TipoTransacao.RECEITA, receitas.get(0).getTipo());
    }

    @Test
    void deveBuscarTransacoesPorPeriodo() {
        transacao.setData(LocalDate.of(2024, 1, 15));
        service.salvar(transacao);

        Transacao transacao2 = new Transacao();
        transacao2.setDescricao("Jantar");
        transacao2.setValor(new BigDecimal("80.00"));
        transacao2.setData(LocalDate.of(2024, 2, 20));
        transacao2.setTipo(Transacao.TipoTransacao.DESPESA);
        service.salvar(transacao2);

        List<Transacao> transacoes = service.buscarPorPeriodo(
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 1, 31)
        );

        assertEquals(1, transacoes.size());
        assertEquals("Almoço", transacoes.get(0).getDescricao());
    }

    @Test
    void deveAtualizarTransacao() {
        Transacao salva = service.salvar(transacao);

        salva.setDescricao("Almoço atualizado");
        salva.setValor(new BigDecimal("75.00"));

        Transacao atualizada = service.atualizar(salva.getId(), salva);

        assertEquals("Almoço atualizado", atualizada.getDescricao());
        assertEquals(0, new BigDecimal("75.00").compareTo(atualizada.getValor()));
    }

    @Test
    void deveLancarExcecaoAoAtualizarTransacaoInexistente() {
        transacao.setId(999L);

        assertThrows(RuntimeException.class, () -> {
            service.atualizar(999L, transacao);
        });
    }

    @Test
    void deveDeletarTransacao() {
        Transacao salva = service.salvar(transacao);

        service.deletar(salva.getId());

        Optional<Transacao> encontrada = service.buscarPorId(salva.getId());
        assertFalse(encontrada.isPresent());
    }
}