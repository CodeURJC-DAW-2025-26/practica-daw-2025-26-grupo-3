import type { UserDTO } from "~/dtos/UserDTO";
import { create } from "zustand";
import { HttpError, login, logout, reqIsLogged, signup } from "~/services/user-service";
import type { userBasicDTO } from "~/dtos/userBasicDTO";

export interface UserState {
    currentUser: UserDTO | null,
    error: string | null,
    loadLoggedUser: () => Promise<void>,
    login: (email: string, pass: string) => Promise<UserDTO | null>,
    logout: () => void
    signup: (data: userBasicDTO) => void,
}

export const useUserState = create<UserState>((set, get) => ({
    currentUser: null,
    error: null,
    loadLoggedUser: async () => {
        set({ currentUser: null, error: null });

        try {
            const user = await reqIsLogged();
            set({ currentUser: user });
        }
        catch (error) {
            if (error instanceof HttpError && error.status === 401) {
                set({ currentUser: null, error: null });
                return;
            }
        }
    },
    login: async (email: string, pass: string) => {
        set({ currentUser: null, error: null });

        try {
            await login(email, pass);
            await get().loadLoggedUser();
            return get().currentUser;
        }
        catch (error) {
            set({ error: "Bad credentials. Incorrect email or password" });
            throw error;
        }
    },
    logout: async () => {
        set({ currentUser: null, error: null });

        try {
            await logout();
        }
        catch (error) {
            set({ error: "Error has ocurred when tried to logout" })
        }
    },
    signup: async (newUserData: userBasicDTO) => {
        set({ currentUser: null, error: null });
        try {
            await signup(newUserData);
        }
        catch (err) {
            const errorMessage = err instanceof Error ? err.message : "Error desconocido al registrarse"
            set({ error: errorMessage });
            throw err;
        }
    },
}));