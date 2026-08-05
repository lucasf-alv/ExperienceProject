import { useEffect, useState } from "react";
import api from "../services/api";
import { ActivityCard } from "../components/ActivityCard";

export function RecommendedActivities() {
  const [activities, setActivities] = useState<any[]>([]);

  useEffect(() => {
    async function loadRecommended() {
      try {
        const preferencesResponse = await api.get("user/preferences");
        const activitiesResponse = await api.get("/activities");

        const preferences = preferencesResponse.data;
        const allActivities = activitiesResponse.data.content;

        const recommended = allActivities.filter((activity: any) =>
          preferences.some(
            (preference: any) =>
              preference.activityType.id === activity.activityType.id,
          ),
        );

        setActivities(recommended);
      } catch (error) {
        console.log("Erro ao carregar recomendados", error);
      }
    }

    loadRecommended();
  }, []);

  return (
    <>
      <h1 className="mb-6 text-3xl font-bold">Recomendados para você</h1>

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
