import { Outlet } from "react-router";
import { Container, Row, Col, Alert, Badge, Button } from "react-bootstrap";
import { useUserState } from "~/stores/user-store";
import { useEffect, useState } from "react";
import { Spinner } from "~/components/spinner";

export default function Index() {
    const baseUrl = import.meta.env.BASE_URL;

    const { loadLoggedUser, currentUser } = useUserState();

    const [loading, setLoading] = useState(true);

    useEffect(() => {
        async function loadUser() {
            setLoading(true);

            try {
                await loadLoggedUser();
            }
            catch (error) {
                console.error(error);
            }
            finally {
                setLoading(false);
            }
        }

        loadUser();
    }, []);

    if (loading) {
        return (
            <Spinner />
        );
    }

    return (
        <>
            <header className="hero-section py-5 bg-light">
                <Container className="alert-container mb-4">
                    <Alert variant="primary" dismissible>
                        PROTOTIPO FRONTEND REACT. Los datos reales llegarán desde la API más adelante.
                    </Alert>
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
                                    <Button href={`${baseUrl}product_search`} variant="primary" size="lg" className="px-5 shadow">
                                        Explorar Ahora
                                    </Button>
                                    :
                                    <Button href={`${baseUrl}login`} variant="primary" size="lg" className="px-5 shadow">
                                        Iniciar sesión
                                    </Button>
                                }
                                {currentUser ?
                                    <Button href={`${baseUrl}signup`} variant="outline-primary" size="lg" className="px-5">
                                        Vender productos
                                    </Button>
                                    :
                                    <Button href={`${baseUrl}signup`} variant="outline-primary" size="lg" className="px-5">
                                        Registrarse
                                    </Button>
                                }


                            </div>
                        </Col>
                        <Col lg={6} className="d-none d-lg-block">
                            <img src={`${baseUrl}assets/Logo_Remarket.png`} className="img-fluid hero-image" alt="ReMarket+" />
                        </Col>
                    </Row>
                </Container>
            </header>

            <Outlet />

            <section className="bg-white pb-5">
                <Container>
                    <div className="text-center pt-4 mb-2">
                        <Button id="load-more-btn" variant="primary" className="rounded-pill px-4 py-2 shadow-sm">
                            <i className="bi bi-arrow-down-circle me-2" /> Mostrar más destacados
                        </Button>
                    </div>

                    <div className="text-center mt-5">
                        <Button href={`${baseUrl}product_search`} variant="outline-primary" size="lg" className="px-5 rounded-pill">
                            Ver todos los productos →
                        </Button>
                    </div>
                </Container>
            </section>
        </>
    );
}