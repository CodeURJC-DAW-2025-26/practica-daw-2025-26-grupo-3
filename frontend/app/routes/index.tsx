
import { Container, Row, Col, Alert, Badge, Button } from "react-bootstrap";
import { Link, useFetcher, useLoaderData, useNavigation } from "react-router";
import { requireUserLoader, useUserState } from "~/stores/user-store";
import { useEffect, useRef, useState } from "react";
import { getProductsPage } from "~/services/product-service";
import ProductList from "~/components/Product/product_list";
import type { ProductBasicDTO } from "~/dtos/ProductBasicDTO";

export async function clientLoader({ request }: { request: Request }) {
    // we load the user
    await requireUserLoader();

    try {
        // We read if the URL has a parameter ?page=X (if there isn't one, it's 0)
        const url = new URL(request.url);
        const pageParam = url.searchParams.get("page");
        const page = pageParam ? parseInt(pageParam, 10) : 0;

        // If we are at the page 0, the size of this is 8, in another case is 4
        const size = page === 0 ? 8 : 4;

        // we get the products of the page
        const data = await getProductsPage(page, size);
        const loadedProducts = data.content || (Array.isArray(data) ? data : []);

        // we check if there are more pages
        const hasMore = data.last !== undefined ? !data.last : loadedProducts.length === size;

        return { newProducts: loadedProducts, hasMore, page };
    } catch (error) {
        console.error("Error en clientLoader:", error);
        return { newProducts: [], hasMore: false, page: 0 };
    }
}

export default function Index() {

    const { currentUser, authMessage, authMessageVariant } = useUserState();
    const initialData = useLoaderData<typeof clientLoader>();
    const fetcher = useFetcher<typeof clientLoader>();
    const navigation = useNavigation();

    const [products, setProducts] = useState<ProductBasicDTO[]>(initialData.newProducts);
    // We start at the page 0, and at the start we have more pages to show
    const [page, setPage] = useState(initialData.page === 0 ? 2 : initialData.page + 1);
    const [hasMore, setHasMore] = useState(initialData.hasMore);

    // These constants are for managing the card displayed when logging in and out.
    const [isAuthMessageClosing, setIsAuthMessageClosing] = useState(false);
    const fadeTimerRef = useRef<number | null>(null);
    const clearTimerRef = useRef<number | null>(null);

    //This useEffect is used to load more products
    useEffect(() => {
        if (fetcher.data && fetcher.data.page === page) {
            // we add the products to the list that we have
            setProducts(prev => [...prev, ...fetcher.data!.newProducts]);
            setPage(prev => prev + 1);
            setHasMore(fetcher.data.hasMore);
        }
    }, [fetcher.data]);

    // **** This code is for managing the card displayed when logging in and out ****
    const clearAuthMessage = () => {

        useUserState.setState({ authMessage: null, authMessageVariant: null });

    };
    const startAuthMessageClose = () => {

        if (isAuthMessageClosing) {

            return;

        }

        setIsAuthMessageClosing(true);
        if (clearTimerRef.current) {

            window.clearTimeout(clearTimerRef.current);

        }
        clearTimerRef.current = window.setTimeout(() => {

            clearAuthMessage();

            setIsAuthMessageClosing(false);

        }, 350);

    };
    useEffect(() => {

        if (fadeTimerRef.current) {

            window.clearTimeout(fadeTimerRef.current);

            fadeTimerRef.current = null;

        }
        if (clearTimerRef.current) {

            window.clearTimeout(clearTimerRef.current);

            clearTimerRef.current = null;

        }
        if (!authMessage) {

            setIsAuthMessageClosing(false);

            return;

        }
        setIsAuthMessageClosing(false);

        fadeTimerRef.current = window.setTimeout(() => {

            setIsAuthMessageClosing(true);

        }, 4600);

        clearTimerRef.current = window.setTimeout(() => {

            clearAuthMessage();

            setIsAuthMessageClosing(false);

        }, 5000);

        return () => {

            if (fadeTimerRef.current) {

                window.clearTimeout(fadeTimerRef.current);

                fadeTimerRef.current = null;

            }

            if (clearTimerRef.current) {

                window.clearTimeout(clearTimerRef.current);

                clearTimerRef.current = null;

            }

        };

    }, [authMessage]);

    useEffect(() => {
        const targetPath = navigation.location?.pathname;

        if (!authMessage || !targetPath) {
            return;
        }

        if (targetPath !== "/") {
            clearAuthMessage();
            setIsAuthMessageClosing(false);
        }
    }, [navigation.location?.pathname, authMessage]);
    //****************************************************************************** 

    const handleLoadMore = () => {
        // we get the next page with the fetcher
        fetcher.load(`/?page=${page}`);
    };

    const isLoadingMore = fetcher.state === "loading";

    return (
        <>
            <header className="hero-section py-5 bg-light">
                <Container className="alert-container mb-4">
                    {authMessage && (
                        <Alert
                            variant={authMessageVariant ?? "success"}
                            dismissible
                            onClose={startAuthMessageClose}
                            className={`shadow-sm auth-message-alert ${isAuthMessageClosing ? "auth-message-alert-closing" : ""}`}
                        >
                            {authMessage}
                        </Alert>
                    )}
                </Container>
                <Container className="py-5">
                    <Row className="align-items-center g-5">
                        <Col lg={6}>
                            <Badge bg="primary" className="text-white mb-3 px-3 py-2">
                                🌱 Sostenible y confiable
                            </Badge>
                            <h1 className="display-4 fw-bold text-dark mb-3">Dale una segunda vida a lo que amas.</h1>
                            <p className="lead text-muted mb-4">
                                Productos nuevos, reacondicionados y usados con la mejor garantía del mercado.
                            </p>
                            <div className="mt-4 d-flex gap-3 flex-wrap">
                                {currentUser ?
                                    <Button as={Link as any} to="/product_search" variant="primary" size="lg" className="px-5 shadow">
                                        Explorar Ahora
                                    </Button>
                                    :
                                    <Button as={Link as any} to="/login" variant="primary" size="lg" className="px-5 shadow">
                                        Iniciar sesión
                                    </Button>
                                }
                                {currentUser ?
                                    <Button as={Link as any} to="/product-publish" variant="outline-primary" size="lg" className="px-5">
                                        Vender productos
                                    </Button>
                                    :
                                    <Button as={Link as any} to="/signup" variant="outline-primary" size="lg" className="px-5">
                                        Registrarse
                                    </Button>
                                }

                            </div>
                        </Col>
                        <Col lg={6} className="d-none d-lg-block">
                            <img
                                src={`${import.meta.env.BASE_URL}assets/Logo_Remarket.png`}
                                className="img-fluid hero-image"
                                alt="ReMarket+"
                            />
                        </Col>
                    </Row>
                </Container>
            </header>
            <div className="bg-white pt-5">
                <div className="text-center">
                    <Badge bg="primary" className="bg-opacity-10 text-primary mb-2 px-3 py-2 fs-1">
                        ⭐ Destacados
                    </Badge>
                    <h2 className="fw-bold display-8 mb-2">Recomendaciones</h2>
                    <p className="text-muted">Creemos que estos productos te encantarán</p>
                </div>

                <ProductList products={products} />
            </div>

            <section className="bg-white pb-5">
                <Container>
                    <div className="text-center pt-4 mb-2">

                        {hasMore && (
                            <Button
                                id="load-more-btn"
                                variant="primary"
                                className="rounded-pill px-4 py-2 shadow-sm"
                                onClick={handleLoadMore}
                                disabled={isLoadingMore}
                            >
                                {isLoadingMore ? (
                                    <>
                                        <span className="spinner-border spinner-border-sm me-2" role="status" aria-hidden="true"></span>
                                        Cargando...
                                    </>
                                ) : (
                                    <>
                                        <i className="bi bi-arrow-down-circle me-2" /> Mostrar más destacados
                                    </>
                                )}
                            </Button>
                        )}
                    </div>

                    <div className="text-center mt-5">
                        <Button as={Link as any} to="/product_search" variant="outline-primary" size="lg" className="px-5 rounded-pill">
                            Ver todos los productos →
                        </Button>
                    </div>
                </Container>
            </section>
        </>
    );
}