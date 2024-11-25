export function getMissingFields(formObject: object): Array<string> {
  let missingFields: Array<string> = []
  Object.keys(formObject).forEach((key) => {
    if (!formObject[key as keyof typeof formObject]) {
      missingFields.push(key)
    }
  })
  return missingFields
}
