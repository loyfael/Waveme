import AsyncStorage from "@react-native-async-storage/async-storage";

export const setLocalStorage = async (key: string, value: string | number | any[] | object | null) => {
  try {
    let data = value
    if (!data) {
      return
    }
    if (typeof data === "number") {
      data = data.toString()
    } else if (typeof data !== "string") {
      data = JSON.stringify(data)
    }

    await AsyncStorage.setItem(key, data)
  } catch (e) {
    console.error(e);
  }
}

export const getLocalStorage = async (key: string, isJson: boolean = false) => {
  try {
    const data = await AsyncStorage.getItem(key)

    if (isJson && data) {
      return JSON.parse(data)
    }
    return data
  } catch (e) {
    console.error(e);
  }
}

export const removeLocalStorage = async (key: string) => {
  try {
    await AsyncStorage.removeItem(key)
  } catch (e) {
    console.error(e)
  }
}
