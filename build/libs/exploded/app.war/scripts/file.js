document.addEventListener('DOMContentLoaded', () => {
    const zone = document.getElementById('uploadFile_Loader');
    const form = document.getElementById('uploadForm');

    ['dragenter','dragover'].forEach(evt => {
        zone.addEventListener(evt, e => {
            e.preventDefault();
            e.stopPropagation();
            zone.classList.add('dragover');
        });
    });
    ['dragleave','drop'].forEach(evt => {
        zone.addEventListener(evt, e => {
            e.preventDefault();
            e.stopPropagation();
            zone.classList.remove('dragover');
        });
    });

    zone.addEventListener('drop', e => {
        const dt = e.dataTransfer;
        if (dt.files.length) {
            const fileInput = document.getElementById('uploadForm_file');
            fileInput.files = dt.files;
            form.dispatchEvent(new Event('submit', { cancelable: true, bubbles: true }));
        }
    });
});


document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('uploadForm');
    const zone = document.getElementById('uploadFile_Loader');
    const spinner = document.getElementById('spinner');

    form.addEventListener('submit', async (e) => {
        e.preventDefault();

        spinner.classList.remove('hidden');
        zone.classList.add('uploading');

        const formData = new FormData(form);

        try {
            const resp = await fetch('/api/upload', {
                method: 'POST',
                credentials: 'include',
                body: formData
            });
            if (!resp.ok) throw new Error(`Upload failed: ${resp.statusText}`);

            const json = await resp.json();
            console.log('Uploaded:', json);

        } catch (err) {
            console.error(err);
            alert('Ошибка при загрузке: ' + err.message);
        } finally {
            spinner.classList.add('hidden');
            zone.classList.remove('uploading');
        }
    });
});
