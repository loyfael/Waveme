import React, { useState } from "react";
import { Loading } from "./Loading";
import { ThemedView } from "./theme/ThemedView";
import { Animated, View, Image, Pressable } from "react-native";
import { BiSolidDownArrowAlt, BiSolidUpArrowAlt } from "react-icons/bi";
import { Colors } from "@/constants/Colors";
import { fadeButtonToClicked, fadeButtonToIdle } from "@/utils/animateButton";
import { ChatDotsFill } from "react-bootstrap-icons";
import { Entypo } from "@expo/vector-icons";
import { memeStyle } from "@/constants/commonStyles";
import { ThemedText } from "./theme/ThemedText";
import { useAnimatedButton } from "@/hooks/useAnimatedButton";
import { useThemeColor } from "@/hooks/useThemeColor";
import { Post } from "@/types";
import { useRouter } from "expo-router";
import ReportModal from "./ReportModal";

type PostListProps = {
  isLoading: boolean,
  posts: Post[],
}

export default function PostList(props: PostListProps) {
  const router = useRouter()
  const [reportModalOpen, setReportModalOpen] = useState(false)
  const [reportedContent, setReportedContent] = useState<"post" | "comment">("post")
  const [reportedUser, setReportedUser] = useState("")
  const [reportedMessage, setReportedMessage] = useState<string | null>(null)

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

  const areaBackgroundColor = useThemeColor({}, 'areaBackground')

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

  return (
    <>
      {!props.isLoading ? props.posts.map((post) => (
        <ThemedView key={post.id} style={styles.postWrapper}>
          {/* Profile and post title */}
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
                  onPress={() => { handleOpenReportModal("post", post.user.userName, post.title) }}>
                  <Animated.View style={{ ...styles.barButton, backgroundColor: backgroundColor1 }}>
                    <Entypo name="flag" size={36} color={Colors.common.memeActionBar} />
                  </Animated.View>
                </AnimatedButton>
              </View>
              <View style={styles.barRight}>
                <AnimatedButton
                  onPressIn={() => fadeButtonToClicked(animatedButton2)}
                  onPressOut={() => fadeButtonToIdle(animatedButton2)}
                  onPress={() => { router.push(`/post/${"11"/*TODO: post id*/}`) }}>
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

          <ReportModal
            visible={reportModalOpen}
            setVisible={setReportModalOpen}
            reportedContent={reportedContent}
            userName={reportedUser}
            id={"11"/*TODO: post id*/}
            message={reportedMessage}
          />
        </ThemedView>
      )) : (<Loading />)}
    </>
  )
}

const styles = { ...memeStyle }
