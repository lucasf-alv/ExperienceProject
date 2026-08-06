import { useEffect, useState } from "react";
import api from "../services/api";
import { ActivityCard } from "../components/ActivityCard";

interface Props {
  type?: string;
}

export function RecommendedActivities({ type }: Props) {
  const [activities, setActivities] = useState<any[]>([]);

  useEffect(() => {
    async function loadRecommended() {
      try {
        const preferencesResponse = await api.get("user/preferences");
        const activitiesResponse = await api.get("/activities");

        const preferences = preferencesResponse.data;
        const allActivities = activitiesResponse.data.content;

        let recommended = allActivities.filter((activity: any) =>
          preferences.some(
            (preference: any) =>
              preference.activityType.id === activity.activityType.id,
          ),
        );

        // Se recebeu um tipo, filtra somente aquele tipo
        if (type) {
          recommended = recommended.filter(
            (activity: any) =>
              activity.activityType.name.toLowerCase() === type.toLowerCase(),
          );
        }

        setActivities(recommended.slice(0, 8));
      } catch (error) {
        console.log("Erro ao carregar recomendados", error);
      }
    }

    loadRecommended();
  }, [type]);

  return (
    <>
      <h1 className="mb-6 text-3xl font-bold">
        {type ? `Populares em ${type}` : "Recomendados para você"}
      </h1>

      <div className="mb-14 grid grid-cols-4 gap-6">
        {activities.map((activity) => (
          <ActivityCard
            key={activity.id}
            title={activity.title}
            image={activity.image}
            date={activity.scheduled_Date}
            participants={0}
          />
        ))}
      </div>
    </>
  );
}
