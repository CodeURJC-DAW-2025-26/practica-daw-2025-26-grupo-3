import { Link } from "react-router";

export function Foot() {
    return (
        <footer className="footer-custom border-top bg-light py-5">
            <div className="container">
                <div className="row">
                    <div className="col-md-4 mb-3">
                        <h5 className="fw-bold mb-3 text-dark">
                            <i className="bi bi-shop-window" /> ReMarket+
                        </h5>
                        <p className="text-muted">Dale una segunda vida a tus productos con garantía y confianza.</p>
                    </div>
                    <div className="col-md-4 mb-3">
                        <h6 className="fw-bold mb-3 text-dark">
                            <i className="bi bi-link-45deg" /> Enlaces rápidos
                        </h6>
                        <ul className="list-unstyled">
                            <li>
                                <Link to="/product_search" className="text-muted text-decoration-none">
                                    <i className="bi bi-bag" /> Productos
                                </Link>
                            </li>
                            <li>
                                <Link to="/login" className="text-muted text-decoration-none">
                                    <i className="bi bi-box-arrow-in-right" /> Iniciar sesión
                                </Link>
                            </li>
                            <li>
                                <Link to="/signup" className="text-muted text-decoration-none">
                                    <i className="bi bi-person-plus" /> Registrarse
                                </Link>
                            </li>
                            <li>
                                <a href="#top" className="text-muted text-decoration-none">
                                    <i className="bi bi-arrow-up-circle" /> Volver arriba
                                </a>
                            </li>
                        </ul>
                    </div>
                    <div className="col-md-4 mb-3">
                        <h6 className="fw-bold mb-3 text-dark">
                            <i className="bi bi-phone" /> Contacto
                        </h6>
                        <p className="text-muted mb-1">
                            <i className="bi bi-envelope me-2" /> info@remarketplus.com
                        </p>
                        <p className="text-muted">
                            <i className="bi bi-telephone" /> +34 900 123 456
                        </p>
                        <div className="mt-3">
                            <h6 className="fw-bold mb-2 text-dark">Redes sociales</h6>
                            <div className="d-flex flex-wrap gap-3">
                                <a href="https://instagram.com" target="_blank" rel="noopener noreferrer" className="text-muted" aria-label="Instagram">
                                    <i className="bi bi-instagram" /> Instagram
                                </a>
                                <a href="https://facebook.com" target="_blank" rel="noopener noreferrer" className="text-muted" aria-label="Facebook">
                                    <i className="bi bi-facebook" /> Facebook
                                </a>
                                <a href="https://twitter.com" target="_blank" rel="noopener noreferrer" className="text-muted" aria-label="Twitter">
                                    <i className="bi bi-twitter-x" /> Twitter
                                </a>
                                <a href="https://tiktok.com" target="_blank" rel="noopener noreferrer" className="text-muted" aria-label="TikTok">
                                    <i className="bi bi-tiktok" /> TikTok
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
                <hr className="border-secondary my-4" />
                <div className="text-center text-muted small">&copy; 2026 ReMarket+. Todos los derechos reservados.</div>
            </div>
        </footer>
    );
}