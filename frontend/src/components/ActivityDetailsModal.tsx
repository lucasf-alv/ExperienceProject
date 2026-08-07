import { X, CalendarDays, Users, Lock, Unlock, Check } from "lucide-react";

import type { Activity } from "../types/activity";
import { MapViewer } from "./MapViewer";
import { useEffect, useState } from "react";
import api from "../services/api";
import { subscribeActivity } from "../services/activityService";
import { EditActivityModal } from "../components/EditActivityModal";
import defaultAvatar from "../assets/images/avatar.png";

interface Props {
  activity: Activity;
  onClose: () => void;
}

export function ActivityDetailsModal({ activity, onClose }: Props) {
  const [participants, setParticipants] = useState<any[]>([]);
  const [loggedUser, setLoggedUser] = useState<any>(null);
  const [editMode, setEditMode] = useState(false);
  const [cancelled, setCancelled] = useState(false);

  useEffect(() => {
    setCancelled(!!activity.deleted_At);
  }, [activity]);
  const [completed, setCompleted] = useState(false);

  useEffect(() => {
    setCancelled(!!activity.deleted_At);
    setCompleted(!!activity.completed_At);
  }, [activity]);
  // participação do usuário logado nessa atividade
  const [myParticipation, setMyParticipation] = useState<any>(null);
  const canSubscribe = () => {
    const activityDate = new Date(activity.scheduled_Date);
    const now = new Date();

    const diff = activityDate.getTime() - now.getTime();

    const oneDay = 24 * 60 * 60 * 1000;

    return diff > oneDay;
  };

  const isCreator = loggedUser?.id === activity?.creator?.id;

  const isCheckInAvailable = () => {
    const activityDate = new Date(activity.scheduled_Date);
    const now = new Date();

    const diff = activityDate.getTime() - now.getTime();

    const oneDay = 24 * 60 * 60 * 1000;

    return diff >= 0 && diff <= oneDay;
  };
  const [checkCode, setCheckCode] = useState("");
  async function handleCheckIn() {
    try {
      await api.put(`/activities/${activity.id}/check-in`, {
        confirmationCode: checkCode,
      });

      alert("Check-in realizado!");

      await loadData();
    } catch (error) {
      console.log(error);

      alert("Código inválido");
    }
  }
  async function handleCancelActivity() {
    try {
      await api.delete(`/activities/${activity.id}/delete`);

      setCancelled(true);
    } catch (error) {
      console.log(error);

      alert("Erro ao cancelar atividade");
    }
  }

  async function loadData() {
    try {
      const participantsResponse = await api.get(
        `/activities/${activity.id}/participants`,
      );

      const participantsData = participantsResponse.data;

      setParticipants(participantsData);

      const userResponse = await api.get("/user");

      const currentUser = userResponse.data;

      setLoggedUser(currentUser);

      const myPart = participantsData.find(
        (participant: any) => participant.user?.id === currentUser.id,
      );

      setMyParticipation(myPart ?? null);
    } catch (error) {
      console.log("Erro ao carregar dados:", error);
    }
  }

  useEffect(() => {
    if (!activity) return;

    loadData();
  }, [activity]);
  const getActivityDate = () => {
    return new Date(activity.scheduled_Date);
  };

  const isWithin24Hours = () => {
    const now = new Date();
    const activityDate = getActivityDate();

    const diff = activityDate.getTime() - now.getTime();

    const oneDay = 24 * 60 * 60 * 1000;

    return diff > 0 && diff <= oneDay;
  };

  const hasStarted = () => {
    const now = new Date();
    const activityDate = getActivityDate();

    return now >= activityDate;
  };

  const canConclude = () => {
    const now = new Date().getTime();
    const activityDate = new Date(activity.scheduled_Date).getTime();

    const oneHour = 60 * 60 * 1000;

    return now >= activityDate + oneHour;
  };

  async function handleSubscribe() {
    try {
      await subscribeActivity(activity.id);

      await loadData();

      alert("Inscrição realizada com sucesso!");
    } catch (error) {
      console.log("Erro ao participar:", error);

      alert("Não foi possível participar da atividade.");
    }
  }
  async function handleConcludeActivity() {
    try {
      await api.put(`/activities/${activity.id}/conclude`);

      setCompleted(true);

      alert("Atividade concluída!");
    } catch (error) {
      console.log(error);
      alert("Erro ao concluir atividade.");
    }
  }

  async function handleUnsubscribe() {
    try {
      await api.delete(`/activities/${activity.id}/unsubscribe`);

      // remove visualmente imediatamente
      setParticipants((prev) =>
        prev.filter((participant) => participant.user?.id !== loggedUser.id),
      );

      // tira a participação do usuário
      setMyParticipation(null);

      alert("Inscrição cancelada!");
    } catch (error) {
      console.log("Erro ao cancelar inscrição:", error);
      alert("Não foi possível cancelar a inscrição.");
    }
  }

  async function approveParticipant(participantId: number) {
    try {
      await api.put(`/activities/${participantId}/approve`);

      await loadData();
    } catch (error) {
      console.log("Erro ao aprovar participante", error);
    }
  }
  const orderedParticipants = [...participants].sort((a, b) => {
    const aIsCreator = a.user?.id === activity.creator?.id;
    const bIsCreator = b.user?.id === activity.creator?.id;

    if (aIsCreator) return -1;
    if (bIsCreator) return 1;

    return 0;
  });

  return (
    <>
      <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/50">
        <div className="relative w-[1100px] rounded-2xl bg-white shadow-xl">
          <button
            onClick={onClose}
            className="absolute right-5 top-5 z-10 rounded-full p-2 hover:bg-gray-100"
          >
            <X size={22} />
          </button>

          <div className="grid grid-cols-2 gap-10 p-8">
            {/* ESQUERDA */}

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
                  {participants.length} participantes
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
                  <>
                    {cancelled ? (
                      <button
                        disabled
                        className="w-full rounded-lg bg-red-600 px-6 py-3 text-white"
                      >
                        Atividade cancelada
                      </button>
                    ) : completed ? (
                      <button
                        disabled
                        className="w-full rounded-lg bg-gray-400 px-6 py-3 text-white"
                      >
                        Atividade concluída
                      </button>
                    ) : canConclude() ? (
                      <button
                        onClick={handleConcludeActivity}
                        className="w-full rounded-lg bg-green-600 px-6 py-3 text-white"
                      >
                        Concluir atividade
                      </button>
                    ) : isWithin24Hours() ? (
                      <>
                        <div className="rounded-lg bg-gray-100 p-4">
                          <p className="font-bold">Código do check-in</p>

                          <p className="text-2xl font-bold">
                            {activity.confirmation_code}
                          </p>
                        </div>

                        <button
                          onClick={handleCancelActivity}
                          className="mt-3 w-full rounded-lg bg-red-500 px-6 py-3 text-white"
                        >
                          Encerrar atividade
                        </button>
                      </>
                    ) : (
                      <button
                        onClick={() => setEditMode(true)}
                        className="rounded-lg bg-green-500 px-6 py-3 text-white"
                      >
                        Editar atividade
                      </button>
                    )}
                  </>
                ) : (
                  <>
                    {hasStarted() ? (
                      <button
                        disabled
                        className="w-full rounded-lg bg-blue-500 px-6 py-3 text-white"
                      >
                        Atividade em andamento
                      </button>
                    ) : !myParticipation ? (
                      canSubscribe() ? (
                        <button
                          onClick={handleSubscribe}
                          className="rounded-lg bg-green-500 px-6 py-3 text-white"
                        >
                          Participar
                        </button>
                      ) : (
                        <button
                          disabled
                          className="rounded-lg bg-gray-400 px-6 py-3 text-white"
                        >
                          Inscrições encerradas
                        </button>
                      )
                    ) : !myParticipation.approved ? (
                      <button
                        disabled
                        className="w-full rounded-lg bg-gray-400 px-6 py-3 text-white"
                      >
                        Aguardando aprovação
                      </button>
                    ) : isCheckInAvailable() ? (
                      myParticipation.confirmed_at ? (
                        <button
                          disabled
                          className="w-full rounded-lg bg-gray-400 px-6 py-3 text-white"
                        >
                          Check-in realizado
                        </button>
                      ) : (
                        <>
                          <p className="mb-2 font-bold">Realizar check-in</p>

                          <input
                            className="mb-3 w-full rounded border p-2"
                            placeholder="Código de confirmação"
                            value={checkCode}
                            onChange={(e) => setCheckCode(e.target.value)}
                          />

                          <button
                            onClick={handleCheckIn}
                            className="w-full rounded-lg bg-green-500 px-6 py-3 text-white"
                          >
                            Confirmar check-in
                          </button>
                        </>
                      )
                    ) : (
                      <button
                        onClick={handleUnsubscribe}
                        className="rounded-lg bg-red-500 px-6 py-3 text-white"
                      >
                        Desinscrever
                      </button>
                    )}
                  </>
                )}
              </div>
            </div>

            {/* DIREITA */}

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

              <div className="max-h-[350px] space-y-3 overflow-y-auto pr-2">
                {orderedParticipants.map((participant) => {
                  const user = participant.user ?? participant;

                  const isParticipantCreator = user.id === activity.creator?.id;

                  return (
                    <div
                      key={participant.id}
                      className={`flex items-center justify-between rounded-lg border p-3 ${
                        isParticipantCreator
                          ? "border-green-500 bg-green-50"
                          : ""
                      }`}
                    >
                      <div className="flex items-center gap-3">
                        <img
                          src={user.avatar || defaultAvatar}
                          onError={(e) => {
                            e.currentTarget.src = defaultAvatar;
                          }}
                          className="h-10 w-10 rounded-full object-cover"
                        />

                        <div className="flex items-center gap-2">
                          <span className="font-medium">{user.name}</span>

                          {isParticipantCreator && (
                            <span className="rounded-full bg-green-100 px-2 py-1 text-xs font-semibold text-green-700">
                              Organizador
                            </span>
                          )}
                        </div>
                      </div>

                      {isCreator && participant.approved === false && (
                        <button
                          onClick={() => approveParticipant(participant.id)}
                          className="rounded-lg bg-green-500 px-3 py-2 text-white"
                        >
                          Aprovar
                        </button>
                      )}

                      {participant.approved && !isParticipantCreator && (
                        <span className="text-green-600">Aprovado</span>
                      )}
                    </div>
                  );
                })}
              </div>
            </div>
          </div>
        </div>
      </div>

      {editMode && (
        <EditActivityModal
          activity={activity}
          onClose={() => setEditMode(false)}
        />
      )}
    </>
  );
}
