import type { UserDTO } from "~/dtos/UserDTO";
import type { UserCreateDTO } from "~/dtos/UserCreateDTO";
import type { UserPostDTO } from "~/dtos/UserPostDTO";
import type { UserPassDTO } from "~/dtos/UserPassDTO";
import type { UserPassBasicDTO } from "~/dtos/UserPassBasicDTO";
import { HttpError } from "./HttpError";

export { HttpError };

const base_admin_url = "/api/v1";

//obtain user details by id 
export async function getUserById(userId: string | number): Promise<UserDTO> {
    const url = `${base_admin_url}/users/${userId}`;
    const response = await fetch(url, {
        method: "GET",
        credentials: "include" //send session cookie
    });

    if (!response.ok) {
        throw new HttpError(response.status, `Error fetching user with ID ${userId}`);
    }

    return await response.json();
}

//obtain all users 
export async function getAllUsers(): Promise<UserDTO[]> {
    const url = `${base_admin_url}/users`;
    const response = await fetch(url, {
        method: "GET",
        credentials: "include"
    })

    if (!response.ok) {
        throw new HttpError(response.status, `Error fetching users list`);
    }

    return await response.json();
}

//change user's block state 
export async function toggleUserBlockStatus(userId: string | number, newState: boolean) {
    const url = `${base_admin_url}/users/${userId}/state`;
    const response = await fetch(url, {
        method: "PUT",
        credentials: "include",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ state: newState })
    });

    if (!response.ok) {
        throw new HttpError(response.status, `Error updating state for user ${userId}`);
    }
}