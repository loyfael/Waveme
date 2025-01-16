import { useState, useEffect, useRef } from 'react'
import { Image, StyleSheet, ImageSourcePropType, View, Pressable, Animated } from 'react-native';
import Entypo from '@expo/vector-icons/Entypo';
import { BiSolidUpArrowAlt, BiSolidDownArrowAlt } from 'react-icons/bi'
import { ChatDotsFill } from 'react-bootstrap-icons';
import { ThemedText } from '@/components/theme/ThemedText';
import { ThemedView } from '@/components/theme/ThemedView';
import React from 'react';
import { useThemeColor } from '@/hooks/useThemeColor';
import { Colors } from '@/constants/Colors';
import { hexToRgbString } from '@/utils/convert';
import { useWebTitle } from '@/hooks/useWebTitle';
import { useAnimatedButton } from '@/hooks/useAnimatedButton';
import { fadeButtonToClicked, fadeButtonToIdle } from '@/utils/animateButton';
import { Post } from '@/types';
import { memeStyle } from '@/constants/commonStyles';

export default function HomeScreen() {
  useWebTitle('Accueil - Waveme')
  const [posts, setPosts] = useState<Post[]>([]);

  const AnimatedButton = Animated.createAnimatedComponent(Pressable)

  const { animatedButton: animatedButton1, backgroundColor: backgroundColor1 } = useAnimatedButton({
    idleColor: hexToRgbString(Colors.common.barButton),
    clickedColor: hexToRgbString(Colors.common.genericButtonPressed)
  })

  const { animatedButton: animatedButton2, backgroundColor: backgroundColor2 } = useAnimatedButton({
    idleColor: hexToRgbString(Colors.common.barButton),
    clickedColor: hexToRgbString(Colors.common.genericButtonPressed)
  })

  const { animatedButton: animatedButton3, backgroundColor: backgroundColor3 } = useAnimatedButton({
    idleColor: hexToRgbString(Colors.common.barButton),
    clickedColor: hexToRgbString(Colors.common.upvote)
  })

  const { animatedButton: animatedButton4, backgroundColor: backgroundColor4 } = useAnimatedButton({
    idleColor: hexToRgbString(Colors.common.barButton),
    clickedColor: hexToRgbString(Colors.common.downvote)
  })

  const areaBackgroundColor = useThemeColor({}, 'areaBackground')

  // NOTE: When connection with backend is established, replace with a fetch request to the correct endpoint
  useEffect(() => {
    setPosts([{
      title: 'Hilarant.',
      meme: require('@/assets/images/meme.png'),
      user: {
        userName: 'Beiten34',
        userPfp: require('@/assets/images/pfp.png'),
      }
    },
    {
      title: 'Repost si tu trouves ça hilarant hahaha trodrol quoi terrible quest-ce que je fais de ma vie jecris littéralement un gros texte pour tester ce que ça donne avec un gros texte et jai la flemme de mettre des backslash du coup je mets juste pas dapostrophe et jai limpression de pas savoir écrire lolool',
      meme: require('@/assets/images/meme.png'),
      user: {
        userName: 'Beiten34',
        userPfp: require('@/assets/images/pfp.png'),
      }
    },
    {
      title: 'Hilarant.',
      meme: require('@/assets/images/meme.png'),
      user: {
        userName: 'Beiten34',
        userPfp: require('@/assets/images/pfp.png'),
      }
    }]);
  }, []);

  return (
    <>
      {posts.map((post: Post, index: number) => (
        <ThemedView key={index} style={styles.postWrapper}>
          <View style={styles.postProfile}>
            {post.user.userPfp ? (
              <Image source={post.user.userPfp} style={styles.profilePicture} />
            ) : null}
            <View style={styles.profileText}>
              <ThemedText type="defaultBold">{post.user.userName}</ThemedText>
              {post.title ? (
                <ThemedText>{post.title}</ThemedText>
              ) : ''}
            </View>
          </View>
          <View style={styles.postMeme}>
            <Image source={post.meme} style={{ ...styles.memeImage, backgroundColor: areaBackgroundColor }} resizeMode='contain' />
            <View style={styles.memeActionBar}>
              <View style={styles.barLeft}>
                <AnimatedButton
                  onPressIn={() => fadeButtonToClicked(animatedButton1)}
                  onPressOut={() => fadeButtonToIdle(animatedButton1)}
                  onPress={() => { }}>
                  <Animated.View style={{ ...styles.barButton, backgroundColor: backgroundColor1 }}>
                    <Entypo name="flag" size={36} color={Colors.common.memeActionBar} />
                  </Animated.View>
                </AnimatedButton>
              </View>
              <View style={styles.barRight}>
                <AnimatedButton
                  onPressIn={() => fadeButtonToClicked(animatedButton2)}
                  onPressOut={() => fadeButtonToIdle(animatedButton2)}
                  onPress={() => { }}>
                  <Animated.View style={{ ...styles.barButton, backgroundColor: backgroundColor2 }}>
                    <ChatDotsFill color={Colors.common.memeActionBar} size={30} />
                  </Animated.View>
                </AnimatedButton>
                <AnimatedButton
                  onPressIn={() => fadeButtonToClicked(animatedButton3)}
                  onPressOut={() => fadeButtonToIdle(animatedButton3)}
                  onPress={() => { }}>
                  <Animated.View style={{ ...styles.barButton, backgroundColor: backgroundColor3 }}>
                    <BiSolidUpArrowAlt color={Colors.common.memeActionBar} size={36} />
                  </Animated.View>
                </AnimatedButton>
                <AnimatedButton
                  onPressIn={() => fadeButtonToClicked(animatedButton4)}
                  onPressOut={() => fadeButtonToIdle(animatedButton4)}
                  onPress={() => { }}>
                  <Animated.View style={{ ...styles.barButton, backgroundColor: backgroundColor4 }}>
                    <BiSolidDownArrowAlt color={Colors.common.memeActionBar} size={36} />
                  </Animated.View>
                </AnimatedButton>
              </View>
            </View>
          </View>
        </ThemedView>
      ))}
    </>
  );
}

const styles = memeStyle;
