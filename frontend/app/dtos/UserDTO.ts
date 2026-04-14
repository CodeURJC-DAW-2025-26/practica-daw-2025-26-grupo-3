export interface UserDTO {
    id: number,
    userName: string,
    surname: string,
    address: string,
    email: string,
    state: boolean,
    password: string,
    imageId: number | null,
    favouriteState: number,
    roles: string[]
}