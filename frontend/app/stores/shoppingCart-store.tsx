import type { CartDTO } from "~/dtos/CartDTO";
import { create } from "zustand";
import { addToCart, cartToOrder, deleteCartItem, getCartInfo, getOrdersInfo, updateQuantity } from "~/services/cart-service";
import type { CartItemDTO } from "~/dtos/CartItemDTO";
import type { OrderDTO } from "~/dtos/OrderDTO";
import { HttpError } from "~/services/HttpError";

export interface CartState {
    totalPrice: number;
    totalQuantity: number;
    cart: CartDTO | null;
    items: CartItemDTO[] | null;
    itemPrices: Record<number, number>;
    itemQuantities: Record<number, number>;
    orders: OrderDTO[] | null;
    getCart: () => void;
    setLineItem: (itemId: number, price: number, quantity: number) => void;
    deleteItem: (id: number) => void;
    changeItemQty: (id: number, op: number) => void;
    convertToOrder: () => void,
    getOrders: () => void,
    addItem: (id: number) => void;
}

export const useCartState = create<CartState>((set, get) => ({
    totalPrice: 0,
    totalQuantity: 0,
    cart: null,
    items: [],
    itemPrices: {},
    itemQuantities: {},
    orders: [],
    getCart: async () => {
        try {
            const userCart = await getCartInfo();
            set({ cart: userCart, items: userCart.cartItems });
        }
        catch (err) {
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
    setLineItem: (itemId: number, price: number, quantity: number) => {
        const newPrices = { ...get().itemPrices, [itemId]: price };
        const newQuantities = { ...get().itemQuantities, [itemId]: quantity };

        const newTotal = Object.entries(newPrices).reduce(
            (sum, [id, p]) => sum + p * (newQuantities[Number(id)] ?? 0),
            0
        );
        const newTotalQuantity = Object.values(newQuantities).reduce((sum, q) => sum + q, 0);

        set({
            itemPrices: newPrices,
            itemQuantities: newQuantities,
            totalPrice: newTotal,
            totalQuantity: newTotalQuantity
        });
    },
    deleteItem: async (id: number) => {
        try {
            await deleteCartItem(id);

            const newPrices = { ...get().itemPrices };
            const newQuantities = { ...get().itemQuantities };

            delete newPrices[id];
            delete newQuantities[id];

            const newTotalPrice = Object.entries(newPrices).reduce(
                (sum, [id, p]) => sum + p * (newQuantities[Number(id)] ?? 0),
                0
            );
            const newTotalQuantity = Object.values(newQuantities).reduce((sum, q) => sum + q, 0);

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
    changeItemQty: async (id: number, op: number) => {
        try {
            await updateQuantity(id, op);

            const currentQty = get().itemQuantities[id] ?? 1;
            const nextQty = op === 1 ? currentQty + 1 : Math.max(1, currentQty - 1);
            const newQuantities = { ...get().itemQuantities, [id]: nextQty };
            const newTotalPrice = Object.entries(get().itemPrices).reduce(
                (sum, [id, p]) => sum + p * (newQuantities[Number(id)] ?? 0),
                0
            );
            const newTotalQuantity = Object.values(newQuantities).reduce((sum, q) => sum + q, 0);

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
    convertToOrder: async () => {
        try {
            await cartToOrder();
            set({ cart: null });
        }
        catch (err) {
            throw err;
        }
    },
    getOrders: async () => {
        try {
            const userOrders = await getOrdersInfo();
            set({ orders: userOrders });
        }
        catch (err) {
            throw err;
        }
    },
    addItem: async (id: number) => {
        try {
            await addToCart(id);
            await get().getCart();  //Update cart state in frontend
        }
        catch (err) {
            throw err;
        }
    }
}));