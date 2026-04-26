import { Col, Container, Row } from "react-bootstrap";
import { Outlet, redirect, useLoaderData } from "react-router";
import Admin_sidebar from "~/components/Admin/admin_sidebar";
// Importamos la función de tu compañero
import { requireUserLoader } from "../auth-loaders";

/*
 * 1. DATA LOADER
 * This is for F5 clicking
 */
export async function clientLoader() {
    try {
        const user = await requireUserLoader();

        // if the cookie expires return error
        if (!user) {
            throw new Error("No autenticado");
        }

        return user;
    } catch (error) {
        return redirect("/login");
    }
}

/*
 * 2. LAYOUT COMPONENT
 */
export default function AdminRoute() {
    const verifiedUser = useLoaderData() as any;

    const isAdmin = verifiedUser.roles.includes("ADMIN") || verifiedUser.roles.includes("ROLE_ADMIN");

    if (!isAdmin) {
        return (
            <div className="container mt-5 text-center min-vh-100">
                <h1 className="text-danger display-4 fw-bold">Error 403: Forbidden</h1>
                <p className="lead">You do not have administrator privileges to access this area.</p>
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