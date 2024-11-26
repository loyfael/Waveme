export type SignupCredentials = {
  username: string,
  email: string,
  password: string,
  confirmPassword: string,
}

export type LoginCredentials = {
  username: string,
  password: string,
}

export type InvalidTooltip = {
  display: boolean,
  field: string,
  message: string,
}
