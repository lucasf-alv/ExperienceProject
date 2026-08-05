import { CalendarDays, Users, Lock } from "lucide-react";

interface ActivityCardProps {
  title: string;
  image: string;
  date: string;
  participants: number;
  isPrivate?: boolean;
}

export function ActivityCard({
  title,
  image,
  date,
  participants,
  isPrivate = false,
}: ActivityCardProps) {
  return (
    <div className="w-72 overflow-hidden rounded-xl bg-white shadow transition hover:shadow-lg">
      <div className="relative">
        <img src={image} alt={title} className="h-44 w-full object-cover" />

        {isPrivate && (
          <div className="absolute left-3 top-3 rounded-full bg-green-500 p-2 text-white">
            <Lock size={16} />
          </div>
        )}
      </div>

      <div className="p-4">
        <h3 className="text-lg font-semibold">{title}</h3>

        <div className="mt-3 flex items-center gap-6 text-sm text-gray-500">
          <div className="flex items-center gap-1">
            <CalendarDays size={16} className="text-green-500" />
            {date}
          </div>

          <div className="flex items-center gap-1">
            <Users size={16} className="text-green-500" />
            {participants}
          </div>
        </div>
      </div>
    </div>
  );
}
