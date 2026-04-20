import { useEffect, useState } from "react";
import type { ProductDetailDTO } from "~/dtos/ProductDetailDTO";
import { getBasicProduct } from "~/services/product-service";
import { useCartState } from "~/stores/shoppingCart-store";

export interface ItemProps {
    id: number;
    productId: number;
    productName: string;
    quantity: number;
}

export function Item({ id, productId, productName, quantity }: ItemProps) {

    const [product, setProduct] = useState<ProductDetailDTO | null>(null);
    const [error, setError] = useState<string | null>(null);
    const [state, setState] = useState<string | null>(null);    //Hook for product state

    const { setLineItem, deleteItem, changeItemQty } = useCartState();

    //Hook for number in +/- buttons. It's not the real value in database. We use it for changing number in real time
    const [itemQuantity, setItemQuantity] = useState<number>(quantity)
    const imageId = product?.images?.[0]?.id;

    async function loadProduct() {
        let errMessage = null;
        try {
            const productData = await getBasicProduct(productId);
            setProduct(productData);
            switch (productData.state) {
                case 0: setState("Nuevo"); break;
                case 1: setState("Reacondicionado"); break;
                case 2: setState("Segunda mano"); break
            }
            setLineItem(productId, productData.price, quantity)
        }
        catch (err) {
            errMessage = err instanceof Error
                ? err.message
                : "Se ha producido un error al cargar este producto."
            setError(errMessage);
        }
    }

    async function handleDeleteItem(id: number) {
        let deleteErrMessage: string | null = null;
        try {
            await deleteItem(id);
        }
        catch (err) {
            deleteErrMessage = err instanceof Error
                ? err.message
                : "Se ha producido un error al borrar el producto del carrito";
        }
    }

    async function handleChangeQuantity(id: number, op: number, productId: number) {
        let qtyErrMessage: string | null = null;
        const previousQuantity = itemQuantity;
        const nextQuantity = op === 1 ? previousQuantity + 1 : Math.max(1, previousQuantity - 1);

        if (nextQuantity === previousQuantity) {
            return;
        }

        try {
            setItemQuantity(nextQuantity);
            await changeItemQty(id, op, productId);
        } catch (err) {
            setItemQuantity(previousQuantity);
            qtyErrMessage = err instanceof Error
                ? err.message
                : "Se ha producido un error al borrar el producto del carrito";
            console.error(err);
        }
    }

    useEffect(() => { loadProduct() }, []);

    return <tr key={id}>
        <td className="ps-4">
            <div className="d-flex align-items-center">
                <img
                    src={
                        imageId
                            ? `/api/v1/images/${imageId}/media`
                            : `https://placehold.co/300x250/667eea/ffffff?text=${encodeURIComponent(product?.productName ?? productName)}`
                    }
                    alt={productName}
                    className="rounded-3 me-3"
                    style={{ width: "72px", height: "72px", objectFit: "cover" }}
                />
                <div>
                    <div className="fw-bold">{productName}</div>
                </div>
            </div>
        </td>
        <td>
            <p className="mb-0" style={{ fontSize: "0.75rem", color: "#333" }}>
                {state}
            </p>
        </td>
        <td>{product?.price} €</td>
        <td>
            <div className="d-flex align-items-center" style={{ maxWidth: "120px" }}>
                <button
                    className="btn btn-outline-secondary btn-sm"
                    onClick={() => { handleChangeQuantity(id, 0, productId) }}
                    style={{ borderTopRightRadius: 0, borderBottomRightRadius: 0 }}
                    disabled={itemQuantity <= 1}
                >
                    -
                </button>

                <input
                    type="text"
                    className="form-control text-center border-secondary border-start-0 border-end-0 rounded-0 p-1"
                    value={itemQuantity}
                    aria-label="Cantidad"
                    readOnly
                    style={{ width: "40px", height: "31px" }}
                />

                <button
                    className="btn btn-outline-secondary btn-sm"
                    onClick={() => { handleChangeQuantity(id, 1, productId) }}
                    style={{ borderTopLeftRadius: 0, borderBottomLeftRadius: 0 }}
                >
                    +
                </button>
            </div>
        </td>
        <td className="text-end pe-4">
            <button className="btn btn-sm btn-outline-danger" title="Quitar" onClick={() => handleDeleteItem(id)}>
                <i className="bi bi-trash"></i>
            </button>
        </td>
    </tr>
}