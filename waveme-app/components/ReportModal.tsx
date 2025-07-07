import { genericButtonStyle, modalContainerStyle } from "@/constants/commonStyles";
import React, { useState } from "react";
import { Modal, Pressable, StyleSheet, TouchableOpacity, View } from "react-native";
import { ThemedView } from "./theme/ThemedView";
import { ThemedText } from "./theme/ThemedText";
import { ThemedTextInput } from "./theme/ThemedTextInput";
import ThemedSelectDropdown from "./ThemedSelectDropdown";
import { useThemeColor } from "@/hooks/useThemeColor";
import { ReasonValues, ReportedContent } from "@/types";
import { reportContent } from "@/services/ModerationAPI";
import { useResponsive } from "@/hooks/useResponsive";

type ReportModalProps = {
  visible: boolean,
  setVisible: Function,
  reportedContent: ReportedContent,
  userName: string,
  message: string | null,
  id: number | string,
}

export default function ReportModal(props: ReportModalProps) {
  const [comment, setComment] = useState("")
  const [reason, setReason] = useState<ReasonValues | null>(null)

  const { isVerySmallScreen } = useResponsive()

  let reportTitle: string
  let reportMessage: string
  let reportContext = ""
  switch (props.reportedContent) {
    case "post":
      reportTitle = " post"
      reportMessage = `du post de ${props.userName}`
      reportContext = props.message ?? ""
      break

    case "comment":
      reportTitle = " commentaire"
      reportMessage = `du commentaire de ${props.userName}`
      reportContext = props.message ?? ""
      break

    case "reply":
      reportTitle = "e réponse"
      reportMessage = `de la réponse de ${props.userName}`
      reportContext = props.message ?? ""
      break

    case "profile":
      reportTitle = " utilisateur"
      reportMessage = `du profil de ${props.userName}`
      break
  }

  const reportReasons = [
    { title: "Spam", value: "SPAM" },
    { title: "Harcèlement", value: "HARASSMENT" },
    { title: "Contenu inapproprié", value: "INAPPROPRIATE_CONTENT" },
    { title: "Usurpation d'identité", value: "IMPERSONATION" },
    { title: "Autre", value: "OTHER" },
  ]

  const handleClose = () => {
    props.setVisible(false)
    // Delay the reason state reset so that you can't see the text disappear while the modal is fading out
    setTimeout(() => {
      setComment("")
      setReason(null)
    }, 250)
  }

  const handleSendReport = () => {
    if (reason) {
      reportContent(props.reportedContent, reason, comment, props.id)
        .catch((err) => {
          console.error(err)
        })
        .then(() => {
          props.setVisible(false)
        })
    }
  }

  return (
    <Modal visible={props.visible} transparent animationType="fade" onRequestClose={handleClose}>
      <Pressable style={{ ...styles.centeredModalView, ...styles.modalCursorOverride }} onPress={handleClose}>
        <Pressable style={styles.modalCursorOverride}>
          <ThemedView style={isVerySmallScreen ? styles.modalViewSmallScreen : styles.modalView}>
            <ThemedText type="title" style={styles.modalTitle}>Signalement d'un{reportTitle}</ThemedText>
            <View style={styles.modalContent}>
              <ThemedText>Vous vous apprêtez à envoyer un signalement {reportMessage}.</ThemedText>
              {["post", "comment", "reply"].includes(props.reportedContent) ? (
                <View style={styles.modalMessageContext}>
                  <ThemedText>{props.reportedContent === "post" ? "Post :" : "Message :"}</ThemedText>
                  <ThemedText type="defaultBold">{reportContext}</ThemedText>
                </View>
              ) : ""}
              <ThemedSelectDropdown
                data={reportReasons}
                onSelect={(selectedItem) => { setReason(selectedItem.value as ReasonValues) }}
                placeholder="Raison du signalement"
              />
              <ThemedTextInput placeholder="Commentaire" value={comment} onChangeText={(value) => { setComment(value) }} />
            </View>
            <TouchableOpacity style={styles.genericButton} onPress={handleSendReport}>
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
    minHeight: 350,
    paddingVertical: 30,
    paddingHorizontal: 60,
    opacity: 0.97,
    flexDirection: 'column',
    alignItems: 'center',
    borderRadius: 20,
  },

  modalViewSmallScreen: {
    width: 300,
    minHeight: 400,
    paddingVertical: 30,
    paddingHorizontal: 60,
    opacity: 0.97,
    flexDirection: "column",
    alignItems: "center",
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
