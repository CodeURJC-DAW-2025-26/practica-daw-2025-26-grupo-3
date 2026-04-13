

export default function ProductSearch() {
    const products = [
        { id: 1, productName: "Portátil Gaming", price: 850, hasImage: true, stateClass: "bg-success", stateName: "Como nuevo" },
        { id: 2, productName: "Bicicleta de montaña", price: 250, hasImage: false, stateClass: "bg-warning", stateName: "Usado" },
        { id: 3, productName: "Smartphone", price: 400, hasImage: true, stateClass: "bg-info", stateName: "Reacondicionado" },
    ];
    const user_logged = true;

    return (
        <>


            <div className="container my-5">
                <div className="row justify-content-center">
                    <main className="col-lg-12">
                        <div className="row g-4 justify-content-center">
                            {products && products.length > 0 ? (
                                products.map((product) => (
                                    <div className="col-md-3" key={product.id}>
                                        <div className="card h-100 border-0 shadow-sm product-card">
                                            {product.hasImage ? (
                                                <img src={`/product-images/${product.id}`} className="card-img-top product-img" alt={product.productName} />
                                            ) : (
                                                <img src={`https://placehold.co/300x250/667eea/ffffff?text=${encodeURIComponent(product.productName)}`} className="card-img-top product-img" alt="Producto" />
                                            )}
                                            <div className="card-body">
                                                <span className={`badge rounded-pill ${product.stateClass} mb-2`}>{product.stateName}</span>
                                                <h5 className="card-title h6">{product.productName}</h5>
                                                <p className="text-primary fw-bold">{product.price}€</p>
                                                {!user_logged ? (
                                                    <a href={`/product_detail/${product.id}`} className="btn btn-sm btn-primary w-100">
                                                        Iniciar sesión para ver más detalles
                                                    </a>
                                                ) : (
                                                    <a href={`/product_detail/${product.id}`} className="btn btn-sm btn-primary w-100">
                                                        Ver más detalles
                                                    </a>
                                                )}
                                            </div>
                                        </div>
                                    </div>
                                ))
                            ) : (
                                <div className="col//-12">
                                    <div className="alert alert-info text-center" role="alert">
                                        No se han encontrado productos que coincidan con tu búsqueda. ¡Intenta ajustar los filtros o busca algo diferente!
                                    </div>
                                </div>
                            )}
                        </div>
                    </main>
                </div>
            </div>


        </>
    );
}