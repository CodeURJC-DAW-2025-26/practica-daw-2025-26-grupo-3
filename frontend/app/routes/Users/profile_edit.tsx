import { Foot } from "../../components/foot";
import { ProfileNavbar } from "../../components/profile_navbar";

type User = {
    id: number;
    userName: string;
    surname: string;
    email: string;
    address: string;
    hasImage: boolean;
};

const mockUser: User = {
    id: 7,
    userName: "Usuario",
    surname: "Demo",
    email: "usuario.demo@remarketplus.com",
    address: "Calle Mayor 21, Madrid",
    hasImage: false,
};

export default function ProfileEdit() {
    const baseUrl = import.meta.env.BASE_URL;

    const avatarUrl = mockUser.hasImage
        ? `/user-images/${mockUser.id}`
        : `https://ui-avatars.com/api/?name=${encodeURIComponent(mockUser.userName)}&background=random`;

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

                            <form>
                                <div className="row g-3">
                                    <div className="col-12 text-center mb-3">
                                        <img
                                            src={avatarUrl}
                                            onError={(event) => {
                                                event.currentTarget.src = `https://ui-avatars.com/api/?name=${encodeURIComponent(mockUser.userName)}&background=random`;
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
                                            defaultValue={mockUser.userName}
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
                                            defaultValue={mockUser.surname}
                                            maxLength={30}
                                            required
                                        />
                                    </div>

                                    <div className="col-md-6">
                                        <label className="form-label text-muted small fw-bold text-uppercase">Correo Electrónico</label>
                                        <input type="email" className="form-control bg-light border-0" name="email" defaultValue={mockUser.email} required />
                                    </div>

                                    <div className="col-md-6">
                                        <label className="form-label text-muted small fw-bold text-uppercase">Dirección</label>
                                        <input
                                            type="text"
                                            className="form-control bg-light border-0"
                                            name="address"
                                            defaultValue={mockUser.address}
                                            maxLength={30}
                                            required
                                        />
                                    </div>

                                    <div className="col-12 text-end mt-4">
                                        <button type="button" className="btn btn-primary">
                                            Guardar Cambios
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