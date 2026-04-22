import { ReviewForm } from "~/components/Product/ReviewForm";
import type { ReviewDTO } from "~/dtos/ReviewDTO";
import { Link, useLocation } from "react-router";
import { Alert, Button } from "react-bootstrap";

interface ReviewEditNavigationState {
    review: ReviewDTO,
    productId: number
}

export default function ReviewEdit() {
    const location = useLocation();
    const state = location.state as ReviewEditNavigationState | undefined;

    if (!state?.review || !state.productId) {
        return (
            <div className="container py-5">
                <Alert variant="warning" className="mb-3">
                    No se han encontrado los datos de la reseña a editar.
                </Alert>
                <Button as={Link as any} to="/" variant="primary">
                    Volver al inicio
                </Button>
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