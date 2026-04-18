export function DeleteAccountForm() {
    return (
        <div className="card border-0 shadow-sm p-4 mt-4">
            <h4 className="fw-bold mb-4 text-danger">Borrar Cuenta</h4>
            <div className="alert alert-warning">
                <i className="bi bi-exclamation-triangle-fill me-2" />
                Advertencia: Esta accion es irreversible. Todos tus datos seran eliminados permanentemente.
            </div>
            <form>
                <div className="mb-3">
                    <label htmlFor="deletePassword" className="form-label fw-bold">
                        Introduce tu contrasena
                    </label>
                    <input type="password" className="form-control" id="deletePassword" name="currentPassword" required />
                </div>

                <div className="mb-3 form-check">
                    <input type="checkbox" className="form-check-input" id="confirmDelete" name="confirmDelete" required />
                    <label className="form-check-label" htmlFor="confirmDelete">
                        Confirmo que quiero borrar mi cuenta permanentemente
                    </label>
                </div>

                <div className="d-grid">
                    <button type="button" className="btn btn-danger fw-bold">
                        Borrar Cuenta
                    </button>
                </div>
            </form>
        </div>
    );
}