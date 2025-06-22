import { ReasonValues, ReportedContent } from "@/types";
import { refreshAuthIfNeeded } from "./AuthToken";
import { REPORT_URL } from "@/constants/API";
import axios from "axios";

export async function reportContent(
  reportedContent: ReportedContent,
  reason: ReasonValues,
  comment: string,
  id: number | string,
) {
  return refreshAuthIfNeeded(() => {
    // No formData required here as the backend expects a request object
    return axios.post(`${REPORT_URL}/${reportedContent}/${id}`, {
      reason,
      description: comment,
    })
  })
}
