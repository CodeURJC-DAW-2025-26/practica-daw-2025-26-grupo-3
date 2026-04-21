import { useEffect, useState } from "react";
import { Card, Col, Image, Row } from "react-bootstrap";
import { useReviewState } from "~/stores/review-store";
import { ErrorCard } from "../error-card";

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
                    <Card key={review.id} className="mb-3 shadow-sm">
                        <Card.Body>
                            <div className="d-flex justify-content-between align-items-start mb-2">
                                <div className="d-flex align-items-center">
                                    <Image
                                        src={review.user.imageId ? `/api/v1/images/${review.user.imageId}/media` : `https://ui-avatars.com/api/?name=${review.user.userName}&background=random`}
                                        className="me-3"
                                        roundedCircle
                                        style={{ width: "50px", height: "50px", objectFit: "cover", backgroundColor: "#eee" }}
                                        alt="User avatar"
                                    />
                                    <div>
                                        <h6 className="mb-0 fw-bold">{review.user.userName} {review.user.surname}</h6>
                                        <small className="text-muted">{review.date}</small>
                                    </div>
                                </div>
                            </div>
                            <Card.Title className="mt-2">{review.title}</Card.Title>
                            <div className="mb-2" style={{ color: "#ffc107", fontSize: "1.2rem" }}>
                                {[1, 2, 3, 4, 5].map(star => (
                                    <i key={star} className={`bi me-1 ${star <= review.stars ? "bi-star-fill" : "bi-star"}`}></i>
                                ))}
                            </div>
                            <Card.Text>{review.body}</Card.Text>
                        </Card.Body>
                    </Card>
                ))
            ) : (
                <p className="text-muted text-center">Sin reseñas todavía.</p>
            )}
        </Col>
    </Row>)
}