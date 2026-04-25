// Import Data Transfer Objects (DTOs) defining the shape of the data from the backend
import type { CartDTO } from "~/dtos/CartDTO";
import type { CartItemDTO } from "~/dtos/CartItemDTO";
import type { OrderDTO } from "~/dtos/OrderDTO";

// Import the 'create' function from Zustand to build our global state hook
import { create } from "zustand";

// Import API service functions to communicate with the backend
import { addToCart, cartToOrder, deleteCartItem, getCartInfo, getOrdersInfo, updateQuantity } from "~/services/cart-service";
import { HttpError } from "~/services/HttpError";

// 1. STATE INTERFACE DEFINITION
// Defines the structure of the data and the methods (actions) available in this store
export interface CartState {
    totalPrice: number;
    totalQuantity: number;
    cart: CartDTO | null; // The main cart object
    items: CartItemDTO[] | null; // Array of items currently in the cart
    itemPrices: Record<number, number>; // Dictionary mapping item ID to its price for fast lookups
    itemQuantities: Record<number, number>; // Dictionary mapping item ID to its quantity
    orders: OrderDTO[] | null; // User's order history

    // Actions
    getCart: () => void;
    setLineItem: (itemId: number, price: number, quantity: number) => void;
    deleteItem: (id: number) => void;
    changeItemQty: (id: number, op: number) => void;
    convertToOrder: () => void;
    getOrders: () => void;
    addItem: (id: number) => void;
}

// 2. ZUSTAND STORE CREATION
// 'set' is used to update the state, 'get' is used to read the current state
export const useCartState = create<CartState>((set, get) => ({
    // Initial default state values
    totalPrice: 0,
    totalQuantity: 0,
    cart: null,
    items: [],
    itemPrices: {},
    itemQuantities: {},
    orders: [],

    // Fetch the current user's cart from the backend
    getCart: async () => {
        try {
            const userCart = await getCartInfo();
            set({ cart: userCart, items: userCart.cartItems });
        }
        catch (err) {
            // DEFENSE NOTE: We specifically catch the 404 Not Found error here.
            // A 404 means the user simply doesn't have an active cart yet. 
            // Instead of crashing, we gracefully reset the local cart state to empty.
            if (err instanceof HttpError && err.status === 404) {
                set({
                    cart: null,
                    items: [],
                    itemPrices: {},
                    itemQuantities: {},
                    totalPrice: 0,
                    totalQuantity: 0,
                });
                return;
            }
            throw err;
        }
    },

    // Update local state without making an API call (used for UI recalculations)
    setLineItem: (itemId: number, price: number, quantity: number) => {
        // DEFENSE NOTE: We use the spread operator (...) to maintain state immutability.
        // Zustand requires returning a new object rather than mutating the existing one.
        const newPrices = { ...get().itemPrices, [itemId]: price };
        const newQuantities = { ...get().itemQuantities, [itemId]: quantity };

        // Recalculate total price using 'reduce' to sum up (price * quantity) for all items
        const newTotal = Object.entries(newPrices).reduce(
            (sum, [id, p]) => sum + p * (newQuantities[Number(id)] ?? 0),
            0
        );
        // Recalculate total quantity
        const newTotalQuantity = Object.values(newQuantities).reduce((sum, q) => sum + q, 0);

        set({
            itemPrices: newPrices,
            itemQuantities: newQuantities,
            totalPrice: newTotal,
            totalQuantity: newTotalQuantity
        });
    },

    // Remove an item from the cart
    deleteItem: async (id: number) => {
        try {
            // Step 1: Send request to the backend to delete the item
            await deleteCartItem(id);

            // Step 2: If successful, remove it from the local frontend state
            const newPrices = { ...get().itemPrices };
            const newQuantities = { ...get().itemQuantities };

            delete newPrices[id];
            delete newQuantities[id];

            // Recalculate totals after deletion
            const newTotalPrice = Object.entries(newPrices).reduce(
                (sum, [id, p]) => sum + p * (newQuantities[Number(id)] ?? 0),
                0
            );
            const newTotalQuantity = Object.values(newQuantities).reduce((sum, q) => sum + q, 0);

            // Update state. Use filter() to keep only the items that don't match the deleted ID.
            set({
                items: get().items?.filter(item => item.id !== id),
                itemPrices: newPrices,
                itemQuantities: newQuantities,
                totalPrice: newTotalPrice,
                totalQuantity: newTotalQuantity
            });
        }
        catch (err) {
            throw err;
        }
    },

    // Increase or decrease the quantity of a specific item
    changeItemQty: async (id: number, op: number) => {
        try {
            // Step 1: Tell backend to update quantity (op is usually 1 or -1)
            await updateQuantity(id, op);

            // Step 2: Update local state
            const currentQty = get().itemQuantities[id] ?? 1;

            // DEFENSE NOTE: Math.max(1, ...) ensures the quantity never drops below 1.
            const nextQty = op === 1 ? currentQty + 1 : Math.max(1, currentQty - 1);

            const newQuantities = { ...get().itemQuantities, [id]: nextQty };

            // Recalculate totals
            const newTotalPrice = Object.entries(get().itemPrices).reduce(
                (sum, [id, p]) => sum + p * (newQuantities[Number(id)] ?? 0),
                0
            );
            const newTotalQuantity = Object.values(newQuantities).reduce((sum, q) => sum + q, 0);

            // Use map() to find the specific item and update its quantity attribute
            set({
                items: get().items?.map((item) =>
                    item.id === id ? { ...item, quantity: nextQty } : item
                ) ?? [],
                itemQuantities: newQuantities,
                totalPrice: newTotalPrice,
                totalQuantity: newTotalQuantity
            });
        }
        catch (err) {
            throw err;
        }
    },

    // Checkout process: convert the active cart into a confirmed order
    convertToOrder: async () => {
        try {
            await cartToOrder();
            // Clear the local cart state since it has been successfully processed
            set({ cart: null });
        }
        catch (err) {
            throw err;
        }
    },

    // Fetch the user's past orders
    getOrders: async () => {
        try {
            const userOrders = await getOrdersInfo();
            set({ orders: userOrders });
        }
        catch (err) {
            throw err;
        }
    },

    // Add a new item to the cart
    addItem: async (id: number) => {
        try {
            // Step 1: Tell backend to add the item
            await addToCart(id);
            // Step 2: Re-fetch the entire cart to ensure frontend and backend are perfectly synced
            await get().getCart();
        }
        catch (err) {
            throw err;
        }
    }
}));