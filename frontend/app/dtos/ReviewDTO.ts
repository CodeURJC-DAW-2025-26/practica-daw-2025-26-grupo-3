import type { UserBasicDTO } from "./userBasicDTO";

export interface ReviewDTO {
    user: UserBasicDTO,
    title: string,
    body: string,
    date: string,
    stars: number,
    id: number
}