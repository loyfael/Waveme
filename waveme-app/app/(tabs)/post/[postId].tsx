import { ThemedText } from "@/components/theme/ThemedText";
import { ThemedView } from "@/components/theme/ThemedView";
import { Colors } from "@/constants/Colors";
import { useWebTitle } from "@/hooks/useWebTitle";
import { fadeButtonToClicked, fadeButtonToIdle } from "@/utils/animateButton";
import { useLocalSearchParams, useRouter } from "expo-router";
import React, { useEffect, useState } from "react";
import { Animated, StyleSheet, View, Image, Pressable } from 'react-native'
import { Comment, Post, UserProfilePictures } from "@/types";
import { memeStyle } from "@/constants/commonStyles";
import { useAnimatedButton } from "@/hooks/useAnimatedButton";
import Entypo from "@expo/vector-icons/Entypo";
import { BiSolidDownArrowAlt, BiSolidUpArrowAlt } from "react-icons/bi";
import { useThemeColor } from "@/hooks/useThemeColor";
import { ThemedTextInput } from "@/components/theme/ThemedTextInput";
import { IoSend } from "react-icons/io5";
import { Loading } from "@/components/Loading";
import ReportModal from "@/components/ReportModal";
import { useMediaQuery } from "react-responsive";
import { getPost } from "@/services/PostAPI";
import { addComment, addReply } from "@/services/CommentAPI";
import { createLocalUriFromBackUri } from "@/utils/api";
import { MaterialIcons } from "@expo/vector-icons";

export default function PostScreen() {
  const [post, setPost] = useState<Post | null>(null)
  const [comments, setComments] = useState<Comment[]>([])
  const [loadedImage, setLoadedImage] = useState<string | null>(null)
  const [loadedProfilePictures, setLoadedProfilePictures] = useState<UserProfilePictures>({
    post: "",
    comments: {},
    replies: {},
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
  const textColor = useThemeColor({}, 'text')
  const { postId } = useLocalSearchParams<{ postId: string }>()

  const iconColor = useThemeColor({}, "icon")

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

  const areaBackgroundColor = useThemeColor({}, 'areaBackground')
  const isSmallScreen = useMediaQuery({ query: '(max-width: 1200px)' })

  const getAllProfilePictures = () => {
    // Removes obnoxious linter error on the actual return; !post should never be true on regular use
    if (!post) return {}

    let commentProfilePictures: { [key: string]: string } = {}
    let replyProfilePictures: { [key: string]: string } = {}
    comments.forEach((comment) => {
      if (comment.userInfo.profileImg) {
        commentProfilePictures[comment.id] = comment.userInfo.profileImg
      }
      comment.replies.forEach((reply) => {
        if (reply.userInfo.profileImg) {
          replyProfilePictures[reply.id] = reply.userInfo.profileImg
        }
      })
    })

    return {
      post: post.user.profileImg,
      comments: commentProfilePictures,
      replies: replyProfilePictures,
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
    addComment({ content: input }, postId)
      .catch((error) => {
        console.error(error)
      })
      .then((response) => {
        setInput("")
        // TODO: Reload comments
      })
  }

  const handleNewReply = (commentId: number) => {
    addReply({ content: replyInput }, commentId)
      .catch((error) => {
        console.error(error)
      })
      .then((response) => {
        setReplyFocus(null)
        setReplyInput("")
        // TODO: Reload comments
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
        setLoadedImage(dataUri)
      })
  }, [postId])

  useEffect(() => {
    if (post) {
      const loadAllProfilePictures = async () => {
        const pageProfilePictures = getAllProfilePictures()

        let loadingImages: UserProfilePictures = { post: "", comments: {}, replies: {} }
        await Promise.all(
          Object.keys(pageProfilePictures).map(async (category) => {
            switch (category) {
              case "post":
                if (pageProfilePictures.post) {
                  const dataUri = await createLocalUriFromBackUri(pageProfilePictures.post, "profile")
                  loadingImages.post = dataUri
                }
                break

              case "comments":
                if (pageProfilePictures.comments) {
                  Object.keys(pageProfilePictures.comments).map(async (comment) => {
                    const dataUri = await createLocalUriFromBackUri(pageProfilePictures.comments[comment], "profile")
                    loadingImages.comments[comment] = dataUri
                  })
                }
                break

              case "replies":
                if (pageProfilePictures.replies) {
                  Object.keys(pageProfilePictures.replies).map(async (reply) => {
                    const dataUri = await createLocalUriFromBackUri(pageProfilePictures.replies[reply], "profile")
                    loadingImages.replies[reply] = dataUri
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
    }
  }, [post])

  return (
    <>
      {post ? (
        <>
          {/* Post */}
          <ThemedView style={styles.postWrapper}>
            <View style={styles.postProfile}>
              <Pressable onPress={() => { router.push(`/user/${post.postUniqueId}`) }}>
                {post.user.profileImg ? (
                  <Image source={{ uri: loadedProfilePictures.post }} style={styles.profilePicture} />
                ) : (
                  <MaterialIcons name="account-circle" size={40} color={iconColor} style={styles.profilePicture} />
                )}
              </Pressable>
              <View style={styles.profileText}>
                <Pressable onPress={() => { router.push(`/user/${post.postUniqueId}`) }}>
                  <ThemedText type="defaultBold">{post.user.pseudo}</ThemedText>
                </Pressable>
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
                  <AnimatedButton
                    onPressIn={() => fadeButtonToClicked(animatedButton2)}
                    onPressOut={() => fadeButtonToIdle(animatedButton2)}
                    onPress={() => { }}>
                    <Animated.View style={{ ...styles.barButton, backgroundColor: backgroundColor2 }}>
                      <BiSolidUpArrowAlt color={Colors.common.memeActionBar} size={36} />
                    </Animated.View>
                  </AnimatedButton>
                  <AnimatedButton
                    onPressIn={() => fadeButtonToClicked(animatedButton3)}
                    onPressOut={() => fadeButtonToIdle(animatedButton3)}
                    onPress={() => { }}>
                    <Animated.View style={{ ...styles.barButton, backgroundColor: backgroundColor3 }}>
                      <BiSolidDownArrowAlt color={Colors.common.memeActionBar} size={36} />
                    </Animated.View>
                  </AnimatedButton>
                </View>
              </View>
            </View>
          </ThemedView>

          {/* Comments */}
          <ThemedView style={styles.comments}>
            <View style={styles.addComment}>
              <View style={isSmallScreen ? styles.commentInputContainerSmallScreen : styles.commentInputContainer}>
                <ThemedTextInput
                  value={input}
                  onChangeText={setInput}
                  placeholder="Ajouter un commentaire..."
                  autoFocus
                  multiline
                  numberOfLines={isSmallScreen ? 3 : 2}
                  style={styles.commentInput}
                />
              </View>
              <Pressable style={styles.sendComment} onPress={handleNewComment}>
                <IoSend size={32} color={iconColor} />
              </Pressable>
            </View>
            <View>
              {comments.map((comment, commentKey) => (
                <View key={commentKey} style={styles.commentWrapper}>
                  <View style={styles.postProfile}>
                    <Pressable onPress={() => { router.push(`/user/${comment.userInfo.id}`) }}>
                      {loadedProfilePictures.comments?.[comment.id] ? (
                        <Image source={{ uri: loadedProfilePictures.comments?.[comment.id] }} style={styles.profilePicture} />
                      ) : (
                        <MaterialIcons name="account-circle" size={40} color={iconColor} style={styles.profilePicture} />
                      )}
                    </Pressable>
                    <View style={styles.profileText}>
                      <Pressable onPress={() => { router.push(`/user/${comment.userInfo.id}`) }}>
                        <ThemedText type="defaultBold">{comment.userInfo.pseudo}</ThemedText>
                      </Pressable>
                      <View style={{ ...styles.commentContainer, backgroundColor: areaBackgroundColor }}>
                        <ThemedText style={styles.commentOverride}>{comment.content}</ThemedText>
                      </View>
                    </View>
                  </View>
                  <View style={styles.commentActionsWrapper}>
                    {comment.id === replyFocus && (
                      <View style={styles.replyInputContainer}>
                        <View style={isSmallScreen ? styles.commentInputContainerSmallScreen : styles.commentInputContainer}>
                          <ThemedTextInput
                            value={replyInput}
                            onChangeText={setReplyInput}
                            placeholder="Ajouter une réponse..."
                            style={styles.commentInput}
                          />
                        </View>
                        <Pressable style={styles.sendComment} onPress={() => { handleNewReply(comment.commentUniqueId) }}>
                          <IoSend size={32} color="white" />
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
                    </View>
                  </View>
                  {comment.replies.length ? loadedReplies.includes(comment.commentUniqueId) ? (
                    <View style={{ ...styles.repliesContainer, borderLeftColor: textColor }}>
                      {comment.replies.map((reply, replyKey) => (
                        <View key={`reply${replyKey}`}>
                          <View style={styles.postProfile}>
                            <Pressable onPress={() => { router.push(`/user/${reply.userInfo.id}`) }}>
                              {loadedProfilePictures.replies?.[reply.id] ? (
                                <Image source={{ uri: loadedProfilePictures.replies?.[reply.id] }} style={styles.profilePicture} />
                              ) : (
                                <MaterialIcons name="account-circle" size={40} color={iconColor} style={styles.profilePicture} />
                              )}
                            </Pressable>
                            <View style={styles.profileText}>
                              <Pressable onPress={() => { router.push(`/user/${reply.userInfo.id}`) }}>
                                <ThemedText type="defaultBold">{reply.userInfo.pseudo}</ThemedText>
                              </Pressable>
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
    width: 750,
  },

  commentInputContainerSmallScreen: {
    width: 450,
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
  },

  viewReplies: {
    marginLeft: 45,
  }
})

const styles = { ...memeStyle, ...localStyles }
