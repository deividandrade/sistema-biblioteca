package com.biblioteca.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "livros")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Livro {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "O título é obrigatório")
	@Column(nullable = false)
	private String titulo;

	@Column(unique = true)
	private String isbn;

	@Column
	private Integer anoPublicacao;

	@Column
	private String editora;

	// Status do livro: true = disponível, false = emprestado
	@Column(nullable = false)
	private Boolean disponivel = true;

	// Muitos livros pertencem a um autor
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "autor_id", nullable = false)
	@NotNull(message = "O autor é obrigatório")
	private Autor autor;

	// Um livro pode ter vários empréstimos (histórico)
	@OneToMany(mappedBy = "livro", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Emprestimo> emprestimos = new ArrayList<>();
}
