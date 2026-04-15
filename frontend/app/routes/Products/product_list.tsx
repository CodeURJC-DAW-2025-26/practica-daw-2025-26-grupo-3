import { Link, useLoaderData } from "react-router";
import { getProducts } from "~/services/product-service";
import type { ProductBasicDTO } from "~/dtos/ProductBasicDTO";
import {
    Container,
    Row,
    Col,
    Card,
    Button,
    Badge,
    Alert
} from "react-bootstrap";
import { useUserState } from "~/stores/user-store";


//This function returns the corresponding string to the state number
function getStateInfo(state: number) {
    switch (state) {
        case 0:
            return { label: "Nuevo", variant: "success" };
        case 1:
            return { label: "Reacondicionado", variant: "info" };
        case 2:
            return { label: "Segunda mano", variant: "warning" };
        default:
            return { label: "Desconocido", variant: "secondary" };
    }
}

export async function clientLoader() {
    try {
        const products = await getProducts();
        return Array.isArray(products) ? products : [];
    } catch (e) {
        console.error("Error loading products:", e);
        return [];
    }
}

// loaderData? ==> The ? says that loaderData is optional

export default function ProductList({ loaderData }: { loaderData?: ProductBasicDTO[] }) {
    const products = useLoaderData<ProductBasicDTO[]>();
    const { currentUser } = useUserState(); //we need the user state to know if he is logged or not

    return (
        <section className="py-5 bg-white">
            <Container>
                {/* --- PAGE TITLE AND DESCRIPTION --- */}
                <div className="text-center mb-5">
                    <Badge bg="primary" className="bg-opacity-10 text-primary mb-2 px-3 py-2 fs-1">
                        ⭐ Destacados
                    </Badge>
                    <h2 className="fw-bold display-8 mb-2">Recomendaciones</h2>
                    <p className="text-muted">Creemos que estos productos te encantarán</p>
                </div>

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
                                        <Badge pill bg={getStateInfo(product.state).variant} className="mb-2">
                                            {getStateInfo(product.state).label}
                                        </Badge>
                                        <Card.Title className="h6 fw-bold">{product.productName}</Card.Title>
                                        <Card.Text className="text-primary fw-bold fs-5 mb-1">{product.price}€</Card.Text>
                                        <div className="small text-warning">
                                            ★ {product.averageRating.toFixed(1)} <span className="text-muted">({product.reviewCount})</span>
                                        </div>
                                    </Card.Body>

                                    <Card.Footer className="bg-transparent border-0 pb-3">

                                        <Button
                                            as={Link as any}
                                            to={`/product_detail/${product.id}`}
                                            variant="primary"
                                            size="sm"
                                            className="w-100 rounded-pill">
                                            { /*If the user is logged, he can see the details of the product, if not he should log in to have permission */}
                                            {currentUser ? "Ver detalles" : "Iniciar sesión para ver más detalles"}

                                        </Button>
                                    </Card.Footer>
                                </Card>
                            </Col>
                        ))
                    ) : (
                        // If there aren´t any products
                        <Col xs={12}>
                            <Alert variant="info" className="text-center">
                                No hay productos destacados disponibles en este momento. ¡Vuelve pronto para descubrir nuevas ofertas!
                            </Alert>
                        </Col>
                    )}
                </Row>
            </Container>
        </section>


    );
}
