import { Link } from "react-router";
import { useEffect, useState } from "react";
import { useCartState } from "~/stores/shoppingCart-store";
import { getAllPDF, getPdfById } from "~/services/cart-service";
import { Card, Button, Table, Alert } from "react-bootstrap";

export function OrdersDashboard() {

    const { orders, getOrders } = useCartState();
    const [error, setError] = useState<string | null>(null);
    const [loadingPdf, setLoadingPdf] = useState(false);
    const hasOrders = orders!.length > 0;
    const base_url = "/api/v1/orders/";

    async function loadOrders() {
        let errMessage: string | null = null;
        try {
            await getOrders();
        }
        catch (err) {
            errMessage = err instanceof Error
                ? err.message
                : "Se ha producido un error al cargar tus pedidos. Inténtalo de nuevo más tarde."
            setError(errMessage);
        }
    }

    async function handleAllOrderPdfButton(id: number | null) {
        let errMessage: string | null = null;
        let pdfFile: Blob | null = null
        setLoadingPdf(true);

        try {
            pdfFile = !id
                ? pdfFile = await getAllPDF()
                : pdfFile = await getPdfById(id!);

            const url = URL.createObjectURL(pdfFile);

            window.open(url, '_blank');
        }
        catch (err) {
            errMessage = err instanceof Error
                ? err.message
                : "Se ha producido un error al cargar tu factura PDF. Inténtalo de nuevo más tarde."
            setError(errMessage);
        }
        finally {
            setLoadingPdf(false);
        }
    }

    function getOrderStateText(state: number): string {
        switch (state) {
            case 0:
                return "Entregado";
            case 1:
                return "Pendiente de entrega";
            case 2:
                return "Pendiente de pago";
            default:
                return "Desconocido";
        }
    }

    useEffect(() => { loadOrders() }, []);

    return (
        <Card className="border-0 shadow-sm p-4">
            <div className="d-flex justify-content-between align-items-center mb-4">
                <h4 className="fw-bold mb-0">Mis Pedidos</h4>
                {hasOrders && (
                    <Button
                        variant="primary"
                        size="sm"
                        className="d-inline-flex align-items-center"
                        id="export-all-orders-pdf"
                        onClick={() => { handleAllOrderPdfButton(null) }}
                    >
                        <i className="bi bi-file-earmark-pdf me-2" />
                        Exportar todos en PDF
                    </Button>
                )}
            </div>

            {hasOrders ? (
                orders!.map((order) => (
                    <Table className="align-middle" key={order.orderID}>
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
                                <td>{getOrderStateText(order.state)}</td>
                                <td className="fw-bold text-center">{order.totalPrice.toFixed(2)} EUR</td>
                            </tr>
                            <tr>
                                <td colSpan={4}>
                                    <div>
                                        <h6 className="fw-bold">Productos del pedido:</h6>
                                        <Table size="sm" className="mb-0">
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
                                        </Table>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td colSpan={4} className="text-end">
                                    <Button
                                        variant="primary"
                                        size="sm"
                                        className="d-inline-flex align-items-center"
                                        id="export-all-orders-pdf"
                                        onClick={() => { handleAllOrderPdfButton(order.orderID) }}
                                    >
                                        <i className="bi bi-file-earmark-pdf me-2" />
                                        Exportar factura en PDF
                                    </Button>
                                </td>
                            </tr>
                        </tbody>
                    </Table>
                ))
            ) : (
                <Alert variant="info" className="mt-3">
                    No tienes pedidos realizados todavia.
                </Alert>
            )}
        </Card>
    );
}