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

const hasOrders = mockOrders.length > 0;

export function OrdersDashboard() {
    return (
        <div className="card border-0 shadow-sm p-4">
            <div className="d-flex justify-content-between align-items-center mb-4">
                <h4 className="fw-bold mb-0">Mis Pedidos</h4>
                {hasOrders && (
                    <a href="/bill/all" className="btn btn-primary btn-sm d-inline-flex align-items-center" id="export-all-orders-pdf">
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
    );
}