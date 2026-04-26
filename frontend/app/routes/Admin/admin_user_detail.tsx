import React, { useState } from 'react';
// Import useLoaderData to read fetched data, and useRevalidator to refresh the screen
import { useLoaderData, Link, useRevalidator, type LoaderFunctionArgs } from 'react-router';
import { Card, Badge, ListGroup, Button, Spinner } from 'react-bootstrap';

// Import the real services and DTO
import { type UserDTO } from '~/dtos/UserDTO';
import { getUserById, toggleUserBlockStatus } from '~/services/admin-service';
import { requireUserLoader } from "../auth-loaders";

/*
 * 1. DATA LOADER
 * DEFENSE NOTE: ClientLoader intercepts the URL, reads the dynamic ID, 
 * and fetches the required data from the Spring Boot API before rendering the screen.
 */
export async function clientLoader({ params }: LoaderFunctionArgs) {
    const currentUser = await requireUserLoader();
    if (!currentUser || !currentUser.roles.includes("ADMIN")) return null;

    const userId = params.id;

    if (!userId) {
        throw new Error("User ID not provided in the URL");
    }

    // Actual backend call to fetch user data
    return await getUserById(userId);
}

/*
 * 2. COMPONENT (The View)
 */
export default function AdminUserDetail() {
    const base_image_url = "/api/v1/images";

    // Extract the data provided by the clientLoader
    const user = useLoaderData() as UserDTO;

    // Instantiate the revalidator to "refresh" the page data after blocking/unblocking
    const revalidator = useRevalidator();

    // Local state to prevent rapid double-clicks on the action button
    const [isUpdating, setIsUpdating] = useState(false);

    // 3. ACTION HANDLER (Toggle user state)
    const handleToggleState = async () => {
        try {
            setIsUpdating(true);

            // Call the API. If user.state is true (active), we send false (blocked), and vice versa
            await toggleUserBlockStatus(user.id, !user.state);

            // If the API responds with OK, tell React Router to re-run the clientLoader.
            // This fetches the updated user data, automatically changing the badge and button colors.
            revalidator.revalidate();

        } catch (error) {
            console.error("Error toggling user state:", error);
            alert("No se pudo actualizar el estado del usuario. Revisa la consola.");
        } finally {
            setIsUpdating(false);
        }
    };

    // 4. RENDER TSX
    return (
        <div className="py-2">
            {/* Header & Breadcrumbs */}
            <div className="d-flex justify-content-between align-items-center mb-4 border-bottom pb-3">
                <div>
                    <nav aria-label="breadcrumb">
                        <ol className="breadcrumb mb-1">
                            <li className="breadcrumb-item">
                                <Link to="/admin/users" className="text-decoration-none">
                                    Gestión de Usuarios
                                </Link>
                            </li>
                            <li className="breadcrumb-item active" aria-current="page">Detalles del Perfil</li>
                        </ol>
                    </nav>
                    <h2 className="h3 fw-bold text-dark mb-0">Expediente de Usuario</h2>
                </div>
                <Link to="/admin/users" className="btn btn-outline-secondary rounded-pill">
                    <i className="bi bi-arrow-left me-1"></i> Volver a la lista
                </Link>
            </div>

            {/* User Profile Card */}
            <div className="row justify-content-center">
                <div className="col-lg-6 col-md-8">
                    <Card className="border-0 shadow-sm p-4 mb-4">
                        <div className="text-center mb-4">

                            {/* Conditional Avatar */}
                            {user.imageId ? (
                                <img
                                    src={`${base_image_url}/${user.imageId}/media`}
                                    className="rounded-circle shadow-sm border border-4 border-white mb-3"
                                    alt="Profile Pic"
                                    style={{ width: '150px', height: '150px', objectFit: 'cover' }}
                                />
                            ) : (
                                <img
                                    src={`https://ui-avatars.com/api/?name=${encodeURIComponent(user.userName + ' ' + user.surname)}&background=random&size=150`}
                                    className="rounded-circle shadow-sm border border-4 border-white mb-3"
                                    alt="Default Pic"
                                    style={{ width: '150px', height: '150px', objectFit: 'cover' }}
                                />
                            )}

                            <h3 className="fw-bold mb-1">{user.userName} {user.surname}</h3>
                            <p className="text-muted mb-3 fs-5">{user.email}</p>

                            {/* Conditional State Badge */}
                            <div className="d-inline-block mb-3">
                                {user.state ? (
                                    <Badge bg="success" className="py-2 px-3 fs-6">
                                        <i className="bi bi-check-circle me-1"></i>Cuenta Activa
                                    </Badge>
                                ) : (
                                    <Badge bg="danger" className="py-2 px-3 fs-6">
                                        <i className="bi bi-x-circle me-1"></i>Cuenta Bloqueada
                                    </Badge>
                                )}
                            </div>

                            {/* Action Button: Block / Unblock */}
                            <div className="d-grid gap-2">
                                <Button
                                    variant={user.state ? "outline-danger" : "outline-success"}
                                    onClick={handleToggleState}
                                    disabled={isUpdating}
                                >
                                    {isUpdating ? (
                                        <Spinner as="span" animation="border" size="sm" role="status" aria-hidden="true" />
                                    ) : user.state ? (
                                        <><i className="bi bi-ban me-2"></i>Bloquear Usuario</>
                                    ) : (
                                        <><i className="bi bi-unlock me-2"></i>Desbloquear Usuario</>
                                    )}
                                </Button>
                            </div>
                        </div>

                        <hr className="text-muted opacity-25 mb-4" />

                        {/* Contact & Info List */}
                        <h5 className="fw-bold mb-3 text-center">Datos de Contacto e Información</h5>
                        <ListGroup variant="flush">
                            <ListGroup.Item className="d-flex justify-content-between align-items-center py-3 px-0">
                                <span className="text-muted fw-bold text-uppercase small">ID de Cliente</span>
                                <span className="text-dark fw-semibold">#USR-{user.id}</span>
                            </ListGroup.Item>
                            <ListGroup.Item className="d-flex justify-content-between align-items-center py-3 px-0">
                                <span className="text-muted fw-bold text-uppercase small">Dirección</span>
                                <span className="text-dark text-end">{user.address}</span>
                            </ListGroup.Item>
                            <ListGroup.Item className="d-flex justify-content-between align-items-center py-3 px-0">
                                <span className="text-muted fw-bold text-uppercase small">Rol del Sistema</span>
                                <span className="text-dark text-end">
                                    {/* DEFENSE NOTE: Explicitly typed 'role' as string to satisfy strict TypeScript rules */}
                                    {user.roles.map((role: string) => (
                                        <Badge key={role} bg="primary" className="ms-1">{role}</Badge>
                                    ))}
                                </span>
                            </ListGroup.Item>
                        </ListGroup>

                    </Card>
                </div>
            </div>
        </div>
    );
}