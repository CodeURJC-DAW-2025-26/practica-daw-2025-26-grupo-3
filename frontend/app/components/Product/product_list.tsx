import { Link , useRevalidator } from "react-router";
import type { ProductBasicDTO } from "~/dtos/ProductBasicDTO";
import {
    Container,
    Row,
    Col,
    Card,
    Button,
    Badge,
} from "react-bootstrap";
import { useUserState } from "~/stores/user-store";
import { removeProduct } from "~/services/product-service";
import { useState } from "react";


//This function returns the corresponding string to the state number
function getStateInfo(state: number) {
    switch (state) {
        case 0:
            return { label: "Nuevo", variant: "primary", bgClass: "bg-primary-subtle text-primary" };
        case 1:
            return { label: "Reacondicionado", variant: "info", bgClass: "bg-info-subtle text-info" };
        case 2:
            return { label: "Segunda Mano", variant: "warning", bgClass: "bg-warning-subtle text-warning" };
        default:
            return { label: "Desconocido", variant: "secondary", bgClass: "bg-secondary-subtle text-secondary" };
    }
}

interface ProductListProps {
    products: ProductBasicDTO[];
    isOwnerMode?: boolean;
    emptyTitle?: string;
    emptySubtitle?: string;
}

export default function ProductList({ products, isOwnerMode = false,
    emptyTitle = "No hay productos destacados disponibles",
    emptySubtitle = "Prueba a buscarlos mas adelante" }: ProductListProps) {
    const { currentUser } = useUserState(); //we need the user state to know if he is logged or not;

    const revalidator = useRevalidator();
    const [deletedIds, setDeletedIds] = useState<Set<number>>(new Set());

    async function handleDeleteProduct(productId: number): Promise<void> {
        const isConfirmed = window.confirm("¿Estás seguro de que quieres eliminar este producto? Esta acción no se puede deshacer.");

        if (isConfirmed) {
            try {
                // We try to remove the product
                await removeProduct(productId);

                // We add it to the local blacklist to hide it inmediately
                setDeletedIds(prev => new Set(prev).add(productId));

                revalidator.revalidate();

            } catch (error) {
                console.error("Error eliminando el producto:", error);
                alert("Hubo un problema al intentar eliminar el producto.");
            }
        }
    }
    // We filter the original list to avoid drawing those on the blacklist
    const visibleProducts = products?.filter(product => !deletedIds.has(product.id)) || [];


    return (
        <section className="py-5 bg-white">
            <Container>

                {/* --- PRODUCTS --- */}
                <Row className="g-4 justify-content-center" id="featured-products">
                    { /* If there are products we show them */}
                    {products && products.length > 0 ? (
                        products.map((product: ProductBasicDTO) => (
                            <Col md={3} key={product.id}>
                                <Card className="h-100 border-0 shadow-sm product-card position-relative">
                                    {/* Image */}
                                    <Card.Img
                                        variant="top"
                                        className="product-img"
                                        src={product.imageId ? `/api/v1/images/${product.imageId}/media` : `https://placehold.co/300x250/667eea/ffffff?text=${encodeURIComponent(product.productName)}`}
                                        alt={product.productName}
                                    />

                                    {/* Product information */}
                                    <Card.Body>
                                        <Badge pill bg="" className={`mb-2 ${getStateInfo(product.state).bgClass}`}>
                                            {getStateInfo(product.state).label}
                                        </Badge>
                                        <Card.Title className="h6 fw-bold">{product.productName}</Card.Title>
                                        <Card.Text className="text-primary fw-bold fs-5 mb-1">{product.price}€</Card.Text>
                                        <div className="small text-warning">
                                            ★ {product.averageRating ? product.averageRating.toFixed(1) : "0.0"} <span className="text-muted">({product.reviewCount || 0})</span>
                                        </div>
                                    </Card.Body>

                                    <Card.Footer className="bg-transparent border-0 pb-3">
                                        {isOwnerMode ? (
                                            <div className="d-grid gap-2">
                                                <Button
                                                    as={Link as any}
                                                    to={`/product_detail/${product.id}`}
                                                    variant="primary"
                                                    size="sm"
                                                    className="d-flex align-items-center justify-content-center">
                                                    <i className="bi bi-eye-fill me-2"></i> Ver Detalle
                                                </Button>
                                                <Button
                                                    as={Link as any}
                                                    to={`/edit_product/${product.id}`}
                                                    size="sm"
                                                    className="btn-edit-custom text-white d-flex align-items-center justify-content-center border-0">
                                                    <i className="bi bi-pencil-square me-2"></i> Editar
                                                </Button>
                                                <Button
                                                    variant="danger"
                                                    size="sm"
                                                    onClick={() => handleDeleteProduct(product.id)}
                                                    className="d-flex align-items-center justify-content-center w-100">
                                                    <i className="bi bi-trash-fill me-2"></i> Eliminar
                                                </Button>
                                            </div>
                                        ) : (
                                            <Button
                                                as={Link as any}
                                                to={`/product_detail/${product.id}`}
                                                variant="primary"
                                                size="sm"
                                                className="w-100 rounded-pill">
                                                { /*If the user is logged, he can see the details of the product, if not he should log in to have permission */}
                                                {currentUser ? "Ver detalles" : "Iniciar sesión para ver más detalles"}

                                            </Button>
                                        )}
                                    </Card.Footer>
                                </Card>
                            </Col>
                        ))
                    ) : (
                        <Col xs={12}>
                            <div className="text-center p-5 bg-light border rounded shadow-sm">
                                <i className="bi bi-box-seam display-4 text-muted mb-3 d-block"></i>
                                <h3 className="text-muted">{emptyTitle}</h3>
                                <p className="mb-0">{emptySubtitle}</p>

                                {/* Botón de vender solo si estamos en modo dueño */}
                                {isOwnerMode && (
                                    <Link to="/product-publish" className="btn btn-primary mt-3 px-4">
                                        Empezar a vender
                                    </Link>
                                )}
                            </div>
                        </Col>
                    )}
                </Row>
            </Container>
        </section>
    );
}
