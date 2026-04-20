import type { OrderItemDTO } from "./OrderItemDTO";

export interface OrderDTO {
    orderID: number,
    date: string,
    state: number,
    totalPrice: number,
    orderItems: OrderItemDTO[];
}