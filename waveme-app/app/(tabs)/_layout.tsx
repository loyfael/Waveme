import React, { useEffect, useState } from 'react';
import { StyleSheet, Image, ScrollView, Pressable, Modal, View, ImageSourcePropType } from 'react-native';
import { useColorScheme } from '@/hooks/useColorScheme';
import { Slot } from 'expo-router';
import { ThemedView } from '@/components/theme/ThemedView';
import MaterialIcons from '@expo/vector-icons/MaterialIcons';

export default function TabLayout() {
  const [showProfileModal, setShowProfileModal] = useState<boolean>(false)
  const [userPfp, setUserPfp] = useState<ImageSourcePropType|null>(null)

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
          <MaterialIcons name="account-circle" size={70} color="black" style={styles.account} />
        </Pressable>
      </ThemedView>
      <Modal visible={showProfileModal} transparent={true} animationType='fade' onRequestClose={() => setShowProfileModal(false)}>
        <View style={styles.centeredModalView}>
          <ThemedView style={styles.modalView}>
            {userPfp ? (
              <Image source={userPfp} style={styles.userPfp} />
            ) : (
              <MaterialIcons name="account-circle" size={120} color="black" style={styles.account} />
            )}
          </ThemedView>
        </View>
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

  modalView: {
    width: 800,
    height: 700,
    opacity: 0.97,
    flexDirection: 'column',
    alignItems: 'center',
  },

  userPfp: {
    width: 120,
    height: 120,
    borderRadius: 60,
    overflow: 'hidden',
  },
})
