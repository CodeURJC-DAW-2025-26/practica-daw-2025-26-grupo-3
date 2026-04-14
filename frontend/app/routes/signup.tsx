export default function Signup() {
    const baseUrl = import.meta.env.BASE_URL;

    return (
        <div className="container-fluid signup-container">
            <div className="row g-0">
                <div className="card card-registration border-0 shadow-sm overflow-hidden">
                    <div className="row g-0">
                        <div className="col-md-4 logo-side d-none d-md-flex">
                            <div className="text-center p-4">
                                <img
                                    src={`${baseUrl}assets/Logo_Remarket.png`}
                                    alt="ReMarket+ Logo"
                                    className="img-fluid"
                                    style={{ maxWidth: 180 }}
                                />
                            </div>
                        </div>

                        <div className="col-md-8 bg-white p-4 p-md-5">
                            <div className="d-flex justify-content-between align-items-center mb-4">
                                <h1 className="h4 fw-bold mb-0">Crear Cuenta</h1>
                                <p className="text-dark small mt-3">Únete a nuestra comunidad</p>
                            </div>

                            <form encType="multipart/form-data">
                                <input type="hidden" name="_csrf" value="" />
                                <div className="row g-3">
                                    <div className="col-md-6">
                                        <label className="form-label small fw-bold">
                                            Nombre <span className="mandatory">*</span>
                                        </label>
                                        <input
                                            type="text"
                                            className="form-control form-control-sm"
                                            placeholder="Tu nombre"
                                            name="userName"
                                            maxLength={8}
                                            required
                                        />
                                    </div>
                                    <div className="col-md-6">
                                        <label className="form-label small fw-bold">
                                            Apellidos <span className="mandatory">*</span>
                                        </label>
                                        <input
                                            type="text"
                                            className="form-control form-control-sm"
                                            placeholder="Tus apellidos"
                                            name="surname"
                                            maxLength={30}
                                            required
                                        />
                                    </div>

                                    <div className="col-md-12">
                                        <label className="form-label small fw-bold">
                                            Dirección de envío <span className="mandatory">*</span>
                                        </label>
                                        <input
                                            type="text"
                                            className="form-control form-control-sm"
                                            placeholder="Calle, número, ciudad..."
                                            name="address"
                                            maxLength={30}
                                            required
                                        />
                                    </div>

                                    <div className="col-md-6">
                                        <label className="form-label small fw-bold">
                                            Correo electrónico <span className="mandatory">*</span>
                                        </label>
                                        <input
                                            type="email"
                                            className="form-control form-control-sm"
                                            placeholder="email@ejemplo.com"
                                            name="email"
                                            required
                                        />
                                    </div>
                                    <div className="col-md-6">
                                        <label className="form-label small fw-bold">
                                            Contraseña <span className="mandatory">*</span>
                                        </label>
                                        <input
                                            type="password"
                                            className="form-control form-control-sm"
                                            placeholder="Crea una contraseña"
                                            name="password"
                                            minLength={8}
                                            maxLength={20}
                                            required
                                        />
                                    </div>
                                    <div className="col-12">
                                        <label className="form-label small fw-bold">Imagen de perfil</label>
                                        <input type="file" className="form-control form-control-sm" name="imageFile" accept="image/*" />
                                    </div>
                                </div>

                                <div className="mt-3 d-flex justify-content-between align-items-center">
                                    <p className="mandatory mb-0">(*) Campo obligatorio</p>
                                    <p className="small text-muted mb-0">
                                        ¿Ya tienes cuenta?{" "}
                                        <a href={`${baseUrl}login`} className="fw-bold text-decoration-none text-primary">
                                            Inicia sesión
                                        </a>
                                    </p>
                                </div>

                                <div className="mt-4">
                                    <button type="submit" className="btn btn-primary w-100 py-2 fw-bold shadow-sm">
                                        Registrarme
                                    </button>
                                </div>
                            </form>

                            <div className="mt-4">
                                <a href={baseUrl} className="btn btn-danger w-100 py-2 fw-bold shadow-sm" title="Volver a la tienda">
                                    Volver a la tienda
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}