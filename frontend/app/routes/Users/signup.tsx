import { useActionState } from "react";
import { useNavigate } from "react-router";
import { useUserState } from "~/stores/user-store";
import { uploadUserImage } from "~/services/user-service";

export default function Signup() {
    const baseUrl = import.meta.env.BASE_URL;
    const { login, signup, error } = useUserState();
    const navigate = useNavigate();

    const [{ errMessage }, formAction, loading] = useActionState(
        handleSignup,
        { errMessage: null }
    );

    async function handleSignup(prevState: { errMessage: string | null }, formData: FormData) {
        const email = formData.get("email") as string;
        const password = formData.get("password") as string
        let frontError: string | null = null;

        try {
            await signup({
                userName: formData.get("userName") as string,
                surname: formData.get("surname") as string,
                address: formData.get("address") as string,
                email: email,
                password: password
            });
            const updatedUser = await login(email, password);

            const image: File = formData.get("imageFile") as File;

            if (image) {
                await uploadUserImage(image, updatedUser!.id!);
            }
        }
        catch (err) {
            frontError = err instanceof Error
                ? err.message?.split('"')[1].split(":")[1]?.trim()
                : "Algunos de los datos enviados no son correctos. Intentalo de nuevo";
            console.log("Error recibido: " + frontError);
        }

        if (!frontError) {
            navigate("/");
        }

        return { errMessage: frontError }
    }

    return (
        <div className="container-fluid signup-container">
            <div className="row g-0">
                <div className="card card-registration border-0 shadow-sm overflow-hidden">
                    <div className="row g-0">
                        <div className="col-md-4 logo-side d-none d-md-flex">
                            <div className="text-center p-4">
                                <img
                                    src={`${baseUrl}assets/Logo_Remarket.png`}
                                    alt="ReMarket+ Logo"
                                    className="img-fluid"
                                    style={{ maxWidth: 180 }}
                                />
                            </div>
                        </div>

                        <div className="col-md-8 bg-white p-4 p-md-5">
                            <div className="d-flex justify-content-between align-items-center mb-4">
                                <h1 className="h4 fw-bold mb-0">Crear Cuenta</h1>
                                <p className="text-dark small mt-3">Únete a nuestra comunidad</p>
                            </div>


                            {errMessage && (
                                <div style={{
                                    backgroundColor: '#fee2e2',
                                    color: '#b91c1c',
                                    padding: '12px 16px',
                                    borderRadius: '8px',
                                    border: '1px solid #fca5a5',
                                    marginBottom: '20px',
                                    fontSize: '0.95rem'
                                }}>
                                    <strong>¡Atención!</strong> {errMessage}
                                </div>
                            )}

                            <form action={formAction}>
                                <input type="hidden" name="_csrf" value="" />
                                <div className="row g-3">
                                    <div className="col-md-6">
                                        <label className="form-label small fw-bold">
                                            Nombre <span className="mandatory">*</span>
                                        </label>
                                        <input
                                            type="text"
                                            className="form-control form-control-sm"
                                            placeholder="Tu nombre"
                                            name="userName"
                                            maxLength={8}
                                            required
                                        />
                                    </div>
                                    <div className="col-md-6">
                                        <label className="form-label small fw-bold">
                                            Apellidos <span className="mandatory">*</span>
                                        </label>
                                        <input
                                            type="text"
                                            className="form-control form-control-sm"
                                            placeholder="Tus apellidos"
                                            name="surname"
                                            maxLength={30}
                                            required
                                        />
                                    </div>

                                    <div className="col-md-12">
                                        <label className="form-label small fw-bold">
                                            Dirección de envío <span className="mandatory">*</span>
                                        </label>
                                        <input
                                            type="text"
                                            className="form-control form-control-sm"
                                            placeholder="Calle, número, ciudad..."
                                            name="address"
                                            maxLength={30}
                                            required
                                        />
                                    </div>

                                    <div className="col-md-6">
                                        <label className="form-label small fw-bold">
                                            Correo electrónico <span className="mandatory">*</span>
                                        </label>
                                        <input
                                            type="email"
                                            className="form-control form-control-sm"
                                            placeholder="email@ejemplo.com"
                                            name="email"
                                            required
                                        />
                                    </div>
                                    <div className="col-md-6">
                                        <label className="form-label small fw-bold">
                                            Contraseña <span className="mandatory">*</span>
                                        </label>
                                        <input
                                            type="password"
                                            className="form-control form-control-sm"
                                            placeholder="Crea una contraseña"
                                            name="password"
                                            minLength={8}
                                            maxLength={20}
                                            required
                                        />
                                    </div>
                                    <div className="col-12">
                                        <label className="form-label small fw-bold">Imagen de perfil</label>
                                        <input type="file" className="form-control form-control-sm" name="imageFile" accept="image/*" />
                                    </div>
                                </div>

                                <div className="mt-3 d-flex justify-content-between align-items-center">
                                    <p className="mandatory mb-0">(*) Campo obligatorio</p>
                                    <p className="small text-muted mb-0">
                                        ¿Ya tienes cuenta?{" "}
                                        <a href={`${baseUrl}login`} className="fw-bold text-decoration-none text-primary">
                                            Inicia sesión
                                        </a>
                                    </p>
                                </div>

                                <div className="mt-4">
                                    <button
                                        type="submit"
                                        className={`btn btn-primary w-100 py-2 fw-bold shadow-sm d-flex align-items-center justify-content-center signup-submit-btn ${loading ? "signup-submit-btn-loading" : ""}`}
                                        disabled={loading}
                                    >
                                        {loading ? (
                                            <span className="signup-loading-spinner" role="status" aria-label="Registrando" />
                                        ) : (
                                            <span>Registrarme</span>
                                        )}
                                    </button>
                                </div>
                            </form>
                            <div className="mt-4">
                                <a href={baseUrl} className="btn btn-danger w-100 py-2 fw-bold shadow-sm" title="Volver a la tienda">
                                    Volver a la tienda
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}