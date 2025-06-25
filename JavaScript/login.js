document.getElementById('loginForm').addEventListener('submit', async function(e) {
    e.preventDefault();
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;

    const response = await fetch('http://localhost:8080/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username, password })
    });

    const result = await response.json();
    if(result.success){
        alert("Inicio de sesión exitoso");
        // Aquí puedes redirigir o mostrar contenido
    } else {
        alert("Usuario o contraseña incorrectos");
    }
});