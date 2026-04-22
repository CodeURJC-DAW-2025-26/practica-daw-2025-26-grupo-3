import { useActionState } from "react";
import { Link, useNavigate } from "react-router";
import { useUserState } from "~/stores/user-store";
import { uploadUserImage } from "~/services/user-service";
import { Container, Row, Col, Card, Alert, Form, Button } from "react-bootstrap";

export default function Signup() {
    const { login, signup, error } = useUserState();
    const navigate = useNavigate();

    const [{ errMessage }, formAction, loading] = useActionState(
        handleSignup,
        { errMessage: null }
    );

    async function handleSignup(prevState: { errMessage: string | null }, formData: FormData) {
        const email = formData.get("email") as string;
        const password = formData.get("password") as string
        let frontError: string | null = null;

        try {
            await signup({
                userName: formData.get("userName") as string,
                surname: formData.get("surname") as string,
                address: formData.get("address") as string,
                email: email,
                password: password
            });
            const updatedUser = await login(email, password);

            const image: File = formData.get("imageFile") as File;

            if (image) {
                await uploadUserImage(image, updatedUser!.id!);
            }
        }
        catch (err) {
            frontError = err instanceof Error
                ? err.message?.split(":")[1]?.trim()
                : "Algunos de los datos enviados no son correctos. Intentalo de nuevo";
            console.log("Error recibido: " + frontError);
        }

        if (!frontError) {
            navigate("/");
        }

        return { errMessage: frontError }
    }

    return (
        <Container fluid className="signup-container">
            <Row className="g-0">
                <Card className="card-registration border-0 shadow-sm overflow-hidden">
                    <Row className="g-0">
                        <Col md={4} className="logo-side d-none d-md-flex">
                            <div className="text-center p-4">
                                <img
                                    src="/assets/Logo_Remarket.png"
                                    alt="ReMarket+ Logo"
                                    className="img-fluid"
                                    style={{ maxWidth: 180 }}
                                />
                            </div>
                        </Col>

                        <Col md={8} className="bg-white p-4 p-md-5">
                            <div className="d-flex justify-content-between align-items-center mb-4">
                                <h1 className="h4 fw-bold mb-0">Crear Cuenta</h1>
                                <p className="text-dark small mt-3">Únete a nuestra comunidad</p>
                            </div>


                            {errMessage && (
                                <Alert variant="danger" style={{
                                    backgroundColor: '#fee2e2',
                                    color: '#b91c1c',
                                    padding: '12px 16px',
                                    borderRadius: '8px',
                                    border: '1px solid #fca5a5',
                                    marginBottom: '20px',
                                    fontSize: '0.95rem'
                                }}>
                                    {errMessage}
                                </Alert>
                            )}

                            <Form action={formAction as unknown as string}>
                                <input type="hidden" name="_csrf" value="" />
                                <Row className="g-3">
                                    <Col md={6}>
                                        <Form.Label className="small fw-bold">
                                            Nombre <span className="mandatory">*</span>
                                        </Form.Label>
                                        <Form.Control
                                            type="text"
                                            size="sm"
                                            placeholder="Tu nombre"
                                            name="userName"
                                            maxLength={8}
                                            required
                                        />
                                    </Col>
                                    <Col md={6}>
                                        <Form.Label className="small fw-bold">
                                            Apellidos <span className="mandatory">*</span>
                                        </Form.Label>
                                        <Form.Control
                                            type="text"
                                            size="sm"
                                            placeholder="Tus apellidos"
                                            name="surname"
                                            maxLength={30}
                                            required
                                        />
                                    </Col>

                                    <Col md={12}>
                                        <Form.Label className="small fw-bold">
                                            Dirección de envío <span className="mandatory">*</span>
                                        </Form.Label>
                                        <Form.Control
                                            type="text"
                                            size="sm"
                                            placeholder="Calle, número, ciudad..."
                                            name="address"
                                            maxLength={30}
                                            required
                                        />
                                    </Col>

                                    <Col md={6}>
                                        <Form.Label className="small fw-bold">
                                            Correo electrónico <span className="mandatory">*</span>
                                        </Form.Label>
                                        <Form.Control
                                            type="email"
                                            size="sm"
                                            placeholder="email@ejemplo.com"
                                            name="email"
                                            required
                                        />
                                    </Col>
                                    <Col md={6}>
                                        <Form.Label className="small fw-bold">
                                            Contraseña <span className="mandatory">*</span>
                                        </Form.Label>
                                        <Form.Control
                                            type="password"
                                            size="sm"
                                            placeholder="Crea una contraseña"
                                            name="password"
                                            minLength={8}
                                            maxLength={20}
                                            required
                                        />
                                    </Col>
                                    <Col xs={12}>
                                        <Form.Label className="small fw-bold">Imagen de perfil</Form.Label>
                                        <Form.Control type="file" size="sm" name="imageFile" accept="image/*" />
                                    </Col>
                                </Row>

                                <div className="mt-3 d-flex justify-content-between align-items-center">
                                    <p className="mandatory mb-0">(*) Campo obligatorio</p>
                                    <p className="small text-muted mb-0">
                                        ¿Ya tienes cuenta?{" "}
                                        <Link to="/login" className="fw-bold text-decoration-none text-primary">
                                            Inicia sesión
                                        </Link>
                                    </p>
                                </div>

                                <div className="mt-4">
                                    <Button
                                        variant="primary"
                                        type="submit"
                                        className={`w-100 py-2 fw-bold shadow-sm d-flex align-items-center justify-content-center signup-submit-btn ${loading ? "signup-submit-btn-loading" : ""}`}
                                        disabled={loading}
                                    >
                                        {loading ? (
                                            <span className="signup-loading-spinner" role="status" aria-label="Registrando" />
                                        ) : (
                                            <span>Registrarme</span>
                                        )}
                                    </Button>
                                </div>
                            </Form>
                            <div className="mt-4">
                                <Link to="/" className="btn btn-danger w-100 py-2 fw-bold shadow-sm" title="Volver a la tienda">
                                    Volver a la tienda
                                </Link>
                            </div>
                        </Col>
                    </Row>
                </Card>
            </Row>
        </Container>
    );
}