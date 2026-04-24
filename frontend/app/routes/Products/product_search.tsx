import { useState, useEffect } from "react";
import { Container } from "react-bootstrap";
import { getAllProducts } from "~/services/product-service";
import ProductList from "~/components/Product/product_list";
import type { ProductBasicDTO } from "~/dtos/ProductBasicDTO";
import { useUserState } from "~/stores/user-store";
import { Spinner } from "~/components/spinner";



export default function ProductSearch() {
    const [products, setProducts] = useState<ProductBasicDTO[]>([]);
    const [loading, setLoading] = useState(true);
    const { loadLoggedUser } = useUserState();

    useEffect(() => {
        async function loadData() {
            setLoading(true);
            try {
                // We load the user
                await loadLoggedUser();

                // We load ALL the products
                const data = await getAllProducts();

                // we save it to the state of react
                setProducts(data || []);
            } catch (error) {
                console.error("Error cargando el catálogo de productos:", error);
            } finally {
                setLoading(false);
            }
        }

        loadData();
    }, []);

    //Load sreen while we wait to the backend
    if (loading) {
        return (
            <div className="my-5 d-flex justify-content-center align-items-center" style={{ minHeight: "60vh" }}>
                <Spinner />
            </div>
        );
    }

    return (<Container className="my-5">
        <h2 className="mb-4 text-center fw-bold">Catálogo de Productos</h2>

        {/* we reuse the ProductList component */}
        <ProductList products={products} />

    </Container>
    );
}