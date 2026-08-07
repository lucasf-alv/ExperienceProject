import { useEffect, useRef, useState } from "react";
import { useNavigate } from "react-router-dom";
import { ArrowLeft, Camera } from "lucide-react";
import api from "../services/api";

import defaultAvatar from "../assets/images/avatar.png";

export function EditProfile() {
  const navigate = useNavigate();

  const fileInput = useRef<HTMLInputElement>(null);

  const [user, setUser] = useState<any>(null);

  const [name, setName] = useState("");
  const [password, setPassword] = useState("");

  const [types, setTypes] = useState<any[]>([]);
  const [preferences, setPreferences] = useState<number[]>([]);

  const [avatar, setAvatar] = useState("");

  const [loading, setLoading] = useState(false);
  const [showDeactivateModal, setShowDeactivateModal] = useState(false);

  useEffect(() => {
    async function loadData() {
      try {
        const userResponse = await api.get("/user");

        setUser(userResponse.data);
        setName(userResponse.data.name);
        setAvatar(userResponse.data.avatar);

        const typesResponse = await api.get("/activities/types");

        setTypes(typesResponse.data);

        const preferencesResponse = await api.get("/user/preferences");

        setPreferences(
          preferencesResponse.data.map((item: any) => item.activityType.id),
        );
      } catch (error) {
        console.log(error);
      }
    }

    loadData();
  }, []);

  function togglePreference(id: number) {
    setPreferences((prev) =>
      prev.includes(id) ? prev.filter((item) => item !== id) : [...prev, id],
    );
  }
  async function handleDeactivateAccount() {
    try {
      await api.delete("/user/desactivate");

      alert("Sua conta foi desativada com sucesso.");

      localStorage.removeItem("token");

      navigate("/");
    } catch (error) {
      console.log("Erro ao desativar conta", error);

      alert("Erro ao desativar sua conta.");
    }
  }

  async function handleAvatar(event: React.ChangeEvent<HTMLInputElement>) {
    const file = event.target.files?.[0];

    if (!file) return;

    const formData = new FormData();

    formData.append("file", file);

    try {
      const response = await api.put("/user/avatar", formData, {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      });

      setAvatar(response.data.avatar);
    } catch (error) {
      console.log("Erro avatar", error);
    }
  }

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault();

    try {
      setLoading(true);

      await api.put("/user/update", {
        name,
        password: password || null,
      });

      await api.post("/user/preferences/define", preferences);

      alert("Perfil atualizado!");

      navigate("/profile");
    } catch (error) {
      console.log(error);

      alert("Erro ao atualizar");
    } finally {
      setLoading(false);
    }
  }

  if (!user) {
    return <div className="p-10">Carregando...</div>;
  }

  return (
    <div className="mx-auto mt-10 max-w-3xl px-6 pb-10">
      <div
        className="
rounded-3xl
bg-white
p-12
shadow-lg
min-h-[1000px]
"
      >
        <button
          onClick={() => navigate("/profile")}
          className="
    mb-8
    flex
    cursor-pointer
    items-center
    gap-2
    rounded-xl
    border
    border-gray-200
    px-5
    py-3
    text-gray-600
    transition
    hover:border-green-500
    hover:bg-green-50
    hover:text-green-700
  "
        >
          <span>Voltar para perfil</span>
        </button>

        <h1
          className="
text-center
text-3xl
font-bold
text-green-700
mb-10
"
        >
          Editar perfil
        </h1>

        {/* FOTO */}

        <div
          className="
flex
justify-center
mb-10
"
        >
          <div
            className="
relative
cursor-pointer
"
            onClick={() => fileInput.current?.click()}
          >
            <img
              src={avatar || defaultAvatar}
              className="
h-36
w-36
rounded-full
object-cover
border-4
border-green-500
"
            />

            <div
              className="
absolute
bottom-0
right-0
rounded-full
bg-green-600
p-3
text-white
"
            >
              <Camera size={22} />
            </div>
          </div>

          <input
            ref={fileInput}
            type="file"
            accept="image/png,image/jpeg"
            hidden
            onChange={handleAvatar}
          />
        </div>

        <form onSubmit={handleSubmit} className="space-y-8">
          <div>
            <label className="font-semibold">Nome</label>

            <input
              value={name}
              onChange={(e) => setName(e.target.value)}
              className="
mt-2
w-full
rounded-xl
border
p-3
focus:ring-2
focus:ring-green-500
outline-none
"
            />
          </div>

          <div>
            <label className="font-semibold">Email</label>

            <input
              disabled
              value={user.email}
              className="
mt-2
w-full
rounded-xl
border
bg-gray-100
p-3
"
            />
          </div>

          <div>
            <label className="font-semibold">Nova senha</label>

            <input
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              placeholder="Digite nova senha"
              className="
mt-2
w-full
rounded-xl
border
p-3
focus:ring-2
focus:ring-green-500
outline-none
"
            />
          </div>

          {/* PREFERÊNCIAS */}

          <div>
            <h2
              className="
mb-5
text-xl
font-bold
"
            >
              Preferências
            </h2>

            <div
              className="
flex
flex-wrap
gap-5
justify-center
"
            >
              {types.map((type) => (
                <div
                  key={type.id}
                  onClick={() => togglePreference(type.id)}
                  className={`
h-24
w-24
rounded-full
flex
items-center
justify-center
text-center
cursor-pointer
border-4
transition

${
  preferences.includes(type.id)
    ? "bg-green-600 text-white border-green-700"
    : "bg-gray-100 border-gray-300"
}

`}
                >
                  <span className="text-sm font-semibold">{type.name}</span>
                </div>
              ))}
            </div>
          </div>

          {/* BOTÕES */}

          <div
            className="
flex
justify-center
gap-5
pt-8
"
          >
            <button
              type="submit"
              disabled={loading}
              className="
    cursor-pointer
    rounded-xl
    bg-green-600
    px-8
    py-3
    text-white
    transition
    hover:bg-green-700
  "
            >
              {loading ? "Salvando..." : "Salvar"}
            </button>

            <button
              type="button"
              onClick={() => navigate("/profile")}
              className="
    cursor-pointer
    rounded-xl
    border
    px-8
    py-3
    transition
    hover:bg-gray-100
  "
            >
              Cancelar
            </button>
          </div>
        </form>

        {/* DESATIVAR */}

        <div
          className="
mt-12
text-center
"
        >
          <button
            type="button"
            onClick={() => setShowDeactivateModal(true)}
            className="
text-red-600
font-bold
hover:underline
"
          >
            Desativar minha conta
          </button>
        </div>
      </div>
      {showDeactivateModal && (
        <div
          className="
fixed
inset-0
z-50
flex
items-center
justify-center
bg-black/50
"
        >
          <div
            className="
w-full
max-w-md
rounded-2xl
bg-white
p-8
shadow-xl
"
          >
            <h2
              className="
text-center
text-2xl
font-bold
text-black-600
"
            >
              Tem certeza que deseja desativar sua conta?
            </h2>

            <p
              className="
mt-5
text-center
text-gray-600
"
            >
              Ao desativar sua conta, todos os seus dados, atividades criadas,
              histórico de participação e conquistas serão removidos
              permanentemente.
            </p>

            <p
              className="
mt-4
text-center
font-semibold
text-gray-700
"
            >
              Essa ação não poderá ser desfeita.
            </p>

            <div
              className="
mt-8
flex
justify-center
gap-4
"
            >
              <button
                onClick={() => setShowDeactivateModal(false)}
                className="
rounded-xl
border
px-6
py-3
hover:bg-gray-100
"
              >
                Cancelar
              </button>

              <button
                onClick={handleDeactivateAccount}
                className="
rounded-xl
bg-red-600
px-6
py-3
text-white
hover:bg-red-700
"
              >
                Sim, desativar
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
