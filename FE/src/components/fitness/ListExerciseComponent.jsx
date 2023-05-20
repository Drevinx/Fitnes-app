import {  useParams } from "react-router-dom"
import { useNavigate , useLocation } from "react-router-dom"
import { deleteExercisesByIdApi, getExercisesApi } from "../api/apiClient"
import {AddButton, DeleteButton, EditButton, InfoButton } from "../other/buttons";
import React, {  useEffect, useState } from "react"
import "../../css/plan.css"
import { Popover, OverlayTrigger, Button } from 'react-bootstrap';


export default function ListExerciseComponent() {

    const {id} = useParams ()
    const {idT} = useParams()
    const navigate = useNavigate()

    const [exercises, setExercise] =useState([])
    const [message, setMessage] = useState("")

    useEffect (
        () => {refreshExercise()}, []
    ) 


    function refreshExercise(){
        getExercisesApi(id, idT)
            .then(
                response => {
                    console.log(response.data)
                    setExercise(response.data)
                }
            )
            .catch( error => console.log(error)) 

    }

      
    function addNewExercise(){
        navigate(`/plans/${id}/trenings/${idT}/exercises/-1`)
    }

    function goToExerciseDetail(idE){
        
    }

    function updateExercise(idE){
        navigate(`/plans/${id}/trenings/${idT}/exercises/${idE}`)
    }

    function deleteExercise(idE, name){

        deleteExercisesByIdApi(id, idT, idE)
            .then( () => {
                setMessage("Exercise: " + name + " has removed")
                refreshExercise()
                setTimeout(() => {
                    setMessage(null);
                    refreshExercise();
                  }, 3000);
            })
            .catch( error => console.log(error))
    }

    function handleExerciseClick() {}

const [infoText, setInfoText] = useState("")
    let popoverFocus = (
        <Popover id="popover-trigger-focus" title="Popover bottom">
          {infoText}
        </Popover>
      );



    return(

        <div className="container">
        <p>Welcome !</p>  

        { message && <div className='alert alert-warning'>{message}</div>}



          <div>
              <table className="table-exercise">
                  <thead>
                          <tr>
                              <th>Exercise{"\u00A0"}name</th>
                              <th>Muscle</th>
                              <th>Desc</th>
                              <th></th>
                              <th></th>
                             
                          </tr>
                  </thead>
                  <tbody>
                  {
                      exercises.map(
                          texercise => (
                            <React.Fragment key={texercise.id}>
                              <tr  >
                                  <td onClick={() => goToExerciseDetail(texercise.exercise.id)}>{texercise.exercise.name}</td>
                                  <td  onClick={() => goToExerciseDetail(texercise.exercise.id)}>{texercise.exercise.exerciseGroup.
                                  nameGroupOfBodies}</td>
                                 
                                 
                                  <td onClick={() => setInfoText(texercise.exercise.description)} > 
                                    
                                    
<OverlayTrigger trigger="focus" placement="top" overlay={popoverFocus}>
      <Button  className="btnT"> <InfoButton className="btnT" /> </Button>
    </OverlayTrigger>

                                  </td>
                                  
  
        
                                  <td> 
                                  <EditButton onClick={() => updateExercise(texercise.id)}/>

                                    </td>
                                  <td>  <DeleteButton onClick={ () => deleteExercise(texercise.id, texercise.exercise.name)}/>
                                    </td> 
                              </tr>
                              
                          </React.Fragment>
                          ))}
              </tbody>
            </table>


     <AddButton onClick={() => addNewExercise(id, idT)} /> 
    

      </div>


      </div>

    )
    
}