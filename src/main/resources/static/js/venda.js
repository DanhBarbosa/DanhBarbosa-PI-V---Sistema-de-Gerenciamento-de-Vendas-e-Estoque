async function buscarProduto() {

    const token = localStorage.getItem("token");
    const id = document.getElementById('product-code').value;

    const resposta = await fetch(`/api/produtos/${id}`, {
        method: 'GET',
        headers: {
            "Authorization": `Bearer ${token}`,
            "Content-Type": "application/json"
        },
    });

    if (!resposta.ok) {
        alert('Produto não encontrado');
        return;

    }
    const produtoBuscado = await resposta.json();


    let dados = [produtoBuscado];

    let produto = dados[0];
    console.log("QTD " +  produto.quantidade);

    if (produto.quantidade < 3){
        alert("O produto procurado está em baixa quantidade no estoque! Verifique primeiro antes de realizar a venda");
        return;
    }

    const tabelaCorpo = document.getElementById('tabelaVendas');


    if (dados && dados.length > 0) {
        console.log(dados)
        let qtd = 1;
        dados.forEach(item => {
            const linha = document.createElement('tr');

            const colunas = [
                item.id,
                item.nome,
                qtd,
                item.preco,

            ];

            colunas.forEach(dado => {
                const celula = document.createElement('td');
                celula.textContent = dado;
                linha.appendChild(celula);
            });

            const editarCelula = document.createElement('td');
            editarCelula.innerHTML = '<a href="#" style="color: #343a40"> <i class="fas fa-pencil-alt"></i></a>';
            editarCelula.addEventListener('click', (event) => editarItem(event, item));
            linha.appendChild(editarCelula);


            const excluirCelula = document.createElement('td');
            excluirCelula.innerHTML = '<a href="#" style="color: #343a40"><i class="fas fa-trash-alt"></i></a>';
            excluirCelula.addEventListener('click', (event) => excluirItem(event));
            linha.appendChild(excluirCelula);

            tabelaCorpo.appendChild(linha);
        });

    }
}

function excluirItem(event) {
    const linha = event.target.closest('tr');
    if (linha) {
        linha.remove();
    }
}


function editarItem(event, item) {
    event.preventDefault();

    const linha = event.target.closest('tr');
    if (!linha) return;

    const celulas = linha.querySelectorAll('td');
    const quantidadeCelula = celulas[2];


    const quantidadeAtual = quantidadeCelula.textContent.trim();
    const valorQuantidade = parseInt(quantidadeAtual, 10) || 1;


    const inputQuantidade = document.createElement('input');
    inputQuantidade.type = 'number';
    inputQuantidade.value = valorQuantidade;
    inputQuantidade.style.width = '60px';
    inputQuantidade.min = '1';

    quantidadeCelula.innerHTML = '';
    quantidadeCelula.appendChild(inputQuantidade);


    const botaoEditar = celulas[4];
    botaoEditar.innerHTML = `<a href="#" style="color: #28a745"><i class="fas fa-check"></i></a>`;


    const confirmarEdicao = (e) => {
        e.preventDefault();

        const novaQuantidade = parseInt(inputQuantidade.value, 10);

        // Valida o novo valor
        if (isNaN(novaQuantidade) || novaQuantidade <= 0) {
            alert("Por favor, insira uma quantidade válida maior que 0.");
            return;
        }

        quantidadeCelula.textContent = novaQuantidade.toString();

        botaoEditar.innerHTML = `<a href="#" style="color: #343a40"><i class="fas fa-pencil-alt"></i></a>`;

        botaoEditar.removeEventListener('click', confirmarEdicao);
        botaoEditar.addEventListener('click', (event) => editarItem(event, item));
    };


    botaoEditar.addEventListener('click', confirmarEdicao);
}


async function validarCompra() {
    const tabela = document.getElementById('tabelaVendas');
    const linhas = tabela.querySelectorAll('tr');

    const listaObjetos = [];
    let total = 0;

    linhas.forEach(linha => {
        const colunas = linha.querySelectorAll('td');
        const objeto = {

            idProduto: colunas[0].innerHTML,
            preco: parseFloat(colunas[3].innerHTML),
            quantidade: parseInt(colunas[2].textContent.trim(), 10)
        };
        total += parseFloat(colunas[3].textContent.trim()) * parseInt(colunas[2].textContent.trim(), 10);
        listaObjetos.push(objeto);

    });


    const token = localStorage.getItem("token");
    const resposta = await fetch(`/api/vendas/produtos`, {
        method: 'POST',
        headers: {
            "Authorization": `Bearer ${token}`,
            "Content-Type": "application/json"
        },
        body: JSON.stringify(listaObjetos)
    });

    console.log("resposta do metodo validar " + resposta.status);
    if (!resposta.ok) {
        if (resposta.status === 400) {
            const erro = await resposta.json();
            alert(erro.message)
            return;
        }
    }

    $('#paymentModal').modal('show');

    localStorage.setItem("produtos", JSON.stringify(listaObjetos));
}


async function finalizarCompra() {

    const metodoPagamento = document.getElementById("paymentMethod").value;
    if (!metodoPagamento) {
        alert("Por favor, selecione um método de pagamento.");
        return;
    }

    const listaProdutos = JSON.parse(localStorage.getItem("produtos"));

    const objetoVenda = {
        itens: listaProdutos,
        metodoPagamento: metodoPagamento
    }


    const token = localStorage.getItem("token");
    const resposta = await fetch(`/api/vendas`, {
        method: 'POST',
        headers: {
            "Authorization": `Bearer ${token}`,
            "Content-Type": "application/json"
        },
        body: JSON.stringify(objetoVenda)
    });
    //
    if (!resposta.ok) {
        if (resposta.status !== 400) {
            alert('Erro no envio dos dados - ' + resposta.status);
            return;
        }
        if (resposta.status === 400) {
            const erro = await resposta.json();
            alert(erro.message)
            return;
        }
    }

    alert("Venda Realizada")

}

