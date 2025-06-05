const form = document.getElementById('addItemEstoqueLojaForm');
const modalTitle = document.querySelector('.modal-title');
let produtoId = null;
const baseUrl = '/api/produtos';

function abrirModalNovoProduto() {
    produtoId = null;

    document.getElementById('txtCodItemLoja').value = '';
    document.getElementById('txtMarcaLoja').value = '';
    document.getElementById('txtNomeLoja').value = '';
    document.getElementById('txtDescricaoLoja').value = '';
    document.getElementById('txtQtdeLoja').value = '';
    document.getElementById('txtPrecoLoja').value = '';

    document.querySelector('.modal-title').textContent = 'Adicionar Produto';

    $('#addItemEstoqueLoja').modal('show');
}

async function editarProduto(id) {
    produtoId = id;

    try {
        const token = localStorage.getItem("token");
        const resposta = await fetch(`${baseUrl}/${id}`, {
            method: 'GET',
            headers: {
                "Authorization": `Bearer ${token}`,
                "Content-Type": "application/json"
            },
        });
        const produto = await resposta.json();

        document.getElementById('txtCodItemLoja').value = produto.codigo;
        document.getElementById('txtMarcaLoja').value = produto.marca;
        document.getElementById('txtNomeLoja').value = produto.nome;
        document.getElementById('txtDescricaoLoja').value = produto.descricao;
        document.getElementById('txtQtdeLoja').value = produto.quantidade;
        document.getElementById('txtPrecoLoja').value = produto.preco;

        document.querySelector('.modal-title').textContent = 'Editar Produto';

        $('#addItemEstoqueLoja').modal('show');
    } catch (error) {
        console.error("Erro ao obter os dados do produto:", error);
        alert("Erro ao obter os dados do produto.");
    }
}

function validarPreco(preco) {
    const precoRegex = /^[0-9]+(\.[0-9]{1,2})?$/;
    return precoRegex.test(preco);
}

form.addEventListener('submit', async (e) => {
    e.preventDefault();

    const codigo = document.getElementById('txtCodItemLoja').value;
    const nome = document.getElementById('txtNomeLoja').value;
    const descricao = document.getElementById('txtDescricaoLoja').value;
    const marca = document.getElementById('txtMarcaLoja').value;
    const quantidade = document.getElementById('txtQtdeLoja').value;
    const preco = document.getElementById('txtPrecoLoja').value;

    if (!validarPreco(preco)) {
        alert("Por favor, insira um preço válido (apenas números e até 2 casas decimais).");
        return;
    }

    console.log({codigo, nome, descricao, marca, quantidade, preco});
    console.log("Produto ID:", produtoId); // Verifique se o produtoId está correto aqui

    try {
        let resposta;

        if (produtoId === null) { // Novo produto
            const token = localStorage.getItem("token");
            resposta = await fetch(baseUrl, {
                method: 'POST',
                headers: {
                    "Authorization": `Bearer ${token}`,
                    "Content-Type": "application/json"

                },
                body: JSON.stringify({
                    codigo,
                    nome,
                    descricao,
                    marca,
                    quantidade: parseInt(quantidade),
                    preco: parseFloat(preco)
                })
            });
        } else { // Produto editado
            const produtoEditado = {
                codigo,
                nome,
                descricao,
                marca,
                quantidade: parseInt(quantidade),
                preco: parseFloat(preco)
            };
            const token = localStorage.getItem("token");
            resposta = await fetch(`${baseUrl}/${produtoId}`, {
                method: 'PUT',
                headers: {'Content-Type': 'application/json',
                "Authorization": `Bearer ${token}`,
                    },
                body: JSON.stringify(produtoEditado)
            });
        }

        if (resposta.ok) {
            alert(produtoId === null ? "Produto cadastrado com sucesso" : "Produto editado com sucesso");
            produtoId = null;  // Reseta o produtoId após salvar
            listarProdutos();
            $('#addItemEstoqueLoja').modal('hide');
            location.reload();
        } else {
            alert("Erro ao salvar produto.");
        }
    } catch (error) {
        console.error("Erro na requisição:", error);
        alert("Erro ao conectar com o servidor.");
    }
});

async function listarProdutos() {
    const token = localStorage.getItem("token");
    const resposta = await fetch(baseUrl, {
        headers: {
            'Content-Type': 'application/json',
            "Authorization": `Bearer ${token}`
        }
    });
    const dados = await resposta.json();

    const estoqueLoja = document.getElementById('estoqueLoja');
    estoqueLoja.innerHTML = '';

    if(dados && dados.length > 0 ){
        dados.forEach(produto => {
            const linha = document.createElement('tr');


            const colunas = [
                produto.id,
                produto.codigo,
                produto.nome,
                produto.descricao,
                produto.marca,
                produto.quantidade,
                produto.preco,

            ];


            colunas.forEach(dado => {
                const celula = document.createElement('td');
                celula.textContent = dado;
                linha.appendChild(celula);
            });

            const editarCelula = document.createElement('td');
            editarCelula.innerHTML = '<a href="#" style="color: #343a40"> <i class="fas fa-pencil-alt"></i></a>';
            editarCelula.addEventListener('click', () => editarProduto(produto.id));
            linha.appendChild(editarCelula);


            const excluirCelula = document.createElement('td');
            excluirCelula.innerHTML = '<a href="#" style="color: #343a40"><i class="fas fa-trash-alt"></i></a>';
            excluirCelula.addEventListener('click', () => excluirProduto(produto.id));
            linha.appendChild(excluirCelula);

            estoqueLoja.appendChild(linha);
        });
    }


}

async function excluirProduto(id) {
    const token = localStorage.getItem("token");
    const resposta = await fetch(`${baseUrl}/${id}`, {
        method: 'DELETE', headers: {
            "Authorization": `Bearer ${token}`,
            "Content-Type": "application/json"
        }
    });

    if (resposta.ok) {
        alert('Produto excluído com sucesso');
        listarProdutos();
    } else {
        alert('Erro ao excluir o produto');
    }
}



window.onload = () => {
    listarProdutos();
};

document.querySelector('.btn-secondary[data-dismiss="modal"]').addEventListener('click', () => {
    location.reload();
});

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