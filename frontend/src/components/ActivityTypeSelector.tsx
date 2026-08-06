import { useEffect, useState } from "react";
import api from "../services/api";

interface ActivityType {
  id: number;
  name: string;
  description: string;
  image: string;
}

interface Props {
  selectedType: string | null;
  onSelect: (name: string) => void;
}

export function ActivityTypeSelector({ selectedType, onSelect }: Props) {
  const [types, setTypes] = useState<ActivityType[]>([]);

  useEffect(() => {
    async function loadTypes() {
      try {
        const response = await api.get("/activities/types");
        setTypes(response.data);
      } catch (error) {
        console.log("Erro ao carregar tipos", error);
      }
    }

    loadTypes();
  }, []);

  return (
    <div className="grid grid-cols-2 gap-3">
      {types.map((type) => (
        <button
          type="button"
          key={type.id}
          onClick={() => onSelect(type.name)}
          className={`flex items-center gap-3 rounded-xl border p-3 transition
          ${
            selectedType === type.name
              ? "border-green-500 bg-green-50"
              : "border-gray-200 hover:bg-gray-50"
          }`}
        >
          <div>
            <p className="font-semibold">{type.name}</p>

            <span className="text-sm text-gray-500">{type.description}</span>
          </div>
        </button>
      ))}
    </div>
  );
}
