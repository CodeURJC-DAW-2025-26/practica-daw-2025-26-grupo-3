import { Foot } from "../components/foot";
import { Outlet } from "react-router";
import { Navbar } from "../components/navbar";
import { Alert, Container } from "react-bootstrap";
import { useUserState } from "~/stores/user-store";

export default function Home() {
  const authMessage = useUserState(state => state.authMessage);
  const authMessageVariant = useUserState(state => state.authMessageVariant);

  return (
    <div className="d-flex flex-column min-vh-100">
      <Navbar />
      {authMessage && (
        <Container className="d-flex justify-content-center auth-message-wrap">
          <Alert
            variant={authMessageVariant ?? "success"}
            dismissible
            onClose={() => useUserState.setState({ authMessage: null, authMessageVariant: null })}
            className="shadow-sm auth-message-alert"
          >
            {authMessage}
          </Alert>
        </Container>
      )}
      <main className="flex-grow-1 d-flex flex-column">
        <Outlet />
      </main>
      <Foot />
    </div>
  );
}