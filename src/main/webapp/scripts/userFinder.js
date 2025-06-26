console.log("jsonForm.js loaded!");

document.getElementById("getAllUsersForm").addEventListener("submit", (e) => {
    e.preventDefault();

    const data = {
        email: document.getElementById("findUserInput").value
    }

    fetch("/filepage", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(data)
    }).then(result => {
        console.log(result.email);
    })
});