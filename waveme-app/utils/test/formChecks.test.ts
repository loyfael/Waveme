import { getMissingFields } from "../formChecks"

describe("formChecks", () => {
  test("Check missing fields (return keys of empty values of object as array)", () => {
    expect(getMissingFields({
      correct: "field",
      empty: "",
      badButCorrect: "df6v54"
    })).toEqual(["empty"])
  })
})
