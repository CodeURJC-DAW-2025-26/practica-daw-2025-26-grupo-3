import { useUserState } from "~/stores/user-store";

export async function requireUserLoader() {
    const store = useUserState.getState();      //Store access without hooks

    if (!store.currentUser) {
        await store.loadLoggedUser();           //Load currentUser if it's not done
    }

    return useUserState.getState().currentUser; //Return current user
}