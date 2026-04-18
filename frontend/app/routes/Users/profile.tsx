import { useState } from "react";
import { useNavigate } from "react-router";
import { Foot } from "../../components/foot";
import { ProfileNavbar } from "../../components/Profile/profile_navbar";
import { useUserState } from "~/stores/user-store";
import { AuthError } from "~/components/auth-error";
import { OrdersDashboard } from "~/components/Profile/OrdersDashboard";
import { DeleteAccountForm } from "../../components/Profile/DeleteAccountForm";
import { requireUserLoader } from "../auth-loaders";
import type { Route } from "../+types";
import type { UserDTO } from "~/dtos/UserDTO";
import { LeftCard } from "~/components/Profile/LeftCard";
import { PersonalInfo } from "~/components/Profile/PersonalInfo";

export async function clientLoader() {
    return await requireUserLoader();
}

export default function Profile({ loaderData }: Route.ComponentProps) {
    const baseUrl = import.meta.env.BASE_URL;

    const baseImageUrl = "/api/v1/images";
    const currentUser = loaderData as unknown as UserDTO | null;
    const isAdmin = currentUser?.roles.includes("ADMIN");

    return (
        <>
            <ProfileNavbar />
            <div className="container my-5">
                <div className="row">
                    {currentUser && (
                        <div className="col-md-4 col-lg-3 mb-4">
                            <LeftCard baseImageUrl={baseImageUrl} userName={currentUser.userName} imageId={currentUser.imageId} />
                        </div>
                    )}

                    <div className={currentUser ? "col-md-8 col-lg-9" : "col-12"}>
                        <div className="d-flex justify-content-between align-items-center mb-4 border-bottom pb-2">
                            <h2 className="h3 fw-bold text-dark">Mi Perfil</h2>
                            {currentUser && (
                                <span className={`badge rounded-pill ${isAdmin ? "bg-primary" : "bg-secondary"}`}>
                                    {isAdmin ? "Admin" : "Usuario"}
                                </span>
                            )}
                        </div>

                        {!currentUser ?
                            <AuthError />
                            : (
                                <>
                                    <PersonalInfo baseImageUrl={baseImageUrl} currentUser={currentUser} isAdmin={isAdmin} />
                                    <OrdersDashboard />
                                    <DeleteAccountForm />
                                </>
                            )}

                        <div className="mt-4 text-center">
                            <a href={baseUrl} className="btn btn-danger py-2 fw-bold shadow-sm" title="Volver a la tienda">
                                Volver a la tienda
                            </a>
                        </div>
                    </div>
                </div>
            </div>
            <Foot />
        </>
    );
}