import { useEffect, useState } from "react";
import { ActivityCard } from "../components/ActivityCard";
import { findAllActivities } from "../services/activityService";
import type { Activity } from "../types/activity";

interface ActivitiesProps {
  type: string;
}

export function Activities({ type }: ActivitiesProps) {
  const [activities, setActivities] = useState<Activity[]>([]);

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
    <div className="grid grid-cols-4 gap-4">
      {activities.map((activity) => (
        <ActivityCard
          key={activity.id}
          title={activity.title}
          image={activity.image}
          date={activity.scheduled_Date}
          participants={activity.participants}
        />
      ))}
    </div>
  );
}
