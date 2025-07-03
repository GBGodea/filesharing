const pageSizeInput = document.getElementById("limit");
const paginationContainer = "pagination";              
const tableBodyId = "t-body";                           
let currentPage = 1;

async function loadPage(page) {
  currentPage = page;

  const limit = parseInt(pageSizeInput.value, 10) || 10;
  const offset = (currentPage - 1) * limit;
  try {
    const resp = await fetch(`/api/admin/files?limit=${limit}&offset=${offset}`, {
      method: "GET",
      credentials: "include"
    });
    if (!resp.ok) throw new Error(`Server responded ${resp.status}`);
    const pageData = await resp.json();

    renderTable(pageData.items);
    const totalPages = Math.ceil(pageData.total / limit);
    renderPagination(paginationContainer, currentPage, totalPages, loadPage);

  } catch (err) {
    console.error("Fetch error:", err);
  }
}

function renderTable(files) {
  const body = document.getElementById(tableBodyId);
  body.innerHTML = "";

  files.forEach(file => {
    const tr = document.createElement("tr");
    tr.innerHTML = `
      <td>${file.name}</td>
      <td>${file.size}</td>
      <td>${file.uploadDate}</td>
      <td>${file.expirationDate}</td>
      <td>${file.isExpired ? "Yes" : "No"}</td>
      <td>${file.downloadCount}</td>
    `;
    body.appendChild(tr);
  });
}

function buildPagination(current, total, maxVisible = 5) {
  const pages = [];
  if (total <= maxVisible) {
    for (let i = 1; i <= total; i++) pages.push(i);
    return pages;
  }
  const half = Math.floor(maxVisible / 2);
  let start = current - half;
  let end   = current + half;
  if (start < 1) {
    start = 1;
    end = maxVisible;
  }
  if (end > total) {
    end = total;
    start = total - maxVisible + 1;
  }
  if (start > 1) {
    pages.push(1);
    if (start > 2) pages.push("...");
  }
  for (let i = start; i <= end; i++) pages.push(i);
  if (end < total) {
    if (end < total - 1) pages.push("...");
    pages.push(total);
  }
  return pages;
}

function renderPagination(containerId, current, total, onPageClick) {
  const container = document.getElementById(containerId);
  container.innerHTML = "";

  const pages = buildPagination(current, total, 5);
  pages.forEach(item => {
    const btn = document.createElement("button");
    btn.textContent = item;
    btn.disabled = (item === "..." || item === current);
    btn.classList.toggle("active", item === current);
    if (item !== "...") {
      btn.addEventListener("click", () => onPageClick(item));
    }
    container.appendChild(btn);
  });
}

document.getElementById("data-storage").addEventListener("submit", (e) => {
  e.preventDefault();
  loadPage(1);
});

window.addEventListener("DOMContentLoaded", () => {
  loadPage(1);
});
