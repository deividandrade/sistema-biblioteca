package com.biblioteca.repository;

import com.biblioteca.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LivroRepository extends JpaRepository<Livro, Long> {

	// Busca livros pelo título (ignorando maiúsculas/minúsculas)
	List<Livro> findByTituloContainingIgnoreCase(String titulo);

	// Busca livros por disponibilidade
	List<Livro> findByDisponivel(Boolean disponivel);

	// Busca livros pelo id do autor
	List<Livro> findByAutorId(Long autorId);

	// Query customizada com JPQL (Java Persistence Query Language)
	@Query("SELECT l FROM Livro l WHERE l.disponivel = true ORDER BY l.titulo")
	List<Livro> findLivrosDisponiveis();

	boolean existsByIsbn(String isbn);
}
