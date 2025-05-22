import { useThemeColor } from "../useThemeColor"
import { renderHook } from "@testing-library/react-native"

describe("useThemeColor", () => {
  test("Return the correct theme color", () => {
    const { result } = renderHook(() => useThemeColor({}, "text"))

    expect(["#9FCBF4", "#0E5865"]).toContain(result.current)
  })

  test("Return a chosen theme color", () => {
    const { result } = renderHook(() => useThemeColor({ light: "#ffffff", dark: "#000000" }, "text"))

    expect(["#ffffff", "#000000"]).toContain(result.current)
  })
})
