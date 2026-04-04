let idParaExcluir = null;

function abrirModalNovo() {
    document.getElementById('modalAutorTitulo').textContent = 'Novo Autor';
    document.getElementById('autorId').value = '';
    document.getElementById('autorNome').value = '';
    document.getElementById('autorNacionalidade').value = '';
    document.getElementById('autorBiografia').value = '';
}

// Lê os data-* do card pai e abre o modal de edição
function abrirModalEditarDoCard(btn) {
    const card = btn.closest('.autor-card');
    const id           = card.dataset.id           || '';
    const nome         = card.dataset.nome         || '';
    const nacionalidade = card.dataset.nacionalidade || '';
    const biografia    = card.dataset.biografia    || '';

    document.getElementById('modalAutorTitulo').textContent = 'Editar Autor';
    document.getElementById('autorId').value           = id;
    document.getElementById('autorNome').value         = nome;
    document.getElementById('autorNacionalidade').value = nacionalidade;
    document.getElementById('autorBiografia').value    = biografia;

    new bootstrap.Modal(document.getElementById('modalAutor')).show();
}

// Lê os data-* do card e abre o modal de confirmação de exclusão
function confirmarExclusaoDoCard(btn) {
    const card = btn.closest('.autor-card');
    idParaExcluir = card.dataset.id;
    document.getElementById('nomeAutorExcluir').textContent = card.dataset.nome;
    new bootstrap.Modal(document.getElementById('modalExclusao')).show();
}

async function salvarAutor() {
    const id   = document.getElementById('autorId').value;
    const nome = document.getElementById('autorNome').value.trim();

    if (!nome) {
        mostrarAlerta('O nome do autor é obrigatório!', 'warning');
        return;
    }

    const dados = {
        nome:          nome,
        nacionalidade: document.getElementById('autorNacionalidade').value.trim() || null,
        biografia:     document.getElementById('autorBiografia').value.trim()     || null
    };

    try {
        const url    = id ? `/api/autores/${id}` : '/api/autores';
        const metodo = id ? 'PUT' : 'POST';

        const resposta = await fetch(url, {
            method:  metodo,
            headers: { 'Content-Type': 'application/json' },
            body:    JSON.stringify(dados)
        });

        const resultado = await resposta.json();

        bootstrap.Modal.getInstance(document.getElementById('modalAutor')).hide();

        if (resultado.sucesso) {
            mostrarAlerta(resultado.mensagem, 'success');
            recarregarPagina();
        } else {
            mostrarAlerta(resultado.mensagem, 'danger');
        }
    } catch (erro) {
        console.error('Erro ao salvar autor:', erro);
        mostrarAlerta('Erro ao conectar com o servidor.', 'danger');
    }
}

async function executarExclusao() {
    if (!idParaExcluir) return;

    try {
        const resposta = await fetch(`/api/autores/${idParaExcluir}`, { method: 'DELETE' });
        const resultado = await resposta.json();

        bootstrap.Modal.getInstance(document.getElementById('modalExclusao')).hide();

        if (resultado.sucesso) {
            mostrarAlerta(resultado.mensagem, 'success');
            recarregarPagina();
        } else {
            mostrarAlerta(resultado.mensagem, 'danger');
        }
    } catch (erro) {
        mostrarAlerta('Erro ao conectar com o servidor.', 'danger');
    }
}

function filtrarCards() {
    const texto = document.getElementById('campoBusca').value.toLowerCase();
    document.querySelectorAll('.autor-card').forEach(card => {
        const nome = (card.dataset.nome || '').toLowerCase();
        card.style.display = nome.includes(texto) ? '' : 'none';
    });
}
