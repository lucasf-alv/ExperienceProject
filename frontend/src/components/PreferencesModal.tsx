import { useState } from "react";
import api from "../services/api";

interface PreferencesModalProps {
  types: {
    id: number;
    name: string;
  }[];
  onClose: () => void;
}

export function PreferencesModal({ types, onClose }: PreferencesModalProps) {
  const [selected, setSelected] = useState<number[]>([]);

  function toggleType(id: number) {
    setSelected((prev) =>
      prev.includes(id) ? prev.filter((item) => item !== id) : [...prev, id],
    );
  }

  async function savePreferences() {
    console.log(selected);
    await api.post("user/preferences/define", selected);

    onClose();
  }

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/50">
      <div className="rounded-xl bg-white p-8">
        <h2 className="mb-5 text-2xl font-bold">
          Escolha suas atividades favoritas
        </h2>

        <div className="grid grid-cols-2 gap-4">
          {types.map((type) => (
            <button
              key={type.id}
              onClick={() => toggleType(type.id)}
              className={`
                rounded-lg border p-4
                ${
                  selected.includes(type.id)
                    ? "bg-blue-500 text-white"
                    : "bg-white"
                }
              `}
            >
              {type.name}
            </button>
          ))}
        </div>

        <button
          onClick={savePreferences}
          className="mt-6 w-full rounded-lg bg-black p-3 text-white"
        >
          Confirmar
        </button>
      </div>
    </div>
  );
}
