import React, { useState } from 'react';
import { Badge, Button, Card, Table, Spinner } from 'react-bootstrap';
// Import useLoaderData and useRevalidator for modern React Router data fetching
import { Link, useLoaderData, useRevalidator } from 'react-router';

import type { UserDTO } from '~/dtos/UserDTO';
// Import your real API services
import { getAllUsers, toggleUserBlockStatus } from '~/services/admin-service';
import { requireUserLoader } from "../auth-loaders";

/*
 * 1. DATA LOADER
 * clientLoader fetches the list of all users from the backend
 */
export async function clientLoader() {
    const currentUser = await requireUserLoader();
    if (!currentUser || !currentUser.roles.includes("ADMIN")) return null;

    const allUsers = await getAllUsers();
    const filteredUsers = allUsers.filter(user => !user.roles.includes("ADMIN"));
    return filteredUsers;
}

export default function AdminUserList() {
    const base_image_url = "/api/v1/images";

    // 2. STATE & HOOKS
    // Retrieve the data fetched by the clientLoader
    const users = useLoaderData() as UserDTO[];

    // Instanciate revalidator to refresh the table after an action
    const revalidator = useRevalidator();

    // Track which user is currently being updated to show a loading spinner on their specific button
    const [updatingId, setUpdatingId] = useState<number | null>(null);

    // 3. ACTION HANDLERS
    const handleToggleBlock = async (userId: number, currentState: boolean) => {
        try {
            setUpdatingId(userId); // Show spinner for this specific user

            // Call API to toggle the state
            await toggleUserBlockStatus(userId, !currentState);

            // Refresh table data
            revalidator.revalidate();
        } catch (error) {
            console.error("Error toggling user state:", error);
            alert("No se pudo actualizar el estado del usuario.");
        } finally {
            setUpdatingId(null); // Hide spinner
        }
    };

    // 4. RENDER TSX
    return (
        <>
            {/* Header Section */}
            <div className="d-flex justify-content-between align-items-center mb-4 border-bottom pb-2">
                <div>
                    <h2 className="h3 fw-bold text-dark mb-0">Gestión de Usuarios</h2>
                    <p className="text-muted small mb-0">Administra el acceso y estado de los usuarios registrados.</p>
                </div>
            </div>

            {/* Table Card */}
            <Card className="shadow-sm border-0">
                <Card.Body className="p-0">
                    <div className="table-responsive">
                        <Table hover className="align-middle mb-0">
                            <thead className="bg-light">
                                <tr>
                                    <th className="ps-4 py-3 border-bottom-0">Usuario</th>
                                    <th className="py-3 border-bottom-0">Estado</th>
                                    <th className="py-3 text-end pe-4 border-bottom-0">Acciones</th>
                                </tr>
                            </thead>
                            <tbody>
                                {users.map((user) => (
                                    <tr key={user.id}>
                                        <td className="ps-4 py-3">
                                            <div className="d-flex align-items-center">
                                                {/* Avatar logic */}
                                                {user.imageId ? (
                                                    <img
                                                        src={`${base_image_url}/${user.imageId}/media`}
                                                        className="rounded-circle me-3 shadow-sm"
                                                        alt="Avatar"
                                                        width="40" height="40"
                                                        style={{ objectFit: 'cover' }}
                                                    />
                                                ) : (
                                                    <img
                                                        src={`https://ui-avatars.com/api/?name=${encodeURIComponent(user.userName)}&background=random`}
                                                        className="rounded-circle me-3 shadow-sm"
                                                        alt="Default Avatar"
                                                        width="40" height="40"
                                                        style={{ objectFit: 'cover' }}
                                                    />
                                                )}
                                                <div>
                                                    {/* DEFENSE NOTE: Fixed the template literal syntax in the Link */}
                                                    <Link to={`/admin/users/${user.id}`} className="text-decoration-none text-dark">
                                                        <div className="fw-bold">{user.userName} {user.surname}</div>
                                                    </Link>
                                                    <div className="small text-muted">{user.email}</div>
                                                </div>
                                            </div>
                                        </td>

                                        {/* State Badge */}
                                        <td>
                                            {user.state ? (
                                                <Badge bg="success" className="bg-opacity-75 px-2 py-1">
                                                    <i className="bi bi-check-circle me-1"></i>Activo
                                                </Badge>
                                            ) : (
                                                <Badge bg="danger" className="bg-opacity-75 px-2 py-1">
                                                    <i className="bi bi-x-circle me-1"></i>Bloqueado
                                                </Badge>
                                            )}
                                        </td>

                                        {/* Action Buttons */}
                                        <td className="text-end pe-4">
                                            {user.state ? (
                                                <Button
                                                    variant="danger"
                                                    size="sm"
                                                    className="d-inline-flex align-items-center"
                                                    onClick={() => handleToggleBlock(user.id, user.state)}
                                                    disabled={updatingId === user.id}
                                                >
                                                    {updatingId === user.id ? (
                                                        <Spinner as="span" animation="border" size="sm" role="status" aria-hidden="true" />
                                                    ) : (
                                                        <><i className="bi bi-ban me-1"></i> Bloquear</>
                                                    )}
                                                </Button>
                                            ) : (
                                                <Button
                                                    variant="success"
                                                    size="sm"
                                                    className="d-inline-flex align-items-center"
                                                    onClick={() => handleToggleBlock(user.id, user.state)}
                                                    disabled={updatingId === user.id}
                                                >
                                                    {updatingId === user.id ? (
                                                        <Spinner as="span" animation="border" size="sm" role="status" aria-hidden="true" />
                                                    ) : (
                                                        <><i className="bi bi-unlock me-1"></i> Desbloquear</>
                                                    )}
                                                </Button>
                                            )}
                                        </td>
                                    </tr>
                                ))}
                            </tbody>
                        </Table>
                    </div>
                </Card.Body>
            </Card>
        </>
    );
}