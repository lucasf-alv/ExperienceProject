import loginBg from "../../assets/images/login.png";
import logo from "../../assets/icons/Logo.png";
import { Eye, EyeOff } from "lucide-react";
import { useState } from "react";
import api from "../../services/api";
import { useNavigate } from "react-router-dom";
import { Link } from "react-router-dom";
export function Login() {
  const navigate = useNavigate();
  const [mostrarSenha, setMostrarSenha] = useState(false);
  const [email, setEmail] = useState("");
  const [senha, setSenha] = useState("");
  const handleLogin = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!email || !senha) {
      alert("Preencha todos os campos!");
      return;
    }

    try {
      const response = await api.post("/auth/login", {
        email,
        password: senha,
      });
      const token = response.data.token;

      localStorage.setItem("token", token);

      navigate("/home");

      console.log(response.data);
    } catch (error) {
      console.log(error);
      alert("E-mail ou senha inválidos.");
    }
  };

  return (
    <div className="flex h-screen">
      {/* Lado esquerdo */}
      <div className="w-1/2">
        <img
          src={loginBg}
          alt="Pessoas treinando"
          className="h-full w-full object-cover"
        />
      </div>

      {/* Lado direito */}
      <div className="flex w-1/2 items-center justify-center bg-white">
        <form
          onSubmit={handleLogin}
          className="flex w-full max-w-md flex-col gap-4"
        >
          <img src={logo} alt="Logo" className="mb-10 w-36" />

          <h1 className="text-4xl font-bold text-black">BEM-VINDO DE VOLTA!</h1>

          <p className="font-serif">
            Encontre parceiros para treinar ao ar livre. Conecte-se e comece
            agora 💪
          </p>

          {/* Email */}
          <div className="mt-8 flex flex-col gap-2">
            <label htmlFor="email" className="text-xl font-medium text-black">
              E-mail
            </label>

            <input
              id="email"
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              placeholder="Digite seu e-mail"
              className="h-14 rounded-md border border-gray-300 px-4 focus:border-green-500 focus:outline-none"
            />
          </div>

          {/* Senha */}
          <div className="flex flex-col gap-2">
            <label
              htmlFor="password"
              className="text-xl font-medium text-black"
            >
              Senha
            </label>

            <div className="relative">
              <input
                id="password"
                type={mostrarSenha ? "text" : "password"}
                value={senha}
                onChange={(e) => setSenha(e.target.value)}
                placeholder="Digite sua senha"
                className="h-14 w-full rounded-md border border-gray-300 px-4 pr-12 focus:border-green-500 focus:outline-none"
              />

              <button
                type="button"
                onClick={() => setMostrarSenha(!mostrarSenha)}
                className="absolute right-4 top-1/2 -translate-y-1/2 text-gray-500 hover:text-gray-700"
              >
                {mostrarSenha ? <EyeOff size={20} /> : <Eye size={20} />}
              </button>
            </div>
          </div>

          {/* Botão */}
          <button
            type="submit"
            className="mt-6 h-14 rounded-md bg-green-500 font-semibold text-white transition hover:bg-green-600"
          >
            Entrar
          </button>

          <p className="text-center text-base text-black">
            Ainda não tem uma conta?{" "}
            <Link
              to="/register"
              className="font-bold text-green-600 hover:underline"
            >
              Cadastre-se
            </Link>
          </p>
        </form>
      </div>
    </div>
  );
}
