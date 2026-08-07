import { useNavigate } from "react-router-dom";

type Props = {
  name: string;
  image: string;
};

export function ActivityTypeCard({ name, image }: Props) {
  const navigate = useNavigate();

  return (
    <button
      onClick={() => navigate(`/activities/${name}`)}
      className="
        flex
        cursor-pointer
        flex-col
        items-center
        gap-2
        transition
        hover:scale-105
      "
    >
      <img
        src={image}
        alt={name}
        className="h-20 w-20 rounded-full object-cover"
      />

      <span className="font-medium">{name}</span>
    </button>
  );
}
