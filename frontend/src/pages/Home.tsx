import { Navbar } from "../components/NavBar";
import { ActivityCard } from "../components/ActivityCard";
import { Activities } from "../pages/Activities";
import { ActivityTypeCard } from "../components/ActivityTypeCard";
import corridaImg from "../assets/images/corrida.jpeg";
import ciclismoImg from "../assets/images/ciclismo.jpeg";
import futebolImg from "../assets/images/futebol.jpeg";
import yogaImg from "../assets/images/yoga.jpeg";
import CrossfitImg from "../assets/images/crossfit.jpeg";
import basqueteImg from "../assets/images/basquete.jpg";
import xadrezImg from "../assets/images/xadrez.jpeg";

import { PreferencesModal } from "../components/PreferencesModal";
import { useEffect, useState } from "react";
import api from "../services/api";
import { RecommendedActivities } from "../components/RecommendedActivities";
export function Home() {
  const [activityTypes, setActivityTypes] = useState([]);
  const [showPreferences, setShowPreferences] = useState(false);
  const [preferences, setPreferences] = useState([]);
  const activityImages: Record<string, string> = {
    Corrida: corridaImg,
    Ciclismo: ciclismoImg,
    Futebol: futebolImg,
    Yoga: yogaImg,
    Crossfit: CrossfitImg,
    Basquete: basqueteImg,
    Xadrez: xadrezImg,
  };

  useEffect(() => {
    async function loadPreferences() {
      try {
        const response = await api.get("user/preferences");

        if (response.data.length === 0) {
          const types = await api.get("activities/types");

          setActivityTypes(types.data);
          setShowPreferences(true);
        } else {
          setPreferences(response.data);
        }
      } catch (error) {
        console.log("Erro ao carregar preferências", error);
      }
    }

    loadPreferences();
  }, []);

  return (
    <>
      <Navbar />

      {showPreferences && (
        <PreferencesModal
          types={activityTypes}
          onClose={() => setShowPreferences(false)}
        />
      )}
      <div className="mx-auto mt-10 max-w-7xl px-8">
        {/* Recomendados */}

        <RecommendedActivities />
        <h2 className="mb-6 text-3xl font-bold">Tipos de atividade</h2>

        <div className="mb-14 flex flex-wrap gap-6">
          {preferences.map((pref) => (
            <ActivityTypeCard
              key={pref.id}
              name={pref.activityType.name}
              image={activityImages[pref.activityType.name]}
            />
          ))}
        </div>
        <div className="grid grid-cols-2 gap-8">
          {preferences.map((pref) => (
            <div key={pref.id}>
              <h2 className="mb-6 text-3xl font-bold">
                {pref.activityType.name}
              </h2>

              <Activities type={pref.activityType.name} />
            </div>
          ))}
        </div>
      </div>
    </>
  );
}
