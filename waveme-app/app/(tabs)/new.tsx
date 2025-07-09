import { ThemedText } from "@/components/theme/ThemedText";
import { ThemedTextInput } from "@/components/theme/ThemedTextInput";
import { useWebTitle } from "@/hooks/useWebTitle";
import React, { useState } from "react";
import { View, StyleSheet, Image, TouchableOpacity, Platform, KeyboardAvoidingView, ScrollView } from "react-native";
import { genericButtonStyle } from "@/constants/commonStyles";
import { NewPost } from "@/types";
import { createPost } from "@/services/PostAPI";
import { useRouter } from "expo-router";
import { pickImage } from "@/utils/api";
import { useResponsive } from "@/hooks/useResponsive";

export default function NewPostScreen() {
  const [post, setPost] = useState<NewPost>({
    description: "",
    file: null,
    bucket: "waveme",
  })

  useWebTitle("Nouveau post")
  const router = useRouter()
  const { isMobile } = useResponsive()

  const handleSendPost = async () => {
    await createPost(post)
      .catch((error) => {
        console.error(error)
      })
      .then((response) => {
        router.push('/feed')
      })
  }

  return (
    <KeyboardAvoidingView 
      style={{ flex: 1 }} 
      behavior={Platform.OS === 'ios' ? 'padding' : 'height'}
    >
      <ScrollView contentContainerStyle={styles.newPostWrapper}>
        <ThemedText type="title">Nouveau post</ThemedText>
        <View style={[styles.postTitle, isMobile && styles.postTitleMobile]}>
          <ThemedText type="subtitle">Description :</ThemedText>
          <ThemedTextInput
            value={post.description}
            onChangeText={(value) => { setPost({ ...post, description: value }) }}
            placeholder="Écrivez votre description ici..."
            multiline={true}
            numberOfLines={4}
            textAlignVertical="top"
            style={styles.descriptionInput}
          />
        </View>
        <TouchableOpacity onPress={() => { pickImage(post, setPost) }} style={styles.genericButton}>
          <ThemedText style={styles.genericButtonText}>Importer une {post.file ? "nouvelle" : ""} image</ThemedText>
        </TouchableOpacity>
        {post.file && <Image source={{ uri: post.file }} resizeMode="contain" style={[styles.postImage, isMobile && styles.postImageMobile]} />}
        {post.file && (
          <TouchableOpacity onPress={handleSendPost} style={styles.genericButton}>
            <ThemedText style={styles.genericButtonText}>Envoyer le post</ThemedText>
          </TouchableOpacity>
        )}
      </ScrollView>
    </KeyboardAvoidingView>
  )
}

const localStyles = StyleSheet.create({
  newPostWrapper: {
    display: "flex",
    alignItems: "center",
    marginTop: 60,
    paddingHorizontal: 20,
    flexGrow: 1,
  },

  postTitle: {
    width: 600,
    marginTop: 50,
    marginBottom: 40,
  },

  postTitleMobile: {
    width: '100%',
    maxWidth: 350,
  },

  postTitleLabel: {
    alignSelf: "flex-start",
  },

  descriptionInput: {
    marginTop: 10,
    minHeight: 80,
    textAlignVertical: 'top',
  },

  postImage: {
    width: "100%",
    height: 400,
    marginVertical: 10,
  },

  postImageMobile: {
    width: "100%",
    height: 250,
    maxWidth: 350,
  },
})

const styles = { ...localStyles, ...genericButtonStyle }
