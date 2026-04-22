import { Link } from "react-router";
import { Container, Row, Col, Card, Form, Button, Badge } from "react-bootstrap";

export default function MyProducts() {
    const token = "mock-csrf-token-123";
    const products = [
        { id: 1, productName: "Portátil Gaming", price: 850, hasImage: true, stateClass: "bg-success", stateName: "Como nuevo" },
        { id: 2, productName: "Bicicleta de montaña", price: 250, hasImage: false, stateClass: "bg-warning", stateName: "Usado" },
        { id: 3, productName: "Smartphone", price: 400, hasImage: true, stateClass: "bg-info", stateName: "Reacondicionado" },
    ];

    return (
        <>
            {/* HEADER */}
            <section className="py-5 bg-light">
                <Container>
                    <div className="d-flex justify-content-between align-items-center">
                        <div>
                            <h1 className="fw-bold">Mis Productos Publicados</h1>
                            <p className="text-muted mb-0">Gestiona todos los productos que has puesto a la venta</p>
                        </div>
                        <Link to="/product-publish" className="btn btn-primary">
                            <i className="bi bi-plus-circle"></i> Publicar Nuevo Producto
                        </Link>
                    </div>
                </Container>
            </section>

            {/* PRODUCTS SECTION */}
            <section className="py-5">
                <Container>
                    <Row className="g-4">
                        {products && products.length > 0 ? (
                            products.map((product) => (
                                <Col md={3} key={product.id}>
                                    <Card className="h-100 border-0 shadow-sm product-card">
                                        <div className="position-relative">
                                            {product.hasImage ? (
                                                <Card.Img variant="top" src={`/product-images/${product.id}`} className="product-img" alt={product.productName} />
                                            ) : (
                                                <Card.Img variant="top" src={`https://placehold.co/300x250/667eea/ffffff?text=${encodeURIComponent(product.productName)}`} className="product-img" alt="Producto" />
                                            )}
                                        </div>
                                        <Card.Body>
                                            <Badge pill className={`${product.stateClass} mb-2`}>{product.stateName}</Badge>
                                            <Card.Title className="h6 fw-bold">{product.productName}</Card.Title>
                                            <Card.Text className="text-primary fw-bold mb-0">{product.price}€</Card.Text>
                                        </Card.Body>
                                        <Card.Footer className="bg-transparent border-0 pb-3">
                                            <div className="d-grid gap-2">
                                                <Link to={`/product_detail/${product.id}`}
                                                    className="btn btn-sm btn-primary d-flex align-items-center justify-content-center">
                                                    <i className="bi bi-eye-fill me-2"></i> Ver Detalle
                                                </Link>
                                                <a href={`/edit_product/${product.id}`}
                                                    className="btn btn-sm btn-edit-custom text-white d-flex align-items-center justify-content-center">
                                                    <i className="bi bi-pencil-square me-2"></i> Editar
                                                </a>
                                                <Form action={`/delete_product/${product.id}`} method="POST" style={{ display: 'inline' }}>
                                                    <input type="hidden" name="_csrf" value={token} />
                                                    <Button variant="danger" size="sm" type="submit"
                                                        onClick={(e) => {
                                                            if (!window.confirm('¿Estás seguro de que deseas borrar este producto?')) {
                                                                e.preventDefault();
                                                            }
                                                        }}
                                                        className="d-flex align-items-center justify-content-center w-100">
                                                        <i className="bi bi-trash me-2"></i> Eliminar
                                                    </Button>
                                                </Form>
                                            </div>
                                        </Card.Footer>
                                    </Card>
                                </Col>
                            ))
                        ) : (
                            <Col xs={12} className="text-center py-5">
                                <p className="text-muted">Aún no has publicado ningún producto.</p>
                            </Col>
                        )}
                    </Row>
                </Container>
            </section>
        </>
    );
}