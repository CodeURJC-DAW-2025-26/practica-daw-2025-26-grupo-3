import { Link } from "react-router";

export default function ShoppingCart() {
    // Mock data for the shopping cart
    const token = "mock-csrf-token-123";
    const cart = {
        cartItems: [
            {
                id: 1,
                product: {
                    id: 101,
                    productName: "MacBook Pro 2021",
                    price: 1200,
                    seller: { userName: "JuanPerez" }
                },
                state: "Segunda mano",
                quantity: 1
            },
            {
                id: 2,
                product: {
                    id: 105,
                    productName: "Monitor Dell 27\"",
                    price: 250,
                    seller: { userName: "TechStore" }
                },
                state: "Como nuevo",
                quantity: 2
            }
        ]
    };

    const productNum = cart.cartItems.reduce((acc, item) => acc + item.quantity, 0);
    const total = cart.cartItems.reduce((acc, item) => acc + (item.product.price * item.quantity), 0);
    const address = "Calle Falsa 123, Madrid";

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
                                            {cart.cartItems && cart.cartItems.length > 0 ? (
                                                cart.cartItems.map((item) => (
                                                    <tr key={item.id}>
                                                        <td className="ps-4">
                                                            <div className="d-flex align-items-center">
                                                                <img
                                                                    src={`/product-images/${item.product.id}`}
                                                                    alt={item.product.productName}
                                                                    className="rounded-3 me-3"
                                                                    style={{ width: "72px", height: "72px", objectFit: "cover" }}
                                                                />
                                                                <div>
                                                                    <div className="fw-bold">{item.product.productName}</div>
                                                                    <div className="small text-muted">
                                                                        Vendedor: {item.product.seller.userName}
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </td>
                                                        <td>
                                                            <p className="mb-0" style={{ fontSize: "0.75rem", color: "#333" }}>
                                                                {item.state}
                                                            </p>
                                                        </td>
                                                        <td>{item.product.price} €</td>
                                                        <td>
                                                            <div className="d-flex align-items-center" style={{ maxWidth: "120px" }}>
                                                                <form action={`/cart/modify_quantity/0/${item.id}`} method="post" style={{ display: "contents" }}>
                                                                    <input type="hidden" name="_csrf" value={token} />
                                                                    <button
                                                                        className="btn btn-outline-secondary btn-sm"
                                                                        type="submit"
                                                                        style={{ borderTopRightRadius: 0, borderBottomRightRadius: 0 }}
                                                                        disabled={item.quantity <= 1}
                                                                    >
                                                                        -
                                                                    </button>
                                                                </form>

                                                                <input
                                                                    type="text"
                                                                    className="form-control text-center border-secondary border-start-0 border-end-0 rounded-0 p-1"
                                                                    value={item.quantity}
                                                                    aria-label="Cantidad"
                                                                    readOnly
                                                                    style={{ width: "40px", height: "31px" }}
                                                                />

                                                                <form action={`/cart/modify_quantity/1/${item.id}`} method="post" style={{ display: "contents" }}>
                                                                    <input type="hidden" name="_csrf" value={token} />
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
                                                            <form action={`/cart/delete/${item.id}`} method="post" style={{ display: "contents" }}>
                                                                <input type="hidden" name="_csrf" value={token} />
                                                                <button className="btn btn-sm btn-outline-danger" title="Quitar">
                                                                    <i className="bi bi-trash"></i>
                                                                </button>
                                                            </form>
                                                        </td>
                                                    </tr>
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
                                <p className="text-muted mb-2">{address}</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    );
}