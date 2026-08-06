import { useEffect, useState } from "react";
import { ActivityListItem } from "../components/ActivityListItem";
import { ActivityDetailsModal } from "../components/ActivityDetailsModal";
import { findAllActivities } from "../services/activityService";
import type { Activity } from "../types/activity";

interface ActivitiesProps {
  type: string;
}

export function Activities({ type }: ActivitiesProps) {
  const [activities, setActivities] = useState<Activity[]>([]);

  // NOVO
  const [selectedActivity, setSelectedActivity] = useState<Activity | null>(
    null,
  );

  useEffect(() => {
    findAllActivities().then((response) => {
      const filtered = response.content.filter(
        (activity) =>
          activity.activityType.name.toLowerCase() === type.toLowerCase(),
      );

      setActivities(filtered);
    });
  }, [type]);

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

      {false && (
        <ActivityDetailsModal
          activity={selectedActivity!}
          onClose={() => setSelectedActivity(null)}
        />
      )}
    </>
  );
}
