import { useActionState, useState } from "react";
import { Button, Card, Col, Form, Row } from "react-bootstrap";
import Spinner from "react-bootstrap/Spinner";
import { useReviewState } from "~/stores/review-store";
import { ErrorCard } from "~/components/error-card";

export interface ReviewFormProps {
    productId: number,
}

export function ReviewForm({ productId }: ReviewFormProps) {
    const [rating, setRating] = useState(0);
    const [hoverRating, setHoverRating] = useState(0);
    const activeRating = hoverRating || rating;

    const { createReview } = useReviewState()

    const [{ error }, formAction, formLoading] = useActionState(
        handleReviewForm,
        { error: null }
    )

    async function handleReviewForm(prevState: { error: string | null }, formData: FormData) {
        let errorMessage: string | null = null
        try {
            await createReview(Number(formData.get("productId")), {
                title: String(formData.get("title") ?? ""),
                body: String(formData.get("body") ?? ""),
                stars: Number(formData.get("stars")),
            });
        } catch (err) {
            errorMessage = err instanceof Error
                ? (err.message.split(":")[1]?.trim() || err.message)
                : "Se ha producido un error al enviar el formulario. Inténtalo de nuevo más tarde."
        }

        return { error: errorMessage }
    }

    return (<Row className="justify-content-center mb-5">
        <Col lg={6}>
            <Card className="p-4 shadow-sm">
                <Form action={formAction}>
                    <h4 className="mb-4 text-center">Añadir reseña</h4>
                    <input type="hidden" name="productId" value={productId} />
                    <Form.Group className="mb-3">
                        <Form.Label>Título</Form.Label>
                        <Form.Control
                            name="title"
                            type="text"
                            required
                            minLength={3}
                            maxLength={100}
                            placeholder="Título de la reseña"
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
                            "Publicar reseña"
                        )}
                    </Button>
                    {error && (
                        <ErrorCard message={error} className="mt-3" />
                    )}
                </Form>
            </Card>
        </Col>
    </Row>)
}