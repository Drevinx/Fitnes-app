import React, {  useEffect, useState } from "react"
import { deletePlanByIdApi, getAllPlansForUserApi, getTreningsApi, deleteTreningsApi} from "../api/apiClient"
import "../../css/plan.css"
import { useNavigate, useLocation} from "react-router-dom"
import {AddButton, DeleteButton, EditButton } from "../other/buttons";



export default function ListPlansComponent() {

    const [message, setMessage] = useState("")

    const [plans, setPlans] = useState([])

    const navigate = useNavigate()

    const [idp, setIdp] = useState(null)
    const location = useLocation();
    
  const [trening, setTrening] = useState([])
  const [clickPlanId, setClickPlanid] = useState(null)


    function goToTrening(id, idt) {
      navigate(`/plans/${id}/trenings/${idt}/exercises`)
    };

      const [isPlanLoad, setIsPlanLoad] = useState(false)
    useEffect (
        () => {refreshPlans()
              
        }, []
    ) 
    
    useEffect(() => {
      const searchParams = new URLSearchParams(location.search);
      const idp = searchParams.get("idp");
      setIdp(idp);
    }, [ location.search]);

    useEffect(() => {
      if (isPlanLoad && idp != null){
      setClickPlanid(-2);
      console.log(" clickPlanu = 0  idp:" + idp)
    }
    }, [ idp, isPlanLoad]);

const [isFirstLoad, setIsFirstLoad] = useState(true)
    useEffect(() => {
      if (clickPlanId === -2 &&  isFirstLoad ) {
        console.log("xxxxxxxxxxxxxxx " + clickPlanId)
       // goToPlanDetail(idp)
        setIsFirstLoad(false)

        setTimeout(() => {
          goToPlanDetail(idp)
        }, 1000);

      }
    }, [clickPlanId, plans, isFirstLoad, isPlanLoad]);
    



    useEffect(() => {
      console.log("clickPlanId changed:", clickPlanId);
     
    }, [clickPlanId]);
  
   function refreshPlans(){

       getAllPlansForUserApi()
        .then( response =>{ 
            console.log(response.data)
            setPlans(response.data)
            setIsPlanLoad(true)
        })
        .catch( error => console.log(error)) 
        

    }

    function deletePlan(id, name){
      deletePlanByIdApi(id)
        .then(
          () => {
            setMessage(`Delete Plan: ${name}!`)
            refreshPlans()

            setTimeout(() => {
              setMessage(null);
            }, 3000);
          }
        )
        .catch( error => console.log(error))

        
    }

    function updatePlan(id){
      console.log(id)
      navigate(`/plans/${id}?idp=${id}`)
    }

    function addNewPlan(id) {
      console.log(id)
      navigate(`/plans/-1`)
  }



 function goToPlanDetail(planId){
  console.log(planId)

  if (planId == clickPlanId){
      setClickPlanid(0)
  } else {
    console.log("vypis pred zmenou clickplanid:" +clickPlanId + " idp:" +idp)
  getTreningsApi(planId)
    .then(response => {
      setTrening(response.data)
      setClickPlanid(planId)
      console.log(response.data )
      console.log("vypis za zmenou clickplanid:" +clickPlanId + " idp:" +idp)
    })
    .catch( error => console.log(error)) 
  }
 }



 function refreshTrenings(id) {

  getTreningsApi(id)
  .then(response => {
    setTrening(response.data)
  })
  .catch( error => console.log(error)) 
  
 }

 function updateTrening(id, idT){
  navigate(`/plans/${id}/trenings/${idT}?idp=${id}`)
}

function deleteTrening(id, idT, name){
  deleteTreningsApi(id, idT)
      .then(
          () => {
          setMessage(`Delete Trening: ${name}!`)
          refreshTrenings(id)

          setTimeout(() => {
              setMessage(null);
          }, 3000);
          }
      )
      .catch( error => console.log(error))
}

function addNewTrening(id){
  navigate(`/plans/${id}/trenings/-1`)
}

    
    return (
        <div className="container">
          <p>Welcome !</p>  

          { message && <div className='alert alert-warning'>{message}</div>}



            <div>
                <table className="table-container">
                    <thead>
                            <tr>
                                <th>Plan{"\u00A0"}name</th>
                                <th>Description</th>
                                <th>Done</th>
                                <th></th>
                                <th></th>
                               
                            </tr>
                    </thead>
                    <tbody>
                    {
                        plans.map(
                            plan => (
                              <React.Fragment key={plan.id}>
                                <tr  >
                                    <td onClick={() => goToPlanDetail(plan.id)}>{plan.name}</td>
                                    <td  onClick={() => goToPlanDetail(plan.id)}>{plan.description}</td>
                                    <td onClick={() => goToPlanDetail(plan.id)}> x/y</td>
                                    <td> <EditButton onClick={() => updatePlan(plan.id)}/>
                                      </td>
                                    <td>  <DeleteButton onClick={ () => deletePlan(plan.id, plan.name)}/>
                                      </td> 
                                </tr>
                                {clickPlanId === plan.id && (
                              
          <tr className="detail-container">
            <td colSpan="5">
              Exercise per week: {JSON.stringify(plan.exercisePerWeek)} Duration in weeks: {JSON.stringify(plan.durationInWeek)}
              
              <table className="table-trening">
                <thead>
                  <tr>
                    <th>Trening name</th>
                    <th></th>
                    <th></th>
                  </tr>
                </thead>
                <tbody>
                  {trening.map(trening => (
                    <tr key={trening.id}>
                      <td onClick={ () => {goToTrening(plan.id, trening.id)}}>{trening.name}</td>
                      <td>
                        <EditButton onClick={() => updateTrening(plan.id, trening.id)} />
                      </td>
                      <td>
                        <DeleteButton onClick={() => deleteTrening(plan.id,trening.id, trening.name)} />
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>


      <div className="trening-middle">  <AddButton onClick={() => addNewTrening(plan.id)} /> </div>
      
            </td>
          </tr>
          
        )}
      </React.Fragment>
    ))}
  </tbody>
</table>
            </div>

            <AddButton onClick={() => addNewPlan()}/>
            <div>
 
  </div>
  <AddButton onClick={() => refreshPlans()}/>     
  
  <AddButton onClick={() => {console.log(clickPlanId)
  console.log(trening)
  }}/>
        </div>

        
    )
}
