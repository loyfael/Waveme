import React, { useEffect, useState } from 'react';
import { StyleSheet, Image, ScrollView, Pressable, Modal, ImageSourcePropType, CursorValue } from 'react-native';
import { useColorScheme } from '@/hooks/useColorScheme';
import { Slot } from 'expo-router';
import { ThemedView } from '@/components/theme/ThemedView';
import MaterialIcons from '@expo/vector-icons/MaterialIcons';
import { PencilFill } from 'react-bootstrap-icons';

export default function TabLayout() {
  const [showProfileModal, setShowProfileModal] = useState<boolean>(false)
  const [userPfp, setUserPfp] = useState<ImageSourcePropType | null>(null)

  const colorScheme = useColorScheme();

  useEffect(() => {
    setUserPfp(require('@/assets/images/pfp.png'))
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
              <Pressable>
                {userPfp ? (
                  <Image source={userPfp} style={styles.userPfp} />
                ) : (
                  <MaterialIcons name="account-circle" size={150} color="black" style={styles.account} />
                )}
                <PencilFill style={styles.editPfpIcon} />
              </Pressable>
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
    width: 800,
    height: 700,
    paddingVertical: 40,
    paddingHorizontal: 20,
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

  editPfpIcon: {
    position: 'absolute',
    left: 130,
    top: 130,
  },
})
