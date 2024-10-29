import { useEffect } from 'react';
import { Platform } from 'react-native';

export function useWebTitle(title: string) {
  useEffect(() => {
    if (Platform.OS === 'web' && typeof document !== 'undefined') {
      document.title = title;
    }
  }, [title]);
}
