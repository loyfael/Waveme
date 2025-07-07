import { renderHook } from "@testing-library/react-native"
import { useWebTitle } from "../useWebTitle"

jest.mock('react-native', () => ({
  Platform: {
    OS: 'web',
  },
}));

describe("useWebTitle", () => {
  test("Set web title", () => {
    renderHook(() => {
      useWebTitle("test")
    })

    expect(document.title).toBe("test - Waveme")
  })
})
