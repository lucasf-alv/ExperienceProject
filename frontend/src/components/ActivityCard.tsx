import { CalendarDays, Users, Lock } from "lucide-react";
import type { Activity } from "../types/activity";

interface ActivityCardProps {
  activity: Activity;
  onClick: (activity: Activity) => void;
}

export function ActivityCard({ activity, onClick }: ActivityCardProps) {
  return (
    <div
      onClick={() => onClick(activity)}
      className="w-72 cursor-pointer overflow-hidden rounded-xl bg-white shadow transition hover:-translate-y-1 hover:shadow-lg"
    >
      <div className="relative">
        <img
          src={activity.image}
          alt={activity.title}
          className="h-44 w-full object-cover"
        />

        {activity.Private && (
          <div className="absolute left-3 top-3 rounded-full bg-green-500 p-2 text-white">
            <Lock size={16} />
          </div>
        )}
      </div>

      <div className="p-4">
        <h3 className="text-lg font-semibold">{activity.title}</h3>

        <div className="mt-3 flex items-center gap-6 text-sm text-gray-500">
          <div className="flex items-center gap-1">
            <CalendarDays size={16} className="text-green-500" />
            {activity.scheduled_Date}
          </div>

          <div className="flex items-center gap-1">
            <Users size={16} className="text-green-500" />
            {activity.participants}
          </div>
        </div>
      </div>
    </div>
  );
}
