const API_URL = '/api/productos';

const form = document.getElementById('producto-form');
const idInput = document.getElementById('producto-id');
const nombreInput = document.getElementById('nombre');
const precioInput = document.getElementById('precio');
const stockInput = document.getElementById('stock');
const categoriaInput = document.getElementById('categoria');
const submitBtn = document.getElementById('submit-btn');
const formTitle = document.getElementById('form-title');
const tablaBody = document.getElementById('tabla-body');
const emptyState = document.getElementById('empty-state');
const toast = document.getElementById('toast');

function mostrarToast(mensaje, tipo = 'ok') {
  toast.textContent = mensaje;
  toast.className = `toast show ${tipo}`;
  setTimeout(() => { toast.className = 'toast'; }, 2500);
}

async function cargarProductos() {
  const res = await fetch(API_URL);
  const productos = await res.json();

  tablaBody.innerHTML = '';
  emptyState.style.display = productos.length === 0 ? 'block' : 'none';

  productos.forEach(p => {
    const tr = document.createElement('tr');
    tr.innerHTML = `
      <td>${p.nombre}</td>
      <td>$${Number(p.precio).toFixed(2)}</td>
      <td>${p.stock}</td>
      <td><span class="badge">${p.categoria}</span></td>
      <td class="row-actions">
        <button data-action="editar" data-id="${p.id}">Editar</button>
        <button data-action="eliminar" data-id="${p.id}" class="del">Eliminar</button>
      </td>
    `;
    tablaBody.appendChild(tr);
  });
}

function limpiarFormulario() {
  idInput.value = '';
  form.reset();
  formTitle.textContent = 'Nuevo producto';
  submitBtn.textContent = 'Guardar';
}

form.addEventListener('submit', async (e) => {
  e.preventDefault();

  const producto = {
    nombre: nombreInput.value.trim(),
    precio: parseFloat(precioInput.value),
    stock: parseInt(stockInput.value, 10),
    categoria: categoriaInput.value.trim()
  };

  if (producto.precio < 0 || producto.stock < 0) {
    mostrarToast('El precio y el stock no pueden ser negativos', 'err');
    return;
  }

  const id = idInput.value;
  const esEdicion = Boolean(id);

  const res = await fetch(esEdicion ? `${API_URL}/${id}` : API_URL, {
    method: esEdicion ? 'PUT' : 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(producto)
  });

  if (!res.ok) {
    const error = await res.json().catch(() => ({ error: 'Ocurrió un error al guardar el producto' }));
    mostrarToast(error.error || 'Ocurrió un error al guardar el producto', 'err');
    return;
  }

  mostrarToast(esEdicion ? 'Producto actualizado' : 'Producto creado');
  limpiarFormulario();
  cargarProductos();
});

tablaBody.addEventListener('click', async (e) => {
  const btn = e.target.closest('button');
  if (!btn) return;

  const id = btn.dataset.id;

  if (btn.dataset.action === 'eliminar') {
    if (!confirm('¿Eliminar este producto?')) return;
    await fetch(`${API_URL}/${id}`, { method: 'DELETE' });
    mostrarToast('Producto eliminado');
    cargarProductos();
  }

  if (btn.dataset.action === 'editar') {
    const res = await fetch(`${API_URL}/${id}`);
    const p = await res.json();

    idInput.value = p.id;
    nombreInput.value = p.nombre;
    precioInput.value = p.precio;
    stockInput.value = p.stock;
    categoriaInput.value = p.categoria;

    formTitle.textContent = `Editando: ${p.nombre}`;
    submitBtn.textContent = 'Actualizar';
    nombreInput.focus();
  }
});

cargarProductos();
