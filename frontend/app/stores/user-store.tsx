import type { UserDTO } from "~/dtos/UserDTO";
import { create } from "zustand";
import { changePassword, deleteUser, HttpError, login, logout, reqIsLogged, signup, updateUser } from "~/services/user-service";
import type { UserPostDTO } from "~/dtos/UserPostDTO";
import type { UserCreateDTO } from "~/dtos/UserCreateDTO";
import type { UserPassDTO } from "~/dtos/UserPassDTO";
import type { UserPassBasicDTO } from "~/dtos/UserPassBasicDTO";

export interface UserState {
    currentUser: UserDTO | null,
    error: string | null,
    loadLoggedUser: () => Promise<void>,
    login: (email: string, pass: string) => Promise<UserDTO | null>,
    logout: () => void
    signup: (data: UserCreateDTO) => void,
    editUser: (data: UserPostDTO, id: number) => void,
    editPass: (data: UserPassDTO, id: number) => void,
    deleteUser: (passsword: UserPassBasicDTO, id: number) => void
}

export const useUserState = create<UserState>((set, get) => ({
    currentUser: null,
    error: null,
    loadLoggedUser: async () => {
        set({ error: null });

        try {
            const user = await reqIsLogged();
            set({ currentUser: user });
        }
        catch (error) {
            if (error instanceof HttpError && error.status === 401) {
                set({ error: null });
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
            set({ error: "Credenciales incorrectas" });
            throw error;
        }
    },
    editUser: async (data: UserPostDTO, id: number) => {
        set({ error: null });
        try {
            await updateUser(data, id);
        }
        catch (err) {
            const errorMessage = err instanceof Error ? err.message : "Se ha producido un error editando el usuario";
            set({ error: errorMessage });
            throw err;
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
    signup: async (newUserData: UserCreateDTO) => {
        set({ currentUser: null, error: null });
        try {
            await signup(newUserData);
        }
        catch (err) {
            const errorMessage = err instanceof Error ? err.message : "Error desconocido al registrarse";
            set({ error: errorMessage });
            throw err;
        }
    },
    editPass: async (newPassData: UserPassDTO, id: number) => {
        set({ error: null });
        try {
            await changePassword(newPassData, id);
        } catch (err) {
            const errorMessage = err instanceof Error ? err.message : "Error desconocido al registrarse";
            set({ error: errorMessage });
            throw err;
        }
    },
    deleteUser: async (password: UserPassBasicDTO, id: number) => {
        set({ error: null });
        try {
            await deleteUser(password, id);
        }
        catch (err) {
            const errorMessage = err instanceof Error ? err.message : "Error desconcido al eliminar la cuenta";
            set({ error: errorMessage });
            throw err;
        }

        set({ currentUser: null });
    }
}));