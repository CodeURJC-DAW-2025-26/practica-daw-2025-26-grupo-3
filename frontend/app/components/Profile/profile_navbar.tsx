import { Link } from "react-router";
import { Navbar, Container } from "react-bootstrap";

export function ProfileNavbar() {
    return (
        <Navbar expand="lg" className="bg-light border-bottom sticky-top shadow-sm">
            <Container>
                <Navbar.Brand as={Link} to="/" className="fw-bold text-primary d-flex align-items-center">
                    <img src="/assets/Logo_Remarket.png" alt="ReMarket+ Logo" style={{ height: 50, width: "auto" }} />
                </Navbar.Brand>
            </Container>
        </Navbar>
    );
}