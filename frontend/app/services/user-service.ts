import type { UserDTO } from "~/dtos/UserDTO";
import type { userBasicDTO } from "~/dtos/userBasicDTO";

const base_auth_url = "/api/v1/auth";
const base_user_url = "/api/v1/users";

export class HttpError extends Error {
    status: number;

    constructor(status: number, message?: string) {
        super(message ?? `HTTP ${status}`)
        this.status = status;
    }
}

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
    console.log("Login response:", data);
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

export async function signup(newUserData: userBasicDTO) {
    const url = `${base_user_url}`;
    const response = await fetch(url, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(newUserData)
    });

    if (!response.ok) {
        const errorData = await response.json(); //Error json sent by backend
        throw new Error(errorData.trace || "Campos incorrectos");
    }

    return await response.json();
}