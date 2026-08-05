import { Link, useNavigate } from "react-router-dom";
import { CirclePlus, LogOut } from "lucide-react";
import logo from "../assets/icons/Logo.png";

export function Navbar() {
  const navigate = useNavigate();

  function handleLogout() {
    localStorage.removeItem("token");
    navigate("/");
  }

  return (
    <header className="flex h-20 items-center justify-between border-b bg-white px-10 shadow-sm">
      {/* Logo */}
      <Link to="/home">
        <img src={logo} alt="FitMeet" className="w-36" />
      </Link>

      {/* Ações */}
      <div className="flex items-center gap-6">
        <Link
          to="/activities/create"
          className="flex items-center gap-2 rounded-lg bg-green-500 px-5 py-3 font-semibold text-white transition hover:bg-green-600"
        >
          <CirclePlus size={20} />
          Criar atividade
        </Link>

        <img
          src="https://i.pravatar.cc/150"
          alt="Perfil"
          className="h-12 w-12 cursor-pointer rounded-full border-2 border-green-500 object-cover"
        />

        <button
          onClick={handleLogout}
          className="text-gray-600 transition hover:text-red-500"
        >
          <LogOut size={22} />
        </button>
      </div>
    </header>
  );
}
