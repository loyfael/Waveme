import { CursorValue, StyleSheet } from "react-native";
import { Colors } from "./Colors";

// Add stylesheets here if they are recurrent in two or more different components/pages

export const memeStyle = StyleSheet.create({
  postWrapper: {
    flexDirection: 'column',
    marginTop: 40,
  },

  postProfile: {
    flexDirection: 'row',
    marginTop: 5,
  },

  profilePicture: {
    width: 40,
    height: 40,
    borderRadius: 20,
    overflow: 'hidden',
    marginEnd: 5,
  },

  profileText: {
    flexDirection: 'column',
    flex: 1,
    flexWrap: 'wrap',
  },

  postMeme: {
    flexDirection: 'column',
  },

  memeImage: {
    alignItems: 'center',
    height: 500,
    width: '100%',
    marginTop: 10,
    overflow: 'hidden',
    borderTopStartRadius: 24,
    borderTopEndRadius: 24,
  },

  memeActionBar: {
    height: 48,
    backgroundColor: Colors.common.memeActionBar,
    borderBottomStartRadius: 24,
    borderBottomEndRadius: 24,
    flexDirection: 'row',
  },

  barLeft: {
    flex: 1,
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'flex-start',
  },

  barRight: {
    flex: 1,
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'flex-end',
  },

  barButton: {
    backgroundColor: Colors.common.barButton,
    borderRadius: 24,
    width: 40,
    height: 40,
    justifyContent: 'center',
    alignItems: 'center',
    marginHorizontal: 6,
  },

  barButtonPressed: {
    backgroundColor: Colors.common.memeActionBar,
    borderRadius: 24,
    width: 40,
    height: 40,
    justifyContent: 'center',
    alignItems: 'center',
    marginHorizontal: 6,
  },
})

export const authStyle = StyleSheet.create({
  connectionField: {
    marginTop: 15,
    width: 250,
  },
})

export const genericButtonStyle = StyleSheet.create({
  genericButton: {
    backgroundColor: Colors.common.button,
    paddingHorizontal: 25,
    paddingVertical: 8,
    borderRadius: 20,
    marginTop: 5,
    cursor: "pointer",
  },

  genericButtonText: {
    color: 'white',
  },
})

export const modalContainerStyle = StyleSheet.create({
  centeredModalView: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#0005',
  },

  modalCursorOverride: {
    cursor: 'default' as unknown as CursorValue,
  },
})
