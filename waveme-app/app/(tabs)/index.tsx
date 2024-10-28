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

// NOTE: When connection with backend is established, change all ImageSourcePropType to string (URIs)
type Post = {
  title: string | null,
  meme: ImageSourcePropType,
  userName: string,
  userPfp: ImageSourcePropType | null,
}

export default function HomeScreen() {
  useWebTitle('Accueil - Waveme')
  const [posts, setPosts] = useState<Post[]>([]);

  const AnimatedButton = Animated.createAnimatedComponent(Pressable)

  // don't even ask
  const animatedButton1 = useRef(new Animated.Value(0)).current
  const backgroundColor1 = animatedButton1.interpolate({
    inputRange: [0, 1],
    outputRange: [hexToRgbString(Colors.common.barButton), hexToRgbString(Colors.common.genericButtonPressed)]
  })
  const animatedButton2 = useRef(new Animated.Value(0)).current
  const backgroundColor2 = animatedButton2.interpolate({
    inputRange: [0, 1],
    outputRange: [hexToRgbString(Colors.common.barButton), hexToRgbString(Colors.common.genericButtonPressed)]
  })
  const animatedButton3 = useRef(new Animated.Value(0)).current
  const backgroundColor3 = animatedButton3.interpolate({
    inputRange: [0, 1],
    outputRange: [hexToRgbString(Colors.common.barButton), hexToRgbString(Colors.common.upvote)]
  })
  const animatedButton4 = useRef(new Animated.Value(0)).current
  const backgroundColor4 = animatedButton4.interpolate({
    inputRange: [0, 1],
    outputRange: [hexToRgbString(Colors.common.barButton), hexToRgbString(Colors.common.downvote)]
  })

  const areaBackgroundColor = useThemeColor({}, 'areaBackground')

  const fadeButtonToClicked = (backgroundToAnimate: Animated.Value) => {
    Animated.timing(backgroundToAnimate, {
      toValue: 1,
      duration: 0,
      useNativeDriver: false,
    }).start()
  }

  const fadeButtonToIdle = (backgroundToAnimate: Animated.Value) => {
    Animated.timing(backgroundToAnimate, {
      toValue: 0,
      duration: 150,
      useNativeDriver: false,
    }).start()
  }

  // NOTE: When connection with backend is established, replace with a fetch request to the correct endpoint
  useEffect(() => {
    setPosts([{
      title: 'Hilarant.',
      meme: require('@/assets/images/meme.png'),
      userName: 'Beiten34',
      userPfp: require('@/assets/images/pfp.png'),
    },
    {
      title: 'Repost si tu trouves ça hilarant hahaha trodrol quoi terrible quest-ce que je fais de ma vie jecris littéralement un gros texte pour tester ce que ça donne avec un gros texte et jai la flemme de mettre des backslash du coup je mets juste pas dapostrophe et jai limpression de pas savoir écrire lolool',
      meme: require('@/assets/images/meme.png'),
      userName: 'Beiten34',
      userPfp: require('@/assets/images/pfp.png'),
    },
    {
      title: 'Hilarant.',
      meme: require('@/assets/images/meme.png'),
      userName: 'Beiten34',
      userPfp: require('@/assets/images/pfp.png'),
    }]);
  }, []);

  return (
    <>
      {posts.map((post: Post, index: number) => (
        <ThemedView key={index} style={styles.postWrapper}>
          <View style={styles.postProfile}>
            {post.userPfp ? (
              <Image source={post.userPfp} style={styles.profilePicture} />
            ) : null}
            <View style={styles.profileText}>
              <ThemedText type="defaultBold">{post.userName}</ThemedText>
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

const styles = StyleSheet.create({
  postWrapper: {
    flexDirection: 'column',
    marginTop: 50,
  },

  postProfile: {
    flexDirection: 'row',
  },

  profilePicture: {
    width: 40,
    height: 40,
    borderRadius: 20,
    overflow: 'hidden',
    marginEnd: 5,
  },

  profileText: {
    flexDirection: 'column',
    flex: 1,
    flexWrap: 'wrap',
  },

  postMeme: {
    flexDirection: 'column',
  },

  memeImage: {
    alignItems: 'center',
    height: 500,
    width: '100%',
    marginTop: 10,
    overflow: 'hidden',
    borderTopStartRadius: 24,
    borderTopEndRadius: 24,
  },

  memeActionBar: {
    height: 48,
    backgroundColor: Colors.common.memeActionBar,
    borderBottomStartRadius: 24,
    borderBottomEndRadius: 24,
    flexDirection: 'row',
  },

  barLeft: {
    flex: 1,
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'flex-start',
  },

  barRight: {
    flex: 1,
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'flex-end',
  },

  barButton: {
    backgroundColor: Colors.common.barButton,
    borderRadius: 24,
    width: 40,
    height: 40,
    justifyContent: 'center',
    alignItems: 'center',
    marginHorizontal: 6,
  },

  barButtonPressed: {
    backgroundColor: Colors.common.memeActionBar,
    borderRadius: 24,
    width: 40,
    height: 40,
    justifyContent: 'center',
    alignItems: 'center',
    marginHorizontal: 6,
  },
});
