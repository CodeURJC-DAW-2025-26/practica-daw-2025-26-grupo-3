import { Foot } from "../components/foot";
import { Outlet } from "react-router";
import { Navbar } from "../components/navbar";

export function clientLoader() {
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