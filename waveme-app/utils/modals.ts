import { Href, router } from "expo-router"

// Href: string enum for all possible routes
export const redirectFromModal = (route: Href, setShowModal: Function) => {
  // Disable the modal before redirecting to prevent it from reopening when going back to that page
  setShowModal(false)
  router.push(route)
}
