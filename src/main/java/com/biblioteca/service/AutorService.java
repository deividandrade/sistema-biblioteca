package com.biblioteca.service;

import com.biblioteca.dto.BibliotecaDTO.AutorRequestDTO;
import com.biblioteca.dto.BibliotecaDTO.AutorResponseDTO;
import com.biblioteca.model.Autor;
import com.biblioteca.repository.AutorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor // Lombok cria o construtor com todos os campos final
public class AutorService {

    private final AutorRepository autorRepository;

    // Busca todos os autores
    public List<AutorResponseDTO> listarTodos() {
        return autorRepository.findAll()
                .stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    // Busca um autor pelo ID
    public AutorResponseDTO buscarPorId(Long id) {
        Autor autor = autorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Autor não encontrado com ID: " + id));
        return converterParaDTO(autor);
    }

    // Cria um novo autor
    @Transactional
    public AutorResponseDTO criar(AutorRequestDTO dto) {
        Autor autor = new Autor();
        autor.setNome(dto.getNome());
        autor.setBiografia(dto.getBiografia());
        autor.setNacionalidade(dto.getNacionalidade());

        Autor autorSalvo = autorRepository.save(autor);
        return converterParaDTO(autorSalvo);
    }

    // Atualiza um autor existente
    @Transactional
    public AutorResponseDTO atualizar(Long id, AutorRequestDTO dto) {
        Autor autor = autorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Autor não encontrado com ID: " + id));

        autor.setNome(dto.getNome());
        autor.setBiografia(dto.getBiografia());
        autor.setNacionalidade(dto.getNacionalidade());

        Autor autorAtualizado = autorRepository.save(autor);
        return converterParaDTO(autorAtualizado);
    }

    // Deleta um autor
    @Transactional
    public void deletar(Long id) {
        Autor autor = autorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Autor não encontrado com ID: " + id));

        if (!autor.getLivros().isEmpty()) {
            throw new RuntimeException("Não é possível excluir um autor que possui livros cadastrados");
        }

        autorRepository.delete(autor);
    }

    // Busca autores pelo nome
    public List<AutorResponseDTO> buscarPorNome(String nome) {
        return autorRepository.findByNomeContainingIgnoreCase(nome)
                .stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    // Converte a entidade Autor para o DTO de resposta
    // Método privado, só usado dentro deste Service
    private AutorResponseDTO converterParaDTO(Autor autor) {
        AutorResponseDTO dto = new AutorResponseDTO();
        dto.setId(autor.getId());
        dto.setNome(autor.getNome());
        dto.setBiografia(autor.getBiografia());
        dto.setNacionalidade(autor.getNacionalidade());
        dto.setTotalLivros(autor.getLivros().size());
        return dto;
    }
}
