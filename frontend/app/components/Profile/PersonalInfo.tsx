import type { UserDTO } from "~/dtos/UserDTO";

export interface PersonalInfoProps {
    baseImageUrl: string;
    currentUser: UserDTO;
    isAdmin: boolean | undefined
}

export function PersonalInfo({ baseImageUrl, currentUser, isAdmin }: PersonalInfoProps) {
    const baseUrl = import.meta.env.BASE_URL;
    return (
        <div className="card border-0 shadow-sm p-4 mb-4">
            <div className="d-flex justify-content-between align-items-center mb-4">
                <h5 className="fw-bold mb-0 text-dark">Informacion Personal</h5>
                <a href={`${baseUrl}profile/edit`} className="btn btn-sm btn-outline-dark rounded-pill px-3">
                    Editar
                </a>
            </div>

            <div className="row g-3">
                <div className="col-12 text-center mb-3">
                    <img
                        src={`${baseImageUrl}/${currentUser.imageId}/media`}
                        onError={(event) => {
                            event.currentTarget.src = `https://ui-avatars.com/api/?name=${encodeURIComponent(currentUser.userName)}&background=random`;
                        }}
                        className="rounded-circle shadow-sm"
                        alt="Foto de perfil"
                        style={{ width: 150, height: 150, objectFit: "cover" }}
                    />
                </div>

                <div className="col-md-6">
                    <label className="form-label text-muted small fw-bold text-uppercase">Nombre Completo</label>
                    <p className="fs-5 mb-0">
                        {currentUser.userName} {currentUser.surname}
                    </p>
                </div>

                <div className="col-md-6">
                    <label className="form-label text-muted small fw-bold text-uppercase">Correo Electronico</label>
                    <p className="fs-5 mb-0">{currentUser.email}</p>
                </div>

                <div className="col-md-6">
                    <label className="form-label text-muted small fw-bold text-uppercase">Direccion</label>
                    <p className="fs-5 mb-0">{currentUser.address}</p>
                </div>

                <div className="col-md-6">
                    <label className="form-label text-muted small fw-bold text-uppercase">Rol</label>
                    <p className="fs-5 mb-0 fw-bold">{isAdmin ? "Admin" : "Usuario"}</p>
                </div>
            </div>
        </div>
    );
}