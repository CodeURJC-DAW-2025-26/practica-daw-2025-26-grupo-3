import type { ProductBasicDTO } from "~/dtos/ProductBasicDTO";
import type { ProductDetailDTO } from "~/dtos/ProductDetailDTO";
import type { ProductEditDTO } from "~/dtos/ProductEditDTO";


const API_URL = "/api/v1/products";
const API_IMAGES_URL = "/api/v1/images";

export async function getProducts(): Promise<ProductBasicDTO[]> {
    const res = await fetch(`${API_URL}/`);
    const json = await res.json();
    return Array.isArray(json) ? json : (json.content || []);
}

export async function getAllProducts(): Promise<ProductBasicDTO[]> {
    const res = await fetch(`${API_URL}/?size=50`);
    const json = await res.json();
    return Array.isArray(json) ? json : (json.content || []);
}

export async function getProductsPage(page: number, size: number = 4): Promise<any> {
    // We request a specific page (each page contains 4 more products)
    const res = await fetch(`${API_URL}/?page=${page}&size=${size}`);
    const data = await res.json();

    // We return the complete object
    return data;
}

export async function getBasicProduct(id: number): Promise<ProductDetailDTO> {
    const res = await fetch(`${API_URL}/${id}`);
    if (!res.ok) {
        throw new Error("Product not found");
    }
    return await res.json();
}

export async function addBasicProduct(
    productName: string,
    description: string,
    price: number,
    state: number
): Promise<ProductBasicDTO> {
    const response = await fetch(`${API_URL}/`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
            productName,
            description,
            price,
            state
        }),
    });

    if (!response.ok) {
        throw new Error("Error adding product");
    }

    return await response.json();
}

export async function removeProduct(id: number): Promise<void> {
    const response = await fetch(`${API_URL}/${id}`, {
        method: "DELETE",
    });

    if (!response.ok) {
        throw new Error("Error removing product");
    }
}

export async function updateProduct(
    id: number,
    productData: ProductEditDTO
): Promise<ProductBasicDTO> {
    const response = await fetch(`${API_URL}/${id}`, {
        method: "PUT",
        credentials: "include",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(productData),
    });

    if (!response.ok) {
        throw new Error("Error al actualizar el producto");
    }

    return await response.json();
}


export async function uploadProductImage(
    id: number,
    imageFile: File,
): Promise<void> {
    const formData = new FormData();
    formData.append("imageFile", imageFile);

    const response = await fetch(`${API_URL}/${id}/images/`, {
        credentials: "include",
        method: "POST",
        body: formData,
    });

    if (!response.ok) {
        throw new Error("Error al subir la imagen");
    }
}

export async function deleteProductImage(
    productId: number,
    imageId: number,
): Promise<void> {
    const response = await fetch(`${API_URL}/${productId}/images/${imageId}`, {
        credentials: "include",
        method: "DELETE",
    });

    if (!response.ok) {
        throw new Error("Error al eliminar la imagen");
    }
}



