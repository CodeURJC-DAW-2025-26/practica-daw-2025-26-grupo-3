import type { CartDTO } from "~/dtos/CartDTo";

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