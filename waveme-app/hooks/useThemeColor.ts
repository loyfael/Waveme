import { Colors } from '@/constants/Colors';
import { ThemeContext } from '@/context/ThemeContext';
import { getLocalStorage } from '@/utils/localStorage';
import { log } from 'console';
import { useState, useEffect, useContext } from 'react'

/**
 * @param props 
 * @param colorName 
 * @returns string
 */
export function useThemeColor(
  props: { light?: string; dark?: string },
  colorName: keyof typeof Colors.light & keyof typeof Colors.dark
) {
  const [theme, setTheme] = useState<keyof typeof props>('light')

  const { isDarkMode } = useContext(ThemeContext)

  useEffect(() => {
    const handleAsync = async () => {
      setTheme(isDarkMode ? 'dark' : 'light')
    }
    handleAsync()
  })

  const colorFromProps = props[theme];

  if (colorFromProps) {
    return colorFromProps;
  }
  return Colors[theme][colorName];
}
