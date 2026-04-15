import { useEffect, useState } from "react";
import { useNavigate } from "react-router";
import { Foot } from "../../components/foot";
import { ProfileNavbar } from "../../components/profile_navbar";
import { useUserState } from "~/stores/user-store";

type OrderItem = {
    id: number;
    quantity: number;
    product: {
        productName: string;
        price: number;
    };
};

type Order = {
    orderID: number;
    date: string;
    stateText: string;
    totalPrice: number;
    orderItems: OrderItem[];
};

const mockOrders: Order[] = [
    {
        orderID: 1042,
        date: "13/04/2026 12:45",
        stateText: "Enviado",
        totalPrice: 89.99,
        orderItems: [
            {
                id: 1,
                quantity: 1,
                product: {
                    productName: "Nintendo Switch de segunda mano",
                    price: 69.99,
                },
            },
            {
                id: 2,
                quantity: 2,
                product: {
                    productName: "Mando compatible",
                    price: 10,
                },
            },
        ],
    },
    {
        orderID: 1036,
        date: "10/04/2026 09:20",
        stateText: "Entregado",
        totalPrice: 35.5,
        orderItems: [
            {
                id: 3,
                quantity: 1,
                product: {
                    productName: "Altavoz Bluetooth",
                    price: 35.5,
                },
            },
        ],
    },
];

export default function Profile() {
    const baseUrl = import.meta.env.BASE_URL;
    const baseImageUrl = "/api/v1/images";

    const { loadLoggedUser, logout, currentUser } = useUserState();
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();

    const displayName = currentUser?.userName ?? "Usuario";
    const isAdmin = currentUser?.roles.includes("ADMIN") ?? false;
    const hasOrders = mockOrders.length > 0;

    async function loadUser() {
        setLoading(true);

        try {
            await loadLoggedUser();
        } catch (error) {
            console.error(error);
        } finally {
            setLoading(false);
        }
    }

    useEffect(() => {
        loadUser();
    }, []);

    async function handleLogout() {
        setLoading(true);
        await logout();
        navigate("/");
    }

    if (loading) {
        return (
            <div className="index-loading-screen" aria-live="polite" aria-busy="true">
                <span className="index-loading-spinner" role="status" aria-label="Cargando" />
            </div>
        );
    }

    return (
        <>
            <ProfileNavbar />

            <div className="container my-5">
                <div className="row">
                    {currentUser && (
                        <div className="col-md-4 col-lg-3 mb-4">
                            <div className="card border-0 shadow-sm text-center p-4">
                                <img
                                    src={`${baseImageUrl}/${currentUser.imageId}/media`}
                                    onError={(event) => {
                                        event.currentTarget.src = `https://ui-avatars.com/api/?name=${encodeURIComponent(displayName)}&background=random`;
                                    }}
                                    className="rounded-circle mx-auto mb-3"
                                    width={80}
                                    height={80}
                                    style={{ objectFit: "cover" }}
                                    alt="Avatar del usuario"
                                />
                                <h5 className="fw-bold mb-0">{displayName}</h5>
                                <hr />
                                <div className="d-grid gap-2">
                                    <a
                                        href={`${baseUrl}my_products`}
                                        className="btn btn-sm btn-primary rounded d-flex align-items-center justify-content-center py-2"
                                    >
                                        Mis Productos
                                    </a>
                                    <a
                                        href={`${baseUrl}product-publish`}
                                        className="btn btn-sm btn-success rounded d-flex align-items-center justify-content-center py-2"
                                    >
                                        Publicar Producto
                                    </a>
                                    <button
                                        type="button"
                                        className="btn btn-sm btn-danger rounded d-flex align-items-center justify-content-center py-2 w-100"
                                        onClick={handleLogout}
                                    >
                                        Cerrar Sesion
                                    </button>
                                </div>
                            </div>
                        </div>
                    )}

                    <div className={currentUser ? "col-md-8 col-lg-9" : "col-12"}>
                        <div className="d-flex justify-content-between align-items-center mb-4 border-bottom pb-2">
                            <h2 className="h3 fw-bold text-dark">Mi Perfil</h2>
                            {currentUser && (
                                <span className={`badge rounded-pill ${isAdmin ? "bg-primary" : "bg-secondary"}`}>
                                    {isAdmin ? "Admin" : "Usuario"}
                                </span>
                            )}
                        </div>

                        {!currentUser ? (
                            <div
                                style={{
                                    backgroundColor: "#fee2e2",
                                    color: "#b91c1c",
                                    padding: "12px 16px",
                                    borderRadius: "8px",
                                    border: "1px solid #fca5a5",
                                    marginBottom: "20px",
                                    fontSize: "0.95rem",
                                }}
                            >
                                <strong>Debes iniciar sesion para acceder a esta pagina.</strong>
                            </div>
                        ) : (
                            <>
                                <div className="card border-0 shadow-sm p-4 mb-4">
                                    <div className="d-flex justify-content-between align-items-center mb-4">
                                        <h5 className="fw-bold mb-0 text-dark">Informacion Personal</h5>
                                        <a href={`${baseUrl}profile/edit`} className="btn btn-sm btn-outline-dark rounded-pill px-3">
                                            Editar
                                        </a>
                                    </div>

                                    <div className="row g-3">
                                        <div className="col-12 text-center mb-3">
                                            <img
                                                src={`${baseImageUrl}/${currentUser.imageId}/media`}
                                                onError={(event) => {
                                                    event.currentTarget.src = `https://ui-avatars.com/api/?name=${encodeURIComponent(displayName)}&background=random`;
                                                }}
                                                className="rounded-circle shadow-sm"
                                                alt="Foto de perfil"
                                                style={{ width: 150, height: 150, objectFit: "cover" }}
                                            />
                                        </div>

                                        <div className="col-md-6">
                                            <label className="form-label text-muted small fw-bold text-uppercase">Nombre Completo</label>
                                            <p className="fs-5 mb-0">
                                                {currentUser.userName} {currentUser.surname}
                                            </p>
                                        </div>

                                        <div className="col-md-6">
                                            <label className="form-label text-muted small fw-bold text-uppercase">Correo Electronico</label>
                                            <p className="fs-5 mb-0">{currentUser.email}</p>
                                        </div>

                                        <div className="col-md-6">
                                            <label className="form-label text-muted small fw-bold text-uppercase">Direccion</label>
                                            <p className="fs-5 mb-0">{currentUser.address}</p>
                                        </div>

                                        <div className="col-md-6">
                                            <label className="form-label text-muted small fw-bold text-uppercase">Rol</label>
                                            <p className="fs-5 mb-0 fw-bold">{isAdmin ? "Admin" : "Usuario"}</p>
                                        </div>
                                    </div>
                                </div>

                                <div className="card border-0 shadow-sm p-4">
                                    <div className="d-flex justify-content-between align-items-center mb-4">
                                        <h4 className="fw-bold mb-0">Mis Pedidos</h4>
                                        {hasOrders && (
                                            <a href={`${baseUrl}bill/all`} className="btn btn-primary btn-sm d-inline-flex align-items-center" id="export-all-orders-pdf">
                                                <i className="bi bi-file-earmark-pdf me-2" />
                                                Exportar todos en PDF
                                            </a>
                                        )}
                                    </div>

                                    {hasOrders ? (
                                        mockOrders.map((order) => (
                                            <table className="table align-middle" key={order.orderID}>
                                                <thead className="table-light small">
                                                    <tr>
                                                        <th className="text-center">ID del pedido</th>
                                                        <th>Fecha y hora</th>
                                                        <th>Estado</th>
                                                        <th className="text-center">Total</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <tr>
                                                        <td className="text-center">#{order.orderID}</td>
                                                        <td>{order.date}</td>
                                                        <td>{order.stateText}</td>
                                                        <td className="fw-bold text-center">{order.totalPrice.toFixed(2)} EUR</td>
                                                    </tr>
                                                    <tr>
                                                        <td colSpan={4}>
                                                            <div>
                                                                <h6 className="fw-bold">Productos del pedido:</h6>
                                                                <table className="table table-sm mb-0">
                                                                    <thead>
                                                                        <tr>
                                                                            <th>Producto</th>
                                                                            <th className="text-center">Cantidad</th>
                                                                            <th className="text-center">Precio unitario</th>
                                                                        </tr>
                                                                    </thead>
                                                                    <tbody>
                                                                        {order.orderItems.map((item) => (
                                                                            <tr key={item.id}>
                                                                                <td>{item.product.productName}</td>
                                                                                <td className="text-center">{item.quantity}</td>
                                                                                <td className="text-center">{item.product.price.toFixed(2)} EUR</td>
                                                                            </tr>
                                                                        ))}
                                                                    </tbody>
                                                                </table>
                                                            </div>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td colSpan={4} className="text-end">
                                                            <a
                                                                href={`/bill/${order.orderID}`}
                                                                target="_blank"
                                                                rel="noreferrer"
                                                                className="btn btn-primary btn-sm d-inline-flex align-items-center"
                                                            >
                                                                <i className="bi bi-file-earmark-pdf me-2" />
                                                                Exportar factura PDF
                                                            </a>
                                                        </td>
                                                    </tr>
                                                </tbody>
                                            </table>
                                        ))
                                    ) : (
                                        <div className="alert alert-info mt-3" role="alert">
                                            No tienes pedidos realizados todavia.
                                        </div>
                                    )}
                                </div>

                                <div className="card border-0 shadow-sm p-4 mt-4">
                                    <h4 className="fw-bold mb-4 text-danger">Borrar Cuenta</h4>
                                    <div className="alert alert-warning">
                                        <i className="bi bi-exclamation-triangle-fill me-2" />
                                        Advertencia: Esta accion es irreversible. Todos tus datos seran eliminados permanentemente.
                                    </div>
                                    <form>
                                        <div className="mb-3">
                                            <label htmlFor="deletePassword" className="form-label fw-bold">
                                                Introduce tu contrasena
                                            </label>
                                            <input type="password" className="form-control" id="deletePassword" name="currentPassword" required />
                                        </div>

                                        <div className="mb-3 form-check">
                                            <input type="checkbox" className="form-check-input" id="confirmDelete" name="confirmDelete" required />
                                            <label className="form-check-label" htmlFor="confirmDelete">
                                                Confirmo que quiero borrar mi cuenta permanentemente
                                            </label>
                                        </div>

                                        <div className="d-grid">
                                            <button type="button" className="btn btn-danger fw-bold">
                                                Borrar Cuenta
                                            </button>
                                        </div>
                                    </form>
                                </div>
                            </>
                        )}

                        <div className="mt-4 text-center">
                            <a href={baseUrl} className="btn btn-danger py-2 fw-bold shadow-sm" title="Volver a la tienda">
                                Volver a la tienda
                            </a>
                        </div>
                    </div>
                </div>
            </div>

            <Foot />
        </>
    );
}