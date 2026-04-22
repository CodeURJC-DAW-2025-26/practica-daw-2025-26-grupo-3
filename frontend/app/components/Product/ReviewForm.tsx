import { useActionState, useState } from "react";
import { Button, Card, Col, Form, Row } from "react-bootstrap";
import Spinner from "react-bootstrap/Spinner";
import { useReviewState } from "~/stores/review-store";
import { ErrorCard } from "~/components/error-card";
import { useNavigate } from "react-router";
import { useUserState } from "~/stores/user-store";

export interface ReviewFormProps {
    productId: number,
    title: string,
    reviewTitleValue: string,
    reviewBodyValue: string,
    operation: string,
    reviewId: number,
    buttonText: string
}

export function ReviewForm(
    { productId, title, reviewTitleValue, reviewBodyValue, operation, reviewId, buttonText }: ReviewFormProps) {
    const [rating, setRating] = useState(0);
    const [hoverRating, setHoverRating] = useState(0);
    const [submitError, setSubmitError] = useState<string | null>(null);
    const activeRating = hoverRating || rating;

    const { createReview, editReview } = useReviewState();
    const { loadLoggedUser } = useUserState()

    const [{ error }, formAction, formLoading] = useActionState(
        handleReviewForm,
        { error: null }
    )

    const navigate = useNavigate();

    async function handleReviewForm(prevState: { error: string | null }, formData: FormData) {
        let errorMessage: string | null = null;
        setSubmitError(null);
        try {
            const operation = String(formData.get("operation"));
            const title = String(formData.get("title") ?? "");
            const body = String(formData.get("body") ?? "");
            const stars = Number(formData.get("stars"));
            const productId = Number(formData.get("productId"));

            if (operation === "create") {
                await createReview(productId, {
                    title: title,
                    body: body,
                    stars: stars,
                });
            }
            else if (operation === "edit") {
                await editReview(Number(formData.get("reviewId")), productId, {
                    title: title,
                    body: body,
                    stars: stars
                });
            }

        } catch (err) {
            errorMessage = err instanceof Error
                ? (err.message.split(":")[1]?.trim() || err.message)
                : "Se ha producido un error al enviar el formulario. Inténtalo de nuevo más tarde.";
            setSubmitError(errorMessage);
        }

        if (operation === "edit" && !errorMessage) {
            await loadLoggedUser();
            navigate("/product_detail/" + productId);
        }

        return { error: errorMessage };
    }

    return (<Row className="justify-content-center mb-5">
        <Col lg={6}>
            <Card className="p-4 shadow-sm">
                <Form action={formAction}>
                    <h4 className="mb-4 text-center">{title}</h4>
                    <input type="hidden" name="productId" value={productId} />
                    <input type="hidden" name="operation" value={operation} />
                    <input type="hidden" name="reviewId" value={reviewId} />
                    <Form.Group className="mb-3">
                        <Form.Label>Título</Form.Label>
                        <Form.Control
                            name="title"
                            type="text"
                            required
                            minLength={3}
                            maxLength={100}
                            placeholder="Título de la reseña"
                            defaultValue={reviewTitleValue}
                        />
                    </Form.Group>
                    <Form.Group className="mb-3">
                        <Form.Label>Cuerpo del mensaje</Form.Label>
                        <Form.Control
                            name="body"
                            as="textarea"
                            rows={3}
                            required
                            minLength={10}
                            maxLength={1000}
                            placeholder="Escribe tu reseña"
                            defaultValue={reviewBodyValue}
                        />
                    </Form.Group>
                    <div className="mb-3 text-center">
                        <Form.Label className="d-block">Valoración</Form.Label>
                        <Form.Control type="hidden" name="stars" value={rating} />
                        <div>
                            {[1, 2, 3, 4, 5].map(star => (
                                <span
                                    key={star}
                                    style={{ color: "#ffc107", fontSize: "2.5rem", cursor: "pointer" }}
                                    onMouseEnter={() => setHoverRating(star)}
                                    onMouseLeave={() => setHoverRating(0)}
                                    onClick={() => setRating(star)}
                                >
                                    <i className={`bi star-icon ${star <= activeRating ? "bi-star-fill" : "bi-star"}`}></i>
                                </span>
                            ))}
                        </div>
                    </div>
                    <Button type="submit" variant="primary" disabled={formLoading} className="w-100">
                        {formLoading ? (
                            <Spinner animation="border" size="sm" role="status" aria-hidden="true" />
                        ) : (
                            `${buttonText}`
                        )}
                    </Button>
                    {(submitError || error) && (
                        <ErrorCard message={submitError || error || ""} className="mt-3" />
                    )}
                </Form>
            </Card>
        </Col>
    </Row>)
}