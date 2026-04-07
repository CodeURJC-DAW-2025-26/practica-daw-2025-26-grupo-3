export function Navbar() {
    return (
        <nav className="navbar navbar-expand-lg navbar-light bg-light border-bottom sticky-top shadow-sm">
            <div className="container">
                <a className="navbar-brand fw-bold text-primary d-flex align-items-center" href="/">
                    <img
                        src="/assets/Logo_Remarket.png"
                        alt="ReMarket+ Logo"
                        style={{ height: 50, width: "auto" }}
                    />
                </a>
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
                            <a className="nav-link fw-semibold" href="/product_search">
                                Productos
                            </a>
                        </li>
                        <li className="nav-item">
                            <a className="nav-link fw-semibold" href="/product-publish">
                                Publicar producto
                            </a>
                        </li>
                        <li className="nav-item">
                            <a className="nav-link fw-semibold" href="/shopping-cart" aria-label="Carrito">
                                <i className="bi bi-cart4" />
                            </a>
                        </li>
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
                                <img
                                    src="https://ui-avatars.com/api/?name=Usuario+demo&background=random"
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
                                <span className="d-none d-lg-inline">Usuario demo</span>
                            </a>
                            <ul className="dropdown-menu dropdown-menu-end">
                                <li>
                                    <a className="dropdown-item" href="/profile">
                                        Ver perfil
                                    </a>
                                </li>
                                <li>
                                    <a className="dropdown-item" href="/my_products">
                                        Mis productos
                                    </a>
                                </li>
                                <li>
                                    <a className="dropdown-item" href="/admin_panel">
                                        Panel administrador
                                    </a>
                                </li>
                            </ul>
                        </li>
                    </ul>
                </div>
            </div>
        </nav>
    );
}