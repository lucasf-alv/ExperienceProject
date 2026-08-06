import { X, CalendarDays, Users, Lock, Unlock } from "lucide-react";
import type { Activity } from "../types/activity";
import { MapViewer } from "./MapViewer";
import { useEffect, useState } from "react";
import api from "../services/api";
import { subscribeActivity } from "../services/activityService";
import { EditActivityModal } from "../components/EditActivityModal";

interface Props {
  activity: Activity;
  onClose: () => void;
}

export function ActivityDetailsModal({ activity, onClose }: Props) {
  const [participants, setParticipants] = useState<any[]>([]);
  const [loggedUser, setLoggedUser] = useState<any>(null);
  const [editMode, setEditMode] = useState(false);
  const [participantCount, setParticipantCount] = useState(0);

  const isCreator = loggedUser?.id === activity?.creator?.id;
  useEffect(() => {
    if (!activity) return;

    async function loadData() {
      try {
        const participantsResponse = await api.get(
          `/activities/${activity.id}/participants`,
        );

        setParticipants(participantsResponse.data);
        setParticipantCount(participantsResponse.data.length);

        const userResponse = await api.get("/user");

        setLoggedUser(userResponse.data);
      } catch (error) {
        console.log("Erro ao carregar dados da atividade", error);
      }
    }

    loadData();
  }, [activity]);

  async function handleSubscribe() {
    try {
      await subscribeActivity(activity.id);

      const response = await api.get(`/activities/${activity.id}/participants`);

      setParticipants(response.data);

      alert("Inscrição realizada com sucesso!");
    } catch (error) {
      console.log("Erro ao participar:", error);
      alert("Não foi possível participar da atividade.");
    }
  }

  return (
    <>
      <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/50">
        <div className="relative w-400px rounded-2xl bg-white shadow-xl">
          {/* Botão fechar */}
          <button
            onClick={onClose}
            className="absolute right-5 top-5 z-10 rounded-full p-2 transition hover:bg-gray-100"
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
                  {participantCount} participantes
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

              <div className="mt-6">
                {isCreator ? (
                  <button
                    onClick={() => setEditMode(true)}
                    className="rounded-lg bg-green-500 px-6 py-3 text-white"
                  >
                    Editar atividade
                  </button>
                ) : (
                  <button
                    onClick={handleSubscribe}
                    className="rounded-lg bg-green-500 px-6 py-3 text-white"
                  >
                    Participar
                  </button>
                )}
              </div>
            </div>

            {/* Coluna direita */}
            <div>
              <h3 className="mb-4 text-2xl font-bold">Ponto de encontro</h3>

              {activity.activityAddress?.latitude &&
              activity.activityAddress?.longitude ? (
                <MapViewer
                  latitude={activity.activityAddress.latitude}
                  longitude={activity.activityAddress.longitude}
                />
              ) : (
                <p className="text-gray-500">Localização não informada</p>
              )}

              <h3 className="mb-4 mt-6 text-2xl font-bold">Participantes</h3>

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

      {/* Modal de edição */}
      {editMode && (
        <EditActivityModal
          activity={activity}
          onClose={() => setEditMode(false)}
        />
      )}
    </>
  );
}
