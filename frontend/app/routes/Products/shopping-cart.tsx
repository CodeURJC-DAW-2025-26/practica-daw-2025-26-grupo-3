import { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router";
import { Item, type ItemProps } from "~/components/ShoppingCart/Item";
import { Spinner } from "~/components/spinner";
import { useUserState } from "~/stores/user-store";
import type { Route } from "../+types";
import { requireUserLoader } from "../auth-loaders";
import { useCartState } from "~/stores/shoppingCart-store";
import { AuthError } from "~/components/auth-error";

export async function clientLoader() {
    return await requireUserLoader();
}

export default function ShoppingCart({ loaderData }: Route.ComponentProps) {

    const [loading, setLoading] = useState(true); //Route component loading
    const [convertLoading, setConvertLoading] = useState(false);
    let [error, setError] = useState<string | null>(null);
    const { currentUser } = useUserState();
    const { items, totalPrice, totalQuantity, getCart, convertToOrder } = useCartState();
    const navigate = useNavigate();
    const hasItems = (items?.length ?? 0) > 0;

    async function loadCart() {
        setLoading(true);
        try {
            await getCart();
        }
        catch (err) {
            error = "Error cargando los productos del carrito. Inténtalo de nuevo más tarde.";
            setError(error);
        }
        finally {
            setLoading(false);
        }
    }

    async function handleCartToOrder() {
        setConvertLoading(true);
        try {
            await convertToOrder();
        }
        catch (err) {
            error = "Error al realizar la compra. Inténtalo de nuevo más tarde."
            setError(error);
        }
        finally {
            setConvertLoading(false);
        }

        if (!error) {
            navigate("/profile");
        }
    }

    useEffect(() => { loadCart() }, []);

    if (loading) {
        return (<Spinner />)
    }

    if (!currentUser) {
        return (<AuthError />)
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
                                            {error !== null ? (
                                                <tr>
                                                    <td colSpan={5} className="p-4">
                                                        <div
                                                            style={{
                                                                backgroundColor: "#fee2e2",
                                                                color: "#b91c1c",
                                                                padding: "12px 16px",
                                                                borderRadius: "8px",
                                                                border: "1px solid #fca5a5",
                                                                fontSize: "0.95rem",
                                                            }}
                                                        >
                                                            <strong>{error}</strong>
                                                        </div>
                                                    </td>
                                                </tr>
                                            ) : hasItems ? (
                                                items!.map((item) => (
                                                    <Item quantity={item.quantity} key={item.id} id={item.id} productId={item.productId} productName={item.productName} />
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
                                    <span>{totalQuantity}</span>
                                </div>
                                <hr />
                                <div className="d-flex justify-content-between mb-4">
                                    <span className="fw-bold">Total a pagar: </span>
                                    <span className="fw-bold">{totalPrice} €</span>
                                </div>
                                <button
                                    type="button"
                                    className={`btn btn-primary w-100 mb-2 d-flex align-items-center justify-content-center signup-submit-btn ${convertLoading ? "signup-submit-btn-loading" : ""}`}
                                    onClick={handleCartToOrder}
                                    disabled={convertLoading}
                                >
                                    {convertLoading ? (
                                        <span className="signup-loading-spinner" role="status" aria-label="Finalizando compra" />
                                    ) : (
                                        <span>Finalizar compra</span>
                                    )}
                                </button>
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