import { useEffect, useState } from "react";
import { Link } from "react-router";
import { Item, type ItemProps } from "~/components/ShoppingCart/Item";
import { Spinner } from "~/components/spinner";
import type { CartDTO } from "~/dtos/CartDTo";
import type { CartItemDTO } from "~/dtos/CartItemDTO";
import { getCartInfo } from "~/services/cart-service";
import { useUserState } from "~/stores/user-store";
import type { Route } from "../+types";
import { requireUserLoader } from "../auth-loaders";

export async function clientLoader() {
    return await requireUserLoader();
}

export default function ShoppingCart({ loaderData }: Route.ComponentProps) {

    const [loading, setLoading] = useState(true);
    const [cart, setCart] = useState<CartDTO | null>(null);
    const [cartItems, setCartItems] = useState<CartItemDTO[] | null>([]);
    const [productNum, setProductNum] = useState<number>(0);
    const [total, setTotal] = useState<number>(0);
    let [error, setError] = useState<string | null>(null);
    const { currentUser } = useUserState();

    async function loadCart() {
        setLoading(true);
        try {
            const cartData = await getCartInfo();
            setCart(cartData);
            setCartItems(cartData.cartItems);
            setProductNum(1);
            setTotal(1);
        }
        catch (err) {
            error = err instanceof Error ?
                err.message.split("'")[1]
                : "Error cargando los productos del carrito. Inténtalo de nuevo más tarde.";
            setError(error);
        }
        finally {
            setLoading(false);
        }
    }

    useEffect(() => { loadCart() }, []);

    if (loading) {
        return (<Spinner />)
    }

    return (
        <section className="py-5 bg-light min-vh-100">
            <div className="container">
                <div className="d-flex flex-wrap justify-content-between align-items-center mb-4">
                    <div>
                        <h1 className="h3 fw-bold text-dark mb-1">Mi carrito</h1>
                        <p className="text-muted mb-0">Resumen de productos del pedido actual.</p>
                    </div>
                    <Link to="/product_search" className="btn btn-primary">
                        <i className="bi bi-arrow-left"></i> Seguir comprando
                    </Link>
                </div>

                <div className="row g-4">
                    <div className="col-lg-8">
                        <div className="card shadow-sm border-0">
                            <div className="card-body p-0">
                                <div className="table-responsive">
                                    <table className="table align-middle mb-0">
                                        <thead className="bg-light">
                                            <tr>
                                                <th className="ps-4 py-3">Producto</th>
                                                <th className="py-3">Estado</th>
                                                <th className="py-3">Precio</th>
                                                <th className="py-3">Cantidad</th>
                                                <th className="py-3 text-end pe-4">Acciones</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            {cartItems!.length > 0 ? (
                                                cartItems!.map((item) => (
                                                    <Item quantity={0} key={item.id} id={0} productId={item.productId} productName={item.productName} />
                                                ))
                                            ) : (
                                                <tr>
                                                    <td colSpan={5} className="text-center py-4">
                                                        No tienes productos en el carrito
                                                    </td>
                                                </tr>
                                            )}
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div className="col-lg-4">
                        <div className="card shadow-sm border-0">
                            <div className="card-body">
                                <h2 className="h5 fw-bold mb-3">Resumen del pedido</h2>
                                <div className="d-flex justify-content-between mb-2">
                                    <span className="text-muted">Cantidad de productos del pedido:</span>
                                    <span>{productNum}</span>
                                </div>
                                <hr />
                                <div className="d-flex justify-content-between mb-4">
                                    <span className="fw-bold">Total a pagar: </span>
                                    <span className="fw-bold">{total} €</span>
                                </div>
                                <a className="btn btn-primary w-100 mb-2" href="/cart/save_order">
                                    Finalizar compra
                                </a>
                            </div>
                        </div>

                        <div className="card shadow-sm border-0 mt-4">
                            <div className="card-body">
                                <h3 className="h6 fw-bold mb-3">Dirección de entrega</h3>
                                <p className="text-muted mb-2">{currentUser!.address}</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    );
}