import api from "./api";

import type { Activity } from "../types/activity";
import type { PageResponse } from "../types/pageResponse";

export async function findAllActivities(): Promise<PageResponse<Activity>> {
  const response = await api.get("/activities");

  return response.data;
}
