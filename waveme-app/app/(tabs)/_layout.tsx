import React, { useContext, useEffect, useState } from 'react';
import { StyleSheet, Image, ScrollView, Pressable, Modal, ImageSourcePropType, CursorValue, View, Switch, Appearance } from 'react-native';
import { Slot } from 'expo-router';
import { ThemedView } from '@/components/theme/ThemedView';
import MaterialIcons from '@expo/vector-icons/MaterialIcons';
import { PencilFill } from 'react-bootstrap-icons';
import { Colors } from '@/constants/Colors';
import { ThemedText } from '@/components/theme/ThemedText';
import { useThemeColor } from '@/hooks/useThemeColor';
import { useColorScheme } from '@/hooks/useColorScheme';
import { ThemeContext } from '@/context/ThemeContext';

export default function TabLayout() {
  const [showProfileModal, setShowProfileModal] = useState<boolean>(false)
  const [userPfp, setUserPfp] = useState<ImageSourcePropType | null>(null)
  const [userName, setUserName] = useState<string | null>(null)

  const { isDarkMode, setDarkMode } = useContext(ThemeContext)
  const theme = useColorScheme()
  const textColor = useThemeColor({}, 'text')

  useEffect(() => {
    setUserPfp(require('@/assets/images/pfp.png'))
    setUserName('Beuteu34')
  }, [])

  return (
    <ThemedView style={styles.wrapper}>
      <ThemedView style={styles.leftColumn}>
        <Image source={require('@/assets/images/waveme.png')} style={styles.logo} />
      </ThemedView>
      <ScrollView style={styles.main} showsVerticalScrollIndicator={false}>
        <Slot />
      </ScrollView>
      <ThemedView style={styles.rightColumn}>
        <Pressable onPress={() => setShowProfileModal(true)}>
          {userPfp ? (
            <Image source={userPfp} style={styles.account} />
          ) : (
            <MaterialIcons name="account-circle" size={70} color="black" style={styles.account} />
          )}
        </Pressable>
      </ThemedView>
      <Modal visible={showProfileModal} transparent={true} animationType='fade' onRequestClose={() => setShowProfileModal(false)}>
        <Pressable style={{ ...styles.centeredModalView, ...styles.modalCursorOverride }} onPress={() => setShowProfileModal(false)}>
          <Pressable style={styles.modalCursorOverride}>
            <ThemedView style={styles.modalView}>
              <Pressable onPress={() => { }}>
                {userPfp ? (
                  <Image source={userPfp} style={styles.userPfp} />
                ) : (
                  <MaterialIcons name="account-circle" size={150} color="black" style={styles.account} />
                )}
                <View style={styles.editPfpButton}>
                  <PencilFill color="white" size={12} />
                </View>
              </Pressable>
              <ThemedText type='title' style={{ ...styles.userName, borderBottomColor: textColor }}>
                {userName}
              </ThemedText>
              <View style={styles.options}>
                <ThemedText>Total d'upvotes : 0</ThemedText>
                <ThemedText>Total de downvotes : 0</ThemedText>
                <ThemedText>Nombre de posts : 0</ThemedText>
                <View style={styles.switch}>
                  <Switch value={isDarkMode} onValueChange={setDarkMode} />
                  <ThemedText style={styles.switchLabel}>Mode sombre</ThemedText>
                </View>
              </View>
              <View style={styles.connection}></View>
            </ThemedView>
          </Pressable>
        </Pressable>
      </Modal>
    </ThemedView>
  );
}

const styles = StyleSheet.create({
  wrapper: {
    flex: 1,
    flexDirection: 'row',
    justifyContent: 'center',
  },

  leftColumn: {
    flex: 1,
    marginTop: 50,
    marginStart: 50,
  },

  logo: {
    width: 90,
    height: 90,
  },

  rightColumn: {
    flex: 1,
    alignItems: 'flex-end',
    marginTop: 35,
    marginEnd: 35,
  },

  account: {
    width: 70,
    height: 70,
    borderRadius: 35,
    overflow: 'hidden',
  },

  main: {
    flex: 6,
    flexGrow: 6,
  },

  centeredModalView: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#0005',
  },

  modalCursorOverride: {
    cursor: 'default' as unknown as CursorValue,
  },

  modalView: {
    width: 700,
    height: 650,
    paddingVertical: 40,
    paddingHorizontal: 80,
    opacity: 0.97,
    flexDirection: 'column',
    alignItems: 'center',
  },

  userPfp: {
    width: 150,
    height: 150,
    borderRadius: 75,
    overflow: 'hidden',
  },

  editPfpButton: {
    position: 'absolute',
    left: 128,
    top: 128,
    padding: 5,
    borderRadius: 10,
    backgroundColor: Colors.common.button,
  },

  userName: {
    marginTop: 35,
    borderBottomWidth: 1,
    width: 350,
    textAlign: 'center',
    paddingBottom: 15,
  },

  options: {
    flexDirection: 'column',
    alignSelf: 'flex-start',
    alignItems: 'flex-start',
    marginStart: 90,
    marginTop: 20,
  },

  switch: {
    flexDirection: 'row',
    alignItems: 'center',
    marginVertical: 5,
  },

  switchLabel: {
    marginStart: 8,
  },

  connection: {
    
  },
})
