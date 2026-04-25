/*
 * DEFENSE/ARCHITECTURE NOTE:
 * We do NOT create a separate 'admin-store' for authentication.
 * An Administrator is simply a regular User with elevated privileges (roles).
 * Reusing the existing 'user-store' ensures a single source of truth 
 * for the authentication state, preventing out-of-sync bugs and 
 * complying with DRY (Don't Repeat Yourself) principles. 
 * We verify admin authorization by checking the 'roles' array inside the UserDTO.
 */

// Import Data Transfer Objects (DTOs) for user data
import type { UserDTO } from "~/dtos/UserDTO";
import type { UserPostDTO } from "~/dtos/UserPostDTO";
import type { UserCreateDTO } from "~/dtos/UserCreateDTO";
import type { UserPassDTO } from "~/dtos/UserPassDTO";
import type { UserPassBasicDTO } from "~/dtos/UserPassBasicDTO";

// Import Zustand to create the global state
import { create } from "zustand";

// Import API services for authentication and user management
import { changePassword, deleteUser, HttpError, login, logout, reqIsLogged, signup, updateUser } from "~/services/user-service";

// 1. STATE INTERFACE
// Defines the structure of the authentication state and user operations
export interface UserState {
    currentUser: UserDTO | null, // Holds the logged-in user's data, or null if guest
    error: string | null, // Stores any authentication or profile update errors
    authMessage: string | null,
    authMessageVariant: "success" | "danger" | null,

    // Actions
    loadLoggedUser: () => Promise<void>,
    login: (email: string, pass: string) => Promise<UserDTO | null>,
    logout: () => void
    signup: (data: UserCreateDTO) => void,
    editUser: (data: UserPostDTO, id: number) => void,
    editPass: (data: UserPassDTO, id: number) => void,
    deleteUser: (passsword: UserPassBasicDTO, id: number) => void
}

// 2. STORE IMPLEMENTATION
export const useUserState = create<UserState>((set, get) => ({
    currentUser: null,
    error: null,
    authMessage: null,
    authMessageVariant: null,

    // Automatically check if a session exists in the backend (e.g., via HttpOnly cookies)
    // This is crucial for SPAs to persist login state across page refreshes.
    loadLoggedUser: async () => {
        set({ error: null }); // Clear previous errors

        try {
            // Ask the backend for the current user's data
            const user = await reqIsLogged();
            set({ currentUser: user });
        }
        catch (error) {
            // DEFENSE NOTE: A 401 Unauthorized simply means the user is not logged in.
            // We catch it specifically so it doesn't crash the app, and leave currentUser as null.
            if (error instanceof HttpError && error.status === 401) {
                set({ error: null });
                return;
            }
        }
    },

    // Authenticate a user
    login: async (email: string, pass: string) => {
        // Reset state before attempting to log in
        set({ currentUser: null, error: null, authMessage: null, authMessageVariant: null });

        try {
            // 1. Send credentials to the backend
            await login(email, pass);
            // 2. If successful, fetch and store the user data using our existing function
            await get().loadLoggedUser();
            set({ authMessage: "¡Inicio de sesión exitoso!", authMessageVariant: "success" });
            return get().currentUser;
        }
        catch (error) {
            // Set error message to be displayed in the UI
            set({ error: "Credenciales incorrectas", authMessage: null, authMessageVariant: null });
            throw error;
        }
    },

    // Update user profile information
    editUser: async (data: UserPostDTO, id: number) => {
        set({ error: null });
        try {
            await updateUser(data, id);
        }
        catch (err) {
            // Extract error message safely
            const errorMessage = err instanceof Error ? err.message : "Se ha producido un error editando el usuario";
            set({ error: errorMessage });
            throw err;
        }
    },

    // Terminate the user session
    logout: async () => {
        // Immediately clear the user from frontend state for a snappy UI
        set({ currentUser: null, error: null, authMessage: null, authMessageVariant: null });

        try {
            // Inform the backend to invalidate the session/cookie
            await logout();
            set({ authMessage: "Se ha cerrado la sesión correctamente.", authMessageVariant: "danger" });
        }
        catch (error) {
            set({ error: "Error has ocurred when tried to logout" })
        }
    },

    // Register a new user account
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

    // Update the user's password
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

    // Delete the user's account (requires password confirmation)
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

        // If deletion is successful on the backend, remove the user from frontend memory
        set({ currentUser: null });
    }
}));

export async function requireUserLoader(forceRefresh = false) {7
    //we get the state from zustand
    const store = useUserState.getState(); 

    //If zustand memory is null we get the user data again (This happens for example when we refresh the page)
    if (forceRefresh || !store.currentUser) {
        await store.loadLoggedUser(); 
    }

    //we return the information
    return useUserState.getState().currentUser;
}