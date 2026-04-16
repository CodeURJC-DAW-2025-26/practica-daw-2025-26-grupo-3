import { useUserState } from "~/stores/user-store";
import { Foot } from "../../components/foot";
import { ProfileNavbar } from "../../components/profile_navbar";
import { useActionState, useEffect, useState } from "react";
import { useNavigate } from "react-router";
import { Spinner } from "~/components/spinner";
import { AuthError } from "~/components/auth-error";
import { updateUser, uploadUserImage } from "~/services/user-service";

export default function ProfileEdit() {
    const baseUrl = import.meta.env.BASE_URL;

    //Necessary Hooks
    const { loadLoggedUser, currentUser } = useUserState();
    const [loading, setLoading] = useState(true);
    const navigate = useNavigate();
    const displayName = currentUser?.userName ?? "Usuario";
    const [{ errMessage }, formAction, editFormLoading] = useActionState(
        handleEditForm,
        { errMessage: null }
    );

    //Current user loading logic 
    const base_image_url = "/api/v1/images";

    async function loadUser() {
        setLoading(true);

        try {
            await loadLoggedUser();
        } catch (error) {
            console.error(error);
        } finally {
            setLoading(false);
        }
    }

    useEffect(() => {
        loadUser();
    }, []);

    if (loading) {
        return (
            <Spinner />
        );
    }

    if (!currentUser) {
        return (
            <>
                <ProfileNavbar />
                <AuthError />
                <Foot />
            </>
        );
    }

    //Edit form logic

    async function handleEditForm(prevState: { errMessage: string | null }, formData: FormData) {
        const user = currentUser;
        if (!user) {
            return { errMessage: "Debes iniciar sesion para editar tu perfil." };
        }

        let editError: string | null = null;

        try {
            await updateUser({
                userName: formData.get("userName") as string,
                surname: formData.get("surname") as string,
                address: formData.get("address") as string,
                email: formData.get("email") as string,
            }, user.id);

            const image = formData.get("imageFile") as File | null;

            if (image && image.size > 0) {
                await uploadUserImage(image, user.id);
            }

            await loadLoggedUser();
        }
        catch (err) {
            editError = err instanceof Error
                ? err.message
                : "Algunos de los datos enviados no son correctos. Intentalo de nuevo";
            console.log("Error recibido: " + editError);
        }

        if (!editError) {
            navigate("/profile");
        }

        return { errMessage: editError };
    }

    return (
        <>
            <ProfileNavbar />

            <div className="container my-5">
                <div className="row justify-content-center">
                    <div className="col-md-8">
                        <div className="card border-0 shadow-sm p-4 mb-4">
                            <div className="d-flex justify-content-between align-items-center mb-4">
                                <h5 className="fw-bold mb-0 text-dark">Información Personal</h5>
                            </div>

                            <form action={formAction}>
                                <div className="row g-3">
                                    <div className="col-12 text-center mb-3">
                                        <img
                                            src={currentUser.imageId ? `${base_image_url}/${currentUser.imageId}/media` : `https://ui-avatars.com/api/?name=${encodeURIComponent(displayName)}&background=random`}
                                            onError={(event) => {
                                                event.currentTarget.src = `https://ui-avatars.com/api/?name=${encodeURIComponent(displayName)}&background=random`;
                                            }}
                                            className="rounded-circle shadow-sm"
                                            alt="Foto de perfil"
                                            style={{ width: 150, height: 150, objectFit: "cover" }}
                                        />

                                        <div className="mt-3">
                                            <label className="form-label text-muted small fw-bold text-uppercase d-block">Cambiar Foto</label>
                                            <input type="file" className="form-control form-control-sm w-50 mx-auto" name="imageFile" accept="image/*" />
                                        </div>
                                    </div>

                                    <div className="col-md-6">
                                        <label className="form-label text-muted small fw-bold text-uppercase">Nombre</label>
                                        <input
                                            type="text"
                                            className="form-control bg-light border-0"
                                            name="userName"
                                            defaultValue={currentUser.userName}
                                            maxLength={8}
                                            required
                                        />
                                    </div>

                                    <div className="col-md-6">
                                        <label className="form-label text-muted small fw-bold text-uppercase">Apellidos</label>
                                        <input
                                            type="text"
                                            className="form-control bg-light border-0"
                                            name="surname"
                                            defaultValue={currentUser.surname}
                                            maxLength={30}
                                            required
                                        />
                                    </div>

                                    <div className="col-md-6">
                                        <label className="form-label text-muted small fw-bold text-uppercase">Correo Electrónico</label>
                                        <input type="email" className="form-control bg-light border-0" name="email" defaultValue={currentUser.email} required />
                                    </div>

                                    <div className="col-md-6">
                                        <label className="form-label text-muted small fw-bold text-uppercase">Dirección</label>
                                        <input
                                            type="text"
                                            className="form-control bg-light border-0"
                                            name="address"
                                            defaultValue={currentUser.address}
                                            maxLength={30}
                                            required
                                        />
                                    </div>
                                    <div className="col-12 text-end mt-4">
                                        <button type="submit" className="btn btn-primary" disabled={editFormLoading}>
                                            {editFormLoading ? (
                                                <span className="signup-loading-spinner" role="status" aria-label="Actualizando" />
                                            ) : (
                                                <span>Guardar Cambios</span>
                                            )}
                                        </button>
                                    </div>

                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>

            <div className="container mb-5">
                <div className="row justify-content-center">
                    <div className="col-md-8">
                        <div className="card border-0 shadow-sm p-4 mb-4">
                            <div className="d-flex justify-content-between align-items-center mb-4">
                                <h5 className="fw-bold mb-0 text-dark">Cambiar Contraseña</h5>
                            </div>

                            <form>
                                <div className="row g-3">
                                    <div className="col-md-6">
                                        <label className="form-label text-muted small fw-bold text-uppercase">Contraseña Actual</label>
                                        <input type="password" className="form-control bg-light border-0" name="oldPassword" minLength={8} maxLength={20} required />
                                    </div>

                                    <div className="col-md-6">
                                        <label className="form-label text-muted small fw-bold text-uppercase">Nueva Contraseña</label>
                                        <input type="password" className="form-control bg-light border-0" name="newPassword" minLength={8} maxLength={20} required />
                                    </div>

                                    <div className="col-12 text-end mt-4">
                                        <button type="button" className="btn btn-primary">
                                            Cambiar Contraseña
                                        </button>
                                    </div>
                                </div>
                            </form>
                        </div>

                        <div className="d-grid gap-2">
                            <a href={`${baseUrl}profile`} className="btn btn-secondary btn-lg">
                                Cancelar
                            </a>
                        </div>
                    </div>
                </div>
            </div>


            <Foot />
        </>
    );
}