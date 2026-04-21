import type { UserDTO } from "~/dtos/UserDTO";
import type { UserCreateDTO } from "~/dtos/UserCreateDTO";
import type { UserPostDTO } from "~/dtos/UserPostDTO";
import type { UserPassDTO } from "~/dtos/UserPassDTO";
import type { UserPassBasicDTO } from "~/dtos/UserPassBasicDTO";
import { HttpError } from "./HttpError";

export { HttpError };

const base_auth_url = "/api/v1/auth";
const base_user_url = "/api/v1/users";

export async function reqIsLogged(): Promise<UserDTO> {
    const res = await fetch(`${base_user_url}/logged`);

    if (!res.ok) {
        throw new HttpError(res.status);
    }

    return await res.json();
}

export async function login(email: string, pass: string) {
    const response = await fetch(base_auth_url + "/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ username: email, password: pass })
    });

    if (!response.ok) {
        const errorData = await response.text();
        throw new HttpError(response.status, errorData);
    }

    const data = await response.json();
    if (data?.status === "FAILURE") {
        throw new HttpError(401, data?.message ?? "Bad credentials");
    }
    return data;
}

export async function logout(): Promise<void> {
    const response = await fetch(`${base_auth_url}/logout`, {
        method: "POST"
    })

    if (!response.ok) {
        throw new Error();
    }
}

export async function signup(newUserData: UserCreateDTO) {
    const url = `${base_user_url}`;
    const response = await fetch(url, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(newUserData)
    });

    if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message);
    }

    return await response.json();
}

export async function uploadUserImage(image: File, userId: number) {
    const formData = new FormData();
    formData.append("file", image);

    const url = `${base_user_url}/${userId}/image`;
    const response = await fetch(url, {
        method: "POST",
        body: formData
    });

    return response;
}

export async function updateUser(newUserData: UserPostDTO, userId: number) {
    const url = `${base_user_url}/${userId}`;

    const response = await fetch(url, {
        method: "PUT",
        credentials: "include",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(newUserData),
    });

    if (!response.ok) {
        const errorData = await response.json();

        throw new Error(errorData.message);
    }

    return await response.json();
}

export async function changePassword(newPassData: UserPassDTO, userId: number) {
    const url = `${base_user_url}/${userId}/pass`;

    const response = await fetch(url, {
        method: "PUT",
        credentials: "include",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(newPassData),
    });

    if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message);
    }

    return await response.json();
}

export async function deleteUser(password: UserPassBasicDTO, userId: number) {
    const url = `${base_user_url}/${userId}`;
    const response = await fetch(url, {
        method: "DELETE",
        credentials: "include",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(password),
    });

    if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.trace)
    }

    return await response.json();
}