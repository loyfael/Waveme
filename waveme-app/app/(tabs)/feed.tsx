import React, { useState, useEffect, useContext } from 'react';
import { View, ActivityIndicator, Pressable, StyleSheet, ScrollView, RefreshControl } from 'react-native';
import { useWebTitle } from '@/hooks/useWebTitle';
import { PostMetadataDto } from '../../types/index';
import { Post } from '../../types';
import PostList from '@/components/PostList';
import { ThemedText } from '@/components/theme/ThemedText';
import { genericButtonStyle } from '@/constants/commonStyles';
import { useRouter } from 'expo-router';
import { AuthContext } from '@/context/AuthContext';
import { getFeedPage } from '@/services/PostAPI';

export default function FeedScreen() {
  const [posts, setPosts] = useState<PostMetadataDto[]>([]);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(1);
  const [isLoading, setIsLoading] = useState(true);
  const [isRefreshing, setIsRefreshing] = useState(false);
  const [isFetchingMore, setIsFetchingMore] = useState(false);

  useWebTitle('Accueil');
  const router = useRouter();
  const { user } = useContext(AuthContext);

  // Convert PostMetadataDto to Post format for PostList component
  const convertToPost = (postMetadata: PostMetadataDto): Post => ({
    ...postMetadata,
    description: postMetadata.description || null,
    user: postMetadata.user || {
      id: 'unknown',
      pseudo: 'Utilisateur inconnu',
      profileImg: null,
    }
  });

  useEffect(() => {
    // Only load posts if user is authenticated
    if (user) {
      loadPosts(0, true);
    } else {
      setIsLoading(false);
    }
  }, [user]);

  const loadPosts = async (pageToLoad: number, isInitial = false) => {
    if (isFetchingMore || isRefreshing) return;
    if (pageToLoad >= totalPages && !isInitial) return;

    if (isInitial) setIsLoading(true);
    else setIsFetchingMore(true);

    try {
      const response = await getFeedPage(pageToLoad);
      const newPosts = response.content;

      // Debug: Log what the API returns
      console.log('Feed API response:', {
        postsCount: newPosts.length,
        firstPost: newPosts[0],
        hasUserData: newPosts.map(p => !!p.user),
        userInfo: newPosts.map(p => p.user)
      });

      if (isInitial) {
        setPosts(newPosts);
      } else {
        setPosts(prev => [...prev, ...newPosts]);
      }

      setPage(response.number + 1);
      setTotalPages(response.totalPages);
    } catch (error: any) {
      console.error('Erreur de chargement des posts', error);
      
      // If authentication error, don't set posts
      if (error.message === 'Authentication required') {
        setPosts([]);
      }
    } finally {
      setIsLoading(false);
      setIsFetchingMore(false);
      setIsRefreshing(false);
    }
  };

  const handleRefresh = () => {
    setIsRefreshing(true);
    loadPosts(0, true);
  };

  const handleLoadMore = () => {
    if (!isFetchingMore && page < totalPages) {
      loadPosts(page);
    }
  };

  return (
    <>
      {user && (
        <View style={styles.newPost}>
          <Pressable onPress={() => router.push('/new')} style={styles.genericButton}>
            <ThemedText style={styles.genericButtonText} type="defaultBold">
              Nouveau post
            </ThemedText>
          </Pressable>
        </View>
      )}

      {!user ? (
        <View style={{ marginTop: 50, alignItems: 'center' }}>
          <ThemedText type="title">Connectez-vous pour voir le feed</ThemedText>
          <Pressable 
            onPress={() => router.push('/login')} 
            style={[styles.genericButton, { marginTop: 20 }]}
          >
            <ThemedText style={styles.genericButtonText} type="defaultBold">
              Se connecter
            </ThemedText>
          </Pressable>
        </View>
      ) : isLoading ? (
        <ActivityIndicator style={{ marginTop: 30 }} size="large" />
      ) : (
        <ScrollView
          refreshControl={
            <RefreshControl refreshing={isRefreshing} onRefresh={handleRefresh} />
          }
          onScroll={({ nativeEvent }) => {
            const { layoutMeasurement, contentOffset, contentSize } = nativeEvent;
            const isCloseToBottom = layoutMeasurement.height + contentOffset.y >= contentSize.height - 20;
            if (isCloseToBottom && !isFetchingMore && page < totalPages) {
              handleLoadMore();
            }
          }}
          scrollEventThrottle={400}
          contentContainerStyle={{ paddingBottom: 20 }}
        >
          <PostList 
            isLoading={false}
            posts={posts.map(convertToPost)}
            setPosts={(newPosts: Post[]) => {
              // Convert back to PostMetadataDto format if needed
              setPosts(newPosts.map(p => ({
                postUniqueId: p.postUniqueId,
                imageUrl: p.imageUrl,
                description: p.description || '',
                upVote: p.upVote,
                downVote: p.downVote,
                voteSum: p.voteSum,
                createdAt: p.createdAt,
                user: p.user // IMPORTANT: Conserver les informations utilisateur
              })));
            }}
          />
          {isFetchingMore && <ActivityIndicator size="large" style={{ marginVertical: 20 }} />}
        </ScrollView>
      )}
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
});

const styles = { ...newPostStyle, ...genericButtonStyle };
