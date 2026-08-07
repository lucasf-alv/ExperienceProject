import { Link, useNavigate } from "react-router-dom";
import { CirclePlus, LogOut } from "lucide-react";
import logo from "../assets/icons/Logo.png";
import { useEffect, useState } from "react";
import { CreateActivityModal } from "./CreateActivityModal";
import api from "../services/api";
import defaultAvatar from "../assets/images/avatar.png";
export function Navbar() {
  const navigate = useNavigate();
  const [showCreateModal, setShowCreateModal] = useState(false);
  const [user, setUser] = useState<any>();

  useEffect(() => {
    api.get("/user").then((response) => {
      setUser(response.data);
    });
  }, []);

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
        <button
          onClick={() => setShowCreateModal(true)}
          className="
    flex
    cursor-pointer
    items-center
    gap-2
    rounded-lg
    bg-green-500
    px-5
    py-3
    font-semibold
    text-white
    transition
    hover:bg-green-600
  "
        >
          <CirclePlus size={20} />
          Criar atividade
        </button>

        <img
          src={user?.avatar || defaultAvatar}
          onError={(e) => (e.currentTarget.src = defaultAvatar)}
          className="h-12 w-12 rounded-full object-cover cursor-pointer"
          onClick={() => navigate("/profile")}
        />

        <button
          onClick={handleLogout}
          className="
    cursor-pointer
    text-gray-600
    transition
    hover:text-red-500
  "
        >
          <LogOut size={22} />
        </button>
        <CreateActivityModal
          open={showCreateModal}
          onClose={() => setShowCreateModal(false)}
        />
      </div>
    </header>
  );
}
