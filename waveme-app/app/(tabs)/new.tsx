import { ThemedText } from "@/components/theme/ThemedText";
import { ThemedTextInput } from "@/components/theme/ThemedTextInput";
import { useWebTitle } from "@/hooks/useWebTitle";
import React, { useState } from "react";
import { View, StyleSheet, Image, TouchableOpacity } from "react-native";
import * as ImagePicker from 'expo-image-picker';
import { genericButtonStyle } from "@/constants/commonStyles";

type NewPost = {
  title: string,
  meme: any,
}

export default function NewPostScreen() {
  useWebTitle("Nouveau post")

  const [post, setPost] = useState<NewPost>({
    title: "",
    meme: null,
  })

  const pickImage = async () => {
    // No permissions request is necessary for launching the image library
    let result = await ImagePicker.launchImageLibraryAsync({
      mediaTypes: ['images'], // Possible to add videos in the future
      allowsEditing: true,
      aspect: [4, 3],
      quality: 1,
    });

    if (!result.canceled) {
      setPost({ ...post, meme: result.assets[0].uri });
    }
  };

  return (
    <View style={styles.newPostWrapper}>
      <ThemedText type="title">Nouveau post</ThemedText>
      <View style={styles.postTitle}>
        <ThemedText type="subtitle">Titre :</ThemedText>
        <ThemedTextInput
          value={post.title}
          onChangeText={(value) => { setPost({ ...post, title: value }) }}
        />
      </View>
      <TouchableOpacity onPress={pickImage} style={styles.genericButton}>
        <ThemedText style={styles.genericButtonText}>Importer une {post.meme ? "nouvelle" : ""} image</ThemedText>
      </TouchableOpacity>
      {post.meme && <Image source={{ uri: post.meme }} resizeMode="contain" style={styles.postImage} />}
      {post.meme && (
        <TouchableOpacity onPress={() => { }} style={styles.genericButton}>
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
