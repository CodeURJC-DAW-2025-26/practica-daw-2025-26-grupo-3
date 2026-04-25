// React Router hook used for SPA navigation without reloading the page
import { Link } from "react-router";

// Import our Zustand global state to access user session data
import { useUserState } from "~/stores/user-store";

// Import React-Bootstrap components to build the UI natively in React
import { Navbar as BootstrapNavbar, Container, Nav, NavDropdown, Button } from "react-bootstrap";

export function Navbar() {
    // We destructure 'logout' function and 'currentUser' object from our global store.
    const { logout, currentUser } = useUserState();

    // Base URL for fetching images from the Spring Boot REST API
    const base_image_url = "/api/v1/images";

    // We check if the logged-in user has administrative privileges.
    const isAdmin = currentUser?.roles.includes("ADMIN") || currentUser?.roles.includes("ROLE_ADMIN");

    return (
        // The main Navbar wrapper. 'sticky-top' keeps it visible when scrolling.
        <BootstrapNavbar expand="lg" className="bg-light border-bottom sticky-top shadow-sm position-relative">
            <Container>
                {/* --- BRAND / LOGO --- */}
                <div className="navbar-brand-wrap">
                    <BootstrapNavbar.Brand as={Link} className="fw-bold text-primary d-flex align-items-center" to="/">
                        <img
                            src="/assets/Logo_Remarket.png"
                            alt="ReMarket+ Logo"
                            style={{ height: 50, width: "auto" }}
                        />
                    </BootstrapNavbar.Brand>
                </div>

                {/* Hamburger menu button for mobile devices */}
                <BootstrapNavbar.Toggle aria-controls="navbarNav" />

                <BootstrapNavbar.Collapse id="navbarNav">
                    <Nav className="ms-auto align-items-lg-center gap-lg-2">

                        {/* Always visible link */}
                        <Nav.Link as={Link} className="fw-semibold" to="/product_search">
                            Productos
                        </Nav.Link>

                        {/* --- CONDITIONAL RENDERING: LOGGED IN USERS ONLY --- */}
                        {/* If currentUser exists, render these links */}
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

                        {/* --- CONDITIONAL RENDERING: USER PROFILE DROPDOWN --- */}
                        {currentUser && (
                            <NavDropdown
                                title={
                                    <div className="d-inline-flex align-items-center gap-2" title="Menú de usuario">
                                        {/* TERNARY OPERATOR: Check if user has uploaded a profile picture */}
                                        {currentUser.imageId ? (
                                            // If true: Load the image from our API
                                            <img
                                                src={`${base_image_url}/${currentUser.imageId}/media`}
                                                alt="Mi perfil"
                                                style={{
                                                    height: 40, width: 40, borderRadius: "50%",
                                                    border: "2px solid #0d6efd", cursor: "pointer",
                                                    objectFit: "cover", flexShrink: 0,
                                                }}
                                            />
                                        ) : (
                                            // If false: Fallback to a generated avatar with their initials
                                            <img
                                                src={`https://ui-avatars.com/api/?name=${encodeURIComponent(currentUser.userName)}&background=random`}
                                                alt="Mi perfil"
                                                style={{
                                                    height: 40, width: 40, borderRadius: "50%",
                                                    border: "2px solid #0d6efd", cursor: "pointer",
                                                    objectFit: "cover", flexShrink: 0,
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
                                {/* --- CONDITIONAL RENDERING: ADMIN ROLE ONLY --- */}
                                {/* The Admin Panel link is injected here ONLY if the user is an Administrator */}
                                {isAdmin && (
                                    <>
                                        <NavDropdown.Item as={Link} to="/admin" className="fw-bold text-danger">
                                            Admin Panel
                                        </NavDropdown.Item>
                                        <NavDropdown.Divider />
                                    </>
                                )}

                                {/* Regular user dropdown options */}
                                <NavDropdown.Item as={Link} to="/profile">
                                    Ver perfil
                                </NavDropdown.Item>
                                <NavDropdown.Item as={Link} to="/my_products">
                                    Mis productos
                                </NavDropdown.Item>
                                <NavDropdown.Divider />

                                {/* Execute the logout function from Zustand when clicked */}
                                <NavDropdown.Item as="button" className="dropdown-item-logout" onClick={logout}>
                                    Cerrar sesión
                                </NavDropdown.Item>
                            </NavDropdown>
                        )}

                        {/* --- CONDITIONAL RENDERING: GUESTS (NOT LOGGED IN) --- */}
                        {/* The '!' operator checks if currentUser is null */}
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