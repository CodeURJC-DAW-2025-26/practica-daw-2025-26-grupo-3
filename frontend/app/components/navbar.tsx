import { Link } from "react-router";
import { useUserState } from "~/stores/user-store";
import { Navbar as BootstrapNavbar, Container, Nav, NavDropdown, Button } from "react-bootstrap";

export function Navbar() {
    const { logout, currentUser } = useUserState();
    const base_image_url = "/api/v1/images";

    return (
        <BootstrapNavbar expand="lg" className="bg-light border-bottom sticky-top shadow-sm position-relative">
            <Container>
                <div className="navbar-brand-wrap">
                    <BootstrapNavbar.Brand as={Link} className="fw-bold text-primary d-flex align-items-center" to="/">
                        <img
                            src="/assets/Logo_Remarket.png"
                            alt="ReMarket+ Logo"
                            style={{ height: 50, width: "auto" }}
                        />
                    </BootstrapNavbar.Brand>
                </div>
                <BootstrapNavbar.Toggle aria-controls="navbarNav" />
                <BootstrapNavbar.Collapse id="navbarNav">
                    <Nav className="ms-auto align-items-lg-center gap-lg-2">
                        <Nav.Link as={Link} className="fw-semibold" to="/product_search">
                            Productos
                        </Nav.Link>
                        {currentUser && (
                            <>
                                <Nav.Link as={Link} className="fw-semibold" to="/product-publish">
                                    Publicar Producto
                                </Nav.Link>
                                <Nav.Link as={Link} className="fw-semibold" to="/shopping-cart" aria-label="Carrito">
                                    <i className="bi bi-cart4" />
                                </Nav.Link>
                            </>
                        )}
                        {currentUser && (
                            <NavDropdown
                                title={
                                    <div className="d-inline-flex align-items-center gap-2" title="Menú de usuario">
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
                                    </div>
                                }
                                id="userDropdown"
                                align="end"
                                className="ms-lg-3 user-dropdown"
                            >
                                <NavDropdown.Item as={Link} to="/profile">
                                    Ver perfil
                                </NavDropdown.Item>
                                <NavDropdown.Item as={Link} to="/my_products">
                                    Mis productos
                                </NavDropdown.Item>
                                <NavDropdown.Divider />
                                <NavDropdown.Item as="button" className="dropdown-item-logout" onClick={logout}>
                                    Cerrar sesión
                                </NavDropdown.Item>
                            </NavDropdown>
                        )}
                        {!currentUser && (
                            <>
                                <Nav.Link as={Link} className="fw-semibold" to="/login">
                                    Iniciar sesión
                                </Nav.Link>
                                <Nav.Item className="ms-2">
                                    <Button as={Link as any} to="/signup" variant="primary" size="sm" className="rounded-pill px-4">
                                        Registrarse
                                    </Button>
                                </Nav.Item>
                            </>
                        )}
                    </Nav>
                </BootstrapNavbar.Collapse>
            </Container>
        </BootstrapNavbar>
    );
}