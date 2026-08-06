import { X, CalendarDays, Users, Lock, Unlock } from "lucide-react";
import type { Activity } from "../types/activity";
import { MapViewer } from "./MapViewer";
import { useEffect, useState } from "react";
import api from "../services/api";

interface Props {
  activity: Activity;
  onClose: () => void;
}
export function ActivityDetailsModal({ activity, onClose }: Props) {
  const [participants, setParticipants] = useState([]);
  const [loggedUser, setLoggedUser] = useState<any>(null);
  const isCreator = loggedUser?.id === activity?.creator.id;
  useEffect(() => {
    if (!activity) return;

    api
      .get(`/activities/${activity.id}/participants`)
      .then((res) => setParticipants(res.data));

    api.get("/user").then((res) => setLoggedUser(res.data));
  }, [activity]);

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/50">
      <div className="relative w-350px rounded-2xl bg-white shadow-xl">
        {/* Botão fechar */}
        <button
          onClick={() => {
            console.log("clicou no fechar");
            onClose();
          }}
          className="absolute right-5 top-5 rounded-full p-2 transition hover:bg-gray-100"
        >
          <X size={22} />
        </button>
        <div className="grid grid-cols-2 gap-10 p-8">
          {/* Coluna esquerda */}
          <div>
            <img
              src={activity.image}
              alt={activity.title}
              className="mb-6 h-64 w-full rounded-xl object-cover"
            />

            <h2 className="mb-4 text-3xl font-bold">{activity.title}</h2>

            <p className="mb-6 text-gray-600">{activity.description}</p>

            <div className="space-y-3">
              <div className="flex items-center gap-2">
                <CalendarDays className="text-green-500" size={20} />
                {activity.scheduled_Date}
              </div>

              <div className="flex items-center gap-2">
                <Users className="text-green-500" size={20} />
                {activity.participants} participantes
              </div>

              <div className="flex items-center gap-2">
                {activity.Private ? (
                  <>
                    <Lock className="text-green-500" size={20} />
                    Requer aprovação
                  </>
                ) : (
                  <>
                    <Unlock className="text-green-500" size={20} />
                    Entrada livre
                  </>
                )}
              </div>
            </div>

            {isCreator ? (
              <button className="rounded-lg bg-green-500 px-6 py-3 text-white">
                Editar atividade
              </button>
            ) : (
              <button className="rounded-lg bg-green-500 px-6 py-3 text-white">
                Participar
              </button>
            )}
          </div>

          {/* Coluna direita */}
          <div>
            <h3 className="mb-4 text-2xl font-bold">Ponto de encontro</h3>

            <MapViewer
              latitude={activity.activityAddress.latitude}
              longitude={activity.activityAddress.longitude}
            />

            <h3 className="mb-4 text-2xl font-bold">Participantes</h3>

            <div className="space-y-3">
              {participants.map((user: any) => (
                <div key={user.id} className="flex items-center gap-3">
                  <img src={user.avatar} className="h-10 w-10 rounded-full" />

                  <span>{user.name}</span>
                </div>
              ))}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
