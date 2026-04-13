export default function ProductPublish() {
    const isAdmin = false; // Mock data (cambia a true para ver la vista de admin)
    const token = "mock-csrf-token-123";

    return (
        <div className="container my-5">
            <div className="row justify-content-center">
                <div className="col-md-8">
                    <div className="card border-0 shadow-sm p-4">
                        <div className="d-flex justify-content-between align-items-start mb-4">
                            <div>
                                <h3 className="fw-bold mb-4">Publicar Producto</h3>
                            </div>
                        </div>

                        <p className="text-muted small">Completa los datos para poner tu artículo a la venta.</p>
                        <form action="/publish_new_product" method="post" encType="multipart/form-data">
                            <div className="mb-3">
                                <label className="form-label fw-bold" htmlFor="productName">Nombre del Producto</label>
                                <input type="text" className="form-control" id="productName" name="productName"
                                    placeholder="Ej. MacBook Pro 2021" minLength={3} maxLength={100} required />
                            </div>
                            <div className="mb-3">
                                <label className="form-label fw-bold" htmlFor="price">Precio (€)</label>
                                {/* Minimun price to sell is 1 cent */}
                                <input type="number" className="form-control" id="price" name="price" placeholder="0.00" min="1"
                                    step="0.01" required />
                            </div>
                            <div className="mb-3">
                                {/* If the user is not an admin */}
                                {!isAdmin && (
                                    <>
                                        <span className="form-label fw-bold d-block mb-2">Estado del artículo</span>
                                        <div className="btn-group w-100" role="group">
                                            <p>Segunda mano</p>
                                        </div>
                                        <input type="hidden" name="state" value="2" />
                                    </>
                                )}

                                {/* If the user is an admin (only admins can upload new and reconditioned productos) */}
                                {isAdmin && (
                                    <>
                                        <label className="form-label fw-bold">Estado del artículo</label>
                                        <select className="form-select" id="state" name="state" required defaultValue="-1">
                                            <option value="-1" disabled>Selecciona el estado...</option>
                                            <option value="0">Nuevo</option>
                                            <option value="1">Reacondicionado</option>
                                            <option value="2">Segunda mano</option>
                                        </select>
                                    </>
                                )}
                            </div>
                            <div className="mb-3">
                                <label className="form-label fw-bold" htmlFor="description">Descripción</label>
                                <textarea className="form-control" id="description" name="description" rows={4}
                                    placeholder="Describe brevemente el estado y características de tu producto"
                                    minLength={15} maxLength={1000} required></textarea>
                            </div>
                            <div className="mb-3">
                                <label className="form-label fw-bold" htmlFor="images">Imagen</label>
                                <input type="file" className="form-control" id="images" name="productimages" accept="image/*"
                                    multiple required />
                                <small className="form-text text-muted">Debes adjuntar al menos una imagen del producto</small>
                            </div>
                            <button type="submit" className="btn btn-primary w-100 mt-4 py-2 fw-bold">
                                Publicar Ahora
                            </button>
                            <input type="hidden" name="_csrf" value={token} />
                        </form>
                        <div className="mt-4 text-center">
                            <a href="/" className="btn btn-danger py-2 fw-bold shadow-sm" title="Volver a la tienda">
                                Volver a la tienda
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}