import { useEffect, useState } from "react";
import { Link } from "react-router";
import { useUserState } from "~/stores/user-store";

export function Navbar() {
    const baseUrl = import.meta.env.BASE_URL;
    const base_image_url = "/api/v1/images";

    const { currentUser, loadLoggedUser, logout } = useUserState();

    const [loading, setLoading] = useState(false);

    async function loadUser() {
        setLoading(true);

        try {
            await loadLoggedUser();
        }
        catch (err) {
            console.error(err);
        }
        finally {
            setLoading(false);
        }
    }

    useEffect(() => { loadUser() }, []);

    return (
        <nav className="navbar navbar-expand-lg navbar-light bg-light border-bottom sticky-top shadow-sm">
            <div className="container">
                {loading && <p>Cargando...</p>}
                <Link className="navbar-brand fw-bold text-primary d-flex align-items-center" to="/">


                    <img
                        src={`${baseUrl}assets/Logo_Remarket.png`}
                        alt="ReMarket+ Logo"
                        style={{ height: 50, width: "auto" }}
                    />

                </Link>
                <button
                    className="navbar-toggler"
                    type="button"
                    data-bs-toggle="collapse"
                    data-bs-target="#navbarNav"
                    aria-controls="navbarNav"
                    aria-expanded="false"
                    aria-label="Toggle navigation"
                >
                    <span className="navbar-toggler-icon" />
                </button>
                <div className="collapse navbar-collapse" id="navbarNav">
                    <ul className="navbar-nav ms-auto align-items-lg-center gap-lg-2">
                        <li className="nav-item">
                            <Link className="nav-link fw-semibold" to="/product_search">
                                Productos
                            </Link>
                        </li>
                        {currentUser && (
                            <>
                                <li className="nav-item">
                                    <Link className="nav-link fw-semibold" to="/product-publish">
                                        Publicar Producto
                                    </Link>
                                </li>
                                <li className="nav-item">
                                    <Link className="nav-link fw-semibold" to="/shopping-cart" aria-label="Carrito">
                                        <i className="bi bi-cart4" />
                                    </Link>
                                </li>
                            </>
                        )}
                        {currentUser ? (
                            <li className="nav-item dropdown ms-lg-3">
                                <a
                                    className="nav-link dropdown-toggle user-dropdown d-flex align-items-center gap-2"
                                    href="#"
                                    id="userDropdown"
                                    role="button"
                                    data-bs-toggle="dropdown"
                                    aria-expanded="false"
                                    title="Menú de usuario"
                                >
                                    {currentUser.imageId ? (
                                        <img
                                            src={`${base_image_url}/${currentUser.imageId}/media`}
                                            alt="Mi perfil"
                                            style={{
                                                height: 40,
                                                width: 40,
                                                borderRadius: "50%",
                                                border: "2px solid #0d6efd",
                                                cursor: "pointer",
                                                objectFit: "cover",
                                                flexShrink: 0,
                                            }}
                                        />
                                    ) : (
                                        <img
                                            src={`https://ui-avatars.com/api/?name=${encodeURIComponent(currentUser.userName)}&background=random`}
                                            alt="Mi perfil"
                                            style={{
                                                height: 40,
                                                width: 40,
                                                borderRadius: "50%",
                                                border: "2px solid #0d6efd",
                                                cursor: "pointer",
                                                objectFit: "cover",
                                                flexShrink: 0,
                                            }}
                                        />
                                    )}
                                    <span className="d-none d-lg-inline">{currentUser.userName}</span>
                                </a>
                                <ul className="dropdown-menu dropdown-menu-end">
                                    <li>
                                        <Link className="dropdown-item" to="/profile">
                                            Ver perfil
                                        </Link>
                                    </li>
                                    <li>
                                        <Link className="dropdown-item" to="/my_products">
                                            Mis productos
                                        </Link>
                                    </li>
                                    <li>
                                        <button className="dropdown-item" type="button" onClick={logout}>
                                            Cerrar sesión
                                        </button>
                                    </li>
                                </ul>
                            </li>
                        ) : (
                            <>
                                <li className="nav-item">
                                    <Link className="nav-link fw-semibold" to="/login">
                                        Iniciar sesión
                                    </Link>
                                </li>
                                <li className="nav-item ms-2">
                                    <Link className="btn btn-primary btn-sm rounded-pill px-4" to="/signup">
                                        Registrarse
                                    </Link>
                                </li>
                            </>
                        )}
                    </ul>
                </div>
            </div>
        </nav>
    );
}