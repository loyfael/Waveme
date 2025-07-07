import { Text, type TextProps, StyleSheet } from 'react-native';

import { useThemeColor } from '@/hooks/useThemeColor';
import React from 'react';
import { useResponsive } from '@/hooks/useResponsive';

export type ThemedTextProps = TextProps & {
  lightColor?: string;
  darkColor?: string;
  type?: 'default' | 'small' | 'title' | 'defaultBold' | 'subtitle' | 'link';
};

// Generic text component
export function ThemedText({
  style,
  lightColor,
  darkColor,
  type = 'default',
  ...rest
}: ThemedTextProps) {
  const color = useThemeColor({ light: lightColor, dark: darkColor }, 'text');
  const { isSmallScreen } = useResponsive()

  return (
    <Text
      style={[
        { color },
        type === 'default' ? isSmallScreen ? styles.defaultSmallScreen : styles.default : undefined,
        type === 'small' ? isSmallScreen ? styles.smallSmallScreen : styles.small : undefined,
        type === 'title' ? isSmallScreen ? styles.titleSmallScreen : styles.title : undefined,
        type === 'defaultBold' ? isSmallScreen ? styles.defaultBoldSmallScreen : styles.defaultBold : undefined,
        type === 'subtitle' ? isSmallScreen ? styles.subtitleSmallScreen : styles.subtitle : undefined,
        type === 'link' ? isSmallScreen ? styles.linkSmallScreen : styles.link : undefined,
        style,
      ]}
      {...rest}
    />
  );
}

const styles = StyleSheet.create({
  default: {
    fontFamily: 'Quicksand',
    fontSize: 16,
    lineHeight: 24,
  },

  defaultSmallScreen: {
    fontFamily: "Quicksand",
    fontSize: 14,
    lineHeight: 20,
  },

  small: {
    fontFamily: 'Quicksand',
    fontSize: 12,
    lineHeight: 16,
  },

  smallSmallScreen: {
    fontFamily: "Quicksand",
    fontSize: 10,
    lineHeight: 14,
  },

  defaultBold: {
    fontFamily: 'Quicksand',
    fontSize: 16,
    lineHeight: 24,
    fontWeight: '700',
  },

  defaultBoldSmallScreen: {
    fontFamily: 'Quicksand',
    fontSize: 14,
    lineHeight: 20,
    fontWeight: '700',
  },

  title: {
    fontFamily: 'Quicksand',
    fontSize: 32,
    fontWeight: 'bold',
    lineHeight: 32,
  },

  titleSmallScreen: {
    fontFamily: 'Quicksand',
    fontSize: 24,
    fontWeight: 'bold',
    lineHeight: 24,
  },

  subtitle: {
    fontFamily: 'Quicksand',
    fontSize: 20,
    fontWeight: 'bold',
  },

  subtitleSmallScreen: {
    fontFamily: 'Quicksand',
    fontSize: 18,
    fontWeight: 'bold',
  },

  link: {
    fontFamily: 'Quicksand',
    lineHeight: 30,
    fontSize: 16,
  },

  linkSmallScreen: {
    fontFamily: 'Quicksand',
    lineHeight: 24,
    fontSize: 14,
  },
});
