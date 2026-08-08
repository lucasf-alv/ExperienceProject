import { useParams } from "react-router-dom";
import { Navbar } from "../components/NavBar";
import { Activities } from "./Activities";
import { useEffect, useState } from "react";
import api from "../services/api";
import corridaImg from "../assets/images/corrida.jpeg";
import ciclismoImg from "../assets/images/ciclismo.jpeg";
import futebolImg from "../assets/images/futebol.jpeg";
import yogaImg from "../assets/images/yoga.jpeg";
import CrossfitImg from "../assets/images/crossfit.jpeg";
import basqueteImg from "../assets/images/basquete.jpg";
import xadrezImg from "../assets/images/xadrez.jpeg";
import caminhadaImg from "../assets/images/caminhada.jpg";
import natacaoImg from "../assets/images/natacao.jpeg";
import tenisImg from "../assets/images/tenis.jpeg";
import voleiImg from "../assets/images/volei.png";
import musculacaoImg from "../assets/images/musculacao.jpeg";
import { ActivityTypeCard } from "../components/ActivityTypeCard";
import { RecommendedActivities } from "../components/RecommendedActivities";
export function ActivitiesByType() {
  const { type } = useParams();
  const [preferences, setPreferences] = useState([]);
  const activityImages: Record<string, string> = {
    Corrida: corridaImg,
    Futebol: futebolImg,
    Ciclismo: ciclismoImg,
    Yoga: yogaImg,
    Crossfit: CrossfitImg,
    Basquete: basqueteImg,
    Xadrez: xadrezImg,
    Caminhada: caminhadaImg,
    Natação: natacaoImg,
    Tênis: tenisImg,
    Vôlei: voleiImg,
    Musculação: musculacaoImg,
  };
  useEffect(() => {
    async function loadPreferences() {
      try {
        const response = await api.get("user/preferences");
        setPreferences(response.data);
      } catch (error) {
        console.log(error);
      }
    }

    loadPreferences();
  }, []);

  return (
    <>
      <Navbar />

      <div className="mx-auto mt-10 max-w-7xl px-8">
        <RecommendedActivities type={type!} />

        <h2 className="mb-6 mt-12 text-3xl font-bold">
          Todas as atividades de {type}
        </h2>

        <Activities type={type!} />

        <h2 className="mb-6 mt-12 text-3xl font-bold">
          Outros tipos de atividade
        </h2>

        <div className="mb-10 flex flex-wrap gap-6">
          {preferences
            .filter((pref) => pref.activityType.name !== type)
            .map((pref) => (
              <ActivityTypeCard
                key={pref.id}
                name={pref.activityType.name}
                image={activityImages[pref.activityType.name]}
              />
            ))}
        </div>
      </div>
    </>
  );
}
