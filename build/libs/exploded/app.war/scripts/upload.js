document.addEventListener('DOMContentLoaded', () => {
    const zone    = document.getElementById('uploadFile_Loader');
    const form    = document.getElementById('uploadForm');
    const fileIn  = document.getElementById('uploadForm_file');
    const spinner = document.getElementById('spinner');

    ['dragenter','dragover'].forEach(evt =>
        zone.addEventListener(evt, e => {
            e.preventDefault();
            zone.classList.add('dragover');
        })
    );
    ['dragleave','drop'].forEach(evt =>
        zone.addEventListener(evt, e => {
            e.preventDefault();
            zone.classList.remove('dragover');
        })
    );

    zone.addEventListener('drop', e => {
        if (e.dataTransfer.files.length) {
            fileIn.files = e.dataTransfer.files;
            submitUpload();
        }
    });

    form.addEventListener('submit', e => {
        e.preventDefault();
        submitUpload();
    });

    async function submitUpload() {
        spinner.classList.remove('hidden');
        zone.classList.add('uploading');

        const formData = new FormData(form);
        try {
            const resp = await fetch('/api/upload', {
                method: 'POST',
                credentials: 'include',
                body: formData
            });
            const json = await resp.json();
            let linkTo = 'localhost:8080/api/file/download?id=' + json.id;

            console.log(linkTo);
            let link = document.getElementById("link");
            let text = document.createTextNode(linkTo);

            link.setAttribute('href', '');
            link.textContent = "";
            link.setAttribute('href', linkTo);
            link.append(text);
            
            console.log('Uploaded:', json);
        } catch (err) {
            console.error(err);
            alert('Ошибка при загрузке: ' + err.message);
        } finally {
            spinner.classList.add('hidden');
            zone.classList.remove('uploading');
        }
    }
});
