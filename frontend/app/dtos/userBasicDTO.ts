export interface UserBasicDTO {
    id: number,
    userName: string,
    surname: string,
    address: string,
    email: string,
    state: boolean,
    favouriteState: number,
    imageId: number | null,
    roles: string[]
}