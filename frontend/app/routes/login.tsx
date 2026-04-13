export default function Login() {
    return (
        <div className="container">
            <div className="row min-vh-100 align-items-center justify-content-center">
                <div className="col-md-5">
                    <div className="text-center mb-4">
                        <a href="/new" className="d-inline-block">
                            <img
                                src="/new/assets/Logo_Remarket.png"
                                alt="ReMarket+ Logo"
                                className="img-fluid"
                                style={{ maxWidth: 120 }}
                            />
                        </a>
                    </div>

                    <div className="card border-0 shadow-sm p-4 p-md-5">
                        <div className="d-flex justify-content-between align-items-start mb-4">
                            <div>
                                <h1 className="h4 fw-bold mb-1">Bienvenido</h1>
                                <p className="text-muted small mb-0">Accede a tu cuenta</p>
                            </div>
                        </div>

                        <form>
                            <input type="hidden" name="_csrf" value="" />
                            <div className="mb-3">
                                <label className="form-label small fw-bold">Email</label>
                                <input type="email" className="form-control" placeholder="Tu email" name="email" />
                            </div>
                            <div className="mb-3">
                                <div className="d-flex justify-content-between">
                                    <label className="form-label small fw-bold">Contraseña</label>
                                    <a href="#" className="small text-decoration-none" style={{ fontSize: "0.75rem" }}>
                                        ¿La olvidaste?
                                    </a>
                                </div>
                                <input type="password" className="form-control" placeholder="Tu contraseña" name="password" />
                            </div>

                            <div className="mt-4">
                                <button type="submit" className="btn btn-primary w-100 py-2 fw-bold shadow-sm">
                                    Entrar
                                </button>
                            </div>
                        </form>

                        <div className="mt-4">
                            <a href="/new" className="btn btn-danger w-100 py-2 fw-bold shadow-sm" title="Volver a la tienda">
                                Volver a la tienda
                            </a>
                        </div>

                        <div className="text-center mt-4 pt-2 border-top">
                            <p className="small text-muted mb-0">
                                ¿No tienes cuenta?{" "}
                                <a href="/new/signup" className="fw-bold text-primary text-decoration-none">
                                    Regístrate gratis
                                </a>
                            </p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}