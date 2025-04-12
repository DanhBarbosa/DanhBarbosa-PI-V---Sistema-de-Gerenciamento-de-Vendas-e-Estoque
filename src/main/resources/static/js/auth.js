const token = localStorage.getItem("token");
if (!token) {
    window.location.href = "/login.html";
}
function logout() {
    localStorage.clear();
    window.location.href = "/login.html";
}
