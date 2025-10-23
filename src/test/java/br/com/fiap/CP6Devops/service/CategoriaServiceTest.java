package br.com.fiap.CP6Devops.service;

import br.com.fiap.CP6Devops.model.Categoria;
import br.com.fiap.CP6Devops.repository.CategoriaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class CategoriaServiceTest {

    @Autowired
    private CategoriaService service;

    @Autowired
    private CategoriaRepository repository;

    private Categoria categoria;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
        categoria = new Categoria();
        categoria.setNome("Alimentação");
        categoria.setDescricao("Despesas com alimentação");
        categoria.setAtiva(true);
    }

    @Test
    void deveSalvarCategoria() {
        Categoria salva = service.salvar(categoria);

        assertNotNull(salva.getId());
        assertEquals("Alimentação", salva.getNome());
        assertEquals("Despesas com alimentação", salva.getDescricao());
        assertTrue(salva.getAtiva());
    }

    @Test
    void deveListarTodasCategorias() {
        service.salvar(categoria);

        Categoria categoria2 = new Categoria();
        categoria2.setNome("Transporte");
        categoria2.setDescricao("Despesas com transporte");
        categoria2.setAtiva(true);
        service.salvar(categoria2);

        List<Categoria> categorias = service.listarTodas();

        assertEquals(2, categorias.size());
    }

    @Test
    void deveListarApenasCategoriasAtivas() {
        categoria.setAtiva(true);
        service.salvar(categoria);

        Categoria categoria2 = new Categoria();
        categoria2.setNome("Transporte");
        categoria2.setAtiva(false);
        service.salvar(categoria2);

        List<Categoria> ativas = service.listarAtivas();

        assertEquals(1, ativas.size());
        assertTrue(ativas.get(0).getAtiva());
    }

    @Test
    void deveBuscarCategoriaPorId() {
        Categoria salva = service.salvar(categoria);

        Optional<Categoria> encontrada = service.buscarPorId(salva.getId());

        assertTrue(encontrada.isPresent());
        assertEquals("Alimentação", encontrada.get().getNome());
    }

    @Test
    void deveAtualizarCategoria() {
        Categoria salva = service.salvar(categoria);

        salva.setNome("Alimentação Atualizada");
        salva.setDescricao("Nova descrição");

        Categoria atualizada = service.atualizar(salva.getId(), salva);

        assertEquals("Alimentação Atualizada", atualizada.getNome());
        assertEquals("Nova descrição", atualizada.getDescricao());
    }

    @Test
    void deveLancarExcecaoAoAtualizarCategoriaInexistente() {
        categoria.setId(999L);

        assertThrows(RuntimeException.class, () ->
            service.atualizar(999L, categoria)
        );
    }

    @Test
    void deveDeletarCategoria() {
        Categoria salva = service.salvar(categoria);

        service.deletar(salva.getId());

        Optional<Categoria> encontrada = service.buscarPorId(salva.getId());
        assertFalse(encontrada.isPresent());
    }
}
