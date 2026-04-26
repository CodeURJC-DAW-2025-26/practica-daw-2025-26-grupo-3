import { ReviewForm } from "~/components/Product/ReviewForm";
import type { ReviewDTO } from "~/dtos/ReviewDTO";
import { Link, useLocation } from "react-router";
import { Alert, Button } from "react-bootstrap";
import { ErrorCard } from "~/components/error-card";
import { requireUserLoader } from "../auth-loaders";
import type { Route } from "../+types";
import type { UserDTO } from "~/dtos/UserDTO";

export async function clientLoader() {
    return await requireUserLoader();
}

interface ReviewEditNavigationState {
    review: ReviewDTO,
    productId: number
}

export default function ReviewEdit({ loaderData }: Route.ComponentProps) {
    const currentUser = loaderData as unknown as UserDTO | null;
    const location = useLocation();
    const state = location.state as ReviewEditNavigationState | undefined;

    if (!currentUser) {
        return (
            <div className="container my-5 d-flex justify-content-center align-items-center" style={{ minHeight: "60vh" }}>
                <div className="w-100" style={{ maxWidth: "720px" }}>
                    <ErrorCard message="Debes iniciar sesión para editar tus reseñas." />
                </div>
            </div>
        );
    }

    if (!state?.review || !state.productId) {
        return (
            <div className="container my-5 d-flex justify-content-center align-items-center" style={{ minHeight: "60vh" }}>
                <div className="w-100" style={{ maxWidth: "720px" }}>
                    <ErrorCard message="No se han encontrado los datos de la reseña a editar." />
                    <div className="mt-3 text-center">
                        <Button as={Link as any} to="/" variant="primary">
                            Volver al inicio
                        </Button>
                    </div>
                </div>
            </div>
        );
    }

    const { review, productId } = state;

    return (
        <div
            className="container d-flex align-items-center py-4"
            style={{ minHeight: "calc(100vh - 170px)" }}
        >
            <div className="w-100">
                <ReviewForm
                    productId={productId}
                    title={"Editar reseña"}
                    reviewTitleValue={review.title}
                    reviewBodyValue={review.body}
                    operation="edit"
                    reviewId={review.id}
                    buttonText="Editar reseña"
                />
            </div>
        </div>
    )
}