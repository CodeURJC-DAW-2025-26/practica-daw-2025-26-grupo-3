import { Foot } from "../components/foot";
import { Outlet } from "react-router";
import { Navbar } from "../components/navbar";

export default function Home() {
  return (
    <div className="d-flex flex-column min-vh-100">
      <Navbar />
      <main className="flex-grow-1 d-flex flex-column">
        <Outlet />
      </main>
      <Foot />
    </div>
  );
}