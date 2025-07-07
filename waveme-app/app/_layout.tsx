import { ThemeProvider } from '@/context/ThemeContext';
import { useFonts } from 'expo-font';
import { Stack } from 'expo-router';
import * as SplashScreen from 'expo-splash-screen';
import { useEffect } from 'react';
import 'react-native-reanimated';
import React from 'react';
import { AuthProvider } from '@/context/AuthContext';

// Prevent the splash screen from auto-hiding before asset loading is complete.
SplashScreen.preventAutoHideAsync();

// Note: Shadow deprecation warning
// The warning "shadow*" style props are deprecated. Use "boxShadow" comes from expo-router v5.1.2
// This is a known issue in the expo-router dependency and will be fixed in a future update.
// Our application code has been updated to use boxShadow instead of shadow* properties.
// Temporarily filtering this warning to avoid log pollution.

// Temporary patch to filter expo-router shadow deprecation warnings
const originalWarn = console.warn;
console.warn = (...args) => {
  const message = args[0];
  if (typeof message === 'string' && message.includes('"shadow*" style props are deprecated. Use "boxShadow"')) {
    return; // Filter out this specific warning from expo-router
  }
  originalWarn.apply(console, args);
};

export default function RootLayout() {
  const [loaded] = useFonts({
    Quicksand: require('../assets/fonts/Quicksand-VariableFont_wght.ttf'),
  });

  useEffect(() => {
    if (loaded) {
      SplashScreen.hideAsync();
    }
  }, [loaded]);

  if (!loaded) {
    return null;
  }

  return (
    <ThemeProvider>
      <AuthProvider>
        <Stack>
          <Stack.Screen name="(tabs)" options={{ headerShown: false }} />
          <Stack.Screen name="+not-found" />
        </Stack>
      </AuthProvider>
    </ThemeProvider>
  );
}
