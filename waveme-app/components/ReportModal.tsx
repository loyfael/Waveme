import { authStyle, genericButtonStyle, modalContainerStyle } from "@/constants/commonStyles";
import React, { useState } from "react";
import { Modal, Pressable, StyleSheet, TouchableOpacity, View } from "react-native";
import { ThemedView } from "./theme/ThemedView";
import { ThemedText } from "./theme/ThemedText";
import { ThemedTextInput } from "./theme/ThemedTextInput";

type ReportModalProps = {
  visible: boolean,
  setVisible: Function,
  reportedContent: "post" | "comment" | "profile",
  userName: string,
  message: string | null,
  id: string | null,
}

export default function ReportModal(props: ReportModalProps) {
  const [reason, setReason] = useState("")

  let reportTitle: string
  let reportMessage: string
  let reportContext = ""
  switch (props.reportedContent) {
    case "post":
      reportTitle = "post"
      reportMessage = `post de ${props.userName}`
      reportContext = props.message ?? ""
      break

    case "comment":
      reportTitle = "commentaire"
      reportMessage = `commentaire de ${props.userName}`
      reportContext = props.message ?? ""
      break

    case "profile":
      reportTitle = "utilisateur"
      reportMessage = `profil de ${props.userName}`
      break
  }

  const handleClose = () => {
    props.setVisible(false)
    // Delay the reason state reset so that you can't see the text disappear while the modal is fading out
    setTimeout(() => {
      setReason("")
    }, 250)
  }

  return (
    <Modal visible={props.visible} transparent animationType="fade" onRequestClose={handleClose}>
      <Pressable style={{ ...styles.centeredModalView, ...styles.modalCursorOverride }} onPress={handleClose}>
        <Pressable style={styles.modalCursorOverride}>
          <ThemedView style={styles.modalView}>
            <ThemedText type="title" style={styles.modalTitle}>Signalement d'un {reportTitle}</ThemedText>
            <View style={styles.modalContent}>
              <ThemedText>Vous vous apprêtez à envoyer un signalement du {reportMessage}.</ThemedText>
              {["post", "comment"].includes(props.reportedContent) ? (
                <View style={styles.modalMessageContext}>
                  <ThemedText>{props.reportedContent === "post" ? "Post :" : "Message :"}</ThemedText>
                  <ThemedText type="defaultBold">{reportContext}</ThemedText>
                </View>
              ) : ""}
              <ThemedTextInput placeholder="Raison" value={reason} onChangeText={(value) => { setReason(value) }} />
            </View>
            <TouchableOpacity style={styles.genericButton} onPress={() => { }}>
              <ThemedText style={styles.genericButtonText}>Envoyer</ThemedText>
            </TouchableOpacity>
          </ThemedView>
        </Pressable>
      </Pressable>
    </Modal>
  )
}

const localStyles = StyleSheet.create({
  modalView: {
    width: 500,
    height: 350,
    paddingVertical: 30,
    paddingHorizontal: 60,
    opacity: 0.97,
    flexDirection: 'column',
    alignItems: 'center',
    borderRadius: 20,
  },

  modalTitle: {
    marginBottom: 10,
  },

  modalContent: {
    width: "100%",
    display: "flex",
    justifyContent: "flex-start",
    marginBottom: 20,
  },

  modalMessageContext: {
    marginVertical: 10,
  },
})

const styles = { ...localStyles, ...modalContainerStyle, ...genericButtonStyle }
