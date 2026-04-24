import { Link, useLoaderData, useNavigation } from "react-router";
import { Container, Row, Col, Card, Form, Button, Badge } from "react-bootstrap";
import { Spinner } from "~/components/spinner";
import ProductList from "~/components/Product/product_list";
import { getMyProducts } from "~/services/product-service";

export async function clientLoader() {
    try {
        const products = await getMyProducts();
        return { products };
    } catch (error) {
        console.error("Error cargando los productos:", error);
        return { products: [] };

    }
}

export default function MyProducts() {
    const data = useLoaderData<typeof clientLoader>();
    const products = data?.products || [];

    const navigation = useNavigation();

    // We check if the app is loading anything
    const isLoading = navigation.state === "loading" || navigation.state === "submitting";

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
                />
            )}

        </>
    );
}