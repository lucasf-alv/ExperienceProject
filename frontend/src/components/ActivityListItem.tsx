import { CalendarDays, Users } from "lucide-react";

interface ActivityListItemProps {
  title: string;
  image: string;
  date: string;
  participants: number;
  onClick?: () => void;
}

export function ActivityListItem({
  title,
  date,
  participants,
  image,
  onClick,
}: ActivityListItemProps) {
  return (
    <div
      onClick={onClick}
      className="flex cursor-pointer items-center gap-4 rounded-xl border border-gray-200 bg-white p-4 shadow-sm transition hover:shadow-md"
    >
      <img
        src={image}
        alt={title}
        className="h-24 w-24 rounded-lg object-cover"
      />

      <div className="flex flex-1 flex-col gap-2">
        <h3 className="text-lg font-semibold">{title}</h3>

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
      </div>
    </div>
  );
}
