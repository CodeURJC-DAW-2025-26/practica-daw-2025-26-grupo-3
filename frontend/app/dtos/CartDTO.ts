import type { CartItemDTO } from "./CartItemDTO";

export interface CartDTO {
    id: number;
    cartItems: CartItemDTO[] | null;
}