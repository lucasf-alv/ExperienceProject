import { X } from "lucide-react";
import { ActivityTypeSelector } from "../components/ActivityTypeSelector";
import { MapPicker } from "../components/MapPicker";
import { useEffect, useState } from "react";
import api from "../services/api";
interface CreateActivityModalProps {
  open: boolean;
  onClose: () => void;
}

export function CreateActivityModal({
  open,
  onClose,
}: CreateActivityModalProps) {
  if (!open) return null;

  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [date, setDate] = useState("");
  const [image, setImage] = useState<File | null>(null);
  const [selectedType, setSelectedType] = useState<string | null>(null);

  const [latitude, setLatitude] = useState<number | null>(null);
  const [longitude, setLongitude] = useState<number | null>(null);

  const [requiresApproval, setRequiresApproval] = useState(false);
  const [imagePreview, setImagePreview] = useState<string | null>(null);
  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault();

    if (!image || !selectedType || latitude === null || longitude === null) {
      console.log("Preencha todos os campos");
      return;
    }

    const formData = new FormData();

    formData.append("title", title);
    formData.append("description", description);
    formData.append("scheduleDate", date);
    formData.append("type", selectedType!);
    formData.append("Private", requiresApproval.toString());
    formData.append("latitute", latitude.toString());
    formData.append("longitude", longitude.toString());

    formData.append("image", image);

    try {
      await api.post("/activities/new", formData, {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      });

      console.log("Atividade criada com sucesso!");

      onClose();
    } catch (error) {
      console.log("Erro ao criar atividade", error);
    }
  }

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/50 p-6">
      <div className="max-h-[95vh] w-full max-w-6xl overflow-y-auto rounded-2xl bg-white shadow-xl">
        {/* Cabeçalho */}
        <div className="flex items-center justify-between border-b px-8 py-6">
          <h2 className="text-3xl font-bold">Nova atividade</h2>

          <button
            onClick={onClose}
            className="rounded-lg p-2 transition hover:bg-gray-100"
          >
            <X />
          </button>
        </div>

        <form onSubmit={handleSubmit} className="grid grid-cols-2 gap-10 p-8">
          {/* COLUNA ESQUERDA */}
          <div className="space-y-6">
            <div>
              <label className="mb-2 block font-semibold">Imagem</label>

              <div className="flex h-64 items-center justify-center rounded-xl border-2 border-dashed border-gray-300 bg-gray-50">
                <div>
                  <label className="mb-3 block font-semibold">Imagem</label>

                  <label
                    htmlFor="image-upload"
                    className="flex h-40 cursor-pointer flex-col items-center justify-center rounded-xl border-2 border-dashed border-gray-300 transition hover:border-green-500 hover:bg-green-50"
                  >
                    {imagePreview ? (
                      <img
                        src={imagePreview}
                        alt="Preview"
                        className="h-full w-full rounded-xl object-cover"
                      />
                    ) : (
                      <>
                        <span className="text-gray-500">
                          Clique para selecionar uma imagem
                        </span>

                        <span className="mt-2 text-sm text-gray-400">
                          PNG ou JPG
                        </span>
                      </>
                    )}
                  </label>

                  <input
                    id="image-upload"
                    type="file"
                    accept="image/png,image/jpeg"
                    className="hidden"
                    onChange={(e) => {
                      const file = e.target.files?.[0] ?? null;

                      setImage(file);

                      if (file) {
                        setImagePreview(URL.createObjectURL(file));
                      }
                    }}
                  />
                </div>
              </div>
            </div>

            <div>
              <label className="mb-2 block font-semibold">Título</label>

              <input
                type="text"
                placeholder="Título"
                value={title}
                onChange={(e) => setTitle(e.target.value)}
                className="w-full rounded-xl border border-gray-300 bg-white px-4 py-3 text-gray-800 outline-none transition focus:border-green-500 focus:ring-2 focus:ring-green-200"
              />
            </div>

            <div>
              <label className="mb-2 block font-semibold">Descrição</label>

              <textarea
                value={description}
                onChange={(e) => setDescription(e.target.value)}
                placeholder="Descrição da atividade"
                rows={5}
                className="w-full resize-none rounded-lg border border-gray-300 p-3 focus:border-green-500 focus:outline-none"
              />
            </div>

            <div>
              <label className="mb-2 block font-semibold">Data</label>

              <input
                type="datetime-local"
                value={date}
                onChange={(e) => setDate(e.target.value)}
                className="w-full rounded-xl border border-gray-300 bg-white px-4 py-3 text-gray-800 outline-none transition focus:border-green-500 focus:ring-2 focus:ring-green-200"
              />
            </div>
          </div>

          {/* COLUNA DIREITA */}
          <div className="space-y-6">
            <div>
              <label className="mb-3 block font-semibold">
                Tipo de atividade
              </label>

              <ActivityTypeSelector
                selectedType={selectedType}
                onSelect={setSelectedType}
              />
            </div>

            <div>
              <label className="mb-3 block font-semibold">
                Ponto de encontro
              </label>

              <MapPicker
                onLocationChange={(lat, lng) => {
                  setLatitude(lat);
                  setLongitude(lng);
                }}
              />
            </div>

            <label className="mb-3 block font-semibold">
              Requer aprovação?
            </label>

            <div className="flex gap-3">
              <button
                type="button"
                onClick={() => setRequiresApproval(true)}
                className={`flex-1 rounded-xl px-6 py-3 font-semibold transition ${
                  requiresApproval
                    ? "bg-green-500 text-white"
                    : "bg-gray-100 text-gray-700 hover:bg-gray-200"
                }`}
              >
                Sim
              </button>

              <button
                type="button"
                onClick={() => setRequiresApproval(false)}
                className={`flex-1 rounded-xl px-6 py-3 font-semibold transition ${
                  !requiresApproval
                    ? "bg-green-500 text-white"
                    : "bg-gray-100 text-gray-700 hover:bg-gray-200"
                }`}
              >
                Não
              </button>
            </div>

            <div className="flex justify-end gap-4 pt-8">
              <button
                type="button"
                onClick={onClose}
                className="rounded-lg border px-6 py-3"
              >
                Cancelar
              </button>

              <button
                type="submit"
                className="rounded-lg bg-green-500 px-8 py-3 font-semibold text-white transition hover:bg-green-600"
              >
                Criar atividade
              </button>
            </div>
          </div>
        </form>
      </div>
    </div>
  );
}
