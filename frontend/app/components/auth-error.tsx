export function AuthError() {
    return (
        <div className="container my-5">
            <div className="row justify-content-center">
                <div className="col-md-8">
                    <div
                        style={{
                            backgroundColor: "#fee2e2",
                            color: "#b91c1c",
                            padding: "12px 16px",
                            borderRadius: "8px",
                            border: "1px solid #fca5a5",
                            marginBottom: "20px",
                            fontSize: "0.95rem",
                        }}
                    >
                        <strong>Debes iniciar sesion para acceder a esta página.</strong>
                    </div>

                </div>
            </div>
        </div>
    );
}