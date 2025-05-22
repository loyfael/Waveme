import { hexToRgbString } from "../convert"

describe("convert", () => {
  test("Convert hex color value to RGB", () => {
    expect(hexToRgbString("#FF00FF")).toBe("rgb(255, 0, 255)")
  })
})
