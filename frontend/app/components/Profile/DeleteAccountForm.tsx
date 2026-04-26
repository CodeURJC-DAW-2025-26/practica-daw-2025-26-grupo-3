import { useActionState } from "react";
import { useNavigate } from "react-router";
import { useUserState } from "~/stores/user-store";
import { Card, Form, Button, Alert, Col } from "react-bootstrap";

export interface DeleteAccountFormProps {
    userId: number;
}

export function DeleteAccountForm({ userId }: DeleteAccountFormProps) {

    const { deleteUser } = useUserState();
    const [{ errMessage }, formAction, loading] = useActionState(
        handleDeleteForm,
        { errMessage: null }
    );
    const navigate = useNavigate();

    async function handleDeleteForm(prevState: { errMessage: string | null }, formData: FormData) {
        console.log('ESTADO DE LA PUTA VERIFICACIÓN: ' + formData.get("confirmDelete") as string)
        let error: string | null = null;

        if (formData.get("confirmDelete") as string === "on") {
            try {
                await deleteUser({ password: formData.get("currentPassword") as string }, Number(formData.get("userId")));
            }
            catch (err) {
                error = err instanceof Error
                    ? err.message.split('"')[1]?.trim()
                    : "Se ha producido un error al enviar el formulario";
            }
        }
        else {
            console.log('HOLA SOY EL ELSE');
            error = "Debes marcar la casilla de verifiación antes de eliminar tu cuenta";
        }

        if (!error) {
            navigate("/");
        }

        return { errMessage: error }
    }

    return (
        <Card className="border-0 shadow-sm p-4 mt-4">
            <h4 className="fw-bold mb-4 text-danger">Borrar Cuenta</h4>
            <Alert variant="warning">
                <i className="bi bi-exclamation-triangle-fill me-2" />
                Advertencia: Esta accion es irreversible. Todos tus datos seran eliminados permanentemente.
            </Alert>
            <Form action={formAction as unknown as string}>
                <input type="hidden" value={userId} name="userId" />
                <Form.Group className="mb-3">
                    <Form.Label htmlFor="deletePassword" className="fw-bold">
                        Introduce tu contraseña
                    </Form.Label>
                    <Form.Control type="password" id="deletePassword" name="currentPassword" required />
                </Form.Group>

                <Form.Group className="mb-3">
                    <Form.Check
                        type="checkbox"
                        id="confirmDelete"
                        name="confirmDelete"
                        label="Confirmo que quiero borrar mi cuenta permanentemente"
                        required
                    />
                </Form.Group>

                <div className="d-grid">
                    <Button type="submit" variant="danger" className="fw-bold">
                        Borrar Cuenta
                    </Button>
                </div>
            </Form>
            {errMessage && (
                <Col xs={12}>
                    <br />
                    <Alert variant="danger">
                        {errMessage}
                    </Alert>
                </Col>
            )}
        </Card>
    );
}