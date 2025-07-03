document.addEventListener('DOMContentLoaded', () => {
  const zone    = document.getElementById('uploadFile_Loader');
  const form    = document.getElementById('uploadForm');
  const fileIn  = document.getElementById('uploadForm_file');
  const spinner = document.getElementById('spinner');
  let isUploading = false;

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

  fileIn.addEventListener('change', () => {
    if (fileIn.files.length) {
      submitUpload();
    }
  });

  async function submitUpload() {
    if (isUploading) return;
    isUploading = true;
    spinner.classList.remove('hidden');
    zone.classList.add('uploading');

    const formData = new FormData(form);
    try {
      const resp = await fetch('/api/upload', {
        method: 'POST',
        credentials: 'include',
        body: formData
      });
      if (!resp.ok) throw new Error(`Ошибка при загрузке: ${resp.status}`);
      const { id } = await resp.json();

      const linkTo = `localhost:8080/api/file/download?id=${id}`;
      const link = document.getElementById('link');
      link.href = linkTo;
      link.textContent = linkTo;
      console.log('Uploaded:', id);
    } catch (err) {
      console.error(err);
      alert('Ошибка при загрузке: ' + err.message);
    } finally {
      spinner.classList.add('hidden');
      zone.classList.remove('uploading');
      isUploading = false;
      form.reset();
      fileIn.value = '';
    }
  }
});
