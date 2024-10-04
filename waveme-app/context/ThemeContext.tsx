import React, { createContext, Children, useState, useEffect } from "react";
import { getLocalStorage, setLocalStorage } from "@/utils/localStorage";

export const ThemeContext = createContext({
    isDarkMode: false,
    setDarkMode: (value: boolean) => {}
})

type Props = {
    children: React.ReactNode
}

export const ThemeProvider = ({children}: Props) => {
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
        <ThemeContext.Provider value={{isDarkMode, setDarkMode}}>
            {children}
        </ThemeContext.Provider>
    )
}
