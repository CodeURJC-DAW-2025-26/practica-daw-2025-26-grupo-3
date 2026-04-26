import React from 'react';
import { useLoaderData, useRevalidator } from 'react-router';
import { getPendingOrders } from '~/services/admin-service';
import type { AdminOrderDataDTO } from '~/dtos/AdminOrderDataDTO';
import { acceptOrder } from '~/services/admin-service';
import { requireUserLoader } from "../auth-loaders";


// Client loader: Fetch pending orders directly from the backend
export async function clientLoader() {
    const currentUser = await requireUserLoader();
    if (!currentUser || !currentUser.roles.includes("ADMIN")) return [];

    // The backend now filters orders with state === 0
    const pendingOrders = await getPendingOrders();
    return pendingOrders;
}

// Function component for the Admin Orders List view
export default function AdminOrdersList() {
    // Retrieve the data typed as AdminOrderDataDTO array
    const orders = useLoaderData() as AdminOrderDataDTO[];
    const base_image_url = "/api/v1/images";
    const revalidator = useRevalidator();

    const handleAccept = async (orderId: number) => {
        try {
            await acceptOrder(orderId);
            revalidator.revalidate(); // This refreshes the data automatically
        } catch (error) {
            alert("Error accepting the order.");
        }
    };

    return (
        <>
            {/* --- HEADER SECTION --- */}
            <div className="d-flex justify-content-between align-items-center mb-4 border-bottom pb-2">
                <div>
                    <h2 className="h3 fw-bold text-dark mb-0">Pending Orders</h2>
                    <p className="text-muted small mb-0">Review and manage orders that require approval.</p>
                </div>
            </div>

            {/* --- MAIN TABLE CARD --- */}
            <div className="card shadow-sm border-0">
                <div className="card-body p-0">
                    <div className="table-responsive">
                        <table className="table table-hover align-middle mb-0">
                            <thead className="bg-light">
                                <tr>
                                    <th className="ps-4 py-3">Order ID</th>
                                    <th className="py-3">Customer</th>
                                    <th className="py-3">Date</th>
                                    <th className="py-3">Total</th>
                                    <th className="py-3">Status</th>
                                    <th className="py-3 text-end pe-4">Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                {orders.length > 0 ? (
                                    orders.map((order) => (
                                        <tr key={order.orderID}>
                                            <td className="ps-4 fw-bold text-primary">#{order.orderID}</td>

                                            {/* --- CLIENT COLUMN: Display user data dynamically --- */}
                                            <td>
                                                <div className="d-flex align-items-center">
                                                    {/* Check if the user has a stored image ID */}
                                                    {order.user?.imageId ? (
                                                        <img src={`${base_image_url}/${order.user.imageId}/media`}
                                                            className="rounded-circle me-2" alt="Avatar"
                                                            style={{ width: "40px", height: "40px", objectFit: "cover" }} />
                                                    ) : (
                                                        <img src={`https://ui-avatars.com/api/?name=${encodeURIComponent(order.user?.userName || 'User')}&background=random`}
                                                            className="rounded-circle me-2" alt="Avatar"
                                                            style={{ width: "40px", height: "40px" }} />
                                                    )}
                                                    <div>
                                                        <div className="fw-bold">{order.user?.userName} {order.user?.surname}</div>
                                                        <div className="small text-muted">{order.user?.email}</div>
                                                    </div>
                                                </div>
                                            </td>

                                            <td>{order.date}</td>
                                            <td className="fw-bold">{order.totalPrice.toFixed(2)} €</td>
                                            <td>
                                                <span className="badge bg-warning text-dark">
                                                    <i className="bi bi-clock me-1"></i> Pending
                                                </span>
                                            </td>
                                            <td className="text-end pe-4">
                                                <div className="d-flex justify-content-end gap-2">
                                                    <button
                                                        type="button"
                                                        className="btn btn-sm btn-success d-flex align-items-center"
                                                        onClick={() => handleAccept(order.orderID)}
                                                    >
                                                        <i className="bi bi-unlock me-1"></i> Accept
                                                    </button>
                                                </div>
                                            </td>
                                        </tr>
                                    ))
                                ) : (
                                    <tr>
                                        <td colSpan={6} className="text-center py-5 text-muted">
                                            <i className="bi bi-inbox fs-2 d-block mb-2 text-secondary"></i>
                                            Everything is up to date! No pending orders to review.
                                        </td>
                                    </tr>
                                )}
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </>
    );
}