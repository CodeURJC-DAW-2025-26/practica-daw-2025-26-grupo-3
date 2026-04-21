import type { CartDTO } from "~/dtos/CartDTO";
import { HttpError } from "./HttpError";

const base_url_cart = "/api/v1/carts";
const base_url_order = "/api/v1/orders";

export async function getCartInfo(): Promise<CartDTO> {
    const url = `${base_url_cart}/me`;
    const response = await fetch(url, {
        credentials: "include"
    });

    if (!response.ok) {
        let errorMessage = `HTTP ${response.status}`;
        try {
            const errorData = await response.json();
            errorMessage = errorData.message ?? errorMessage;
        } catch {
            // Keep default message when response body is not JSON.
        }
        throw new HttpError(response.status, errorMessage);
    }

    return await response.json();
}

export async function deleteCartItem(itemId: number) {
    const url = `${base_url_cart}/me/items/${itemId}`;
    const response = await fetch(url, {
        method: "DELETE",
        credentials: "include"
    });

    if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message);
    }

    return await response.json();
}

export async function updateQuantity(id: number, op: number) {
    const url = `${base_url_cart}/me/items/${id}`;

    const response = await fetch(url, {
        method: "PUT",
        credentials: "include",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ operation: op })
    });

    if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message);
    }

    return await response.json();
}

export async function cartToOrder() {
    const url = `${base_url_order}`;
    const response = await fetch(url, {
        method: "POST",
        credentials: "include"
    });

    if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message);
    }

    return await response.json();
}

export async function getOrdersInfo() {
    const url = `${base_url_order}/me`;
    const response = await fetch(url, {
        credentials: "include"
    });

    if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message);
    }

    return await response.json();
}

export async function addToCart(id: number) {
    const url = `${base_url_cart}/me/items`;
    const response = await fetch(url, {
        method: "POST",
        credentials: "include",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ productId: id })
    });

    if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message);
    }

    return await response.json();
}

//PDF BILL API CALLS

export async function getAllPDF() {
    const url = `${base_url_order}/all/bill`;
    const response = await fetch(url, {
        credentials: "include"
    });

    if (!response.ok) {
        const errroData = await response.json();
        throw new Error(errroData.mesage);
    }

    return await response.blob();   //We return a blob file instead a json object.
}

export async function getPdfById(id: number) {
    const url = `${base_url_order}/${id}/bill`;
    const response = await fetch(url, {
        credentials: "include"
    });

    if (!response.ok) {
        const errroData = await response.json();
        throw new Error(errroData.mesage);
    }

    return await response.blob();
}