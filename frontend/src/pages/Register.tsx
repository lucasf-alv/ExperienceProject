import { useState } from "react";
import api from "../services/api";
import { useNavigate } from "react-router-dom";
import { Link } from "react-router-dom";
import loginBg from "../assets/images/login.png";
import logo from "../assets/icons/Logo.png";
import { Eye, EyeOff } from "lucide-react";

export function Register() {
  const navigate = useNavigate();
  const [mostrarSenha, setMostrarSenha] = useState(false);
  const [name, setName] = useState("");
  const [cpf, setCpf] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const handleRegister = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!name || !cpf || !email || !password) {
      alert("Preencha todos os campos!");
      return;
    }

    try {
      await api.post("/auth/register", {
        name,
        cpf,
        email,
        password,
      });

      alert("Cadastro realizado com sucesso!");
      navigate("/");
    } catch (error: any) {
      if (error.response?.status === 409) {
        alert("Já existe uma conta com esse e-mail ou CPF.");
      } else if (error.response?.status === 400) {
        alert("Verifique os dados informados.");
      } else {
        alert("Ocorreu um erro ao realizar o cadastro.");
      }
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
          onSubmit={handleRegister}
          className="flex w-full max-w-md flex-col gap-4"
        >
          <img src={logo} alt="Logo" className="mb-10 w-36" />
          <h1 className="text-4xl font-bold text-black">CRIE SUA CONTA</h1>
          <p className="font-sans">
            Cadastre-se para encontrar parceiros de treino e começar a se
            exercitar ao ar livre. Vamos juntos! 💪
          </p>
          {/*Nome */}
          <div className="flex flex-col gap-2">
            <label htmlFor="name" className="font-medium text-xl text-black ">
              Nome Completo
            </label>
            <input
              id="name"
              value={name}
              type="text"
              onChange={(e) => setName(e.target.value)}
              placeholder="Ex: João da Silva"
              className="h-14 rounded-md border border-gray-300 px-4 focus:border-green-500 focus:outline-none"
            />
          </div>
          {/*CPF*/}
          <div className="flex flex-col gap-2">
            <label htmlFor="cpf" className="font-medium text-xl text-black ">
              CPF
            </label>
            <input
              id="cpf"
              value={cpf}
              type="text"
              onChange={(e) => setCpf(e.target.value)}
              placeholder="Ex: 123.456.789-01"
              className="h-14 rounded-md border border-gray-300 px-4 focus:border-green-500 focus:outline-none"
            />
          </div>
          {/*E-mail */}
          <div className="flex flex-col gap-2">
            <label htmlFor="email" className="font-medium text-xl text-black ">
              E-mail
            </label>
            <input
              id="email"
              value={email}
              type="email"
              onChange={(e) => setEmail(e.target.value)}
              placeholder="Ex: joao@email.com"
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
                value={password}
                onChange={(e) => setPassword(e.target.value)}
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
          {/*Butão*/}
          <button
            type="submit"
            className=" mt-6 h-14 bg-green-500 font-semibold text-white transition hover:bg-green-600 "
          >
            CADASTRE-SE
          </button>

          {/*footer*/}
          <p className="text-center text-base text-black">
            Já tem uma conta?{" "}
            <Link to="/" className="font-bold text-green-600 hover:underline">
              Faça login
            </Link>
          </p>
        </form>
      </div>
    </div>
  );
}
