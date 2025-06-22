import React, { useState, useEffect, useContext } from 'react'
import { Pressable, StyleSheet, View } from 'react-native';
import { useWebTitle } from '@/hooks/useWebTitle';
import { Post } from '@/types';
import PostList from '@/components/PostList';
import { ThemedText } from '@/components/theme/ThemedText';
import { genericButtonStyle } from '@/constants/commonStyles';
import { useRouter } from 'expo-router';
import { AuthContext } from '@/context/AuthContext';

export default function HomeScreen() {
  const [posts, setPosts] = useState<Post[]>([]);
  const [isLoading, setIsLoading] = useState<boolean>(true)
  
  useWebTitle('Accueil')
  const router = useRouter()
  const { user } = useContext(AuthContext)

  // NOTE: When connection with backend is established, replace with a fetch request to the correct endpoint
  useEffect(() => {
    // TODO: Implement when API is good
    // setPosts([{
    //   id: 32,
    //   title: 'Hilarant.',
    //   meme: require('@/assets/images/meme.png'),
    //   user: {
    //     id: 1,
    //     userName: 'Beiten34',
    //     userPfp: require('@/assets/images/pfp.png'),
    //   }
    // },
    // {
    //   id: 33,
    //   title: 'Repost si tu trouves ça hilarant hahaha trodrol quoi terrible quest-ce que je fais de ma vie jecris littéralement un gros texte pour tester ce que ça donne avec un gros texte et jai la flemme de mettre des backslash du coup je mets juste pas dapostrophe et jai limpression de pas savoir écrire lolool',
    //   meme: require('@/assets/images/meme.png'),
    //   user: {
    //     id: 2,
    //     userName: 'Beiten34',
    //     userPfp: require('@/assets/images/pfp.png'),
    //   }
    // },
    // {
    //   id: 34,
    //   title: 'Hilarant.',
    //   meme: require('@/assets/images/meme.png'),
    //   user: {
    //     id: 3,
    //     userName: 'Beiten34',
    //     userPfp: require('@/assets/images/pfp.png'),
    //   }
    // }]);
    setIsLoading(false)
  }, []);

  return (
    <>
      {user && (
        <View style={styles.newPost}>
          <Pressable onPress={() => { router.push('/new') }} style={styles.genericButton}>
            <ThemedText style={styles.genericButtonText} type="defaultBold">
              Nouveau post
            </ThemedText>
          </Pressable>
        </View>
      )}
      <PostList isLoading={isLoading} posts={posts} />
    </>
  );
}

const newPostStyle = StyleSheet.create({
  newPost: {
    display: "flex",
    alignSelf: "center",
    marginTop: 25,
    width: 200,
  },
})

const styles = { ...newPostStyle, ...genericButtonStyle }
