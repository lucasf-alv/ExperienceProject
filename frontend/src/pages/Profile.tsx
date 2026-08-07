import { Navbar } from "../components/NavBar";
import { ActivityCard } from "../components/ActivityCard";
import { ActivityDetailsModal } from "../components/ActivityDetailsModal";

import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

import api from "../services/api";
import trofeu from "../assets/images/trofeu.png";

import defaultAvatar from "../assets/images/avatar.png";

import type { Activity } from "../types/activity";

export function Profile() {
  const [user, setUser] = useState<any>(null);
  const [createdActivities, setCreatedActivities] = useState<Activity[]>([]);
  const [history, setHistory] = useState<Activity[]>([]);
  const [selectedActivity, setSelectedActivity] = useState<Activity | null>(
    null,
  );

  const navigate = useNavigate();

  useEffect(() => {
    async function loadData() {
      try {
        const userResponse = await api.get("/user");
        setUser(userResponse.data);

        // Atividades criadas pelo usuário
        const createdResponse = await api.get("/activities/user/creator/all");

        setCreatedActivities(createdResponse.data);

        // Atividades que o usuário participa
        const historyResponse = await api.get("/activities/user/participant");

        /*
        Esse endpoint retorna Page<ActivityParticipants>
        então provavelmente vem:
        {
          content: [...]
          pageable: ...
        }
      */

        setHistory(
          historyResponse.data.content.map((item: any) => item.activity),
        );
      } catch (error) {
        console.log("Erro ao carregar perfil", error);
      }
    }

    loadData();
  }, []);

  if (!user) return null;

  return (
    <>
      <Navbar />

      <div className="mx-auto mt-10 max-w-7xl px-8">
        {/* CARD DO PERFIL */}
        <div className="rounded-2xl bg-white p-10 shadow">
          <button
            onClick={() => navigate("/profile/edit")}
            className="float-right rounded-lg border px-4 py-2 transition hover:bg-gray-100"
          >
            Editar perfil
          </button>

          <div className="flex flex-col items-center">
            <img
              src={user.avatar || defaultAvatar}
              onError={(e) => {
                e.currentTarget.src = defaultAvatar;
              }}
              className="h-36 w-36 rounded-full object-cover"
            />

            <h1 className="mt-4 text-4xl font-bold">{user.name}</h1>
          </div>

          <div className="mt-10 grid grid-cols-2 gap-8">
            {/* NÍVEL */}
            <div className="rounded-xl bg-gray-50 p-6">
              <h2 className="text-xl font-bold">Seu nível</h2>

              <p className="mt-4 text-5xl font-bold">Nível {user.level}</p>

              <div className="mt-6 h-3 rounded-full bg-gray-200">
                <div
                  className="h-3 rounded-full bg-green-500"
                  style={{
                    width: `${user.xp % 100}%`,
                  }}
                />
              </div>

              <p className="mt-3">{user.xp} XP</p>
            </div>

            {/* CONQUISTAS */}
            {/* CONQUISTAS */}
            <div className="rounded-xl bg-gray-50 p-6">
              <h2 className="mb-6 text-xl font-bold">Conquistas</h2>

              <div className="flex gap-6 overflow-x-auto">
                {user.userAchiviements?.map((item: any) => (
                  <div
                    key={item.id}
                    className="min-w-[140px] rounded-xl bg-white p-4 text-center shadow"
                  >
                    <img
                      src={trofeu}
                      className="mx-auto h-16 w-16 object-contain"
                    />

                    <p className="mt-3 text-sm font-bold">
                      {item.achievement.name}
                    </p>

                    <p className="mt-2 text-xs text-gray-500">
                      {item.achievement.criterion}
                    </p>
                  </div>
                ))}
              </div>
            </div>
          </div>
        </div>

        {/* MINHAS ATIVIDADES */}
        <h2 className="mb-6 mt-10 text-3xl font-bold">Minhas atividades</h2>

        <div className="grid grid-cols-4 gap-6">
          {createdActivities.map((activity: any) => (
            <ActivityCard
              key={activity.id}
              activity={activity}
              onClick={() => setSelectedActivity(activity)}
            />
          ))}
        </div>

        {/* HISTÓRICO */}
        <h2 className="mb-6 mt-10 text-3xl font-bold">
          Histórico de atividades
        </h2>

        <div className="grid grid-cols-4 gap-6">
          {history.map((activity: any) => (
            <ActivityCard
              key={activity.id}
              activity={activity}
              onClick={() => setSelectedActivity(activity)}
            />
          ))}
        </div>
      </div>

      {selectedActivity && (
        <ActivityDetailsModal
          activity={selectedActivity}
          onClose={() => setSelectedActivity(null)}
        />
      )}
    </>
  );
}
