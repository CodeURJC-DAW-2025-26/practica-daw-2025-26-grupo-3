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

export async function clientLoader() {
    return await requireUserLoader(true);
}

export default function Profile({ loaderData }: Route.ComponentProps) {
    const baseImageUrl = "/api/v1/images";
    const currentUser = loaderData as unknown as UserDTO | null;
    const isAdmin = currentUser?.roles.includes("ADMIN");

    return (
        <>
            <ProfileNavbar />
            <Container className="my-5">
                <Row>
                    {currentUser && (
                        <Col md={4} lg={3} className="mb-4">
                            <LeftCard baseImageUrl={baseImageUrl} userName={currentUser.userName} imageId={currentUser.imageId} />
                            <div className="mt-4 text-center">
                                <Link to="/" className="btn btn-danger py-2 fw-bold shadow-sm" title="Volver a la tienda">
                                    Volver a la tienda
                                </Link>
                            </div>
                        </Col>
                    )}

                    <Col className={currentUser ? "col-md-8 col-lg-9" : "col-12"}>
                        <div className="d-flex justify-content-between align-items-center mb-4 border-bottom pb-2">
                            <h2 className="h3 fw-bold text-dark">Mi Perfil</h2>
                            {currentUser && (
                                <Badge pill bg={isAdmin ? "primary" : "secondary"}>
                                    {isAdmin ? "Admin" : "Usuario"}
                                </Badge>
                            )}
                        </div>

                        {!currentUser ?
                            <ErrorCard message="Debes iniciar sesion para acceder a esta página." />
                            : (
                                <>
                                    <PersonalInfo baseImageUrl={baseImageUrl} currentUser={currentUser} isAdmin={isAdmin} />
                                    <OrdersDashboard />
                                    <DeleteAccountForm userId={currentUser.id} />
                                </>
                            )}


                    </Col>
                </Row>
            </Container>
            <Foot />
        </>
    );
}