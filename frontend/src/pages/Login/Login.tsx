import loginBg from "../../assets/images/login.png";

export function Login() {
  return (
    <div className="flex h-screen">
      <div className="w-1/2">
        <img
          src={loginBg}
          alt="Pessoas treinando"
          className="h-full w-full object-cover"
        />
      </div>

      <div className="flex w-1/2 items-center justify-center bg-white">
        <h1 className="text-4xl font-bold text-black">Login</h1>
      </div>
    </div>
  );
}
