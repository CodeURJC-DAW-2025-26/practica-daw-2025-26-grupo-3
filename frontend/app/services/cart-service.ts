import type { CartDTO } from "~/dtos/CartDTO";

const base_url = "/api/v1/carts"

export async function getCartInfo(): Promise<CartDTO> {
    const url = `${base_url}/me`;
    const response = await fetch(url, {
        credentials: "include"
    });

    if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message);
    }

    return await response.json();
}

export async function deleteCartItem(itemId: number) {
    const url = `${base_url}/me/items/${itemId}`;
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
    const url = `${base_url}/me/items/${id}`;

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