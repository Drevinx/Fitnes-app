import {  useParams } from "react-router-dom"
import { useEffect, useState } from "react"
import { Formik, Field, Form, ErrorMessage} from "formik"
import { useNavigate , useLocation } from "react-router-dom"
import { NoButton, OkButton } from "../other/buttons"
import { getTreningByIdApi, patchTreningByIdApi, postTreningByIdApi } from "../api/apiClient"


export default function TreningComponent() {

    const {id} = useParams ()
    const {idT} = useParams()
    const [name, setName] = useState("")

    const location = useLocation();
    const searchParams = new URLSearchParams(location.search);
    const idp = searchParams.get('idp');

    const navigate = useNavigate()

    useEffect(
        () => getTrening(),[idT]
    )

    function getTrening(){
        if( idT != -1){
            getTreningByIdApi(id,idT)
                .then( response => {
                    console.log(response.data)
                    setName(response.data.name)
                })
                .catch(error => console.log(error))
            }
    }

    function onSubmit(values){
        const trening = {
            id: idT,
            name: values.name
        }

        if (idT == -1){
            postTreningByIdApi(id, trening)
                .then(response => navigate(`/plans?idp=${idp}`))
                .catch(error => console.log(error))
        } else {
            patchTreningByIdApi(id, idT, trening)
                .then(response => navigate(`/plans?idp=${idp}`))
                .catch(error => console.log(error))
        }
    }

    function validate(values){

        let errors = {
        }
    
        if(values.name.length < 1 ){
            errors.name = "Enter atleast 1 character"
        }
        if( values.name.length > 30){
            errors.name = "Enter atleast 30 character"}
    }

    function handleGoPlans(){

        navigate(`/plans?idp=${idp}`)
      }

    return(


        <div className="container">
        <h1>Update trening</h1>

        <Formik initialValues = { {name}} 
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

                        <fieldset className="form-group">
                            <label>Name</label>
                            <Field className="form-control" type="text" name="name" />
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