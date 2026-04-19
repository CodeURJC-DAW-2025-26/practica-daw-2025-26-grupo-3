import { useActionState } from "react";
import { useNavigate } from "react-router";
import { useUserState } from "~/stores/user-store";

export interface DeleteAccountFormProps {
    userId: number;
}

export function DeleteAccountForm({ userId }: DeleteAccountFormProps) {

    const { deleteUser } = useUserState();
    const [{ errMessage }, formAction, loading] = useActionState(
        handleDeleteForm,
        { errMessage: null }
    );
    const navigate = useNavigate();

    async function handleDeleteForm(prevState: { errMessage: string | null }, formData: FormData) {
        let error: string | null = null;
        try {
            if (formData.get("confirmDelete") as string !== "on") {
                error = "Debes marcar la casilla de verificación antes de eliminar tu cuenta."
            }
            await deleteUser({ password: formData.get("currentPassword") as string }, Number(formData.get("userId")));
        }
        catch (err) {
            error = err instanceof Error
                ? err.message.split('"')[1].trim()
                : "Se ha producido un error al enviar el formulario";
        }

        if (!error) {
            navigate("/");
        }

        return { errMessage: error }
    }

    return (
        <div className="card border-0 shadow-sm p-4 mt-4">
            <h4 className="fw-bold mb-4 text-danger">Borrar Cuenta</h4>
            <div className="alert alert-warning">
                <i className="bi bi-exclamation-triangle-fill me-2" />
                Advertencia: Esta accion es irreversible. Todos tus datos seran eliminados permanentemente.
            </div>
            <form action={formAction}>
                <input type="hidden" value={userId} name="userId" />
                <div className="mb-3">
                    <label htmlFor="deletePassword" className="form-label fw-bold">
                        Introduce tu contraseña
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
                    <button type="submit" className="btn btn-danger fw-bold">
                        Borrar Cuenta
                    </button>
                </div>
            </form>
            {errMessage && (
                <div className="col-12">
                    <br />
                    <div className="alert alert-danger" role="alert">
                        {errMessage}
                    </div>
                </div>
            )}
        </div>
    );
}