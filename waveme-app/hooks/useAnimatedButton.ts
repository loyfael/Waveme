import { useRef } from "react"
import { Animated } from 'react-native'

type AnimatedButtonProps = {
  idleColor: string,
  clickedColor: string,
}

// Use to create a generic animation style on the app's various buttons
// See utils/animateButton to animate the buttons
export function useAnimatedButton({ idleColor, clickedColor }: AnimatedButtonProps) {
  const animatedButton = useRef(new Animated.Value(0)).current
  const backgroundColor = animatedButton.interpolate({
    inputRange: [0, 1],
    outputRange: [idleColor, clickedColor]
  })

  return { animatedButton, backgroundColor }
}
