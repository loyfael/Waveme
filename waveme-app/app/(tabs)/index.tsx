import { useState, useEffect, useMemo } from 'react'
import { Image, StyleSheet, Platform, Text, ImageSourcePropType } from 'react-native';
import MaterialIcons from '@expo/vector-icons/MaterialIcons';

import { ThemedText } from '@/components/ThemedText';
import { ThemedView } from '@/components/ThemedView';

type Post = {
  title: string | null,
  meme: ImageSourcePropType,
  userName: string,
  userPfp: ImageSourcePropType | null,
}

export default function HomeScreen() {
  const [posts, setPosts] = useState<Post[]>([]);
  const [memeIds, setMemeIds] = useState<number[]>([]);
  const [userPfpIds, setUserPfpIds] = useState<number[]>([]);

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
          <ThemedView style={styles.postProfile}>
            {post.userPfp ? (
              <Image source={post.userPfp} style={styles.profilePicture} />
            ) : null}
            <ThemedView style={styles.profileText}>
              <ThemedText type="defaultSemiBold">{post.userName}</ThemedText>
              {post.title ? (
                <ThemedText>{post.title}</ThemedText>
              ) : ''}
            </ThemedView>
          </ThemedView>
          <ThemedView style={styles.postMeme}>
            <Image source={post.meme} style={styles.memeImage} />
          </ThemedView>
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

  },

  memeImage: {
    alignItems: 'center',
    minHeight: 150,
    maxHeight: 600,
    resizeMode: 'contain',
    marginTop: 10,
    borderRadius: 10,
    overflow: 'hidden',
  },
});