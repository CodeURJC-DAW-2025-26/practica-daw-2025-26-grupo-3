import { useActionState } from "react";
import { useNavigate } from "react-router";
import { useUserState } from "~/stores/user-store";

export interface PasswordChangeFormProps {
    userId: number;
}

async function changePassHandler(prevState: { errMessage: string | null, succMessage: string | null },
    formData: FormData) {

    let error: string | null = null;
    let succ: string | null = null;

    try {
        const { editPass } = useUserState.getState();
        await editPass({
            currentPass: formData.get("oldPassword") as string,
            newPass: formData.get("newPassword") as string
        }, Number(formData.get("userId")));
        succ = "¡La contraseña se ha cambiado con éxito!";
    }
    catch (err) {
        error = err instanceof Error
            ? err.message.split(":")[1]
            : "Algunos de los datos enviados no son correctos, inténtalo de nuevo";
    }
    return { errMessage: error, succMessage: succ };
}

export function PasswordChangeForm({ userId }: PasswordChangeFormProps) {
    const [{ errMessage, succMessage }, formAction, loading] = useActionState(
        changePassHandler,
        { errMessage: null, succMessage: null }
    );

    const navigate = useNavigate();

    return (
        <div className="card border-0 shadow-sm p-4">
            <div className="d-flex justify-content-between align-items-center mb-4">
                <h5 className="fw-bold mb-0 text-dark">Cambiar Contraseña</h5>
            </div>

            <form action={formAction}>
                <input type="hidden" value={userId} name="userId" />
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
                        <button type="submit" className="btn btn-primary w-100" disabled={loading}>
                            {loading ? (
                                <span className="signup-loading-spinner" role="status" aria-label="Actualizando" />
                            ) : (
                                <span>Cambiar contraseña</span>
                            )}
                        </button>
                    </div>

                    {errMessage && (
                        <div className="col-12">
                            <div className="alert alert-danger" role="alert">
                                {errMessage}
                            </div>
                        </div>
                    )}

                    {succMessage && (
                        <div className="col-12">
                            <div className="alert alert-success" role="alert">
                                {succMessage}
                            </div>
                        </div>
                    )}

                </div>
            </form>
        </div>
    );
}