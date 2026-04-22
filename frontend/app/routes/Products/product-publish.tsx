import { Link } from "react-router";
import { Container, Row, Col, Card, Form, Button, ButtonGroup } from "react-bootstrap";

export default function ProductPublish() {
    const isAdmin = false; // Mock data (cambia a true para ver la vista de admin)
    const token = "mock-csrf-token-123";

    return (
        <Container className="my-5">
            <Row className="justify-content-center">
                <Col md={8}>
                    <Card className="border-0 shadow-sm p-4">
                        <div className="d-flex justify-content-between align-items-start mb-4">
                            <div>
                                <h3 className="fw-bold mb-4">Publicar Producto</h3>
                            </div>
                        </div>

                        <p className="text-muted small">Completa los datos para poner tu artículo a la venta.</p>
                        <Form action="/publish_new_product" method="post" encType="multipart/form-data">
                            <Form.Group className="mb-3">
                                <Form.Label className="fw-bold" htmlFor="productName">Nombre del Producto</Form.Label>
                                <Form.Control type="text" id="productName" name="productName"
                                    placeholder="Ej. MacBook Pro 2021" minLength={3} maxLength={100} required />
                            </Form.Group>
                            <Form.Group className="mb-3">
                                <Form.Label className="fw-bold" htmlFor="price">Precio (€)</Form.Label>
                                {/* Minimun price to sell is 1 cent */}
                                <Form.Control type="number" id="price" name="price" placeholder="0.00" min="1"
                                    step="0.01" required />
                            </Form.Group>
                            <Form.Group className="mb-3">
                                {/* If the user is not an admin */}
                                {!isAdmin && (
                                    <>
                                        <span className="form-label fw-bold d-block mb-2">Estado del artículo</span>
                                        <ButtonGroup className="w-100">
                                            <p>Segunda mano</p>
                                        </ButtonGroup>
                                        <input type="hidden" name="state" value="2" />
                                    </>
                                )}

                                {/* If the user is an admin (only admins can upload new and reconditioned productos) */}
                                {isAdmin && (
                                    <>
                                        <Form.Label className="fw-bold">Estado del artículo</Form.Label>
                                        <Form.Select id="state" name="state" required defaultValue="-1">
                                            <option value="-1" disabled>Selecciona el estado...</option>
                                            <option value="0">Nuevo</option>
                                            <option value="1">Reacondicionado</option>
                                            <option value="2">Segunda mano</option>
                                        </Form.Select>
                                    </>
                                )}
                            </Form.Group>
                            <Form.Group className="mb-3">
                                <Form.Label className="fw-bold" htmlFor="description">Descripción</Form.Label>
                                <Form.Control as="textarea" id="description" name="description" rows={4}
                                    placeholder="Describe brevemente el estado y características de tu producto"
                                    minLength={15} maxLength={1000} required />
                            </Form.Group>
                            <Form.Group className="mb-3">
                                <Form.Label className="fw-bold" htmlFor="images">Imagen</Form.Label>
                                <Form.Control type="file" id="images" name="productimages" accept="image/*"
                                    multiple required />
                                <Form.Text className="text-muted d-block">Debes adjuntar al menos una imagen del producto</Form.Text>
                            </Form.Group>
                            <Button variant="primary" type="submit" className="w-100 mt-4 py-2 fw-bold">
                                Publicar Ahora
                            </Button>
                            <input type="hidden" name="_csrf" value={token} />
                        </Form>
                        <div className="mt-4 text-center">
                            <Link to="/" className="btn btn-danger py-2 fw-bold shadow-sm" title="Volver a la tienda">
                                Volver a la tienda
                            </Link>
                        </div>
                    </Card>
                </Col>
            </Row>
        </Container>
    );
}