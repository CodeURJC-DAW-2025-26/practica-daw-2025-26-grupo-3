import { Foot } from "../components/foot";
import { Outlet } from "react-router";
import { Navbar } from "../components/navbar";

export function loader() {
  return { name: "React Router" };
}

export default function Home() {
  return (
    <>
      <Navbar />
      <Outlet />
      <Foot />
    </>
  );
}