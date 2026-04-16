import { Foot } from "../components/foot";
import { Outlet } from "react-router";
import { Navbar } from "../components/navbar";
import { useUserState } from "~/stores/user-store";
import { useEffect, useState } from "react";

export default function Home() {
  const { loadLoggedUser } = useUserState();
  const [loading, setLoading] = useState(false);
  useEffect(() => {
    async function loadUser() {
      setLoading(true);

      try {
        await loadLoggedUser();
      }
      catch (error) {
        console.error(error);
      }
      finally {
        setLoading(false);
      }
    }

    loadUser();
  }, []);
  return (
    <>
      <Navbar />
      <Outlet />
      <Foot />
    </>
  );
}