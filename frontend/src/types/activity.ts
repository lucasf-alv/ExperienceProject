export interface Activity {
  id: number;
  title: string;
  description: string;
  scheduledDate: string;
  image?: string;
  participants: number;
  activityTypeId: number;
}
