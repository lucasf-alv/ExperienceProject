import { useEffect, useState } from "react";
import { ActivityListItem } from "../components/ActivityListItem";
import { ActivityDetailsModal } from "../components/ActivityDetailsModal";
import { findAllActivities } from "../services/activityService";
import type { Activity } from "../types/activity";
import api from "../services/api";

interface ActivitiesProps {
  type: string;
}

export function Activities({ type }: ActivitiesProps) {
  const [activities, setActivities] = useState<Activity[]>([]);

  const [selectedActivity, setSelectedActivity] = useState<Activity | null>(
    null,
  );

  findAllActivities().then(async (response) => {
    const filtered = response.content
      .filter(
        (activity) =>
          activity.activityType.name.toLowerCase() === type.toLowerCase(),
      )
      .slice(0, 4);

    const activitiesWithParticipants = await Promise.all(
      filtered.map(async (activity) => {
        const participantsResponse = await api.get(
          `/activities/${activity.id}/participants`,
        );

        return {
          ...activity,
          participants: participantsResponse.data.length,
        };
      }),
    );

    setActivities(activitiesWithParticipants);
  });

  return (
    <>
      <div className="flex flex-col gap-4">
        {activities.map((activity) => (
          <ActivityListItem
            key={activity.id}
            title={activity.title}
            image={activity.image}
            date={activity.scheduled_Date}
            participants={activity.participants}
            onClick={() => setSelectedActivity(activity)}
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
