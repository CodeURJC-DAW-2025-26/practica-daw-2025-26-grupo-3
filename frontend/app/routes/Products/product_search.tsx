import { Container } from "react-bootstrap";
import { getAllProducts } from "~/services/product-service";
import ProductList from "~/components/Product/product_list";
import { requireUserLoader } from "~/stores/user-store";
import { useLoaderData } from "react-router";
import type { ProductBasicDTO } from "~/dtos/ProductBasicDTO";

export async function clientLoader() {
    // We load the user into Zustand's memory before rendering the page.
    await requireUserLoader();

    // We request the products from the server.
    try {
        const response = await getAllProducts();

        const productsArray = Array.isArray(response) ? response : (response?.content || []);

        return { products: productsArray as ProductBasicDTO[] };
    } catch (error) {
        console.error("Error cargando el catálogo de productos:", error);
        // If there is an error, we return an empty array
        return { products: [] as ProductBasicDTO[] };
    }
}

export default function ProductSearch() {
    const { products } = useLoaderData<typeof clientLoader>();

    return (
        <Container className="my-5">
            <h2 className="mb-4 text-center fw-bold">Catálogo de Productos</h2>

            {/* we reuse the ProductList component */}
            <ProductList products={products} />

        </Container>
    );
}