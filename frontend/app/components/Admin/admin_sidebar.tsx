import React, { useState } from 'react';
// Import NavLink for active-state routing, and Link for standard routing
import { NavLink, Link } from 'react-router';
// Import React-Bootstrap components
import { Nav, Collapse } from 'react-bootstrap';
// Import our global store to access user data and logout action
import { useUserState } from '~/stores/user-store';

export default function AdminSidebar() {
    // Extract the logged-in user and the logout function from Zustand
    const { currentUser, logout } = useUserState();
    const base_image_url = "/api/v1/images";

    // State to manage the open/close behavior of the "Gestionar Productos" submenu
    const [openProducts, setOpenProducts] = useState(false);

    // Helper function to define classes for active links
    const getNavLinkClass = ({ isActive }: { isActive: boolean }) =>
        isActive ? "nav-link fw-bold text-primary bg-white rounded shadow-sm px-3 py-2 mb-1" : "nav-link text-dark px-3 py-2 mb-1";

    return (
        <div className="bg-light p-3 border-end h-100">

            {/* --- PROFILE SECTION --- */}
            <div className="admin-profile text-center mb-4 mt-2">
                {/* Conditional rendering for the user's avatar */}
                {currentUser?.imageId ? (
                    <img
                        src={`${base_image_url}/${currentUser.imageId}/media`}
                        alt="Admin"
                        className="rounded-circle mb-2 shadow-sm"
                        style={{ width: "70px", height: "70px", objectFit: "cover", border: "2px solid #0d6efd" }}
                    />
                ) : (
                    <img
                        src={`https://ui-avatars.com/api/?name=${encodeURIComponent(currentUser?.userName || 'Admin')}&background=random`}
                        alt="Admin"
                        className="rounded-circle mb-2 shadow-sm"
                        style={{ width: "70px", height: "70px", objectFit: "cover", border: "2px solid #0d6efd" }}
                    />
                )}
                <h6 className="fw-bold mb-0">{currentUser?.userName}</h6>
                <small className="text-muted">Administrador</small>
            </div>

            {/* --- NAVIGATION MENU --- */}
            <Nav className="flex-column">

                {/* Dashboard */}
                <NavLink to="/admin" end className={getNavLinkClass}>
                    <i className="bi bi-speedometer2 me-2"></i> Panel de control
                </NavLink>

                {/* Collapsible Submenu: Products */}
                <div className="nav-item">
                    <div
                        className="nav-link text-dark px-3 py-2 mb-1 d-flex justify-content-between align-items-center"
                        onClick={() => setOpenProducts(!openProducts)}
                        style={{ cursor: 'pointer' }}
                        aria-controls="productsMenu"
                        aria-expanded={openProducts}
                    >
                        <span><i className="bi bi-box-seam me-2"></i> Gestionar productos</span>
                        <i className={`bi bi-chevron-${openProducts ? 'up' : 'down'} small`}></i>
                    </div>

                    {/* React-Bootstrap Collapse component handles the animation */}
                    <Collapse in={openProducts}>
                        <div id="productsMenu" className="ps-4 pe-2 mb-2">
                            <Nav className="flex-column border-start ms-2">
                                <NavLink to="/product-publish" className={({ isActive }) => isActive ? "nav-link fw-bold text-primary py-1" : "nav-link text-secondary py-1"}>
                                    Añadir productos
                                </NavLink>
                                <NavLink to="/products_published" className={({ isActive }) => isActive ? "nav-link fw-bold text-primary py-1" : "nav-link text-secondary py-1"}>
                                    Productos publicados
                                </NavLink>
                            </Nav>
                        </div>
                    </Collapse>
                </div>

                {/* Orders Management */}
                <NavLink to="/orders_list" className={getNavLinkClass}>
                    <i className="bi bi-receipt me-2"></i> Gestionar pedidos
                </NavLink>

                {/* Users Management */}
                <NavLink to="/admin/users" className={getNavLinkClass}>
                    <i className="bi bi-people me-2"></i> Gestionar usuarios
                </NavLink>

                <hr className="text-secondary my-3 opacity-25" />

                {/* Other Links */}
                <NavLink to="/profile" className={getNavLinkClass}>
                    <i className="bi bi-person-badge me-2"></i> Mi perfil
                </NavLink>
                <Link to="/" className="nav-link text-dark px-3 py-2 mb-1">
                    <i className="bi bi-shop me-2"></i> Volver a la tienda
                </Link>

                {/* --- LOGOUT BUTTON --- */}
                <button
                    onClick={logout}
                    className="nav-link text-danger text-start bg-transparent border-0 px-3 py-2 mt-auto"
                >
                    <i className="bi bi-box-arrow-right me-2"></i> Cerrar sesión
                </button>

            </Nav>
        </div>
    );
}