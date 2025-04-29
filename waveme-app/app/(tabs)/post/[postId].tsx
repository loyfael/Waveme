import { ThemedText } from "@/components/theme/ThemedText";
import { ThemedView } from "@/components/theme/ThemedView";
import { Colors } from "@/constants/Colors";
import { useWebTitle } from "@/hooks/useWebTitle";
import { fadeButtonToClicked, fadeButtonToIdle } from "@/utils/animateButton";
import { useLocalSearchParams, useRouter } from "expo-router";
import React, { useEffect, useState } from "react";
import { Animated, StyleSheet, View, Image, Pressable } from 'react-native'
import { Comment, Post } from "@/types";
import { memeStyle } from "@/constants/commonStyles";
import { useAnimatedButton } from "@/hooks/useAnimatedButton";
import Entypo from "@expo/vector-icons/Entypo";
import { BiSolidDownArrowAlt, BiSolidUpArrowAlt } from "react-icons/bi";
import { useThemeColor } from "@/hooks/useThemeColor";
import { ThemedTextInput } from "@/components/theme/ThemedTextInput";
import { IoSend } from "react-icons/io5";
import { Loading } from "@/components/Loading";

export default function PostScreen() {
  useWebTitle('Post de user')

  const router = useRouter()
  const textColor = useThemeColor({}, 'text')
  const { postId } = useLocalSearchParams()
  const [post, setPost] = useState<Post | null>(null)
  const [comments, setComments] = useState<Comment[]>([])
  const [input, setInput] = useState('')

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

  const handleViewReplies = (commentId: number) => { }

  useEffect(() => {
    // TODO: API call
    setPost({
      id: 34,
      title: 'Hilarant.',
      meme: require('@/assets/images/meme.png'),
      user: {
        id: 1,
        userName: 'Beiten34',
        userPfp: require('@/assets/images/pfp.png'),
      }
    })

    setComments([
      {
        id: 1,
        message: "J'avoue",
        user: {
          id: 2,
          userName: "heyeah",
          userPfp: require('@/assets/images/pfp.png'),
        },
        hasReplies: true,
        replies: [],
        upvotes: 12,
      },
      {
        id: 2,
        message: "J'avoue pas",
        user: {
          id: 3,
          userName: "heyeah2",
          userPfp: require('@/assets/images/pfp.png'),
        },
        hasReplies: false,
        replies: [],
        upvotes: -5,
      },
      {
        id: 3,
        message: "J'avoue peut-être",
        user: {
          id: 4,
          userName: "heyeah3",
          userPfp: require('@/assets/images/pfp.png'),
        },
        hasReplies: true,
        replies: [
          {
            id: 1,
            message: "Tu as tort",
            user: {
              id: 5,
              userName: "hater",
              userPfp: require('@/assets/images/pfp.png'),
            },
          },
          {
            id: 2,
            message: "Tu as taure",
            user: {
              id: 6,
              userName: "hater2",
              userPfp: require('@/assets/images/pfp.png'),
            },
          }
        ],
        upvotes: 0,
      }
    ])
  }, [postId])

  return (
    <>
      {post ? (
        <>
          {/* Post */}
          <ThemedView style={styles.postWrapper}>
            <View style={styles.postProfile}>
              {post.user.userPfp ? (
                <Pressable onPress={() => { router.push(`/user/${"23"/*TODO: Post user id*/}`) }}>
                  <Image source={post.user.userPfp} style={styles.profilePicture} />
                </Pressable>
              ) : null}
              <View style={styles.profileText}>
                <Pressable onPress={() => { router.push(`/user/${"23"/*TODO: Post user id*/}`) }}>
                  <ThemedText type="defaultBold">{post.user.userName}</ThemedText>
                </Pressable>
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
              <View style={styles.commentInputContainer}>
                <ThemedTextInput
                  value={input}
                  onChangeText={setInput}
                  placeholder="Ajouter un commentaire..."
                  autoFocus
                  multiline
                  numberOfLines={1}
                  style={styles.commentInput}
                />
              </View>
              <Pressable style={styles.sendComment}>
                <IoSend size={32} color="white" />
              </Pressable>
            </View>
            <View>
              {comments.map((comment, commentKey) => (
                <View key={commentKey} style={styles.postWrapper}>
                  <View style={styles.postProfile}>
                    <Pressable onPress={() => { router.push(`/user/${"23"/*TODO: Post user id*/}`) }}>
                      <Image source={comment.user.userPfp} style={styles.profilePicture} />
                    </Pressable>
                    <View style={styles.profileText}>
                      <Pressable onPress={() => { router.push(`/user/${"23"/*TODO: Post user id*/}`) }}>
                        <ThemedText type="defaultBold">{comment.user.userName}</ThemedText>
                      </Pressable>
                      <View style={{ ...styles.commentContainer, backgroundColor: areaBackgroundColor }}>
                        <ThemedText style={styles.commentOverride}>{comment.message}</ThemedText>
                      </View>
                    </View>
                  </View>
                  {comment.replies.length > 0 ? (
                    <View style={{ ...styles.repliesContainer, borderLeftColor: textColor }}>
                      {comment.replies.map((reply, replyKey) => (
                        <View key={replyKey} style={styles.postProfile}>
                          <Pressable onPress={() => { router.push(`/user/${"23"/*TODO: Post user id*/}`) }}>
                            <Image source={reply.user.userPfp} style={styles.profilePicture} />
                          </Pressable>
                          <View style={styles.profileText}>
                            <Pressable onPress={() => { router.push(`/user/${"23"/*TODO: Post user id*/}`) }}>
                              <ThemedText type="defaultBold">{reply.user.userName}</ThemedText>
                            </Pressable>
                            <View style={{ ...styles.commentContainer, backgroundColor: areaBackgroundColor }}>
                              <ThemedText style={styles.commentOverride}>{reply.message}</ThemedText>
                            </View>
                          </View>
                        </View>
                      ))}
                    </View>
                  ) :
                    comment.hasReplies ? (
                      <Pressable onPress={() => { handleViewReplies(comment.id) }}>
                        <ThemedText style={styles.viewReplies}>Voir les réponses</ThemedText>
                      </Pressable>
                    ) : (<></>)}
                </View>
              ))}
            </View>
          </ThemedView>
        </>
      ) : (<Loading />)}
    </>
  )
}

const localStyles = StyleSheet.create({
  comments: {
    flex: 1,
    flexDirection: 'column',
  },

  addComment: {
    width: '100%',
    alignItems: 'center',
    justifyContent: 'center',
    flexDirection: 'row',
    marginVertical: 15,
  },

  commentInputContainer: {
    width: 850,
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

  repliesContainer: {
    marginLeft: 5,
    paddingLeft: 15,
    borderLeftWidth: 1,
    borderBottomLeftRadius: 30,
  },

  // Override for memeStyle.postWrapper
  postWrapper: {
    marginTop: 15,
  },

  viewReplies: {
    marginLeft: 45,
  }
})

const styles = { ...memeStyle, ...localStyles }
