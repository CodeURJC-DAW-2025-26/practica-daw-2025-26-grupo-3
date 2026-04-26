import { Foot } from "../../components/foot";
import { ProfileNavbar } from "../../components/Profile/profile_navbar";
import { useActionState } from "react";
import { Link, useNavigate } from "react-router";
import { ErrorCard } from "~/components/error-card";
import { updateUser, uploadUserImage } from "~/services/user-service";
import { PasswordChangeForm } from "~/components/ProfileEdit/PasswordChangeForm";
import { requireUserLoader } from "../auth-loaders";
import type { Route } from "../+types";
import type { UserDTO } from "~/dtos/UserDTO";
import { useUserState } from "~/stores/user-store";
import { Container, Row, Col, Card, Alert, Form, Button } from "react-bootstrap";

export async function clientLoader() {
    return await requireUserLoader();
}

export default function ProfileEdit({ loaderData }: Route.ComponentProps) {
    const navigate = useNavigate();
    const { loadLoggedUser } = useUserState();
    const [{ errMessage }, formAction, editFormLoading] = useActionState(
        handleEditForm,
        { errMessage: null }
    );

    const baseImageUrl = "/api/v1/images";
    const currentUser = loaderData as unknown as UserDTO | null;

    //Edit form logic

    async function handleEditForm(prevState: { errMessage: string | null }, formData: FormData) {
        if (!currentUser) {
            return { errMessage: "Debes iniciar sesión para editar la información de tu perfil." };
        }

        let editError: string | null = null;

        try {
            await updateUser({
                userName: formData.get("userName") as string,
                surname: formData.get("surname") as string,
                address: formData.get("address") as string,
                email: formData.get("email") as string,
            }, currentUser.id);

            const image = formData.get("imageFile") as File | null;

            if (image && image.size > 0) {
                await uploadUserImage(image, currentUser.id);
            }

            await loadLoggedUser();
        }
        catch (err) {
            editError = err instanceof Error
                ? err.message.split(":")[1]?.trim()
                : "Algunos de los datos enviados no son correctos. Intentalo de nuevo";
        }

        if (!editError) {
            navigate("/profile");
        }

        return { errMessage: editError };
    }

    if (!currentUser) {
        return (
            <div className="d-flex flex-column min-vh-100">
                <ProfileNavbar />
                <div className="flex-grow-1 d-flex flex-column justify-content-center">
                    <ErrorCard message="Debes iniciar sesión para entrar en la edición de tu perfil." className="container my-5" />
                </div>
                <Foot />
            </div>
        );
    }

    return (
        <div className="d-flex flex-column min-vh-100">
            <ProfileNavbar />

            <Container className="my-3 mb-3 flex-grow-1">
                <Row className="g-3 align-items-start">
                    <Col lg={8}>
                        <Card className="border-0 shadow-sm p-3 h-100">
                            <div className="d-flex justify-content-between align-items-center mb-3">
                                <h5 className="fw-bold mb-0 text-dark">Información Personal</h5>
                            </div>

                            <Form action={formAction as unknown as string}>
                                <Row className="g-2">
                                    <Col xs={12} className="text-center mb-2">
                                        <img
                                            src={currentUser.imageId ? `${baseImageUrl}/${currentUser.imageId}/media` : `https://ui-avatars.com/api/?name=${encodeURIComponent(currentUser.userName)}&background=random`}
                                            onError={(event) => {
                                                event.currentTarget.src = `https://ui-avatars.com/api/?name=${encodeURIComponent(currentUser.userName)}&background=random`;
                                            }}
                                            className="rounded-circle shadow-sm"
                                            alt="Foto de perfil"
                                            style={{ width: 120, height: 120, objectFit: "cover" }}
                                        />

                                        <div className="mt-2">
                                            <Form.Label className="text-muted small fw-bold text-uppercase d-block">Cambiar Foto</Form.Label>
                                            <Form.Control type="file" size="sm" className="w-50 mx-auto" name="imageFile" accept="image/*" />
                                        </div>
                                    </Col>

                                    <Col md={6}>
                                        <Form.Label className="text-muted small fw-bold text-uppercase">Nombre</Form.Label>
                                        <Form.Control
                                            type="text"
                                            className="bg-light border-0"
                                            name="userName"
                                            defaultValue={currentUser.userName}
                                            maxLength={8}
                                            required
                                        />
                                    </Col>

                                    <Col md={6}>
                                        <Form.Label className="text-muted small fw-bold text-uppercase">Apellidos</Form.Label>
                                        <Form.Control
                                            type="text"
                                            className="bg-light border-0"
                                            name="surname"
                                            defaultValue={currentUser.surname}
                                            maxLength={30}
                                            required
                                        />
                                    </Col>

                                    <Col md={6}>
                                        <Form.Label className="text-muted small fw-bold text-uppercase">Correo Electrónico</Form.Label>
                                        <Form.Control type="email" className="bg-light border-0" name="email" defaultValue={currentUser.email} required />
                                    </Col>

                                    <Col md={6}>
                                        <Form.Label className="text-muted small fw-bold text-uppercase">Dirección</Form.Label>
                                        <Form.Control
                                            type="text"
                                            className="bg-light border-0"
                                            name="address"
                                            defaultValue={currentUser.address}
                                            maxLength={30}
                                            required
                                        />
                                    </Col>

                                    <Col xs={12} className="text-end mt-3">
                                        <Button
                                            variant="primary"
                                            type="submit"
                                            className="d-inline-flex align-items-center justify-content-center gap-2"
                                            disabled={editFormLoading}
                                        >
                                            {editFormLoading ? (
                                                <span className="signup-loading-spinner" role="status" aria-label="Actualizando" />
                                            ) : (
                                                <span>Guardar Cambios</span>
                                            )}
                                        </Button>
                                    </Col>
                                    {errMessage && (
                                        <Col xs={12}>
                                            <Alert variant="danger">
                                                {errMessage}
                                            </Alert>
                                        </Col>
                                    )}
                                </Row>
                            </Form>
                        </Card>
                    </Col>

                    <Col lg={4}>
                        <PasswordChangeForm userId={currentUser.id} />
                        <div className="d-grid mt-3">
                            <Link to="/profile" className="btn btn-secondary w-100">
                                Cancelar y volver al perfil
                            </Link>
                        </div>
                    </Col>

                </Row>
            </Container>
            <Foot />
        </div>
    );
}