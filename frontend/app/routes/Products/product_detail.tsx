import { useEffect, useState } from "react";
import { useParams, Link, useNavigate } from "react-router";
import { getBasicProduct, removeProduct } from "~/services/product-service";
import type { ProductDetailDTO } from "~/dtos/ProductDetailDTO";
import { useUserState } from "~/stores/user-store";
import { ErrorCard } from "~/components/error-card";
import { requireUserLoader } from "../auth-loaders";
import type { Route } from "../+types";

// React Bootstrap imports
import Container from "react-bootstrap/Container";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import Carousel from "react-bootstrap/Carousel";
import Button from "react-bootstrap/Button";
import Form from "react-bootstrap/Form";
import Card from "react-bootstrap/Card";
import Image from "react-bootstrap/Image";
import { Spinner } from "~/components/spinner";
import { useCartState } from "~/stores/shoppingCart-store";
import { ReviewList } from "~/components/Product/ReviewList";
import { ReviewForm } from "~/components/Product/ReviewForm";

export async function clientLoader() {
    return await requireUserLoader();
}

export default function ProductDetail({ loaderData }: Route.ComponentProps) {
    const { id } = useParams();
    const navigate = useNavigate();
    const [product, setProduct] = useState<ProductDetailDTO | null>(null);
    const [productIdNumber, setProductIdNumber] = useState<number | null>(null);

    const [loading, setLoading] = useState(true);
    const [cartLoading, setCartLoading] = useState(false);
    const [cartError, setCartError] = useState<string | null>(null);

    const { currentUser, loadLoggedUser } = useUserState();
    const { addItem } = useCartState();

    function loadProductId() {
        if (!id) {
            setProductIdNumber(null);
            return;
        }

        const numericId = Number(id);
        if (Number.isNaN(numericId)) {
            setProductIdNumber(null);
            setLoading(false);
            return;
        }

        setProductIdNumber(numericId);
    }

    async function handleAddCart(id: number) {
        let errorMessage: string | null = null;
        setCartError(null);
        setCartLoading(true);
        try {
            await addItem(id);
        }
        catch (err) {
            errorMessage = "Se ha producido un error al añadir el producto a tu carrito. Inténtalo de nuevo más tarde.";
            setCartError(errorMessage);
        }
        finally {
            setCartLoading(false);
        }

        if (!errorMessage) {
            navigate("/shopping-cart")
        }
    }

    useEffect(() => {
        loadLoggedUser();
    }, [loadLoggedUser]);

    useEffect(() => {
        loadProductId();
    }, [id]);

    useEffect(() => {
        if (productIdNumber !== null) {
            getBasicProduct(productIdNumber)
                .then(data => {
                    setProduct(data);
                    setLoading(false);
                })
                .catch(err => {
                    console.error(err);
                    setLoading(false);
                });
        }
    }, [productIdNumber]);

    if (!currentUser) {
        return (
            <Container className="my-5 d-flex justify-content-center align-items-center" style={{ minHeight: "60vh" }}>
                <ErrorCard message="Debes iniciar sesion para acceder a esta página." />
            </Container>
        );
    }

    if (loading) {
        return (
            <Container className="my-5 d-flex justify-content-center align-items-center" style={{ minHeight: "60vh" }}>
                <Spinner />
            </Container>
        );
    }

    if (!product) {
        return (
            <Container className="my-5 d-flex justify-content-center align-items-center" style={{ minHeight: "60vh" }}>
                <h2>Producto no encontrado</h2>
            </Container>
        );
    }

    //We see if the user can edit or delete a product
    const isAdmin = currentUser.roles?.includes("ADMIN") ?? false;
    const isOwner = currentUser.id === product.seller?.id;
    const canModifyProduct = isAdmin || isOwner; //If the user is an admin or an owner, he can edit or delete the product

    //We put the state of the product
    let stateClass = "bg-secondary text-white";
    let stateName = "Estado desconocido";

    switch (product.state) {
        case 0:
            stateClass = "bg-primary bg-opacity-10 text-primary";
            stateName = "Nuevo";
            break;
        case 1:
            stateClass = "bg-info bg-opacity-10 text-info";
            stateName = "Reacondicionado";
            break;
        case 2:
            stateClass = "bg-warning bg-opacity-10 text-warning";
            stateName = "Segunda Mano";
            break;
    }

    async function handleDeleteProduct(productId: number): Promise<void> {
        const isConfirmed = window.confirm("¿Estás seguro de que quieres eliminar este producto? Esta acción no se puede deshacer.");

        if (isConfirmed) {
            try {
                // We try to remove the product
                await removeProduct(productId);

                // When finished removing, we navigate to index page
                navigate("/");
            } catch (error) {
                console.error("Error eliminando el producto:", error);
                alert("Hubo un problema al intentar eliminar el producto.");
            }
        }
    }

    return (
        <Container className="my-5">
            <Row className="g-5">
                <Col md={6} className="mb-4">
                    <Carousel
                        className="shadow-sm rounded"
                        slide={true}
                        interval={null}
                        prevIcon={<span className="carousel-control-prev-icon bg-dark rounded-circle p-2"></span>}
                        nextIcon={<span className="carousel-control-next-icon bg-dark rounded-circle p-2"></span>}
                    >
                        {product.images && product.images.length > 0 ? (
                            product.images.map((img: any) => (
                                <Carousel.Item key={img.id}>
                                    <img
                                        src={`/api/v1/images/${img.id}/media`}
                                        className="d-block w-100"
                                        style={{ objectFit: "contain", height: "600px", backgroundColor: "#f8f9fa" }}
                                        alt={product.productName}
                                        onError={(e) => { e.currentTarget.src = "https://placehold.co/600x600?text=Error+Cargando" }}
                                    />
                                </Carousel.Item>
                            ))
                        ) : (
                            <Carousel.Item>
                                <img
                                    src="https://placehold.co/600x600?text=Sin+Imagen"
                                    className="d-block w-100"
                                    style={{ objectFit: "cover", height: "600px" }}
                                    alt="Sin imagen"
                                />
                            </Carousel.Item>
                        )}
                    </Carousel>
                    <div className="d-flex justify-content-center gap-2 mt-3 overflow-auto">
                        {product.images && product.images.map((img: any) => (
                            <Image
                                key={img.id}
                                src={`/api/v1/images/${img.id}/media`}
                                thumbnail
                                className="opacity-75"
                                style={{ width: "70px", height: "70px", objectFit: "cover", cursor: "pointer" }}
                                alt={`Thumbnail`}
                                onError={(e) => { e.currentTarget.src = "https://placehold.co/70x70?text=Err" }}
                            />
                        ))}
                    </div>
                </Col>

                <Col md={6}>
                    <span className={`badge ${stateClass} mb-2`} style={{ fontSize: "0.875em", padding: "0.45em 0.6em", borderRadius: "0.375rem" }}>{stateName}</span>
                    <h1 className="fw-bold">{product.productName}</h1>
                    <p className="display-6 text-primary fw-bold">{product.price.toFixed(1)}€</p>
                    <hr />
                    <p className="text-muted">{product.description}</p>

                    <div className="my-4">
                        <Button
                            onClick={() => {
                                if (productIdNumber !== null) {
                                    handleAddCart(productIdNumber);
                                }
                            }}
                            variant="primary"
                            className="w-100 d-flex align-items-center justify-content-center shadow-sm font-weight-bold"
                            style={{ padding: "0.6rem 1.5rem", fontSize: "1.1rem" }}
                            disabled={cartLoading}
                        >
                            {cartLoading ? (
                                <Spinner />
                            ) : (
                                <>
                                    <i className="bi bi-cart-fill me-2" style={{ fontSize: "1.3rem" }}></i>
                                    Añadir al Carrito
                                </>
                            )}
                        </Button>

                        {cartError !== null && (
                            <ErrorCard message={cartError} className="mt-3" />
                        )}

                        {canModifyProduct && (
                            <div className="d-flex gap-3 mt-3">
                                <Button
                                    as={Link as any}
                                    to={`/edit_product/${product.id}`}
                                    className="flex-grow-1 d-flex align-items-center justify-content-center shadow-sm font-weight-bold"
                                    style={{ backgroundColor: "#fd7e14", borderColor: "#fd7e14", color: "white", padding: "0.5rem 1.5rem", fontSize: "1rem" }}
                                >
                                    <i className="bi bi-pencil-square me-2" style={{ fontSize: "1.2rem" }}></i> Editar
                                </Button>

                                <Button onClick={() => handleDeleteProduct(product.id)}
                                    type="submit"
                                    variant="danger"
                                    className="flex-grow-1 d-flex align-items-center justify-content-center shadow-sm font-weight-bold"
                                    style={{ padding: "0.5rem 1.5rem", fontSize: "1rem" }}
                                >
                                    <i className="bi bi-trash me-2" style={{ fontSize: "1.2rem" }}></i> Eliminar
                                </Button>
                            </div>
                        )}
                    </div>
                </Col>
            </Row>

            <hr className="my-5" />

            {currentUser && (
                <ReviewForm
                    productId={productIdNumber!}
                    title={"Añadir Reseña"}
                    reviewTitleValue=""
                    reviewBodyValue=""
                    operation="create"
                    reviewId={0}    //reviewId is not needed for creating a review.
                    buttonText="Publicar reseña"
                />
            )}

            {productIdNumber !== null && <ReviewList productId={productIdNumber} />}
        </Container>
    );
}
