import { useState } from "react"
import { useNavigate } from "react-router-dom"
import { useAuth } from "./security/AuthContext"



export default function LoginComponent(){

    const authContext = useAuth()

    const [username, setUsername] = useState("name")
    const [password, setPassword] = useState("password")

    const [showFailMsg, setShowFailMsg] = useState(false)

    function hanndleUsernameChange( event){
        setUsername(event.target.value);
    }
    function handlePasswordChange(event){
        setPassword(event.target.value)
    }
    

    async function handleSubmit(){
        console.log(username)
        console.log(password)

            if (await authContext.login(username,password)){
                navigate(`/plans`) // ` jine uvozovky
            } else {
                setShowFailMsg(true)
            }
        }



    function FailMsgComponent(){
        if (showFailMsg)
        return <div className="successMsg">Authenticated Failed</div>
    }

    const navigate = useNavigate();


    return(
        <div className="Login">
    
            {showFailMsg && <div className="successMsg">Authenticated Failed</div>}
            <FailMsgComponent/>

            <div className="LoginForm">
                <div className="m-2">
                    <label>Username</label>
                    <input type="text" name="username" value={username} onChange={hanndleUsernameChange}/>
                </div>
                <div className="m-2">
                    <label>Password</label>
                    <input type="password" name="password" value={password} onChange={handlePasswordChange}/>
                </div>
            </div>

            <div className="m-2">
                <button type="button" name="login" onClick={handleSubmit}>Login</button>
            </div>

        </div>
    )
    }