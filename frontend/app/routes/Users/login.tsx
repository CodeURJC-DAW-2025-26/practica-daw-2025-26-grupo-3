import { useUserState } from "~/stores/user-store";
import type { SubmitEvent } from "react";
import { useState } from "react";
import { Link, useNavigate } from "react-router";
import { Container, Row, Col, Card, Alert, Form, Button } from "react-bootstrap";

export default function Login() {
    const navigate = useNavigate();

    const { login, error } = useUserState();
    const [submitting, setSubmitting] = useState(false);

    async function loginFormHandler(event: SubmitEvent) {
        event.preventDefault();

        if (submitting) {
            return;
        }

        setSubmitting(true);

        const form = event.currentTarget as HTMLFormElement;
        const formData = new FormData(form);
        const email = (formData.get("email") as string)?.trim();
        const pass = formData.get("password") as string;

        try {
            await login(email, pass);
            form.reset();
            navigate("/");
        } finally {
            setSubmitting(false);
        }
    }

    return (
        <Container>
            <Row className="min-vh-100 align-items-center justify-content-center">
                <Col md={5}>
                    <div className="text-center mb-4">
                        <Link to="/" className="d-inline-block">
                            <img
                                src={`${import.meta.env.BASE_URL}assets/Logo_Remarket.png`}
                                alt="ReMarket+ Logo"
                                className="img-fluid"
                                style={{ maxWidth: 120 }}
                            />
                        </Link>
                    </div>

                    <Card className="border-0 shadow-sm p-4 p-md-5">
                        <div className="d-flex justify-content-between align-items-start mb-4">
                            <div>
                                <h1 className="h4 fw-bold mb-1">Bienvenido</h1>
                                <p className="text-muted small mb-0">Accede a tu cuenta</p>
                            </div>
                        </div>

                        {error && (
                            <Alert variant="danger" className="py-2">
                                {error}
                            </Alert>
                        )}

                        <Form onSubmit={loginFormHandler}>
                            <Form.Group className="mb-3">
                                <Form.Label className="small fw-bold">Email</Form.Label>
                                <Form.Control type="email" placeholder="Tu email" name="email" required />
                            </Form.Group>
                            <Form.Group className="mb-3">
                                <div className="d-flex justify-content-between">
                                    <Form.Label className="small fw-bold">Contraseña</Form.Label>
                                    <a href="#" className="small text-decoration-none" style={{ fontSize: "0.75rem" }}>
                                        ¿La olvidaste?
                                    </a>
                                </div>
                                <Form.Control type="password" placeholder="Tu contraseña" name="password" required />
                            </Form.Group>

                            <div className="mt-4">
                                <Button
                                    variant="primary"
                                    type="submit"
                                    className="w-100 py-2 fw-bold shadow-sm d-inline-flex align-items-center justify-content-center"
                                    disabled={submitting}
                                >
                                    {submitting ? (
                                        <span className="signup-loading-spinner" role="status" aria-label="Entrando" />
                                    ) : (
                                        "Entrar"
                                    )}
                                </Button>
                            </div>
                        </Form>

                        <div className="mt-4">
                            <Link to="/" className="btn btn-danger w-100 py-2 fw-bold shadow-sm" title="Volver a la tienda">
                                Volver a la tienda
                            </Link>
                        </div>

                        <div className="text-center mt-4 pt-2 border-top">
                            <p className="small text-muted mb-0">
                                ¿No tienes cuenta?{" "}
                                <Link to="/signup" className="fw-bold text-primary text-decoration-none">
                                    Regístrate gratis
                                </Link>
                            </p>
                        </div>
                    </Card>
                </Col>
            </Row>
        </Container>
    );
}