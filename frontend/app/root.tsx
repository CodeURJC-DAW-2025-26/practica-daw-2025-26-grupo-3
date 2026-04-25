import {
  isRouteErrorResponse,
  Links,
  Meta,
  Outlet,
  Scripts,
  ScrollRestoration,
  useNavigation,
} from "react-router";

import type { Route } from "./+types/root";
import { Head } from "./components/head";
import "./app.css";
import { Spinner } from "./components/spinner";
import { Container } from "react-bootstrap";

function LoadingOverlay() {
  return (
    <div 
      className="position-fixed top-0 start-0 w-100 h-100 d-flex justify-content-center align-items-center" 
      style={{ 

        backgroundColor: "rgba(255, 255, 255, 0.8)", 
        backdropFilter: "blur(4px)",
        zIndex: 9999 
      }}
    >
      <div className="d-flex flex-column align-items-center">
        <Spinner />
        <span className="mt-3 fw-bold text-primary fs-5">
            {"Cargando..."}
        </span>
      </div>
    </div>
  );
}

export function HydrateFallback() {
  return <LoadingOverlay/>;
}

export function Layout({ children }: { children: React.ReactNode }) {
  return (
    <html lang="es" className="h-100">
      <head>
        <meta charSet="utf-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <Meta />
        <Links />
        <Head />
      </head>
      <body className="bg-light d-flex flex-column h-100 min-vh-100">
        <div id="root" className="d-flex flex-column flex-grow-1">
          {children}
        </div>
        <ScrollRestoration />
        <Scripts />
      </body>
    </html>
  );
}

export default function App() {
  const navigation = useNavigation();
  const isGlobalLoading = navigation.state === "loading" || navigation.state === "submitting";

  return (
    <>
      {isGlobalLoading && (
        <LoadingOverlay/>
      )}
      <Outlet />
    </>
  );
}

export function ErrorBoundary({ error }: Route.ErrorBoundaryProps) {
  let message = "Oops!";
  let details = "An unexpected error occurred.";
  let stack: string | undefined;

  if (isRouteErrorResponse(error)) {
    message = error.status === 404 ? "404" : "Error";
    details =
      error.status === 404
        ? "The requested page could not be found."
        : error.statusText || details;
  } else if (import.meta.env.DEV && error && error instanceof Error) {
    details = error.message;
    stack = error.stack;
  }

  return (
    <Container as="main" className="pt-16 p-4 mx-auto">
      <h1>{message}</h1>
      <p>{details}</p>
      {stack && (
        <pre className="w-full p-4 overflow-x-auto">
          <code>{stack}</code>
        </pre>
      )}
    </Container>
  );
}
