import { ThemedText } from "@/components/theme/ThemedText";
import { ThemedView } from "@/components/theme/ThemedView";
import { Colors } from "@/constants/Colors";
import { useWebTitle } from "@/hooks/useWebTitle";
import { fadeButtonToClicked, fadeButtonToIdle } from "@/utils/animateButton";
import { useLocalSearchParams, useRouter } from "expo-router";
import React, { useContext, useEffect, useState } from "react";
import { Animated, StyleSheet, View, Image, Pressable } from 'react-native'
import { Comment, Post, UserInfoLesser, UserProfilePictures } from "@/types";
import { memeStyle } from "@/constants/commonStyles";
import { useAnimatedButton } from "@/hooks/useAnimatedButton";
import Entypo from "@expo/vector-icons/Entypo";
import { useThemeColor } from "@/hooks/useThemeColor";
import { ThemedTextInput } from "@/components/theme/ThemedTextInput";
import { Loading } from "@/components/Loading";
import ReportModal from "@/components/ReportModal";
import { getPost, getPostVotes, votePost } from "@/services/PostAPI";
import { addComment, addReply, voteComment, voteReply } from "@/services/CommentAPI";
import { createLocalUriFromBackUri } from "@/utils/api";
import { MaterialIcons, Ionicons } from "@expo/vector-icons";
import dayjs from "dayjs";
import { AuthContext } from "@/context/AuthContext";
import { useResponsive } from "@/hooks/useResponsive";

export default function PostScreen() {
  const [post, setPost] = useState<Post | null>(null)
  const [reloadPost, setReloadPost] = useState(false)
  const [comments, setComments] = useState<Comment[]>([])
  const [loadedImage, setLoadedImage] = useState<string | null>(null)
  const [postVoteState, setPostVoteState] = useState<number>(0)
  const [profilePicturesLoading, setProfilePicturesLoading] = useState(true)
  const [loadedProfilePictures, setLoadedProfilePictures] = useState<UserProfilePictures>({
    post: "",
    comments: {},
  })
  const [input, setInput] = useState("")
  const [replyFocus, setReplyFocus] = useState<string | null>(null)
  const [replyInput, setReplyInput] = useState("")
  const [loadedReplies, setLoadedReplies] = useState<number[]>([])
  const [reportModalOpen, setReportModalOpen] = useState(false)
  const [reportedContent, setReportedContent] = useState<"post" | "comment" | "reply">("post")
  const [reportedUser, setReportedUser] = useState("")
  const [reportedMessage, setReportedMessage] = useState<string | null>(null)
  const [reportedId, setReportedId] = useState<string | number | null>(null)

  useWebTitle(`Post de ${post?.user?.pseudo ?? "l'utilisateur"}`)
  const router = useRouter()
  const { postId } = useLocalSearchParams<{ postId: string }>()
  const textColor = useThemeColor({}, 'text')
  const iconColor = useThemeColor({}, "icon")
  const areaBackgroundColor = useThemeColor({}, 'areaBackground')
  const { isSmallScreen, isVerySmallScreen } = useResponsive()
  const { user } = useContext(AuthContext)

  const AnimatedButton = Animated.createAnimatedComponent(Pressable)

  const { animatedButton: animatedButton1, backgroundColor: backgroundColor1 } = useAnimatedButton({
    idleColor: Colors.common.barButton,
    clickedColor: Colors.common.genericButtonPressed
  })

  const { animatedButton: animatedButton2, backgroundColor: backgroundColor2 } = useAnimatedButton({
    idleColor: Colors.common.barButton,
    clickedColor: Colors.common.upvote
  })

  const { animatedButton: animatedButton3, backgroundColor: backgroundColor3 } = useAnimatedButton({
    idleColor: Colors.common.barButton,
    clickedColor: Colors.common.downvote
  })

  const { animatedButton: animatedButton4, backgroundColor: backgroundColor4 } = useAnimatedButton({
    idleColor: Colors.common.barButton,
    clickedColor: Colors.common.downvote
  })

  const getAllProfilePictures = () => {
    // Removes obnoxious linter error on the actual return; !post should never be true on regular use
    if (!post) return {}

    let commentProfilePictures: { [key: string]: string } = {}
    comments.forEach((comment) => {
      if (comment.userInfo.profileImg && !commentProfilePictures?.[comment.userInfo.id]) {
        commentProfilePictures[comment.userInfo.id] = comment.userInfo.profileImg
      }
      comment.replies.forEach((reply) => {
        if (reply.userInfo.profileImg && !commentProfilePictures?.[reply.userInfo.id]) {
          commentProfilePictures[reply.userInfo.id] = reply.userInfo.profileImg
        }
      })
    })

    return {
      post: post.user.profileImg,
      comments: commentProfilePictures,
    }
  }

  const handleOpenReportModal = (
    contentType: "post" | "comment" | "reply",
    userName: string,
    message: string | null = null,
    id: string | number,
  ) => {
    setReportedContent(contentType)
    setReportedUser(userName)
    setReportedMessage(message)
    setReportedId(id)
    setReportModalOpen(true)
  }

  const handleViewReplies = (commentId: number) => {
    setLoadedReplies([commentId, ...loadedReplies])
  }

  const handleNewComment = () => {
    console.log('=== ADDING COMMENT ===');
    console.log('Comment content:', input);
    console.log('Post ID:', postId);
    
    addComment({ content: input }, postId)
      .then((response) => {
        console.log('Comment added successfully:', response?.data);
        setInput("")
        setReloadPost(!reloadPost)
      })
      .catch((error) => {
        console.error('Comment error:', error);
        console.error('Error response:', error.response?.data);
        console.error('Error status:', error.response?.status);
        console.error('Error message:', error.message);
      })
  }

  const handleNewReply = (commentId: number) => {
    console.log('=== ADDING REPLY ===');
    console.log('Reply content:', replyInput);
    console.log('Comment ID:', commentId);
    
    addReply({ content: replyInput }, commentId)
      .then((response) => {
        console.log('Reply added successfully:', response?.data);
        setReplyFocus(null)
        setReplyInput("")
        setReloadPost(!reloadPost)
      })
      .catch((error) => {
        console.error('Reply error:', error);
        console.error('Error response:', error.response?.data);
        console.error('Error status:', error.response?.status);
        console.error('Error message:', error.message);
        // Ne pas vider l'input ni recharger si il y a une erreur
      })
  }

  const handleVotePost = async (upvote: boolean) => {
    // Prevent multiple votes while processing
    if (!post) return;

    const currentVoteState = postVoteState; // 0: no vote, 1: upvote, -1: downvote
    const newVoteState = upvote ? 1 : -1;

    // If clicking the same vote, remove it (toggle off)
    if (currentVoteState === newVoteState) {
      setPostVoteState(0);

      // Update post data for vote removal
      let voteChange = currentVoteState === 1 ? -1 : 1; // Remove the previous vote
      let upVoteChange = currentVoteState === 1 ? -1 : 0;
      let downVoteChange = currentVoteState === -1 ? -1 : 0;

      setPost({
        ...post,
        voteSum: post.voteSum + voteChange,
        upVote: post.upVote + upVoteChange,
        downVote: post.downVote + downVoteChange,
      });

      // Make API call
      try {
        await votePost(postId, upvote);
      } catch (err) {
        console.error('Vote removal error:', err);
        // Revert on error
        setPostVoteState(currentVoteState);
        setPost({
          ...post,
          voteSum: post.voteSum,
          upVote: post.upVote,
          downVote: post.downVote,
        });
      }
      return;
    }

    // If changing vote type or voting for first time
    try {
      await votePost(postId, upvote);

      // Update vote state
      setPostVoteState(newVoteState);

      // Update post data
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

      setPost({
        ...post,
        voteSum: post.voteSum + voteChange,
        upVote: post.upVote + upVoteChange,
        downVote: post.downVote + downVoteChange,
      });
    } catch (err: any) {
      console.error('Vote error:', err);

      // Handle specific error types
      if (err?.response?.status === 403) {
        console.log('Vote forbidden - User may not have permission to vote on this post');
      } else if (err?.response?.status === 401) {
        console.log('Vote unauthorized - User needs to log in');
      } else {
        console.log('Unknown vote error:', err?.response?.status, err?.message);
      }
    }
  }

  const handleVoteComment = (commentId: number, upvote: boolean) => {
    voteComment(commentId, upvote)
      .catch((err) => {
        console.error(err)
      })
      .then(() => {
        setReloadPost(!reloadPost)
      })
  }

  const handleVoteReply = (replyId: number, upvote: boolean) => {
    voteReply(replyId, upvote)
      .catch((err) => {
        console.error(err)
      })
      .then(() => {
        setReloadPost(!reloadPost)
      })
  }

  useEffect(() => {
    getPost(postId)
      .catch((error) => {
        console.error(error)
      })
      .then(async (response) => {
        // Split the response into two values: comments contains the key of the same name, post contains the rest without comments
        const { comments, ...post } = response.data
        setPost(post)
        setComments(comments)
        const dataUri = await createLocalUriFromBackUri(response.data.imageUrl, "post")
        if (dataUri) {
          setLoadedImage(dataUri)
        }
      })
  }, [postId, reloadPost])

  // TODO: Absolutely not critical, maybe figure out a way to not have to reload already loaded images
  // (occurrence related to further reloads while on the page)
  useEffect(() => {
    if (post && profilePicturesLoading) {
      const loadAllProfilePictures = async () => {
        const pageProfilePictures = getAllProfilePictures()

        let loadingImages: UserProfilePictures = { post: "", comments: {} }
        await Promise.all(
          Object.keys(pageProfilePictures).map(async (category) => {
            switch (category) {
              case "post":
                if (pageProfilePictures.post) {
                  const dataUri = await createLocalUriFromBackUri(pageProfilePictures.post, "profile")
                  if (dataUri) {
                    loadingImages.post = dataUri
                  }
                }
                break

              case "comments":
                if (pageProfilePictures.comments) {
                  Object.keys(pageProfilePictures.comments).map(async (comment) => {
                    const dataUri = await createLocalUriFromBackUri(pageProfilePictures.comments[comment], "profile")
                    if (dataUri) {
                      loadingImages.comments[comment] = dataUri
                    }
                  })
                }
                break

              default:
                break
            }
          })
        )
        setLoadedProfilePictures(loadingImages)
      }
      loadAllProfilePictures()
      setProfilePicturesLoading(false)
    }
  }, [post, loadedProfilePictures])

  useEffect(() => {
    // Using prev because setting the state in a standard fashion doesn't work with setInterval
    const interval = setInterval(() => setReloadPost(prev => !prev), 10000);

    // Cleanup interval on unmount
    return () => clearInterval(interval);
  }, []);

  useEffect(() => {
    if (post) {
      getPostVotes(post.postUniqueId)
        .catch((err) => {
          console.error(err)
        })
        .then(({ data }) => {
          if (data.upvoters.map((upvoter: UserInfoLesser) => upvoter.id).includes(user?.id)) {
            setPostVoteState(1)
          } else if (data.downvoters.map((downvoter: UserInfoLesser) => downvoter.id).includes(user?.id)) {
            setPostVoteState(-1)
          } else {
            setPostVoteState(0)
          }
        })
    }
  }, [post])

  return (
    <>
      {post ? (
        <>
          {/* Post */}
          <ThemedView style={styles.postWrapper}>
            <View style={styles.postProfile}>
              <Pressable onPress={() => { router.push(`/user/${post.user.id}`) }}>
                {post.user.profileImg ? (
                  <Image source={{ uri: loadedProfilePictures.post }} style={styles.profilePicture} />
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
              <Image source={{ uri: loadedImage as string }} style={{ ...styles.memeImage, backgroundColor: areaBackgroundColor }} resizeMode='contain' />
              <View style={styles.memeActionBar}>
                <View style={styles.barLeft}>
                  <AnimatedButton
                    onPressIn={() => fadeButtonToClicked(animatedButton1)}
                    onPressOut={() => fadeButtonToIdle(animatedButton1)}
                    onPress={() => { handleOpenReportModal("post", post.user.pseudo, post.description, postId) }}>
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
                    key={`upvote-${postId}-${postVoteState}`}
                    onPressIn={() => fadeButtonToClicked(animatedButton2)}
                    onPressOut={() => fadeButtonToIdle(animatedButton2)}
                    onPress={() => { handleVotePost(true) }}>
                    <Animated.View style={{
                      ...styles.barButton,
                      backgroundColor: postVoteState === 1 ? Colors.common.upvote : backgroundColor2
                    }}>
                      <Ionicons name="arrow-up" color={Colors.common.memeActionBar} size={36} />
                    </Animated.View>
                  </AnimatedButton>
                  <AnimatedButton
                    key={`downvote-${postId}-${postVoteState}`}
                    onPressIn={() => fadeButtonToClicked(animatedButton3)}
                    onPressOut={() => fadeButtonToIdle(animatedButton3)}
                    onPress={() => { handleVotePost(false) }}>
                    <Animated.View style={{
                      ...styles.barButton,
                      backgroundColor: postVoteState === -1 ? Colors.common.downvote : backgroundColor3
                    }}>
                      <Ionicons name="arrow-down" color={Colors.common.memeActionBar} size={36} />
                    </Animated.View>
                  </AnimatedButton>
                </View>
              </View>
            </View>
          </ThemedView>

          {/* Comments */}
          <ThemedView style={styles.comments}>
            <View style={styles.addComment}>
              <View
                style={isSmallScreen
                  ? isVerySmallScreen
                    ? styles.commentInputContainerTinyScreen
                    : styles.commentInputContainerSmallScreen
                  : styles.commentInputContainer}
              >
                <ThemedTextInput
                  value={input}
                  onChangeText={setInput}
                  placeholder="Ajouter un commentaire..."
                  multiline
                  numberOfLines={isSmallScreen ? 3 : 2}
                  style={styles.commentInput}
                />
              </View>
              <Pressable style={styles.sendComment} onPress={handleNewComment}>
                <Ionicons name="send" size={32} color={iconColor} />
              </Pressable>
            </View>
            <View>
              {comments.map((comment, commentKey) => (
                <View key={commentKey} style={styles.commentWrapper}>
                  <View style={styles.postProfile}>
                    <Pressable onPress={() => { router.push(`/user/${comment.userInfo.id}`) }}>
                      {loadedProfilePictures.comments?.[comment.userInfo.id] ? (
                        <Image source={{ uri: loadedProfilePictures.comments?.[comment.userInfo.id] }} style={styles.profilePicture} />
                      ) : (
                        <MaterialIcons name="account-circle" size={40} color={iconColor} style={styles.profilePicture} />
                      )}
                    </Pressable>
                    <View style={styles.profileText}>
                      <Pressable onPress={() => { router.push(`/user/${comment.userInfo.id}`) }}>
                        <ThemedText type="defaultBold">{comment.userInfo.pseudo}</ThemedText>
                      </Pressable>
                      <ThemedText type="small">{dayjs(comment.createdAt).format("HH:mm DD/MM/YYYY")}</ThemedText>
                      <View style={{ ...styles.commentContainer, backgroundColor: areaBackgroundColor }}>
                        <ThemedText style={styles.commentOverride}>{comment.content}</ThemedText>
                      </View>
                    </View>
                  </View>
                  <View style={styles.commentActionsWrapper}>
                    {comment.id === replyFocus && (
                      <View style={styles.replyInputContainer}>
                        <View
                          style={isSmallScreen
                            ? isVerySmallScreen
                              ? styles.commentInputContainerTinyScreen
                              : styles.commentInputContainerSmallScreen
                            : styles.commentInputContainer}
                        >
                          <ThemedTextInput
                            value={replyInput}
                            onChangeText={setReplyInput}
                            placeholder="Ajouter une réponse..."
                            style={styles.commentInput}
                          />
                        </View>
                        <Pressable style={styles.sendComment} onPress={() => { handleNewReply(comment.commentUniqueId) }}>
                          <Ionicons name="send" size={32} color={iconColor} />
                        </Pressable>
                      </View>
                    )}
                    <View style={styles.commentActionsContainer}>
                      <Pressable onPress={() => { setReplyFocus(comment.id) }}>
                        <ThemedText type="small">Répondre</ThemedText>
                      </Pressable>
                      <Pressable onPress={() => { handleOpenReportModal("comment", comment.userInfo.pseudo, comment.content, comment.commentUniqueId) }}>
                        <ThemedText type="small">Signaler</ThemedText>
                      </Pressable>
                      <ThemedText type="small">|</ThemedText>
                      <Pressable onPress={() => { handleVoteComment(comment.commentUniqueId, true) }}>
                        <ThemedText type="small">Upvote</ThemedText>
                      </Pressable>
                      <ThemedText type="small" style={{ color: comment.upVote - comment.downVote >= 0 ? Colors.common.upvote : Colors.common.downvote }}>
                        {comment.upVote - comment.downVote > 0 ? "+" : ""}{comment.upVote - comment.downVote}
                      </ThemedText>
                      <Pressable onPress={() => { handleVoteComment(comment.commentUniqueId, false) }}>
                        <ThemedText type="small">Downvote</ThemedText>
                      </Pressable>
                    </View>
                  </View>
                  {comment.replies.length ? loadedReplies.includes(comment.commentUniqueId) ? (
                    <View style={{ ...styles.repliesContainer, borderLeftColor: textColor }}>
                      {comment.replies.map((reply, replyKey) => (
                        <View key={`reply${replyKey}`}>
                          <View style={styles.postProfile}>
                            <Pressable onPress={() => { router.push(`/user/${reply.userInfo.id}`) }}>
                              {loadedProfilePictures.comments?.[reply.userInfo.id] ? (
                                <Image source={{ uri: loadedProfilePictures.comments?.[reply.userInfo.id] }} style={styles.profilePicture} />
                              ) : (
                                <MaterialIcons name="account-circle" size={40} color={iconColor} style={styles.profilePicture} />
                              )}
                            </Pressable>
                            <View style={styles.profileText}>
                              <Pressable onPress={() => { router.push(`/user/${reply.userInfo.id}`) }}>
                                <ThemedText type="defaultBold">{reply.userInfo.pseudo}</ThemedText>
                              </Pressable>
                              <ThemedText type="small">{dayjs(reply.createdAt).format("HH:mm DD/MM/YYYY")}</ThemedText>
                              <View style={{ ...styles.commentContainer, backgroundColor: areaBackgroundColor }}>
                                <ThemedText style={styles.commentOverride}>{reply.content}</ThemedText>
                              </View>
                            </View>
                          </View>
                          <View style={styles.commentActionsWrapper}>
                            <View style={styles.commentActionsContainer}>
                              <Pressable onPress={() => { handleOpenReportModal("reply", reply.userInfo.pseudo, reply.content, reply.replyUniqueId) }}>
                                <ThemedText type="small">Signaler</ThemedText>
                              </Pressable>
                              <ThemedText type="small">|</ThemedText>
                              <Pressable onPress={() => { handleVoteReply(reply.replyUniqueId, true) }}>
                                <ThemedText type="small">Upvote</ThemedText>
                              </Pressable>
                              <ThemedText type="small" style={{ color: reply.upVote - reply.downVote >= 0 ? Colors.common.upvote : Colors.common.downvote }}>
                                {reply.upVote - reply.downVote > 0 ? "+" : ""}{reply.upVote - reply.downVote}
                              </ThemedText>
                              <Pressable onPress={() => { handleVoteReply(reply.replyUniqueId, false) }}>
                                <ThemedText type="small">Downvote</ThemedText>
                              </Pressable>
                            </View>
                          </View>
                        </View>
                      ))}
                    </View>
                  ) : (
                    <Pressable onPress={() => { handleViewReplies(comment.commentUniqueId) }}>
                      <ThemedText style={styles.viewReplies}>Voir les réponses</ThemedText>
                    </Pressable>
                  ) : (<></>)}
                </View>
              ))}
            </View>
          </ThemedView>

          <ReportModal
            visible={reportModalOpen}
            setVisible={setReportModalOpen}
            reportedContent={reportedContent}
            userName={reportedUser}
            id={reportedId ?? ""}
            message={reportedMessage}
          />
        </>
      ) : (<Loading />)}
    </>
  )
}

const localStyles = StyleSheet.create({
  comments: {
    flex: 1,
    flexDirection: 'column',
    marginBottom: 25,
  },

  addComment: {
    width: '100%',
    alignItems: 'center',
    justifyContent: 'center',
    flexDirection: 'row',
    marginVertical: 15,
  },

  commentInputContainer: {
    width: 500,
  },

  commentInputContainerSmallScreen: {
    width: 300,
  },

  commentInputContainerTinyScreen: {
    width: 200,
  },

  commentInput: {
    flex: 1,
  },

  sendComment: {
    marginLeft: 10,
  },

  commentContainer: {
    maxWidth: 750,
    padding: 8,
    alignSelf: 'flex-start',
    borderBottomLeftRadius: 8,
    borderBottomRightRadius: 8,
    borderTopRightRadius: 8,
  },

  commentOverride: {
    color: Colors.common.placeholderTextColor,
  },

  commentActionsWrapper: {
    marginLeft: 45,
    gap: 5,
    paddingTop: 8,
  },

  replyInputContainer: {
    display: "flex",
    flexDirection: "row",
    alignItems: "center",
  },

  commentActionsContainer: {
    display: "flex",
    flexDirection: "row",
    gap: 8,
  },

  repliesContainer: {
    marginLeft: 5,
    paddingLeft: 15,
    borderLeftWidth: 1,
    borderBottomLeftRadius: 30,
  },

  commentWrapper: {
    marginTop: 15,
    marginBottom: 30,
  },

  viewReplies: {
    marginLeft: 45,
  }
})

const styles = { ...memeStyle, ...localStyles }
