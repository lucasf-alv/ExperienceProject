import api from "./api";

import type { Activity } from "../types/activity";
import type { PageResponse } from "../types/pageResponse";

export async function findAllActivities(): Promise<PageResponse<Activity>> {
  const response = await api.get("/activities");

  return response.data;
}
export async function subscribeActivity(activityId: number) {
  const response = await api.post(`/activities/${activityId}/subscribe`);

  return response.data;
}

export async function updateActivity(activityId: number, formData: FormData) {
  const response = await api.put(`/activities/${activityId}/update`, formData, {
    headers: {
      "Content-Type": "multipart/form-data",
    },
  });

  return response.data;
}
