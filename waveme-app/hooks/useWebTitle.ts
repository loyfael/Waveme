import { useEffect } from 'react';
import { Platform } from 'react-native';

// Sets the page title (web only)
export function useWebTitle(title: string) {
  useEffect(() => {
    if (Platform.OS === 'web' && typeof document !== 'undefined') {
      document.title = title;
    }
  }, [title]);
}
