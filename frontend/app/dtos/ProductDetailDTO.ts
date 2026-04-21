import type { UserBasicDTO } from "./UserBasicDTO";

export interface ProductDetailDTO {
    id: number,
    productName: string,
    price: number,
    state: number,
    description: string,
    averageRating: number,
    reviewCount: number,
    images: { id: number }[],
    seller: UserBasicDTO
}
