import { Foot } from "../../components/foot";
import { ProfileNavbar } from "../../components/Profile/profile_navbar";
import { useActionState, useEffect, useState } from "react";
import { useNavigate } from "react-router";
import { AuthError } from "~/components/auth-error";
import { updateUser, uploadUserImage } from "~/services/user-service";
import { PasswordChangeForm } from "~/components/ProfilEdit/PasswordChangeForm";
import { requireUserLoader } from "../auth-loaders";
import type { Route } from "../+types";
import type { UserDTO } from "~/dtos/UserDTO";

export async function clientLoader() {
    return await requireUserLoader();
}

export default function ProfileEdit({ loaderData }: Route.ComponentProps) {
    const baseUrl = import.meta.env.BASE_URL;

    const navigate = useNavigate();
    const [{ errMessage }, formAction, editFormLoading] = useActionState(
        handleEditForm,
        { errMessage: null }
    );

    const baseImageUrl = "/api/v1/images";
    const currentUser = loaderData as unknown as UserDTO | null;

    //Edit form logic

    async function handleEditForm(prevState: { errMessage: string | null }, formData: FormData) {
        if (!currentUser) {
            return { errMessage: "Debes iniciar sesion para editar tu perfil." };
        }

        let editError: string | null = null;

        try {
            await updateUser({
                userName: formData.get("userName") as string,
                surname: formData.get("surname") as string,
                address: formData.get("address") as string,
                email: formData.get("email") as string,
            }, currentUser.id);

            const image = formData.get("imageFile") as File | null;

            if (image && image.size > 0) {
                await uploadUserImage(image, currentUser.id);
            }
        }
        catch (err) {
            editError = err instanceof Error
                ? err.message.split(":")[1]?.trim()
                : "Algunos de los datos enviados no son correctos. Intentalo de nuevo";
            console.log("Error recibido: " + editError);
        }

        if (!editError) {
            navigate("/profile");
        }

        return { errMessage: editError };
    }

    if (!currentUser) {
        return (<>
            <ProfileNavbar />
            <AuthError />
            <Foot />
        </>);
    }

    return (
        <>
            <ProfileNavbar />

            <div className="container my-3 mb-3">
                <div className="row g-3 align-items-start">
                    <div className="col-lg-8">
                        <div className="card border-0 shadow-sm p-3 h-100">
                            <div className="d-flex justify-content-between align-items-center mb-3">
                                <h5 className="fw-bold mb-0 text-dark">Información Personal</h5>
                            </div>

                            <form action={formAction}>
                                <div className="row g-2">
                                    <div className="col-12 text-center mb-2">
                                        <img
                                            src={currentUser.imageId ? `${baseImageUrl}/${currentUser.imageId}/media` : `https://ui-avatars.com/api/?name=${encodeURIComponent(currentUser.userName)}&background=random`}
                                            onError={(event) => {
                                                event.currentTarget.src = `https://ui-avatars.com/api/?name=${encodeURIComponent(currentUser.userName)}&background=random`;
                                            }}
                                            className="rounded-circle shadow-sm"
                                            alt="Foto de perfil"
                                            style={{ width: 120, height: 120, objectFit: "cover" }}
                                        />

                                        <div className="mt-2">
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

                                    <div className="col-12 text-end mt-3">
                                        <button
                                            type="submit"
                                            className="btn btn-primary d-inline-flex align-items-center justify-content-center gap-2"
                                            disabled={editFormLoading}
                                        >
                                            {editFormLoading ? (
                                                <span className="signup-loading-spinner" role="status" aria-label="Actualizando" />
                                            ) : (
                                                <span>Guardar Cambios</span>
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
                                </div>
                            </form>
                        </div>
                    </div>

                    <div className="col-lg-4">
                        <PasswordChangeForm userId={currentUser.id} />
                        <div className="d-grid mt-3">
                            <a href={`${baseUrl}profile`} className="btn btn-secondary w-100">
                                Cancelar y volver al perfil
                            </a>
                        </div>
                    </div>

                </div>
            </div>
            <Foot />
        </>
    );
}