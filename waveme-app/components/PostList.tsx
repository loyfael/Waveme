import React, { useContext, useEffect, useState } from "react";
import { Loading } from "./Loading";
import { ThemedView } from "./theme/ThemedView";
import { Animated, View, Image, Pressable } from "react-native";
import { Colors } from "@/constants/Colors";
import { fadeButtonToClicked, fadeButtonToIdle } from "@/utils/animateButton";
import { Entypo, MaterialIcons, Ionicons } from "@expo/vector-icons";
import { memeStyle } from "@/constants/commonStyles";
import { ThemedText } from "./theme/ThemedText";
import { useAnimatedButton } from "@/hooks/useAnimatedButton";
import { useThemeColor } from "@/hooks/useThemeColor";
import { Post, UserInfoLesser } from "@/types";
import { useRouter } from "expo-router";
import ReportModal from "./ReportModal";
import { createLocalUriFromBackUri } from "@/utils/api";
import { getPostVotes, votePost } from "@/services/PostAPI";
import dayjs from "dayjs";
import { AuthContext } from "@/context/AuthContext";

type PostListProps = {
  isLoading: boolean,
  posts: Post[],
  setPosts: Function
}

export default function PostList(props: PostListProps) {
  const [voteStates, setVoteStates] = useState<{ [key: number]: number | string }>({})
  const [reportModalOpen, setReportModalOpen] = useState(false)
  const [reportedContent, setReportedContent] = useState<"post" | "comment">("post")
  const [reportedUser, setReportedUser] = useState("")
  const [reportedMessage, setReportedMessage] = useState<string | null>(null)
  const [loadedImages, setLoadedImages] = useState<{ [key: string]: string }>({})
  const [loadedProfilePictures, setLoadedProfilePictures] = useState<{ [key: string]: string }>({})

  const AnimatedButton = Animated.createAnimatedComponent(Pressable)

  const { animatedButton: animatedButton1, backgroundColor: backgroundColor1 } = useAnimatedButton({
    idleColor: Colors.common.barButton,
    clickedColor: Colors.common.genericButtonPressed
  })

  const { animatedButton: animatedButton2, backgroundColor: backgroundColor2 } = useAnimatedButton({
    idleColor: Colors.common.barButton,
    clickedColor: Colors.common.genericButtonPressed
  })

  const { animatedButton: animatedButton3, backgroundColor: backgroundColor3 } = useAnimatedButton({
    idleColor: Colors.common.barButton,
    clickedColor: Colors.common.upvote
  })

  const { animatedButton: animatedButton4, backgroundColor: backgroundColor4 } = useAnimatedButton({
    idleColor: Colors.common.barButton,
    clickedColor: Colors.common.downvote
  })

  const { user } = useContext(AuthContext)
  const router = useRouter()
  const areaBackgroundColor = useThemeColor({}, 'areaBackground')
  const iconColor = useThemeColor({}, "icon")

  const handleOpenReportModal = (
    contentType: "post" | "comment",
    userName: string,
    message: string | null = null,
  ) => {
    setReportedContent(contentType)
    setReportedUser(userName)
    setReportedMessage(message)
    setReportModalOpen(true)
  }

  const handleVotePost = async (postId: number, upvote: boolean, index: number) => {
    // Prevent multiple votes while processing
    if (voteStates[postId] === 'loading') return;
    
    const currentVoteState = typeof voteStates[postId] === 'number' ? voteStates[postId] : 0; // 0: no vote, 1: upvote, -1: downvote
    const newVoteState = upvote ? 1 : -1;
    
    // If clicking the same vote, remove it (toggle off)
    if (currentVoteState === newVoteState) {
      setVoteStates(prev => ({ ...prev, [postId]: 0 }));
      
      // Update post data for vote removal
      let newList = [...props.posts];
      const post = newList[index];
      
      // Calculate changes for vote removal
      let voteChange = currentVoteState === 1 ? -1 : 1; // Remove the previous vote
      let upVoteChange = currentVoteState === 1 ? -1 : 0;
      let downVoteChange = currentVoteState === -1 ? -1 : 0;
      
      newList[index] = {
        ...post,
        voteSum: post.voteSum + voteChange,
        upVote: post.upVote + upVoteChange,
        downVote: post.downVote + downVoteChange,
      };
      
      props.setPosts(newList);
      return;
    }
    
    // If changing vote type or voting for first time
    setVoteStates(prev => ({ ...prev, [postId]: 'loading' }));
    
    try {
      await votePost(postId, upvote);
      
      // Update vote state
      setVoteStates(prev => ({ ...prev, [postId]: newVoteState }));
      
      // Update post data
      let newList = [...props.posts];
      const post = newList[index];
      
      // Calculate vote changes
      let voteChange = 0;
      let upVoteChange = 0;
      let downVoteChange = 0;
      
      if (currentVoteState === 0) {
        // First vote
        voteChange = upvote ? 1 : -1;
        upVoteChange = upvote ? 1 : 0;
        downVoteChange = upvote ? 0 : 1;
      } else if (currentVoteState === 1 && !upvote) {
        // Changed from upvote to downvote
        voteChange = -2;
        upVoteChange = -1;
        downVoteChange = 1;
      } else if (currentVoteState === -1 && upvote) {
        // Changed from downvote to upvote
        voteChange = 2;
        upVoteChange = 1;
        downVoteChange = -1;
      }
      
      newList[index] = {
        ...post,
        voteSum: post.voteSum + voteChange,
        upVote: post.upVote + upVoteChange,
        downVote: post.downVote + downVoteChange,
      };
      
      props.setPosts(newList);
    } catch (err: any) {
      console.error('Vote error:', err);
      
      // Handle specific error types
      if (err?.response?.status === 403) {
        console.log('Vote forbidden - User may not have permission to vote on this post');
        // You might want to show a user-friendly message here
      } else if (err?.response?.status === 401) {
        console.log('Vote unauthorized - User needs to log in');
      } else {
        console.log('Unknown vote error:', err?.response?.status, err?.message);
      }
      
      // Reset vote state on error
      setVoteStates(prev => ({ ...prev, [postId]: currentVoteState }));
    }
  }

  useEffect(() => {
    const loadAllImages = async () => {
      let loadingImages: { [key: string]: string } = {}
      let loadingProfilePictures: { [key: string]: string } = {}
      await Promise.all(
        props.posts.map(async (post) => {
          const dataUri = await createLocalUriFromBackUri(post.imageUrl, "post")
          loadingImages[post.postUniqueId] = dataUri
          if (post.user.profileImg) {
            const profilePictureDataUri = await createLocalUriFromBackUri(post.user.profileImg, "profile")
            loadingProfilePictures[post.user.id] = profilePictureDataUri
          }
        })
      )
      setLoadedImages(loadingImages)
      setLoadedProfilePictures(loadingProfilePictures)
    }
    loadAllImages()

    // Only load votes when posts are initially loaded (not on subsequent updates)
    const loadAllVotes = async () => {
      // Only load votes if voteStates is empty (initial load)
      if (Object.keys(voteStates).length === 0) {
        let upvoteStates: { [key: number]: number } = {}
        if (props.posts) {
          await Promise.all(
            props.posts.map(async (post) => {
              await getPostVotes(post.postUniqueId)
                .catch((err) => {
                  console.error(err)
                })
                .then(({ data }) => {
                  if (data.upvoters.map((upvoter: UserInfoLesser) => upvoter.id).includes(user?.id)) {
                    upvoteStates[post.postUniqueId] = 1
                  } else if (data.downvoters.map((downvoter: UserInfoLesser) => downvoter.id).includes(user?.id)) {
                    upvoteStates[post.postUniqueId] = -1
                  } else {
                    upvoteStates[post.postUniqueId] = 0
                  }
                })
            })
          )
        }
        setVoteStates(upvoteStates)
      }
    }
    loadAllVotes()

    // Clean up the images on unmount
    return () => {
      Object.values(loadedImages).forEach((image) => {
        URL.revokeObjectURL(image)
      })
    }
  }, [props.posts])

  return (
    <>
      {!props.isLoading ? props.posts.map((post, index) => (
        <ThemedView key={post.postUniqueId} style={styles.postWrapper}>
          {/* Profile and post title */}
          <View style={styles.postProfile}>
            <Pressable onPress={() => { router.push(`/user/${post.user.id}`) }}>
              {loadedProfilePictures[post.user.id] ? (
                <Image source={{ uri: loadedProfilePictures[post.user.id] }} style={styles.profilePicture} />
              ) : (
                <MaterialIcons name="account-circle" size={40} color={iconColor} style={styles.profilePicture} />
              )}
            </Pressable>
            <View style={styles.profileText}>
              <Pressable onPress={() => { router.push(`/user/${post.user.id}`) }}>
                <ThemedText type="defaultBold">{post.user.pseudo}</ThemedText>
              </Pressable>
              <ThemedText type="small">{dayjs(post.createdAt).format("HH:mm DD/MM/YYYY")}</ThemedText>
              {post.description ? (
                <ThemedText>{post.description}</ThemedText>
              ) : ''}
            </View>
          </View>

          <View style={styles.postMeme}>
            <Image
              source={{ uri: loadedImages?.[post.postUniqueId] }}
              style={{ ...styles.memeImage, backgroundColor: areaBackgroundColor }}
              resizeMode='contain'
            />
            <View style={styles.memeActionBar}>
              <View style={styles.barLeft}>
                <AnimatedButton
                  onPressIn={() => fadeButtonToClicked(animatedButton1)}
                  onPressOut={() => fadeButtonToIdle(animatedButton1)}
                  onPress={() => { handleOpenReportModal("post", post.user.pseudo, post.description) }}>
                  <Animated.View style={{ ...styles.barButton, backgroundColor: backgroundColor1 }}>
                    <Entypo name="flag" size={36} color={Colors.common.memeActionBar} />
                  </Animated.View>
                </AnimatedButton>
              </View>
              <View style={styles.barRight}>
                <View style={{ ...styles.barButton, backgroundColor: Colors.common.memeActionBar }}>
                  <ThemedText type="defaultBold" style={{ color: post.voteSum >= 0 ? Colors.common.upvote : Colors.common.downvote }}>
                    {post.voteSum > 0 ? "+" : ""}{post.voteSum}
                  </ThemedText>
                </View>
                <AnimatedButton
                  onPressIn={() => fadeButtonToClicked(animatedButton2)}
                  onPressOut={() => fadeButtonToIdle(animatedButton2)}
                  onPress={() => { router.push(`/post/${post.postUniqueId}`) }}>
                  <Animated.View style={{ ...styles.barButton, backgroundColor: backgroundColor2 }}>
                    <Ionicons name="chatbubbles" color={Colors.common.memeActionBar} size={30} />
                  </Animated.View>
                </AnimatedButton>
                <AnimatedButton
                  onPressIn={() => fadeButtonToClicked(animatedButton3)}
                  onPressOut={() => fadeButtonToIdle(animatedButton3)}
                  onPress={() => { handleVotePost(post.postUniqueId, true, index) }}>
                  <Animated.View style={{
                    ...styles.barButton,
                    backgroundColor: voteStates[post.postUniqueId] === 1 ? Colors.common.upvote : backgroundColor3
                  }}>
                    <Ionicons name="chevron-up" color={Colors.common.memeActionBar} size={36} />
                  </Animated.View>
                </AnimatedButton>
                <AnimatedButton
                  onPressIn={() => fadeButtonToClicked(animatedButton4)}
                  onPressOut={() => fadeButtonToIdle(animatedButton4)}
                  onPress={() => { handleVotePost(post.postUniqueId, false, index) }}>
                  <Animated.View style={{
                    ...styles.barButton,
                    backgroundColor: voteStates[post.postUniqueId] === -1 ? Colors.common.downvote : backgroundColor3
                  }}>
                    <Ionicons name="chevron-down" color={Colors.common.memeActionBar} size={36} />
                  </Animated.View>
                </AnimatedButton>
              </View>
            </View>
          </View>

          <ReportModal
            visible={reportModalOpen}
            setVisible={setReportModalOpen}
            reportedContent={reportedContent}
            userName={reportedUser}
            id={post.postUniqueId}
            message={reportedMessage}
          />
        </ThemedView>
      )) : (<Loading />)}
    </>
  )
}

const styles = { ...memeStyle }
