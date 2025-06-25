console.log("JsonForm.js loaded!");

document.getElementById("getAllUsersForm").addEventListener("submit", (e) => {
    e.preventDefault();

    const data = {
        email: document.getElementById("findUserInput").value
    }

    fetch("/main", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(data)
    }).then(resp => {
        // if(!resp.ok) {
        //     throw new Error("Error during registration");
        // }
        // return resp.json();
    }).then(result => {
        console.log(result.email);
        // location.replace("/main.html")
    }) .catch(err => {
        // alert("Error: " + err.message)
    })
});