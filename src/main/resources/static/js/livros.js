let idParaExcluir = null;

// Abre o modal para criar um novo livro (isso limpa os campos)
function abrirModalNovo() {
    document.getElementById('modalLivroTitulo').textContent = 'Novo Livro';
    document.getElementById('livroId').value = '';
    document.getElementById('livroTitulo').value = '';
    document.getElementById('livroAutorId').value = '';
    document.getElementById('livroIsbn').value = '';
    document.getElementById('livroAno').value = '';
    document.getElementById('livroEditora').value = '';
}

// Lê os data-* do <tr> e abre o modal de edição
function abrirModalEditarDaLinha(btn) {
    // Sobe até o <tr> pai do botão
    const tr = btn.closest('tr');
    const id = tr.dataset.id;
    const titulo = tr.dataset.titulo || '';
    const isbn = tr.dataset.isbn || '';
    const ano = tr.dataset.ano || '';
    const editora = tr.dataset.editora || '';
    const autorId = tr.dataset.autorId || '';

    document.getElementById('modalLivroTitulo').textContent = 'Editar Livro';
    document.getElementById('livroId').value = id;
    document.getElementById('livroTitulo').value = titulo;
    document.getElementById('livroAutorId').value = autorId;
    document.getElementById('livroIsbn').value = isbn;
    document.getElementById('livroAno').value = ano;
    document.getElementById('livroEditora').value = editora;

    new bootstrap.Modal(document.getElementById('modalLivro')).show();
}

// Lê os data-* do <tr> e abre o modal de confirmação de exclusão
function confirmarExclusaoDaLinha(btn) {
    const tr = btn.closest('tr');
    idParaExcluir = tr.dataset.id;
    document.getElementById('nomeLivroExcluir').textContent = tr.dataset.titulo;
    new bootstrap.Modal(document.getElementById('modalExclusao')).show();
}

// Salva o livro (cria ou atualiza dependendo se há ID)
async function salvarLivro() {
    const id = document.getElementById('livroId').value;
    const titulo = document.getElementById('livroTitulo').value.trim();
    const autorId = document.getElementById('livroAutorId').value;

    if (!titulo) {
        mostrarAlerta('O título do livro é obrigatório!', 'warning');
        return;
    }
    if (!autorId) {
        mostrarAlerta('Selecione um autor!', 'warning');
        return;
    }

    const dados = {
        titulo: titulo,
        isbn: document.getElementById('livroIsbn').value.trim() || null,
        anoPublicacao: document.getElementById('livroAno').value || null,
        editora: document.getElementById('livroEditora').value.trim() || null,
        autorId: parseInt(autorId)
    };

    try {
        const url = id ? `/api/livros/${id}` : '/api/livros';
        const metodo = id ? 'PUT' : 'POST';

        const resposta = await fetch(url, {
            method: metodo,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(dados)
        });

        const resultado = await resposta.json();

        bootstrap.Modal.getInstance(document.getElementById('modalLivro')).hide();

        if (resultado.sucesso) {
            mostrarAlerta(resultado.mensagem, 'success');
            recarregarPagina();
        } else {
            mostrarAlerta(resultado.mensagem, 'danger');
        }
    } catch (erro) {
        console.error('Erro ao salvar livro:', erro);
        mostrarAlerta('Erro ao conectar com o servidor.', 'danger');
    }
}

// Executa a exclusão após confirmação
async function executarExclusao() {
    if (!idParaExcluir) return;

    try {
        const resposta = await fetch(`/api/livros/${idParaExcluir}`, { method: 'DELETE' });
        const resultado = await resposta.json();

        bootstrap.Modal.getInstance(document.getElementById('modalExclusao')).hide();

        if (resultado.sucesso) {
            mostrarAlerta(resultado.mensagem, 'success');
            recarregarPagina();
        } else {
            mostrarAlerta(resultado.mensagem, 'danger');
        }
    } catch (erro) {
        console.error('Erro ao excluir livro:', erro);
        mostrarAlerta('Erro ao conectar com o servidor.', 'danger');
    }
}

// Filtra a tabela com base no texto digitado e no filtro de disponibilidade
function filtrarTabela() {
    const textoBusca = document.getElementById('campoBusca').value.toLowerCase();
    const filtroDisp = document.getElementById('filtroDisponibilidade').value;
    const linhas = document.querySelectorAll('#tabelaLivros tbody tr[data-titulo]');

    linhas.forEach(linha => {
        const titulo = (linha.dataset.titulo || '').toLowerCase();
        const disponivel = linha.dataset.disponivel || '';

        const combinaTitulo = titulo.includes(textoBusca);
        const combinaDisp = filtroDisp === '' || disponivel === filtroDisp;

        linha.style.display = (combinaTitulo && combinaDisp) ? '' : 'none';
    });
}