import { CalendarDays, MapPin, Users } from "lucide-react";

interface ActivityListItemProps {
  title: string;
  date: string;
  location: string;
  participants: number;
}

export function ActivityListItem({
  title,
  date,
  location,
  participants,
}: ActivityListItemProps) {
  return (
    <div className="flex items-center justify-between rounded-xl border border-gray-200 bg-white p-4 shadow-sm transition hover:shadow-md">
      <div className="flex flex-col gap-2">
        <h3 className="text-lg font-semibold text-gray-800">{title}</h3>

        <div className="flex items-center gap-4 text-sm text-gray-500">
          <span className="flex items-center gap-1">
            <CalendarDays size={16} className="text-green-500" />
            {date}
          </span>

          <span className="flex items-center gap-1">
            <MapPin size={16} className="text-green-500" />
            {location}
          </span>

          <span className="flex items-center gap-1">
            <Users size={16} className="text-green-500" />
            {participants}
          </span>
        </div>
      </div>

      <button className="rounded-lg bg-green-500 px-5 py-2 font-medium text-white transition hover:bg-green-600">
        Participar
      </button>
    </div>
  );
}
