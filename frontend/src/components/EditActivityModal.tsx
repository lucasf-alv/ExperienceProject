import { X, Upload } from "lucide-react";
import { useState } from "react";
import type { Activity } from "../types/activity";
import { updateActivity } from "../services/activityService";
import { MapViewer } from "./MapViewer";
interface Props {
  activity: Activity;
  onClose: () => void;
}

export function EditActivityModal({ activity, onClose }: Props) {
  const [title, setTitle] = useState(activity.title);
  const [description, setDescription] = useState(activity.description);
  const [latitude, setLatitude] = useState(activity.activityAddress.latitude);

  const [longitude, setLongitude] = useState(
    activity.activityAddress.longitude,
  );

  const [scheduleDate, setScheduleDate] = useState(
    activity.scheduled_Date.slice(0, 16),
  );

  const [type, setType] = useState(activity.activityType.name);

  const [isPrivate, setIsPrivate] = useState(activity.Private);

  const [image, setImage] = useState<File | null>(null);

  const [preview, setPreview] = useState(activity.image);

  function handleImage(e: React.ChangeEvent<HTMLInputElement>) {
    const file = e.target.files?.[0];

    if (!file) return;

    setImage(file);
    setPreview(URL.createObjectURL(file));
  }

  async function handleUpdate() {
    const formData = new FormData();

    formData.append("title", title);
    formData.append("description", description);
    formData.append("scheduleDate", scheduleDate);
    formData.append("type", type);
    formData.append("Private", String(isPrivate));

    formData.append("latitute", String(activity.activityAddress.latitude));

    formData.append("longitude", String(activity.activityAddress.longitude));

    if (image) {
      formData.append("image", image);
    }

    try {
      await updateActivity(activity.id, formData);

      alert("Atividade atualizada!");
      onClose();
    } catch (error) {
      console.log(error);
      alert("Erro ao atualizar atividade");
    }
  }

  return (
    <div className="fixed inset-0 z-[60] flex items-center justify-center bg-black/60">
      <div className="relative max-h-[90vh] w-[650px] overflow-y-auto rounded-2xl bg-white p-8 shadow-xl">
        <button
          onClick={onClose}
          className="absolute right-5 top-5 rounded-full p-2 hover:bg-gray-100"
        >
          <X />
        </button>

        <h2 className="mb-6 text-3xl font-bold">Editar atividade</h2>

        {/* Imagem */}
        <label className="mb-5 block cursor-pointer">
          <div className="relative overflow-hidden rounded-xl border-2 border-dashed">
            <img src={preview} className="h-48 w-full object-cover" />

            <div className="absolute inset-0 flex items-center justify-center bg-black/40 text-white opacity-0 transition hover:opacity-100">
              <Upload size={30} />

              <span className="ml-2">Alterar imagem</span>
            </div>
          </div>

          <input
            type="file"
            accept="image/png,image/jpeg"
            className="hidden"
            onChange={handleImage}
          />
        </label>

        {/* Titulo */}
        <input
          className="mb-3 w-full rounded-lg border p-3"
          placeholder="Título"
          value={title}
          onChange={(e) => setTitle(e.target.value)}
        />

        {/* Descrição */}
        <textarea
          className="mb-3 h-28 w-full rounded-lg border p-3"
          placeholder="Descrição"
          value={description}
          onChange={(e) => setDescription(e.target.value)}
        />

        <div className="grid grid-cols-2 gap-4">
          <input
            type="datetime-local"
            className="rounded-lg border p-3"
            value={scheduleDate}
            onChange={(e) => setScheduleDate(e.target.value)}
          />

          <input
            className="rounded-lg border p-3"
            value={type}
            onChange={(e) => setType(e.target.value)}
          />
        </div>
        <h3 className="mb-3 mt-5 font-bold">Ponto de encontro</h3>

        <MapViewer
          latitude={latitude}
          longitude={longitude}
          editable
          onLocationChange={(lat, lng) => {
            setLatitude(lat);
            setLongitude(lng);
          }}
        />

        {/* Aprovação */}
        <div className="mt-5 flex items-center justify-between rounded-lg bg-gray-100 p-4">
          <div>
            <p className="font-semibold">Requer aprovação</p>

            <span className="text-sm text-gray-500">
              Usuários precisam ser aprovados para entrar
            </span>
          </div>

          <button
            onClick={() => setIsPrivate(!isPrivate)}
            className={`h-7 w-14 rounded-full p-1 transition ${
              isPrivate ? "bg-green-500" : "bg-gray-300"
            }`}
          >
            <div
              className={`h-5 w-5 rounded-full bg-white transition ${
                isPrivate ? "translate-x-7" : ""
              }`}
            />
          </button>
        </div>

        {/* Botões */}
        <div className="mt-8 flex justify-end gap-3">
          <button onClick={onClose} className="rounded-lg border px-6 py-3">
            Cancelar
          </button>

          <button
            onClick={handleUpdate}
            className="rounded-lg bg-green-500 px-6 py-3 text-white"
          >
            Salvar alterações
          </button>
        </div>
      </div>
    </div>
  );
}
