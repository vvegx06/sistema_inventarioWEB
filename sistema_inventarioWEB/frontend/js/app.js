document.addEventListener("DOMContentLoaded", async function() {
    await cargarResumenDashboard();
    await cargarProductos();

    const formProducto = document.getElementById("formProducto");
    if (formProducto) {
        formProducto.addEventListener("submit", async function(e) {
            e.preventDefault();

            const producto = {
                nombreProducto: document.getElementById("nombreProducto").value.trim(),
                costo: parseFloat(document.getElementById("costoProducto").value),
                estado: document.getElementById("estadoProducto").value.trim(),
                marca: document.getElementById("marcaProducto").value.trim()
            };

            const resultado = await guardarProducto(producto);

            if (resultado) {
                alert("Producto guardado correctamente");
                formProducto.reset();

                const modalElement = document.getElementById("modalProducto");
                if (modalElement && window.bootstrap) {
                    const modal = bootstrap.Modal.getInstance(modalElement);
                    if (modal) {
                        modal.hide();
                    }
                }

                await cargarProductos();
                await cargarResumenDashboard();
            }
        });
    }
});

async function cargarResumenDashboard() {
    const totalProductos = document.getElementById("total-productos");
    if (!totalProductos) return;

    const productos = await obtenerProductos();
    totalProductos.textContent = productos.length;
}

async function cargarProductos() {
    const tabla = document.getElementById("tabla-productos");
    if (!tabla) return;

    const productos = await obtenerProductos();
    tabla.innerHTML = "";

    if (productos.length === 0) {
        tabla.innerHTML = `
            <tr>
                <td colspan="6">No hay productos registrados</td>
            </tr>
        `;
        return;
    }

    productos.forEach(producto => {
        tabla.innerHTML += `
            <tr>
                <td>${producto.idProducto}</td>
                <td>${producto.nombreProducto}</td>
                <td>${producto.costo}</td>
                <td>${producto.estado}</td>
                <td>${producto.marca}</td>
                <td>
                    <button class="btn btn-warning btn-sm" disabled>
                        <i class="bi bi-pencil"></i>
                    </button>
                    <button class="btn btn-danger btn-sm" disabled>
                        <i class="bi bi-trash"></i>
                    </button>
                </td>
            </tr>
        `;
    });
}