import type { UserDTO } from "~/dtos/user-dto";

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
        console.error("Error response body:", errorData);
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

    console.log(response);
}