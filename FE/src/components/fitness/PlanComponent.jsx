import { getPlanByIdApi, patchPlanByIdApi, postPlanByIdApi, postTreningByIdApi } from "../api/apiClient"
import {  useParams } from "react-router-dom"
import { useEffect, useState } from "react"
import { Formik, Field, Form, ErrorMessage} from "formik"
import { useNavigate, useLocation } from "react-router-dom"
import { NoButton, OkButton } from "../other/buttons"

export default function PlanComponent(){

    const {id} = useParams ()
    

    const [name, setName] = useState("")
    const [description, setDescription] = useState("")
    const [exercisePerWeek, setExercisePerWeek] = useState("")
    const [durationInWeek, setDurationInWeek] = useState("")
    const navigate = useNavigate()

    const location = useLocation();
    const searchParams = new URLSearchParams(location.search);
    const idp = searchParams.get('idp');

    useEffect(
        () => getPlan(), []
    )

  function getPlan(){
    if (id != -1) {
        getPlanByIdApi(id)
            .then( response => {
                console.log(response.data)
                setName(response.data.name)
                setDescription(response.data.description)
                setExercisePerWeek(response.data.exercisePerWeek)
                setDurationInWeek(response.data.durationInWeek)
            })     
    }
  }

  function onSubmit(values){
    console.log(values)

    const plan = {
        id: id,
        name: values.name,
        description: values.description,
        durationInWeek: values.durationInWeek,
        exercisePerWeek: values.exercisePerWeek
    }
    if (id == -1){
        postPlanByIdApi(plan)
            .then(response => navigate(`/plans?idp=${idp}`))
            .catch(error => console.log(error))
    } else {
        patchPlanByIdApi(id, plan)
            .then(response => navigate(`/plans?idp=${idp}`))
            .catch(error => console.log(error))
    }
    
  }

  function handleGoPlans(){

    navigate(`/plans?idp=${idp}`)
  }

  function validate(values){
    let errors = {
    }

    if(values.name.length < 1 ){
        errors.name = "Enter atleast 1 character"
    }
    if( values.name.length > 30){
        errors.name = "Enter atleast 30 character"
    }
    if(values.description.length < 1 ){
        errors.description = "Enter atleast 1 character"
    }
    if( values.description.length > 30){
        errors.description = "Enter atleast 30 character"
    }
    if (isNaN(values.exercisePerWeek) || values.exercisePerWeek <1 || values.exercisePerWeek > 99) {
        errors.exercisePerWeek = "Enter only mumbers 1-99"
      }
    if (isNaN(values.durationInWeek) || values.durationInWeek <1 || values.durationInWeek > 99) {
        errors.exercisePerWeek = "Enter only mumbers 1-99"
      }  

    return errors
  }

    return(
        <div className="container">
            <h1>Update plan</h1>

            <Formik initialValues = { {name, description, exercisePerWeek, durationInWeek}} 
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

                            <ErrorMessage
                            name="description"
                            component="div"
                            className="alert alert-warning"
                            />

                            <ErrorMessage
                            name="exercisePerWeek"
                            component="div"
                            className="alert alert-warning"
                            />

                            <ErrorMessage
                            name="durationInWeek"
                            component="div"
                            className="alert alert-warning"
                            />

                            <fieldset className="form-group">
                                <label>Name</label>
                                <Field className="form-control" type="text" name="name" />
                            </fieldset>

                            <fieldset className="form-group">
                                <label>Description</label>
                                <Field className="form-control" type="text" name="description" />
                            </fieldset>

                            <fieldset className="form-group">
                                <label>Duration weeks</label>
                                <Field className="form-control" type="number" name="durationInWeek" />
                            </fieldset>

                            <fieldset className="form-group">
                                <label>Trening per week</label>
                                <Field className="form-control" type="number" name="exercisePerWeek" />
                            </fieldset>

                            <div>
                                <button className="btn  m-2" type="submit" >
                            <OkButton/>
                            </button>

                            <button className="btn m-2" type="cloese" onClick={() => handleGoPlans()}> <NoButton/> </button>
                            
                                
                            </div>
                        </Form>

                        
                    )
                }

            </Formik>
        </div>

    )
}