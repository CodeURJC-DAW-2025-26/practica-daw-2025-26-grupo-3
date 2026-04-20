import type { ProductBasicDTO } from "./ProductBasicDTO";

export interface OrderItemDTO {
    id: number,
    product: ProductBasicDTO,
    quantity: number
}