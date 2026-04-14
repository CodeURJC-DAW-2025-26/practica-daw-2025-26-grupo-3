export interface UserDTO {
    id: number,
    userName: string,
    surname: string,
    email: string,
    address: string,
    password: string,
    imageId: number | null,
    favouriteState: number,
    roles: string[]
}