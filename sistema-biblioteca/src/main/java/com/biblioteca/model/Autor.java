package com.biblioteca.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "autores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Autor {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "O nome do autor é obrigatório")
	@Column(nullable = false)
	private String nome;

	@Column(length = 500)
	private String biografia;

	@Column
	private String nacionalidade;

	// Um autor pode ter vários livros
	// mappedBy = nome do campo na classe Livro que referencia Autor
	@OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Livro> livros = new ArrayList<>();

	// Construtor sem a lista de livros (mais fácil de usar)
	public Autor(String nome, String biografia, String nacionalidade) {
		this.nome = nome;
		this.biografia = biografia;
		this.nacionalidade = nacionalidade;
	}
}
