export function loader() {
    return { name: "React Router" };
}

export default function Index() {
    const featuredProducts = [
        {
            id: 1,
            name: "Portátil reacondicionado",
            price: "349",
            status: "Reacondicionado",
            rating: 4,
            reviews: 12,
        },
        {
            id: 2,
            name: "Silla ergonómica",
            price: "89",
            status: "Usado en buen estado",
            rating: 5,
            reviews: 8,
        },
        {
            id: 3,
            name: "Smartphone de exposición",
            price: "279",
            status: "Seminuevo",
            rating: 4,
            reviews: 16,
        },
    ];
    return (
        <>
            <header className="hero-section py-5 bg-light">
                <div className="alert-container container mb-4">
                    <div className="alert alert-primary alert-dismissible fade show" role="alert">
                        PROTOTIPO FRONTEND REACT. Los datos reales llegarán desde la API más adelante.
                        <button type="button" className="btn-close" data-bs-dismiss="alert" aria-label="Close" />
                    </div>
                </div>
                <div className="container py-5">
                    <div className="row align-items-center g-5">
                        <div className="col-lg-6">
                            <span className="badge bg-primary text-white mb-3 px-3 py-2">🌱 Sostenible y confiable</span>
                            <h1 className="display-4 fw-bold text-dark mb-3">Dale una segunda vida a lo que amas.</h1>
                            <p className="lead text-muted mb-4">
                                Productos nuevos, reacondicionados y usados con la mejor garantía del mercado.
                            </p>
                            <div className="mt-4 d-flex gap-3 flex-wrap">
                                <a href="/login" className="btn btn-primary btn-lg px-5 shadow">
                                    Iniciar sesión
                                </a>
                                <a href="/signup" className="btn btn-outline-primary btn-lg px-5">
                                    Registrarse
                                </a>
                            </div>
                        </div>
                        <div className="col-lg-6 d-none d-lg-block">
                            <img src="/assets/Logo_Remarket.png" className="img-fluid hero-image" alt="ReMarket+" />
                        </div>
                    </div>
                </div>
            </header>

            <section className="py-5 bg-white">
                <div className="container">
                    <div className="text-center mb-5">
                        <span className="badge bg-primary bg-opacity-10 text-primary mb-2 px-3 py-2 fs-1">⭐ Destacados</span>
                        <h2 className="fw-bold display-6 mb-2">Recomendaciones</h2>
                        <p className="text-muted">Creemos que estos productos te encantarán</p>
                    </div>
                    <div className="row g-4 justify-content-center" id="featured-products">
                        {featuredProducts.map((product) => (
                            <div className="col-md-4" key={product.id}>
                                <div className="card h-100 border-0 shadow-sm product-card position-relative">
                                    <img
                                        src={`https://placehold.co/600x400/667eea/ffffff?text=${encodeURIComponent(product.name)}`}
                                        className="card-img-top product-img"
                                        alt={product.name}
                                    />
                                    <div className="card-body">
                                        <span className="badge rounded-pill bg-secondary mb-2">{product.status}</span>
                                        <h5 className="card-title h6 fw-bold">{product.name}</h5>
                                        <p className="text-primary fw-bold fs-5 mb-1">{product.price}€</p>
                                        <div className="small text-warning">
                                            {Array.from({ length: 5 }, (_, index) => (index < product.rating ? "★" : "☆")).join("")}
                                            <span className="text-muted"> ({product.reviews})</span>
                                        </div>
                                    </div>
                                    <div className="card-footer bg-transparent border-0 pb-3">
                                        <a href={`/product_detail/${product.id}`} className="btn btn-sm btn-primary w-100 rounded-pill">
                                            Ver detalles
                                        </a>
                                    </div>
                                </div>
                            </div>
                        ))}
                    </div>

                    <div className="text-center mt-4 mb-2">
                        <button id="load-more-btn" className="btn btn-primary rounded-pill px-4 py-2 shadow-sm" type="button">
                            <i className="bi bi-arrow-down-circle me-2" /> Mostrar más destacados
                        </button>
                    </div>

                    <div className="text-center mt-5">
                        <a href="/product_search" className="btn btn-outline-primary btn-lg px-5 rounded-pill">
                            Ver todos los productos →
                        </a>
                    </div>
                </div>
            </section>
        </>
    );
}