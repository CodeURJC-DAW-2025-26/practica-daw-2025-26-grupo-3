import { Link, useLoaderData, useNavigation } from "react-router";
import { Container, Row, Col, Card, Form, Button, Badge } from "react-bootstrap";
import { Spinner } from "~/components/spinner";
import ProductList from "~/components/Product/product_list";
import { getMyProducts } from "~/services/product-service";

import { requireUserLoader } from "~/stores/user-store";
import { ErrorCard } from "~/components/error-card";

export async function clientLoader() {

    const currentUser = await requireUserLoader();

    if (!currentUser) {
        return { products: null, currentUser: null, error: "Debes iniciar sesión para ver y gestionar tus productos." };
    }

    try {
        const products = await getMyProducts();
        return { products, currentUser, error: null };
    } catch (error) {
        console.error("Error cargando los productos:", error);
        return { products: [], currentUser, error: "Hubo un problema de conexión al cargar tus productos." };

    }
}

export default function MyProducts() {
    const data = useLoaderData<typeof clientLoader>();
    const products = data?.products || [];
    const currentUser = data?.currentUser;
    const error = data?.error;

    const navigation = useNavigation();

    // We check if the app is loading anything
    const isLoading = navigation.state === "loading" || navigation.state === "submitting";

    // If has occured an error and the user is not logged
    if (error && !currentUser) {
        return (
            <Container className="my-5 d-flex justify-content-center align-items-center" style={{ minHeight: "60vh" }}>
                <ErrorCard message={error} />
            </Container>
        );
    }
    // If has occured an error and the user is logged
    if (error && currentUser) {
        return (
            <Container className="my-5 d-flex justify-content-center align-items-center" style={{ minHeight: "60vh" }}>
                <ErrorCard message={error} />
            </Container>
        );
    }

    return (
        <>
            {/* HEADER */}
            <section className="py-5 bg-white">
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
            {isLoading ? (
                <Container className="my-5 d-flex flex-column justify-content-center align-items-center" style={{ minHeight: "60vh" }}>
                    <Spinner />
                    <p className="text-muted mt-3 fs-5">Cargando tus productos...</p>
                </Container>) : (
                <ProductList
                    products={products}
                    isOwnerMode={true}
                    emptyTitle="Aún no has publicado ningún producto"
                    emptySubtitle="Anímate y pon a la venta lo que ya no usas."
                    currentUser={currentUser}
                />
            )}

        </>
    );
}