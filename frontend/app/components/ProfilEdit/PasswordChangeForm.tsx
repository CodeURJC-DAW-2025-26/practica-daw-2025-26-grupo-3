export function PasswordChangeForm() {
    return (
        <div className="card border-0 shadow-sm p-4">
            <div className="d-flex justify-content-between align-items-center mb-4">
                <h5 className="fw-bold mb-0 text-dark">Cambiar Contraseña</h5>
            </div>

            <form>
                <div className="row g-3">
                    <div className="col-12">
                        <label className="form-label text-muted small fw-bold text-uppercase">Contraseña Actual</label>
                        <input type="password" className="form-control bg-light border-0" name="oldPassword" minLength={8} maxLength={20} required />
                    </div>

                    <div className="col-12">
                        <label className="form-label text-muted small fw-bold text-uppercase">Nueva Contraseña</label>
                        <input type="password" className="form-control bg-light border-0" name="newPassword" minLength={8} maxLength={20} required />
                    </div>

                    <div className="col-12 text-end mt-4">
                        <button type="button" className="btn btn-primary w-100">
                            Cambiar Contraseña
                        </button>
                    </div>
                </div>
            </form>
        </div>
    );
}