import { useWindowDimensions } from 'react-native';

// Returns a bunch of breakpoints -> easy possible evolutions with more breakpoints added
export const useResponsive = () => {
  const { width, height } = useWindowDimensions();

  return {
    width,
    height,
    isVerySmallScreen: width < 480,
    isSmallScreen: width < 768,
    isMobile: width < 768, // Added isMobile property
  };
};
