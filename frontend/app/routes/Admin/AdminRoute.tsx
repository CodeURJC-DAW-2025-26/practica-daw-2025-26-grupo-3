import { Col, Container, Row } from "react-bootstrap";
import { Outlet, redirect, useLoaderData } from "react-router";
import Admin_sidebar from "~/components/Admin/admin_sidebar";
import { requireUserLoader } from "../auth-loaders";
import { ErrorCard } from "~/components/error-card";
import { Navbar } from "~/components/navbar";
import { Foot } from "~/components/foot";

/*
 * 1. DATA LOADER
 * This is for F5 clicking
 */
export async function clientLoader() {
    const user = await requireUserLoader();
    return user;
}

/*
 * 2. LAYOUT COMPONENT
 */
export default function AdminRoute() {
    const verifiedUser = useLoaderData() as any;

    if (!verifiedUser) {
        return (
            <div className="d-flex flex-column min-vh-100">
                <Navbar />
                <div className="flex-grow-1 d-flex flex-column justify-content-center">
                    <Container className="my-5 d-flex justify-content-center align-items-center">
                        <ErrorCard message="Debes iniciar sesión para acceder al panel de administración." />
                    </Container>
                </div>
                <Foot />
            </div>
        );
    }

    const isAdmin = verifiedUser.roles.includes("ADMIN") || verifiedUser.roles.includes("ROLE_ADMIN");

    if (!isAdmin) {
        return (
            <div className="d-flex flex-column min-vh-100">
                <Navbar />
                <div className="flex-grow-1 d-flex flex-column justify-content-center">
                    <Container className="my-5 d-flex justify-content-center align-items-center">
                        <ErrorCard message="No tienes permisos de administrador para acceder a este área." />
                    </Container>
                </div>
                <Foot />
            </div>
        );
    }

    return (
        <Container fluid className="bg-light">
            <Row className="min-vh-100">
                <Col md={3} lg={2} className="p-0 bg-light border-end">
                    <Admin_sidebar />
                </Col>

                <Col md={9} lg={10} className="py-3 px-4">
                    <Outlet />
                </Col>
            </Row>
        </Container>
    );
}