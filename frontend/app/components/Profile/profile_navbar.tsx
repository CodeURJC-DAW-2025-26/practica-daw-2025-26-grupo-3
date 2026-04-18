export function ProfileNavbar() {
    const baseUrl = import.meta.env.BASE_URL;

    return (
        <nav className="navbar navbar-expand-lg navbar-light bg-light border-bottom sticky-top shadow-sm">
            <div className="container">
                <a className="navbar-brand fw-bold text-primary d-flex align-items-center" href={baseUrl}>
                    <img src={`${baseUrl}assets/Logo_Remarket.png`} alt="ReMarket+ Logo" style={{ height: 50, width: "auto" }} />
                </a>
            </div>
        </nav>
    );
}