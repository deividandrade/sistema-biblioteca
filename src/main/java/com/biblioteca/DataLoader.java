package com.biblioteca;

import com.biblioteca.model.Autor;
import com.biblioteca.model.Livro;
import com.biblioteca.repository.AutorRepository;
import com.biblioteca.repository.LivroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * DataLoader — Carrega dados iniciais no banco ao iniciar a aplicação.
 * Útil para testar o sistema sem precisar cadastrar tudo manualmente.
 *
 * ATENÇÃO e lembrete: Este componente insere dados apenas se o banco estiver vazio.
 * Para desabilitar, basta comentar ou remover a anotação @Component.
 */
@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final AutorRepository autorRepository;
    private final LivroRepository livroRepository;

    @Override
    public void run(String... args) throws Exception {
        // Só insere dados se ainda não há autores no banco
        if (autorRepository.count() > 0) {
            System.out.println(">>> Banco já possui dados. Pulando DataLoader.");
            return;
        }

        System.out.println(">>> Carregando dados iniciais...");

        // Cria autores de exemplo
        Autor machado = autorRepository.save(new Autor(
                "Machado de Assis",
                "Considerado o maior nome da literatura brasileira, fundador da Academia Brasileira de Letras.",
                "Brasileiro"
        ));

        Autor dostoievski = autorRepository.save(new Autor(
                "Fiódor Dostoiévski",
                "Um dos maiores romancistas russos do século XIX, conhecido por obras de profundidade psicológica.",
                "Russo"
        ));

        Autor tolkien = autorRepository.save(new Autor(
                "J.R.R. Tolkien",
                "Escritor e professor inglês, criador do universo da Terra-média.",
                "Inglês"
        ));

        // Cria livros de exemplo
        Livro livro1 = new Livro();
        livro1.setTitulo("Dom Casmurro");
        livro1.setIsbn("978-85-359-0277-5");
        livro1.setAnoPublicacao(1899);
        livro1.setEditora("Companhia das Letras");
        livro1.setDisponivel(true);
        livro1.setAutor(machado);
        livroRepository.save(livro1);

        Livro livro2 = new Livro();
        livro2.setTitulo("Memórias Póstumas de Brás Cubas");
        livro2.setIsbn("978-85-359-0278-2");
        livro2.setAnoPublicacao(1881);
        livro2.setEditora("Companhia das Letras");
        livro2.setDisponivel(true);
        livro2.setAutor(machado);
        livroRepository.save(livro2);

        Livro livro3 = new Livro();
        livro3.setTitulo("Crime e Castigo");
        livro3.setIsbn("978-85-325-2346-7");
        livro3.setAnoPublicacao(1866);
        livro3.setEditora("Editora 34");
        livro3.setDisponivel(true);
        livro3.setAutor(dostoievski);
        livroRepository.save(livro3);

        Livro livro4 = new Livro();
        livro4.setTitulo("O Senhor dos Anéis");
        livro4.setIsbn("978-85-264-0270-4");
        livro4.setAnoPublicacao(1954);
        livro4.setEditora("Martins Fontes");
        livro4.setDisponivel(true);
        livro4.setAutor(tolkien);
        livroRepository.save(livro4);

        System.out.println(">>> Dados iniciais carregados com sucesso! (" +
                autorRepository.count() + " autores, " +
                livroRepository.count() + " livros)");
    }
}
