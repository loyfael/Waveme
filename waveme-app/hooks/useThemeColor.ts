import { Colors } from '@/constants/Colors';
import { ThemeContext } from '@/context/ThemeContext';
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
  const { isDarkMode } = useContext(ThemeContext)
  const theme: keyof typeof props = isDarkMode ? 'dark' : 'light';

  const colorFromProps = props[theme];

  if (colorFromProps) {
    return colorFromProps;
  }
  return Colors[theme][colorName];
}
