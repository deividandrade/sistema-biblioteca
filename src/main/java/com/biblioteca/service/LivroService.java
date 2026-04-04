package com.biblioteca.service;

import com.biblioteca.dto.BibliotecaDTO.LivroRequestDTO;
import com.biblioteca.dto.BibliotecaDTO.LivroResponseDTO;
import com.biblioteca.model.Autor;
import com.biblioteca.model.Livro;
import com.biblioteca.repository.AutorRepository;
import com.biblioteca.repository.LivroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LivroService {

    private final LivroRepository livroRepository;
    private final AutorRepository autorRepository;

    // Lista todos os livros
    public List<LivroResponseDTO> listarTodos() {
        return livroRepository.findAll()
                .stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    // Busca livro por ID
    public LivroResponseDTO buscarPorId(Long id) {
        Livro livro = livroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Livro não encontrado com ID: " + id));
        return converterParaDTO(livro);
    }

    // Busca a entidade Livro (usada internamente)
    public Livro buscarEntidadePorId(Long id) {
        return livroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Livro não encontrado com ID: " + id));
    }

    // Cria um novo livro
    @Transactional
    public LivroResponseDTO criar(LivroRequestDTO dto) {
        // Verifica se o ISBN já existe
        if (dto.getIsbn() != null && !dto.getIsbn().isEmpty()) {
            if (livroRepository.existsByIsbn(dto.getIsbn())) {
                throw new RuntimeException("Já existe um livro com esse ISBN: " + dto.getIsbn());
            }
        }

        // Busca o autor no banco
        Autor autor = autorRepository.findById(dto.getAutorId())
                .orElseThrow(() -> new RuntimeException("Autor não encontrado com ID: " + dto.getAutorId()));

        Livro livro = new Livro();
        livro.setTitulo(dto.getTitulo());
        livro.setIsbn(dto.getIsbn());
        livro.setAnoPublicacao(dto.getAnoPublicacao());
        livro.setEditora(dto.getEditora());
        livro.setDisponivel(true); // Novo livro sempre começa disponível
        livro.setAutor(autor);

        Livro livroSalvo = livroRepository.save(livro);
        return converterParaDTO(livroSalvo);
    }

    // Atualiza um livro
    @Transactional
    public LivroResponseDTO atualizar(Long id, LivroRequestDTO dto) {
        Livro livro = livroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Livro não encontrado com ID: " + id));

        Autor autor = autorRepository.findById(dto.getAutorId())
                .orElseThrow(() -> new RuntimeException("Autor não encontrado com ID: " + dto.getAutorId()));

        livro.setTitulo(dto.getTitulo());
        livro.setIsbn(dto.getIsbn());
        livro.setAnoPublicacao(dto.getAnoPublicacao());
        livro.setEditora(dto.getEditora());
        livro.setAutor(autor);

        return converterParaDTO(livroRepository.save(livro));
    }

    // Deleta um livro
    @Transactional
    public void deletar(Long id) {
        Livro livro = livroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Livro não encontrado com ID: " + id));

        if (!livro.getDisponivel()) {
            throw new RuntimeException("Não é possível excluir um livro que está emprestado");
        }

        livroRepository.delete(livro);
    }

    // Lista livros disponíveis
    public List<LivroResponseDTO> listarDisponiveis() {
        return livroRepository.findByDisponivel(true)
                .stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    // Busca livros pelo título
    public List<LivroResponseDTO> buscarPorTitulo(String titulo) {
        return livroRepository.findByTituloContainingIgnoreCase(titulo)
                .stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    // Converte entidade para DTO
    private LivroResponseDTO converterParaDTO(Livro livro) {
        LivroResponseDTO dto = new LivroResponseDTO();
        dto.setId(livro.getId());
        dto.setTitulo(livro.getTitulo());
        dto.setIsbn(livro.getIsbn());
        dto.setAnoPublicacao(livro.getAnoPublicacao());
        dto.setEditora(livro.getEditora());
        dto.setDisponivel(livro.getDisponivel());
        dto.setNomeAutor(livro.getAutor().getNome());
        dto.setAutorId(livro.getAutor().getId());
        return dto;
    }
}
