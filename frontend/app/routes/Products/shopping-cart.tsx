import { useState } from "react";
import { Link, useLoaderData, useNavigate } from "react-router";
import { Item } from "~/components/ShoppingCart/Item";
import { useUserState } from "~/stores/user-store";
import type { Route } from "../+types";
import { requireUserLoader } from "../auth-loaders";
import { useCartState } from "~/stores/shoppingCart-store";
import { ErrorCard } from "~/components/error-card";
import { Container, Row, Col, Card, Table, Button } from "react-bootstrap";

export async function clientLoader() {
    // we verify if the user is logged
    const user = await requireUserLoader();

    let cartError = null;

    // If the user is logged, we load his shopping cart before showing the page
    if (user) {
        try {
            //we use getState() to access Zustand from outside a component
            await useCartState.getState().getCart();
        } catch (err) {
            cartError = "Error cargando los productos del carrito. Inténtalo de nuevo más tarde.";
        }
    }

    return { cartError };
}

export default function ShoppingCart({ loaderData }: Route.ComponentProps) {

    // cartError if it fails to load the page for the first time
    const { cartError } = useLoaderData<typeof clientLoader>();

    const [convertLoading, setConvertLoading] = useState(false);

    //ActionError if the user clicks "Complete purchase" and the server gives an error at that moment
    const [actionError, setActionError] = useState<string | null>(null);
    
    //If there is an error loading or trying to purchase, we will show it.
    const displayError = actionError || cartError;

    const { currentUser } = useUserState();
    const { items, totalPrice, totalQuantity, convertToOrder } = useCartState();
    const navigate = useNavigate();
    const hasItems = (items?.length ?? 0) > 0;


    async function handleCartToOrder() {
        setConvertLoading(true);
        setActionError(null);
        try {
            await convertToOrder();
            navigate("/profile");
        }
        catch (err) {
            setActionError("Error al realizar la compra. Inténtalo de nuevo más tarde.");
        }
        finally {
            setConvertLoading(false);
        }
    }

    if (!currentUser) {
        return (<ErrorCard message="Debes iniciar sesión para ver y gestionar tu carrito." className="container my-5" />)
    }

    return (
        <section className="py-5 bg-light">
            <Container>
                <div className="d-flex flex-wrap justify-content-between align-items-center mb-4">
                    <div>
                        <h1 className="h3 fw-bold text-dark mb-1">Mi carrito</h1>
                        <p className="text-muted mb-0">Resumen de productos del pedido actual.</p>
                    </div>
                    <Link to="/product_search" className="btn btn-primary">
                        <i className="bi bi-arrow-left"></i> Seguir comprando
                    </Link>
                </div>

                <Row className="g-4">
                    <Col lg={8}>
                        <Card className="shadow-sm border-0">
                            <Card.Body className="p-0">
                                <Table responsive className="align-middle mb-0">
                                    <thead className="bg-light">
                                        <tr>
                                            <th className="ps-4 py-3">Producto</th>
                                            <th className="py-3">Estado</th>
                                            <th className="py-3">Precio</th>
                                            <th className="py-3">Cantidad</th>
                                            <th className="py-3 text-end pe-4">Acciones</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        {displayError !== null ? (
                                            <tr>
                                                <td colSpan={5} className="p-4">
                                                    <ErrorCard message={displayError} />
                                                </td>
                                            </tr>
                                        ) : hasItems ? (
                                            items!.map((item) => (
                                                <Item quantity={item.quantity} key={item.id} id={item.id} productId={item.productId} productName={item.productName} />
                                            ))
                                        ) : (
                                            <tr>
                                                <td colSpan={5} className="text-center py-4">
                                                    No tienes productos en el carrito
                                                </td>
                                            </tr>
                                        )}
                                    </tbody>
                                </Table>
                            </Card.Body>
                        </Card>
                    </Col>

                    <Col lg={4}>
                        <Card className="shadow-sm border-0">
                            <Card.Body>
                                <h2 className="h5 fw-bold mb-3">Resumen del pedido</h2>
                                <div className="d-flex justify-content-between mb-2">
                                    <span className="text-muted">Cantidad de productos del pedido:</span>
                                    <span>{totalQuantity}</span>
                                </div>
                                <hr />
                                <div className="d-flex justify-content-between mb-4">
                                    <span className="fw-bold">Total a pagar: </span>
                                    <span className="fw-bold">{totalPrice} €</span>
                                </div>
                                <Button
                                    variant="primary"
                                    type="button"
                                    className={`w-100 mb-2 d-flex align-items-center justify-content-center signup-submit-btn ${convertLoading ? "signup-submit-btn-loading" : ""}`}
                                    onClick={handleCartToOrder}
                                    disabled={convertLoading}
                                >
                                    {convertLoading ? (
                                        <span className="signup-loading-spinner" role="status" aria-label="Finalizando compra" />
                                    ) : (
                                        <span>Finalizar compra</span>
                                    )}
                                </Button>
                            </Card.Body>
                        </Card>

                        <Card className="shadow-sm border-0 mt-4">
                            <Card.Body>
                                <h3 className="h6 fw-bold mb-3">Dirección de entrega</h3>
                                <p className="text-muted mb-2">{currentUser!.address}</p>
                            </Card.Body>
                        </Card>
                    </Col>
                </Row>
            </Container>
        </section>
    );
}