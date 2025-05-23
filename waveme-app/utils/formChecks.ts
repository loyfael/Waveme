import { InvalidTooltip, SignupCredentials } from "@/types"

// Validates all fields on a form and returns all invalid fields or lack thereof
export function getMissingFields(formObject: object): Array<string> {
  let missingFields: Array<string> = []
  Object.keys(formObject).forEach((key) => {
    if (!formObject[key as keyof typeof formObject]) {
      missingFields.push(key)
    }
  })
  return missingFields
}

export function getIncorrectLengthFields(credentials: SignupCredentials): Array<InvalidTooltip> {
  let incorrectFields: Array<InvalidTooltip> = []
  const [ usernameLength, emailLength, passwordLength ]: Array<number> = Object.values(credentials).map((value) => value.length)
  if ((usernameLength < 3) || usernameLength > 20) {
    incorrectFields.push({
      display: true,
      field: "username",
      message: `Ce pseudo est trop ${usernameLength < 3 ? "court (minimum : 3 caractères)" : "long (maximum : 20 caractères)"}`,
    })
  }
  if (emailLength > 50) {
    incorrectFields.push({
      display: true,
      field: "email",
      message: "Cette adresse email est trop longue (maximum : 50 caractères)",
    })
  }
  if (passwordLength < 6 || passwordLength > 40) {
    incorrectFields.push({
      display: true,
      field: "password",
      message: `Ce mot de passe est trop ${passwordLength < 6 ? "court (minimum : 6 caractères)" : "long (maximum : 40 caractères)"}`,
    })
  }
  return incorrectFields
}
