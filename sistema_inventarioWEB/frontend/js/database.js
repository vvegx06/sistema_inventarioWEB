const API_URL = "http://localhost:8080/api";

async function obtenerProductos() {
    try {
        const response = await fetch(`${API_URL}/productos`);

        if (!response.ok) {
            throw new Error("No se pudieron obtener los productos");
        }

        return await response.json();
    } catch (error) {
        console.error("Error al obtener productos:", error);
        return [];
    }
}

async function guardarProducto(producto) {
    try {
        const response = await fetch(`${API_URL}/productos`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(producto)
        });

        const data = await response.json();

        if (!response.ok) {
            throw new Error(data.error || "No se pudo guardar el producto");
        }

        return data;
    } catch (error) {
        console.error("Error al guardar producto:", error);
        alert(error.message);
        return null;
    }
}