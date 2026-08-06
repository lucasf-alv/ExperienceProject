import { useEffect, useState } from "react";
import api from "../services/api";
import { ActivityCard } from "../components/ActivityCard";
import { ActivityDetailsModal } from "../components/ActivityDetailsModal";
import type { Activity } from "../types/activity";

interface Props {
  type?: string;
}

export function RecommendedActivities({ type }: Props) {
  const [activities, setActivities] = useState<Activity[]>([]);
  const [selectedActivity, setSelectedActivity] = useState<Activity | null>(
    null,
  );

  useEffect(() => {
    async function loadRecommended() {
      try {
        const preferencesResponse = await api.get("user/preferences");
        const activitiesResponse = await api.get("/activities");

        const preferences = preferencesResponse.data;
        const allActivities = activitiesResponse.data.content;

        let recommended = allActivities.filter((activity: Activity) =>
          preferences.some(
            (preference: any) =>
              preference.activityType.id === activity.activityType.id,
          ),
        );

        if (type) {
          recommended = recommended.filter(
            (activity: Activity) =>
              activity.activityType.name.toLowerCase() === type.toLowerCase(),
          );
        }

        const activitiesWithParticipants = await Promise.all(
          recommended.slice(0, 8).map(async (activity: Activity) => {
            const response = await api.get(
              `/activities/${activity.id}/participants/count`,
            );

            return {
              ...activity,
              participants: response.data,
            };
          }),
        );

        setActivities(activitiesWithParticipants);
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
            activity={activity}
            onClick={(activity) => {
              setSelectedActivity(activity);
            }}
          />
        ))}
      </div>

      {selectedActivity && (
        <ActivityDetailsModal
          activity={selectedActivity}
          onClose={() => setSelectedActivity(null)}
        />
      )}
    </>
  );
}
