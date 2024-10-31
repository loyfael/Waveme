import { useRef } from "react"
import { Animated } from 'react-native'

type AnimatedButtonProps = {
  idleColor: string,
  clickedColor: string,
}

export function useAnimatedButton({ idleColor, clickedColor }: AnimatedButtonProps) {
  const animatedButton = useRef(new Animated.Value(0)).current
  const backgroundColor = animatedButton.interpolate({
    inputRange: [0, 1],
    outputRange: [idleColor, clickedColor]
  })

  return { animatedButton, backgroundColor }
}
