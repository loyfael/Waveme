import { getCurrentUser } from "@/services/UserAPI";
import { ChildrenProps, UserInfo } from "@/types";
import axios from "axios";
import React, { createContext, useEffect, useState } from "react";

type AuthContextValues = {
  user: UserInfo | null,
  reloadUser: Function
}

export const AuthContext = createContext<AuthContextValues>({
  user: null,
  reloadUser: () => { }
})

export const AuthProvider = ({ children }: ChildrenProps) => {
  const [user, setUser] = useState<UserInfo | null>(null)
  // refresh's value is useless, we only want to monitor when it gets toggled
  const [refresh, setRefresh] = useState(false)

  useEffect(() => {
    const fetchCurrentUser = async () => {
      if (axios.defaults.headers.common?.["Authorization"]) {
        await getCurrentUser()
          .then((res) => setUser(res.data))
          .catch(() => { });
      } else {
        setUser(null)
      }
    }
    fetchCurrentUser()
  }, [refresh])

  const reloadUser = () => {
    setRefresh(!refresh)
  }

  return (
    <AuthContext.Provider value={{ user, reloadUser }}>
      {children}
    </AuthContext.Provider>
  )
}
