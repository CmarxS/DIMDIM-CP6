package br.com.fiap.CP6Devops.service;

import br.com.fiap.CP6Devops.model.Categoria;
import br.com.fiap.CP6Devops.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository repository;

    public List<Categoria> listarTodas() {
        return repository.findAll();
    }

    public List<Categoria> listarAtivas() {
        return repository.findByAtivaTrue();
    }

    public Optional<Categoria> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public Categoria salvar(Categoria categoria) {
        return repository.save(categoria);
    }

    public Categoria atualizar(Long id, Categoria categoria) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Categoria não encontrada");
        }
        categoria.setId(id);
        return repository.save(categoria);
    }

    public void deletar(Long id) {
        repository.deleteById(id);
    }
}