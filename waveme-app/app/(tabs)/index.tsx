import { useState, useEffect } from 'react'
import { Image, StyleSheet, Platform, Text } from 'react-native';
import MaterialIcons from '@expo/vector-icons/MaterialIcons';

import { ThemedText } from '@/components/ThemedText';
import { ThemedView } from '@/components/ThemedView';

type Post = {
  title: string | null,
  memeId: number,
  userName: string,
  userPfpId: number | null,
}

export default function HomeScreen() {
  const [posts, setPosts] = useState<Post[]>([]);
  const [memeIds, setMemeIds] = useState<number[]>([]);
  const [userPfpIds, setUserPfpIds] = useState<number[]>([]);

  useEffect(() => {
    setPosts([{
      title: 'Hilarant.',
      memeId: require('@/assets/images/meme.png'), 
      userName: 'Beiten34',
      userPfpId: require('@/assets/images/pfp.png'),
    }]);
  }, []);

  return (
    <>
      {posts.map((post: Post, index: number) => (
        <ThemedView key={index}>
          <ThemedView>
            <ThemedText>{post.userName}</ThemedText>
            {post.userPfpId ? (
              <Image source={post.userPfpId} />
            ) : null}
            {post.memeId ? (
              <Image source={post.memeId} />
            ) : null}
          </ThemedView>
        </ThemedView>
      ))}
    </>
  );
} 

const styles = StyleSheet.create({
  titleContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 8,
  },
  stepContainer: {
    gap: 8,
    marginBottom: 8,
  },
  reactLogo: {
    height: 178,
    width: 290,
    bottom: 0,
    left: 0,
    position: 'absolute',
  },
});