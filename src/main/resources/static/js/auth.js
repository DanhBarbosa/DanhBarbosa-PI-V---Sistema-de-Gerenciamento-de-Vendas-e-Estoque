const token = localStorage.getItem("token");
if (!token) {
    window.location.href = "/login.html";
}

function logout() {
    localStorage.clear();
    window.location.href = "/login.html";
}

function toggleSubMenu(subMenuId) {
    const allSubMenus = document.querySelectorAll('.sub-menu');
    allSubMenus.forEach(subMenu => {
        subMenu.classList.remove('active');
    });
    const currentSubMenu = document.getElementById(subMenuId);
    currentSubMenu.classList.toggle('active');
}

function jwtFetch(url, options = {}) {
    const token = localStorage.getItem("token");

    const headers = {
        ...(options.headers || {}),
        "Authorization": `Bearer ${token}`,
        "Content-Type": "application/json"
    };

    return fetch(url, {
        ...options,
        headers
    });
}

async function carregarConteudo() {
    jwtFetch("/api/usuarios")
        .then(res => res.json())
        .then(data => {
            console.log("Usuários:", data);
        })
        .catch(err => {
            console.error("Erro ao buscar usuários", err);
            window.location.href = "/login.html";
        });
}

carregarConteudo();