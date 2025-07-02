console.log("login.js loaded!");

document.getElementById("loginForm").addEventListener("submit", (e) => {
    e.preventDefault();

    const data = {
        email: document.getElementById("email").value,
        password: document.getElementById("password").value
    }

    console.log(data);

    fetch("/api/login", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(data)
    }).then(resp => {
        if(!resp.ok) {
            throw new Error("Error during login");
        }
        return resp.json();
    }).then(result => {
        window.location.href = '/api/file';
    }) .catch(err => {
        alert("Error: " + err.message);
    })
});