import type { ProductBasicDTO } from "~/dtos/ProductBasicDTO";


const API_URL = "/api/v1/products";
const API_IMAGES_URL = "/api/v1/images";

export async function getProducts(): Promise<ProductBasicDTO[]> {
    const res = await fetch(`${API_URL}/`);
    const json = await res.json();
    return json.content || [];
}

export async function getBasicProduct(id: number): Promise<ProductBasicDTO> {
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
    productName: string,
    description: string,
    price: number,
    state: number
): Promise<ProductBasicDTO> {
    const response = await fetch(`${API_URL}/${id}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ productName, description, price, state }),
    });

    if (!response.ok) {
        throw new Error("Error updating product");
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
        method: "POST",
        body: formData,
    });

    if (!response.ok) {
        throw new Error("Error uploading image");
    }
}

export async function deleteProductImage(
    bookId: number,
    imageId: number,
): Promise<void> {
    const response = await fetch(`${API_URL}/${bookId}/images/${imageId}`, {
        method: "DELETE",
    });

    if (!response.ok) {
        throw new Error("Error deleting image");
    }
}

export async function replaceImage(
    imageId: number,
    imageFile: File,
): Promise<void> {
    const formData = new FormData();
    formData.append("imageFile", imageFile);

    const response = await fetch(`${API_IMAGES_URL}/${imageId}/media`, {
        method: "PUT",
        body: formData,
    });

    if (!response.ok) {
        throw new Error("Error replacing image");
    }
}



