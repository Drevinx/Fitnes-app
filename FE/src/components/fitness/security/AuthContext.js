import { createContext, useContext, useState } from "react";
import { getAuth, apiClient } from "../../api/apiClient"

export const AuthContext = createContext()

export const useAuth = () => useContext(AuthContext)

export default function AuthProvider ( {children} ) {

    const [isAuthenticated, setIsAuthenticated] = useState (false)
    const [token, setToken] = useState("")


    async function login(username, password) {

        const baToken = "Basic " + window.btoa(username + ":" + password)

        try {
            const response = await getAuth(baToken)

            if (response.status==200){
                setIsAuthenticated(true);
                setToken(baToken)
                apiClient.interceptors.request.use(
                    (config) => {
                        console.log("intercepting and add a token")
                        config.headers.Authorization=baToken
                        return config
                    }
                )
                return true
            } else {
                logout()
                return false
            }

        } catch (error) {
            logout()
        }
 
      }
    


 
    function logout(){
        setIsAuthenticated(false)
    }

    return (
        <AuthContext.Provider value={ {isAuthenticated, login, logout, token} }>
            {children}
        </AuthContext.Provider>
    )

}

