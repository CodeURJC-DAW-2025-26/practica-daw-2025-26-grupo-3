import type { UserBasicDTO } from "./UserBasicDTO";

export interface ReviewDTO {
    user: UserBasicDTO,
    title: string,
    body: string,
    date: string,
    stars: number
}