import { useParams } from "react-router-dom";
import { Navbar } from "../components/NavBar";
import { Activities } from "./Activities";

export function ActivitiesByType() {
  const { type } = useParams();

  return (
    <>
      <Navbar />

      <div className="mx-auto mt-10 max-w-7xl px-8">
        <h1 className="mb-8 text-3xl font-bold">{type}</h1>

        <Activities type={type!} />
      </div>
    </>
  );
}
