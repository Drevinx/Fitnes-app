import {  useParams } from "react-router-dom"
import { useEffect, useState } from "react"
import { Formik, Field, Form, ErrorMessage} from "formik"
import { useNavigate , useLocation } from "react-router-dom"
import { NoButton, OkButton } from "../other/buttons"
import { getExerciseApi, getExerciseByIdApi, getTreningExercisesByIdApi, postExercisesApi } from "../api/apiClient"


export default function ExerciseComponent() {
    const {id} = useParams ()
    const {idT} = useParams()
    const {idE} = useParams()

    const navigate = useNavigate()


    const [name, setName] = useState("dsd")
    const [exercises, setExercises] = useState([])

    useEffect( 
        () => {loadExercises()},[]
        )

    function loadExercises(){
        getExerciseApi()
            .then( response => {
                setExercises(response.data)

                if (idE != -1){
                    getTreningExercisesByIdApi(id, idT,idE)
                        .then(
                            response => {
                                console.log(response.data)
                                setSelectedGroup(response.data.exercise.exerciseGroup.nameGroupOfBodies)
                                console.log(response.data.exercise.exerciseGroup.nameGroupOfBodies)
                                setSelectedExercise(response.data.id);
                                console.log(response.data.name)
                            }
                        )
                        .catch(error => console.log(error))
            }})
            .catch(error => console.log(error))


        }
    

    function validate(values){

    }

    function onSubmit(values){
        console.log( values)
        console.log( selectedExercise)

        let trening
        if (idE == -1){
            trening = {
                exerciseId: selectedExercise,
                exerciseOrder: 2,
                weight: 99
              };

        } else {
          trening = {
            exerciseId: selectedExercise,
            exerciseOrder: 2,
            weight: 99
              };
        }

        
        console.log(trening)

        postExercisesApi(id, idT, trening)
        navigate(`/plans/${id}/trenings/${idT}/exercises`)

    }

    function handleGoExercise(){
            navigate(`/plans/${id}/trenings/${idT}/exercises`)
    }




    const [selectedGroup, setSelectedGroup] = useState("");
    const [selectedExercise, setSelectedExercise] = useState("");

    const handleChangeGroup = (e) => {
        exercises.forEach(trainingExercise => { console.log(trainingExercise.exerciseGroup.nameGroupOfBodies)})
        
        ;
        setSelectedGroup(e.target.value);
        setSelectedExercise("");
        console.log("SELECTED GROP:" + selectedGroup)
        console.log(exercisesInSelectedGroup)
      };
    
      const handleChangeExercise = (e) => {
        setSelectedExercise(e.target.value);
      };
    
      const exercisesInSelectedGroup = exercises.filter(
        
        (exercise) =>    exercise.exerciseGroup.nameGroupOfBodies === selectedGroup
      );
    
      const exerciseOptions = exercisesInSelectedGroup.map((exercise) => (
        <option key={exercise.name} value={exercise.id}>
          {exercise.name}
        </option>
      ));




    return(

        <div className="container">
        <h1>Update Exercise</h1>

        <Formik initialValues = { { selectedGroup, selectedExercise}} 
            enableReinitialize = {true}
            onSubmit = {onSubmit}
            validate = {validate}
            validateOnChange = {false}
            validateOnBlur = {false}
            
        >
            {
                (props) => (
                    <Form>

                        <ErrorMessage
                        name="name"
                        component="div"
                        className="alert alert-warning"
                        />


            
                        <label htmlFor="group">Group:</label>
                                <Field as="select" name="group" onChange={handleChangeGroup} value={selectedGroup}>
                                    <option value="">-- Please choose a group --</option>
                                    <option value="Chest">Chest</option>
                                    <option value="Quadriceps">Quadriceps</option>
                                </Field>

                        <label htmlFor="exercise">Exercise:</label>
                                <Field as="select" name="exercise" onChange={handleChangeExercise} value={selectedExercise}>
                                    <option value="">-- Please choose an exercise --</option>
                                    {exerciseOptions}
                                </Field>



                        <fieldset className="form-group">
                                <label>Max. weight</label>
                                <Field className="form-control" type="text" name="maxWeight" />
                            </fieldset>

            
                      
                        <div>
                            <button className="btn  m-2" type="submit" >
                        <OkButton/>
                        </button>

                        <button className="btn m-2" type="close" onClick={() => handleGoExercise()}> <NoButton/> </button>
                        
                            
                        </div>







                    </Form>

                    
                )
            }

        </Formik>
    </div>

    )
}