import React, { useEffect, useState } from "react";
import { Loading } from "./Loading";
import { ThemedView } from "./theme/ThemedView";
import { Animated, View, Image, Pressable, ImageSourcePropType } from "react-native";
import { BiSolidDownArrowAlt, BiSolidUpArrowAlt } from "react-icons/bi";
import { Colors } from "@/constants/Colors";
import { fadeButtonToClicked, fadeButtonToIdle } from "@/utils/animateButton";
import { ChatDotsFill } from "react-bootstrap-icons";
import { Entypo, MaterialIcons } from "@expo/vector-icons";
import { memeStyle } from "@/constants/commonStyles";
import { ThemedText } from "./theme/ThemedText";
import { useAnimatedButton } from "@/hooks/useAnimatedButton";
import { useThemeColor } from "@/hooks/useThemeColor";
import { Post } from "@/types";
import { useRouter } from "expo-router";
import ReportModal from "./ReportModal";
import { createLocalUriFromBackUri } from "@/utils/api";

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
            loadedProfilePictures[post.user.id] = profilePictureDataUri
          }
        })
      )
      setLoadedImages(loadingImages)
      setLoadedProfilePictures(loadingProfilePictures)
    }
    loadAllImages()

    // Clean up the images on unmount
    return () => {
      Object.values(loadedImages).forEach((image) => {
        URL.revokeObjectURL(image)
      })
    }
  }, [props.posts])

  return (
    <>
      {!props.isLoading ? props.posts.map((post) => (
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
                <AnimatedButton
                  onPressIn={() => fadeButtonToClicked(animatedButton2)}
                  onPressOut={() => fadeButtonToIdle(animatedButton2)}
                  onPress={() => { router.push(`/post/${post.postUniqueId}`) }}>
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
            id={post.postUniqueId}
            message={reportedMessage}
          />
        </ThemedView>
      )) : (<Loading />)}
    </>
  )
}

const styles = { ...memeStyle }
