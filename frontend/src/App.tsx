import { Routes, Route } from "react-router-dom";
import { Login } from "./pages/Login";
import { Home } from "./pages/Home";
import { PrivateRoute } from "./routes/PrivateRoute";
import { Register } from "./pages/Register";
import { ActivitiesByType } from "./pages/ActivitiesByType";

function App() {
  return (
    <Routes>
      <Route path="/" element={<Login />} />
      <Route path="/register" element={<Register />} />

      <Route
        path="/home"
        element={
          <PrivateRoute>
            <Home />
          </PrivateRoute>
        }
      />
      <Route
        path="/activities/:type"
        element={
          <PrivateRoute>
            <ActivitiesByType />
          </PrivateRoute>
        }
      />
    </Routes>
  );
}

export default App;
