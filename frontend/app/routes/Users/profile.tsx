import { Link } from "react-router";
import { Foot } from "../../components/foot";
import { ProfileNavbar } from "../../components/Profile/profile_navbar";
import { ErrorCard } from "~/components/error-card";
import { OrdersDashboard } from "~/components/Profile/OrdersDashboard";
import { DeleteAccountForm } from "../../components/Profile/DeleteAccountForm";
import { requireUserLoader } from "../auth-loaders";
import type { Route } from "../+types";
import type { UserDTO } from "~/dtos/UserDTO";
import { LeftCard } from "~/components/Profile/LeftCard";
import { PersonalInfo } from "~/components/Profile/PersonalInfo";
import { Container, Row, Col, Badge } from "react-bootstrap";
import { useCartState } from "~/stores/shoppingCart-store";

export async function clientLoader() {
    const currentUser = await requireUserLoader(true);

    //we load the orders from the user
    try {
        await useCartState.getState().getOrders();
    } catch (err) {
        console.error("Error precargando pedidos en el perfil:", err);
    }
    return currentUser;
}

export default function Profile({ loaderData }: Route.ComponentProps) {
    const baseImageUrl = "/api/v1/images";
    const currentUser = loaderData as unknown as UserDTO | null;
    const isAdmin = currentUser?.roles.includes("ADMIN");

    if (!currentUser) {
        return (
            <div className="d-flex flex-column min-vh-100">
                <ProfileNavbar />
                <div className="flex-grow-1 d-flex flex-column justify-content-center">
                    <ErrorCard message="Debes iniciar sesión para acceder a tu perfil." className="container my-5" />
                </div>
                <Foot />
            </div>
        );
    }

    return (
        <div className="d-flex flex-column min-vh-100">
            <ProfileNavbar />
            <Container className="my-5 flex-grow-1">
                <Row>
                    <Col md={4} lg={3} className="mb-4">
                        <LeftCard baseImageUrl={baseImageUrl} userName={currentUser.userName} imageId={currentUser.imageId} />
                        <div className="mt-4 text-center">
                            <Link to="/" className="btn btn-danger py-2 fw-bold shadow-sm" title="Volver a la tienda">
                                Volver a la tienda
                            </Link>
                        </div>
                    </Col>

                    <Col className="col-md-8 col-lg-9">
                        <div className="d-flex justify-content-between align-items-center mb-4 border-bottom pb-2">
                            <h2 className="h3 fw-bold text-dark">Mi Perfil</h2>
                            <Badge pill bg={isAdmin ? "primary" : "secondary"}>
                                {isAdmin ? "Admin" : "Usuario"}
                            </Badge>
                        </div>

                        <PersonalInfo baseImageUrl={baseImageUrl} currentUser={currentUser} isAdmin={isAdmin} />
                        <OrdersDashboard />
                        <DeleteAccountForm userId={currentUser.id} />

                    </Col>
                </Row>
            </Container>
            <Foot />
        </div>
    );
}