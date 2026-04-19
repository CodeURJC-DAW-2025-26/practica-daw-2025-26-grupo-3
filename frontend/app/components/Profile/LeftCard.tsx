import { Link, useNavigate } from "react-router";
import type { UserDTO } from "~/dtos/UserDTO";
import { useUserState } from "~/stores/user-store";

export interface LeftCardProps {
    baseImageUrl: string;
    userName: string;
    imageId: number | null;
}

export function LeftCard({ baseImageUrl, userName, imageId }: LeftCardProps) {
    const { logout } = useUserState();
    const navigate = useNavigate();

    async function handleLogout() {
        await logout();
        navigate("/");
    }

    return (
        <div className="card border-0 shadow-sm text-center p-4">
            <img
                src={`${baseImageUrl}/${imageId}/media`}
                onError={(event) => {
                    event.currentTarget.src = `https://ui-avatars.com/api/?name=${encodeURIComponent(userName)}&background=random`;
                }}
                className="rounded-circle mx-auto mb-3"
                width={80}
                height={80}
                style={{ objectFit: "cover" }}
                alt="Avatar del usuario"
            />
            <h5 className="fw-bold mb-0">{userName}</h5>
            <hr />
            <div className="d-grid gap-2">
                <Link
                    to="/my_products"
                    className="btn btn-sm btn-primary rounded d-flex align-items-center justify-content-center py-2"
                >
                    Mis Productos
                </Link>
                <Link
                    to="/product-publish"
                    className="btn btn-sm btn-success rounded d-flex align-items-center justify-content-center py-2"
                >
                    Publicar Producto
                </Link>
                <button
                    type="button"
                    className="btn btn-sm btn-danger rounded d-flex align-items-center justify-content-center py-2 w-100"
                    onClick={handleLogout}
                >
                    Cerrar Sesion
                </button>
            </div>
        </div>
    );
}