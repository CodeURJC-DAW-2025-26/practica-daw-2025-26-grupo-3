import type { UserBasicDTO } from "~/dtos/userBasicDTO";

export interface AdminOrderDataDTO {
    orderID: number;
    date: string;
    state: number;
    totalPrice: number;
    user: UserBasicDTO;
}