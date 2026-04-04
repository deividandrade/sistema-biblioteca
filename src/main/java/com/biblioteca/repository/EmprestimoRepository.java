package com.biblioteca.repository;

import com.biblioteca.model.Emprestimo;
import com.biblioteca.model.Emprestimo.StatusEmprestimo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {

	// Busca empréstimos pelo status
	List<Emprestimo> findByStatus(StatusEmprestimo status);

	// Busca empréstimos pelo nome do leitor
	List<Emprestimo> findByNomeLeitorContainingIgnoreCase(String nomeLeitor);

	// Verifica se um livro tem empréstimo ativo
	Optional<Emprestimo> findByLivroIdAndStatus(Long livroId, StatusEmprestimo status);

	// Busca empréstimos atrasados (data prevista menor que hoje e ainda ativos)
	@Query("SELECT e FROM Emprestimo e WHERE e.dataDevolucaoPrevista < :hoje AND e.status = 'ATIVO'")
	List<Emprestimo> findEmprestimosAtrasados(LocalDate hoje);

	// Conta quantos empréstimos estão ativos
	long countByStatus(StatusEmprestimo status);
}
