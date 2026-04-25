import { Col, Container, Row } from "react-bootstrap";
import { Navigate, Outlet } from "react-router";
import Admin_sidebar from "~/components/Admin/admin_sidebar";
import { useUserState } from "~/stores/user-store";

/*
 * We use this component as a 'layout' in routes.ts. It acts as a route guard.
 * It checks the Zustand store for the user's role. If they are not an ADMIN,
 * it prevents access to the <Outlet /> (the child routes) and shows an error instead.
 */
export default function AdminRoute() {
    const currentUser = useUserState(state => state.currentUser);

    // If no one is logged in, redirect to login page
    if (!currentUser) {
        return <Navigate to="/login" replace />;
    }

    // Check if the user has admin privileges
    const isAdmin = currentUser.roles.includes("ADMIN") || currentUser.roles.includes("ROLE_ADMIN");

    // If a registered user tries to access without being an admin, show an error.
    if (!isAdmin) {
        return (
            <div className="container mt-5 text-center min-vh-100">
                <h1 className="text-danger display-4 fw-bold">Error 403: Forbidden</h1>
                <p className="lead">You do not have administrator privileges to access this area.</p>
            </div>
        );
    }

    // If the user is an admin, render the requested admin page
    return (
        <Container fluid className="bg-light">
            <Row className="min-vh-100">
                {/* SIDEBAR SECTION */}
                <Col md={3} lg={2} className="p-0 bg-light border-end">
                    {/* import and use the AdminSidebar component we created */}
                    <Admin_sidebar />
                </Col>

                {/* MAIN CONTENT SECTION */}
                <Col md={9} lg={10} className="py-3 px-4">
                    <Outlet />
                </Col>
            </Row>
        </Container>
    );
}