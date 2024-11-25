import { Animated } from 'react-native'

export const fadeButtonToClicked = (backgroundToAnimate: Animated.Value, duration: number = 0) => {
  Animated.timing(backgroundToAnimate, {
    toValue: 1,
    duration,
    useNativeDriver: false,
  }).start()
}

export const fadeButtonToIdle = (backgroundToAnimate: Animated.Value, duration: number = 150) => {
  Animated.timing(backgroundToAnimate, {
    toValue: 0,
    duration,
    useNativeDriver: false,
  }).start()
}
