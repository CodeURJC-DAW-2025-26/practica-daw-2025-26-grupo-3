import { useEffect, useState } from "react";
import { Card, Col, Image, Row } from "react-bootstrap";
import { useReviewState } from "~/stores/review-store";
import { ErrorCard } from "../error-card";
import { useUserState } from "~/stores/user-store";
import { ReviewItem } from "./ReviewItem";

export interface ReviewListProps {
    productId: number
}

export function ReviewList({ productId }: ReviewListProps) {
    const { reviews, getProductReviews } = useReviewState();
    const [error, setError] = useState<string | null>(null);

    async function loadProductReviews(id: number) {
        let errorMessage: string | null = null;
        try {
            await getProductReviews(id);
        } catch (err) {
            errorMessage = "Se ha producido un error al cargar las reseñas de este producto.";
            setError(errorMessage);
        }
    }

    useEffect(() => { loadProductReviews(productId) }, []);

    if (error) {
        <ErrorCard message={error} />
    }

    return (<Row className="justify-content-center">
        <Col lg={8}>
            <h4 className="mb-4 text-center">Reseñas del producto</h4>
            {reviews!.length > 0 ? (
                reviews!.map((review: any) => (
                    <ReviewItem review={review} productId={productId} />
                ))
            ) : (
                <p className="text-muted text-center">Sin reseñas todavía.</p>
            )}
        </Col>
    </Row>)
}