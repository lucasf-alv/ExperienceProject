import { CalendarDays, MapPin, Users } from "lucide-react";

interface ActivityListItemProps {
  title: string;
  date: string;
  image: string;
  participants: number;
}

export function ActivityListItem({
  title,
  date,
  participants,
  image,
}: ActivityListItemProps) {
  return (
    <div className="flex items-center gap-4 rounded-xl border border-gray-200 bg-white p-4 shadow-sm transition hover:shadow-md">
      <img
        src={image}
        alt={title}
        className="h-24 w-24 rounded-lg object-cover"
      />

      <div className="flex flex-1 flex-col gap-2">
        <h3 className="text-lg font-semibold">{title}</h3>

        <div className="flex items-center gap-4 text-sm text-gray-500">...</div>
      </div>
      <div className="flex items-center gap-4 text-sm text-gray-500">
        <span className="flex items-center gap-1">
          <CalendarDays size={16} className="text-green-500" />
          {date}
        </span>

        <span className="flex items-center gap-1">
          <Users size={16} className="text-green-500" />
          {participants}
        </span>
      </div>

      <button className="rounded-lg bg-green-500 px-5 py-2 text-white">
        Participar
      </button>
    </div>
  );
}
