import type { UserDTO } from "~/dtos/user-dto";
import { create } from "zustand";
import { HttpError, login, logout, reqIsLogged } from "~/services/login-service";

export interface UserState {
    currentUser: UserDTO | null,
    error: string | null,
    loadLoggedUser: () => Promise<void>,
    login: (email: string, pass: string) => void,
    logout: () => void
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
        }
        catch (error) {
            const message = "Bad credentials. Incorrect email or password";
            set({ error: message });
        }
    },

    logout: async () => {
        set({ currentUser: null, error: null });

        try {
            await logout();
        }
        catch (error) {
            console.log(error);
            set({ error: "Error has ocurred when tried to logout" })
        }
    }

}));