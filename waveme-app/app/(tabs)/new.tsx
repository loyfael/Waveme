import { ThemedText } from "@/components/theme/ThemedText";
import { ThemedTextInput } from "@/components/theme/ThemedTextInput";
import { useWebTitle } from "@/hooks/useWebTitle";
import React, { useState } from "react";
import { View, StyleSheet, Image, TouchableOpacity } from "react-native";
import { genericButtonStyle } from "@/constants/commonStyles";
import { NewPost } from "@/types";
import { createPost } from "@/services/PostAPI";
import { useRouter } from "expo-router";
import { pickImage } from "@/utils/api";

export default function NewPostScreen() {
  const [post, setPost] = useState<NewPost>({
    description: "",
    file: null,
    bucket: "waveme",
  })

  useWebTitle("Nouveau post")
  const router = useRouter()

  const handleSendPost = async () => {
    await createPost(post)
      .catch((error) => {
        console.error(error)
      })
      .then((response) => {
        router.push('/')
      })
  }

  return (
    <View style={styles.newPostWrapper}>
      <ThemedText type="title">Nouveau post</ThemedText>
      <View style={styles.postTitle}>
        <ThemedText type="subtitle">Description :</ThemedText>
        <ThemedTextInput
          value={post.description}
          onChangeText={(value) => { setPost({ ...post, description: value }) }}
        />
      </View>
      <TouchableOpacity onPress={() => { pickImage(post, setPost) }} style={styles.genericButton}>
        <ThemedText style={styles.genericButtonText}>Importer une {post.file ? "nouvelle" : ""} image</ThemedText>
      </TouchableOpacity>
      {post.file && <Image source={{ uri: post.file }} resizeMode="contain" style={styles.postImage} />}
      {post.file && (
        <TouchableOpacity onPress={handleSendPost} style={styles.genericButton}>
          <ThemedText style={styles.genericButtonText}>Envoyer le post</ThemedText>
        </TouchableOpacity>
      )}
    </View>
  )
}

const localStyles = StyleSheet.create({
  newPostWrapper: {
    display: "flex",
    alignItems: "center",
    marginTop: 60,
  },

  postTitle: {
    width: 600,
    marginTop: 50,
    marginBottom: 40,
  },

  postTitleLabel: {
    alignSelf: "flex-start",
  },

  postImage: {
    width: "100%",
    height: 400,
    marginVertical: 10,
  },
})

const styles = { ...localStyles, ...genericButtonStyle }
