import { useEffect, useState } from "react";
import { Button, Card, Image, Spinner } from "react-bootstrap";
import { Link } from "react-router";
import type { ReviewDTO } from "~/dtos/ReviewDTO";
import { useReviewState } from "~/stores/review-store";
import { useUserState } from "~/stores/user-store";

export interface ReviewItemProps {
    review: ReviewDTO;
    productId: number
}
export function ReviewItem({ review, productId }: ReviewItemProps) {

    const { currentUser, loadLoggedUser } = useUserState();
    const { deleteReview } = useReviewState();
    const [deleteLoading, setDeleteLoading] = useState(false);

    useEffect(() => { loadLoggedUser() }, []);

    const isAdmin = currentUser!.roles.includes("ADMIN");
    const isOwner = currentUser!.id === review.user.id;
    const canModifyReview = isAdmin || isOwner;

    async function handleDeleteReview(reviewId: number, productId: number) {
        let errMessage: string | null = null;

        try {
            setDeleteLoading(true);
            await deleteReview(reviewId, productId);
        }
        catch (err) {
            errMessage = err instanceof Error
                ? err.message.split('"')[1]?.trim()
                : "Se ha producido un error al borrar la reseña"
        }
        finally {
            setDeleteLoading(false);
        }
    }

    return (
        <Card key={review.id} className="mb-3 shadow-sm">
            <Card.Body>
                <div className="d-flex justify-content-between align-items-start gap-3 mb-2">
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

                    {canModifyReview && (
                        <div className="d-flex gap-2 flex-shrink-0">
                            <Button
                                as={Link as any}
                                to={`/products/${productId}/reviews/${review.id}`}
                                className="d-flex align-items-center justify-content-center shadow-sm font-weight-bold"
                                style={{ backgroundColor: "#fd7e14", borderColor: "#fd7e14", color: "white", padding: "0.5rem 1.5rem", fontSize: "1rem" }}
                            >
                                <i className="bi bi-pencil-square me-2" style={{ fontSize: "1.2rem" }}></i> Editar
                            </Button>

                            <Button onClick={() => handleDeleteReview(review.id, productId)}
                                type="submit"
                                variant="danger"
                                disabled={deleteLoading}
                                className="d-flex align-items-center justify-content-center shadow-sm font-weight-bold"
                                style={{ padding: "0.5rem 1.5rem", fontSize: "1rem", minWidth: "fit-content" }}
                            >
                                {deleteLoading ? (
                                    <Spinner animation="border" size="sm" />
                                ) : (
                                    <>
                                        <i className="bi bi-trash me-2" style={{ fontSize: "1.2rem" }}></i> Eliminar
                                    </>
                                )}
                            </Button>
                        </div>
                    )}
                </div>
                <Card.Title className="mt-2">{review.title}</Card.Title>
                <div className="mb-2" style={{ color: "#ffc107", fontSize: "1.2rem" }}>
                    {[1, 2, 3, 4, 5].map(star => (
                        <i key={star} className={`bi me-1 ${star <= review.stars ? "bi-star-fill" : "bi-star"}`}></i>
                    ))}
                </div>
                <Card.Text>{review.body}</Card.Text>
            </Card.Body>
        </Card>)
}