import LoginComponent from "./LoginComponent"
import ListPlansComponent from "./ListPlanComponent"
import { BrowserRouter, Routes, Route, useNavigate, Navigate } from "react-router-dom"
import ErrorComponent from "./ErrorComponent"
import HeadComponent from "./HeadComponent"
import FootherComponent from "./FootherComponent"
import AuthProvider, { useAuth } from "./security/AuthContext"
import LogoutComponent from "./LogoutComponent"
import PlanComponent from "./PlanComponent"
import TreningComponent from "./TreningComponent"
import ListExerciseComponent from "./ListExerciseComponent"
import ExerciseComponent from "./ExerciseComponent"


function AuthenticatedRoute({children}){
    const authContext = useAuth()

    if (authContext.isAuthenticated)
        return (children)
   
    return <Navigate to="/"/>
    
}

export default function FitnessApp() {

    return(
        <div className="FitnessApp">
            
            <AuthProvider>
                <BrowserRouter>

                <HeadComponent/>

                    <Routes>
                        <Route path="/login" element={ <LoginComponent/>}/>
                        <Route path="*" element= {<ErrorComponent/>}/>


                        <Route path="/plans" element={
                        <AuthenticatedRoute>
                        <ListPlansComponent/>
                        </AuthenticatedRoute>
                        }/>

                        <Route path="/plans/:id" element={
                        <AuthenticatedRoute>
                        <PlanComponent/>
                        </AuthenticatedRoute>
                        }/>



                        <Route path="/plans/:id/trenings/:idT" element={
                        <AuthenticatedRoute>
                        <TreningComponent/>
                        </AuthenticatedRoute>
                        }/>

                        <Route path="/plans/:id/trenings/:idT/exercises" element={
                        <AuthenticatedRoute>
                        <ListExerciseComponent/>
                        </AuthenticatedRoute>
                        }/>

                        <Route path="/plans/:id/trenings/:idT/exercises/:idE" element={
                        <AuthenticatedRoute>
                        <ExerciseComponent/>
                        </AuthenticatedRoute>
                        }/>
                        
                        <Route path="/logout" element={
                        <AuthenticatedRoute>
                        <LogoutComponent/>
                        </AuthenticatedRoute>
                        }/>

                    </Routes>

                    <FootherComponent/>
                    
                </BrowserRouter>
            </AuthProvider>

        </div>

    )
}