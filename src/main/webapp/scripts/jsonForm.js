console.log("jsonForm.js loaded!");

document.getElementById("registerForm").addEventListener("submit", (e) => {
    e.preventDefault();

    const data = {
        email: document.getElementById("email").value,
        password: document.getElementById("password").value
    }

    fetch("/api/register", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(data)
    }).then(resp => {
        if(!resp.ok) {
            throw new Error("Error during registration");
        }
        return resp.json();
    }).then(result => {
        location.replace("/login.html")
    }) .catch(err => {
        alert("Error: " + err.message)
    })
});