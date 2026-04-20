import { useEffect, useState } from "react";
import type { ProductDetailDTO } from "~/dtos/ProductDetailDTO";
import { getBasicProduct } from "~/services/product-service";

export interface ItemProps {
    id: number;
    productId: number;
    productName: string;
    quantity: number;
}

export function Item({ id, productId, productName, quantity }: ItemProps) {

    const [product, setProduct] = useState<ProductDetailDTO | null>(null);
    const [error, setError] = useState<string | null>(null);
    const imageId = product?.images?.[0]?.id;

    async function loadProduct() {
        let errMessage = null;
        try {
            const productData = await getBasicProduct(productId);
            setProduct(productData);
        }
        catch (err) {
            errMessage = err instanceof Error
                ? errMessage!.message
                : "Se ha producido un error al cargar este producto."
            setError(errMessage);
        }
    }

    useEffect(() => { loadProduct() }, [])

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
                {product?.state}
            </p>
        </td>
        <td>{product?.price} €</td>
        <td>
            <div className="d-flex align-items-center" style={{ maxWidth: "120px" }}>
                <form action={`/cart/modify_quantity/0/${id}`} method="post" style={{ display: "contents" }}>
                    <input type="hidden" name="_csrf" value="" />
                    <button
                        className="btn btn-outline-secondary btn-sm"
                        type="submit"
                        style={{ borderTopRightRadius: 0, borderBottomRightRadius: 0 }}
                        disabled={quantity <= 1}
                    >
                        -
                    </button>
                </form>

                <input
                    type="text"
                    className="form-control text-center border-secondary border-start-0 border-end-0 rounded-0 p-1"
                    value={quantity}
                    aria-label="Cantidad"
                    readOnly
                    style={{ width: "40px", height: "31px" }}
                />

                <form action={`/cart/modify_quantity/1/${id}`} method="post" style={{ display: "contents" }}>
                    <input type="hidden" name="_csrf" value="" />
                    <button
                        className="btn btn-outline-secondary btn-sm"
                        type="submit"
                        style={{ borderTopLeftRadius: 0, borderBottomLeftRadius: 0 }}
                    >
                        +
                    </button>
                </form>
            </div>
        </td>
        <td className="text-end pe-4">
            <form action={`/cart/delete/${id}`} method="post" style={{ display: "contents" }}>
                <input type="hidden" name="_csrf" value="" />
                <button className="btn btn-sm btn-outline-danger" title="Quitar">
                    <i className="bi bi-trash"></i>
                </button>
            </form>
        </td>
    </tr>
}