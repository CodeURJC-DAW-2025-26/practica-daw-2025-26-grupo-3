import { useUserState } from "~/stores/user-store";
import type { SubmitEvent } from "react";
import { useState } from "react";
import { Link, useNavigate } from "react-router";

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
        <div className="container">
            <div className="row min-vh-100 align-items-center justify-content-center">
                <div className="col-md-5">
                    <div className="text-center mb-4">
                        <Link to="/" className="d-inline-block">
                            <img
                                src="/assets/Logo_Remarket.png"
                                alt="ReMarket+ Logo"
                                className="img-fluid"
                                style={{ maxWidth: 120 }}
                            />
                        </Link>
                    </div>

                    <div className="card border-0 shadow-sm p-4 p-md-5">
                        <div className="d-flex justify-content-between align-items-start mb-4">
                            <div>
                                <h1 className="h4 fw-bold mb-1">Bienvenido</h1>
                                <p className="text-muted small mb-0">Accede a tu cuenta</p>
                            </div>
                        </div>

                        {error && (
                            <div className="alert alert-danger py-2" role="alert">
                                {error}
                            </div>
                        )}

                        <form onSubmit={loginFormHandler}>
                            <div className="mb-3">
                                <label className="form-label small fw-bold">Email</label>
                                <input type="email" className="form-control" placeholder="Tu email" name="email" required />
                            </div>
                            <div className="mb-3">
                                <div className="d-flex justify-content-between">
                                    <label className="form-label small fw-bold">Contraseña</label>
                                    <a href="#" className="small text-decoration-none" style={{ fontSize: "0.75rem" }}>
                                        ¿La olvidaste?
                                    </a>
                                </div>
                                <input type="password" className="form-control" placeholder="Tu contraseña" name="password" required />
                            </div>

                            <div className="mt-4">
                                <button
                                    type="submit"
                                    className="btn btn-primary w-100 py-2 fw-bold shadow-sm d-inline-flex align-items-center justify-content-center"
                                    disabled={submitting}
                                >
                                    {submitting ? (
                                        <span className="signup-loading-spinner" role="status" aria-label="Entrando" />
                                    ) : (
                                        "Entrar"
                                    )}
                                </button>
                            </div>
                        </form>

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
                    </div>
                </div>
            </div>
        </div>
    );
}