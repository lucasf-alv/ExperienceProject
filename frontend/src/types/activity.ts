export interface Activity {
  id: number;
  title: string;
  description: string;
  confirmation_code: string;
  image: string;
  scheduled_Date: string;
  criated_At: string;
  deleted_At: string | null;
  completed_At: string | null;
  Private: boolean;

  participants: number;

  creator: {
    id: number;
    name: string;
    avatar: string;
  };

  activityAddress: {
    latitude: number | null;
    longitude: number | null;
  };

  activityType: {
    id: number;
    name: string;
    description: string;
    image: string;
  };
}
