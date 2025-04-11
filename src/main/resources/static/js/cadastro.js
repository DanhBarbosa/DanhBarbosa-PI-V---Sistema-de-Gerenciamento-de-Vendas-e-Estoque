var idGlobal = null;
document.getElementById('cadastroForm').addEventListener('submit', function enviarDados(evt) {
    evt.preventDefault();
    if (idGlobal === null) {
        salvarDadosUsuario();
    } else {
        editarDadosUsuario();
    }
});

async function salvarDadosUsuario() {
    const form = document.getElementById('cadastroForm');

    let dados;

    dados = {
        login: form.login.value,
        nome: form.nome.value,
        senha: form.senha.value,

    }
    console.log("DADOS " + JSON.stringify(dados))

    const token = localStorage.getItem("token");
    const httpResp = await fetch('/api/usuarios', {
        method: 'POST',
        headers: {
            "Authorization": `Bearer ${token}`,
            "Content-Type": "application/json"
        },
        body: JSON.stringify(dados)
    });
    if (!httpResp.ok) {
        if (httpResp.status !== 400) {
            alert('Erro no envio dos dados - ' + httpResp.status);
            return;
        }
        const erros = await httpResp.json();
        console.log("ERROS " + erros.mensagem);
        alert(erros.mensagem);

        if (erros.erros !== null) {
            for (const erro of erros.errors) {
                const mensagem = erro.defaultMessage;
                const field = erro.field;
                console.log(mensagem)
            }
        }
        return;
    }
    alert('Sucesso ao salvar');
}

async function editarDadosUsuario() {
    console.log("Editar" + idGlobal)
    const form = document.getElementById('cadastroForm');

    let dados = {
        nome: form.nome.value,
        senha: form.senha.value,
    }

    const token = localStorage.getItem("token");
    const httpResp = await fetch(`/api/usuarios/${idGlobal}`, {
        method: 'PATCH',
        headers: {
            'content-type': 'application/json',
            "Authorization": `Bearer ${token}`,
        },
        body: JSON.stringify(dados)
    });
    if (!httpResp.ok) {
        if (httpResp.status !== 400) {
            alert('Erro no envio dos dados - ' + httpResp.status);
            return;
        }
        const erros = await httpResp.json();
        for (const erro of erros.errors) {
            const mensagem = erro.defaultMessage;
            const field = erro.field;
            console.log(mensagem)
        }
    }

    alert('Sucesso ao atualizar ');
    document.getElementById('cadastroForm').reset();


}

async function carregarUsuarios() {


    const token = localStorage.getItem("token");

    const resposta = await fetch('/api/usuarios', {
        method: "GET", // opcional, pois GET é o padrão
        headers: {
            "Authorization": `Bearer ${token}`,
            "Content-Type": "application/json"
        }
    });

    if (!resposta.ok) {
        throw new Error('Erro ao carregar os dados da API.');
        return;
    }
    const dados = await resposta.json();

    console.log(dados);

    const tabelaCorpo = document.getElementById('estoqueUsuario');

    tabelaCorpo.innerHTML = '';

    if (dados && dados.length > 0) {
        dados.forEach(item => {
            const linha = document.createElement('tr');

            const colunas = [
                item.id,
                item.nome,
                item.login

            ];

            colunas.forEach(dado => {
                const celula = document.createElement('td');
                celula.textContent = dado;
                linha.appendChild(celula);
            });

            const editarCelula = document.createElement('td');
            editarCelula.innerHTML = '<a href="#" style="color: #343a40"> <i class="fas fa-pencil-alt"></i></a>';
            editarCelula.addEventListener('click', () => editarItem(item.id));
            linha.appendChild(editarCelula);


            const excluirCelula = document.createElement('td');
            excluirCelula.innerHTML = '<a href="#" style="color: #343a40"><i class="fas fa-trash-alt"></i></a>';
            excluirCelula.addEventListener('click', () => excluirItem(item.id));
            linha.appendChild(excluirCelula);

            tabelaCorpo.appendChild(linha);
        });
    } else {
        tabelaCorpo.innerHTML = '<tr><td colspan="10">Nenhum item encontrado.</td></tr>';
    }
}

function preencherModalComDados(dados) {

    console.log(dados.nome);

    document.getElementById('senha').value = dados.senha || '';

    // Campos bloqueados para edição
    document.getElementById('nome').value = dados.nome || ''; // bloqueado
    document.getElementById('login').value = dados.login || '';

    document.getElementById('login').readOnly = true;
}


async function excluirItem(id) {

    const token = localStorage.getItem("token");
    const httpResp = await fetch(`/api/usuarios/${id}`, {
        method: 'delete',
        headers: {
            "Authorization": `Bearer ${token}`,
            "Content-Type": "application/json"
        }

    });

    if (httpResp.ok) {
        alert("Usuario excluído")
        window.location.reload();
        return
    }
}


async function editarItem(id) {

    console.log("id " + id);
    const token = localStorage.getItem("token");
    const resposta = await fetch(`/api/usuarios/${id}`, {
        method: "GET", // opcional, pois GET é o padrão
        headers: {
            "Authorization": `Bearer ${token}`,
            "Content-Type": "application/json"
        }});


    const dados = await resposta.json();


    idGlobal = id;
    preencherModalComDados(dados);

    // Abre o modal
    var modal = new bootstrap.Modal(document.getElementById('modalCadastro'));
    modal.show();
}


function toggleSidebar() {
    const sidebar = document.getElementById('sidebar');
    sidebar.classList.toggle('collapsed');
}

function toggleSubMenu(subMenuId) {
    const allSubMenus = document.querySelectorAll('.sub-menu');
    allSubMenus.forEach(subMenu => {
        subMenu.classList.remove('active');
    });
    const currentSubMenu = document.getElementById(subMenuId);
    currentSubMenu.classList.toggle('active');
}


// Inicializar a página com a função chamada
window.onload = function () {
    carregarUsuarios();

    document.getElementById('cadastroForm').reset();
};