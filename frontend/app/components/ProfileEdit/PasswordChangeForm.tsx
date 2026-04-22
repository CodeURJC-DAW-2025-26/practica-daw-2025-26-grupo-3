import { useActionState } from "react";
import { useNavigate } from "react-router";
import { useUserState } from "~/stores/user-store";
import { Card, Form, Row, Col, Button, Alert } from "react-bootstrap";

export interface PasswordChangeFormProps {
    userId: number;
}

async function changePassHandler(prevState: { errMessage: string | null, succMessage: string | null },
    formData: FormData) {

    let error: string | null = null;
    let succ: string | null = null;

    try {
        const { editPass } = useUserState.getState();
        await editPass({
            currentPass: formData.get("oldPassword") as string,
            newPass: formData.get("newPassword") as string
        }, Number(formData.get("userId")));
        succ = "¡La contraseña se ha cambiado con éxito!";
    }
    catch (err) {
        error = err instanceof Error
            ? err.message.split(":")[1]
            : "Algunos de los datos enviados no son correctos, inténtalo de nuevo";
    }
    return { errMessage: error, succMessage: succ };
}

export function PasswordChangeForm({ userId }: PasswordChangeFormProps) {
    const [{ errMessage, succMessage }, formAction, loading] = useActionState(
        changePassHandler,
        { errMessage: null, succMessage: null }
    );

    return (
        <Card className="border-0 shadow-sm p-4">
            <div className="d-flex justify-content-between align-items-center mb-4">
                <h5 className="fw-bold mb-0 text-dark">Cambiar Contraseña</h5>
            </div>

            <Form action={formAction as unknown as string}>
                <input type="hidden" value={userId} name="userId" />
                <Row className="g-3">
                    <Col xs={12}>
                        <Form.Label className="text-muted small fw-bold text-uppercase">Contraseña Actual</Form.Label>
                        <Form.Control type="password" className="bg-light border-0" name="oldPassword" minLength={8} maxLength={20} required />
                    </Col>

                    <Col xs={12}>
                        <Form.Label className="text-muted small fw-bold text-uppercase">Nueva Contraseña</Form.Label>
                        <Form.Control type="password" className="bg-light border-0" name="newPassword" minLength={8} maxLength={20} required />
                    </Col>

                    <Col xs={12} className="text-end mt-4">
                        <Button type="submit" variant="primary" className="w-100" disabled={loading}>
                            {loading ? (
                                <span className="signup-loading-spinner" role="status" aria-label="Actualizando" />
                            ) : (
                                <span>Cambiar contraseña</span>
                            )}
                        </Button>
                    </Col>

                    {errMessage && (
                        <Col xs={12}>
                            <Alert variant="danger">
                                {errMessage}
                            </Alert>
                        </Col>
                    )}

                    {succMessage && (
                        <Col xs={12}>
                            <Alert variant="success">
                                {succMessage}
                            </Alert>
                        </Col>
                    )}

                </Row>
            </Form>
        </Card>
    );
}