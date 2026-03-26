package com.biblioteca.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "emprestimos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Emprestimo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "O nome do leitor é obrigatório")
	@Column(nullable = false)
	private String nomeLeitor;

	@Column
	private String emailLeitor;

	@Column(nullable = false)
	private LocalDate dataEmprestimo;

	@Column
	private LocalDate dataDevolucaoPrevista;

	@Column
	private LocalDate dataDevolucaoReal;

	// Status do empréstimo
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private StatusEmprestimo status = StatusEmprestimo.ATIVO;

	// Muitos empréstimos pertencem a um livro
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "livro_id", nullable = false)
	@NotNull(message = "O livro é obrigatório")
	private Livro livro;

	// Enum interno para o status do empréstimo
	public enum StatusEmprestimo {
		ATIVO, DEVOLVIDO, ATRASADO
	}
}
