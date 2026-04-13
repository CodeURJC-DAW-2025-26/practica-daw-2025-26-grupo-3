export default function MyProducts() {
    const token = "mock-csrf-token-123";
    const products = [
        { id: 1, productName: "Portátil Gaming", price: 850, hasImage: true, stateClass: "bg-success", stateName: "Como nuevo" },
        { id: 2, productName: "Bicicleta de montaña", price: 250, hasImage: false, stateClass: "bg-warning", stateName: "Usado" },
        { id: 3, productName: "Smartphone", price: 400, hasImage: true, stateClass: "bg-info", stateName: "Reacondicionado" },
    ];

    return (
        <>
            {/* HEADER */}
            <section className="py-5 bg-light">
                <div className="container">
                    <div className="d-flex justify-content-between align-items-center">
                        <div>
                            <h1 className="fw-bold">Mis Productos Publicados</h1>
                            <p className="text-muted mb-0">Gestiona todos los productos que has puesto a la venta</p>
                        </div>
                        <a href="/product-publish" className="btn btn-primary">
                            <i className="bi bi-plus-circle"></i> Publicar Nuevo Producto
                        </a>
                    </div>
                </div>
            </section>

            {/* PRODUCTS SECTION */}
            <section className="py-5">
                <div className="container">
                    <div className="row g-4">
                        {products && products.length > 0 ? (
                            products.map((product) => (
                                <div className="col-md-3" key={product.id}>
                                    <div className="card h-100 border-0 shadow-sm product-card">
                                        <div className="position-relative">
                                            {product.hasImage ? (
                                                <img src={`/product-images/${product.id}`} className="card-img-top product-img" alt={product.productName} />
                                            ) : (
                                                <img src={`https://placehold.co/300x250/667eea/ffffff?text=${encodeURIComponent(product.productName)}`} className="card-img-top product-img" alt="Producto" />
                                            )}
                                        </div>
                                        <div className="card-body">
                                            <span className={`badge rounded-pill ${product.stateClass} mb-2`}>{product.stateName}</span>
                                            <h5 className="card-title h6 fw-bold">{product.productName}</h5>
                                            <p className="text-primary fw-bold mb-0">{product.price}€</p>
                                        </div>
                                        <div className="card-footer bg-transparent border-0 pb-3">
                                            <div className="d-grid gap-2">
                                                <a href={`/product_detail/${product.id}`}
                                                    className="btn btn-sm btn-primary d-flex align-items-center justify-content-center">
                                                    <i className="bi bi-eye-fill me-2"></i> Ver Detalle
                                                </a>
                                                <a href={`/edit_product/${product.id}`}
                                                    className="btn btn-sm btn-edit-custom text-white d-flex align-items-center justify-content-center">
                                                    <i className="bi bi-pencil-square me-2"></i> Editar
                                                </a>
                                                <form action={`/delete_product/${product.id}`} method="POST" style={{ display: 'inline' }}>
                                                    <input type="hidden" name="_csrf" value={token} />
                                                    <button type="submit"
                                                        onClick={(e) => {
                                                            if (!window.confirm('¿Estás seguro de que deseas borrar este producto?')) {
                                                                e.preventDefault();
                                                            }
                                                        }}
                                                        className="btn btn-sm btn-danger d-flex align-items-center justify-content-center w-100">
                                                        <i className="bi bi-trash me-2"></i> Eliminar
                                                    </button>
                                                </form>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            ))
                        ) : (
                            <div className="col-12 text-center py-5">
                                <p className="text-muted">Aún no has publicado ningún producto.</p>
                            </div>
                        )}
                    </div>
                </div>
            </section>
        </>
    );
}