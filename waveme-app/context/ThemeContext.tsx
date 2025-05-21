import React, { createContext, useState, useEffect } from "react";
import { getLocalStorage, setLocalStorage } from "@/utils/localStorage";
import { ChildrenProps } from "@/types";

export const ThemeContext = createContext({
  isDarkMode: false,
  setDarkMode: (value: boolean) => { }
})

export const ThemeProvider = ({ children }: ChildrenProps) => {
  const [theme, setTheme] = useState<string | null>('light')

  const isDarkMode = (theme === 'dark')
  const setDarkMode = (value: boolean) => {
    setTheme(value ? 'dark' : 'light')
  }

  // Get the current theme from localStorage
  useEffect(() => {
    const setDefaultTheme = async () => {
      setTheme(await getLocalStorage('theme'))
    }
    setDefaultTheme()
  }, [])

  // Set the new theme in localStorage when setDarkMode is called
  useEffect(() => {
    setLocalStorage('theme', theme)
  }, [theme])

  return (
    <ThemeContext.Provider value={{ isDarkMode, setDarkMode }}>
      {children}
    </ThemeContext.Provider>
  )
}
