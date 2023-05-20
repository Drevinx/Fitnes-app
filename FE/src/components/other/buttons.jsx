import React, { useState } from "react"
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { icon } from '@fortawesome/fontawesome-svg-core/import.macro'


export const DeleteButton = ({onClick }) => {


    const [isHovered, setIsHovered] = useState(false);
  
    
    if (isHovered) {
      return (
        <button className="btn" onClick={() => onClick()}>
        <FontAwesomeIcon 
          icon={icon({name: 'trash'})} 
          size="2xl" 
          style={{ color: "#000000" }} 
          onMouseLeave={() => setIsHovered(false)}
          beatFade
        />
        </button>
      );
    } else {
      return (
        <button className="btn" onClick={() => onClick()}>
        <FontAwesomeIcon 
          icon={icon({name: 'trash'})} 
          size="2xl" 
          style={{ color: "#000000" }} 
          onMouseEnter={() => setIsHovered(true)}
          
        />
        </button>
      );



  }
}




export const EditButton = ({onClick }) => {


    const [isHovered, setIsHovered] = useState(false);
  
    
    if (isHovered) {
      return (
        <button className="btn" onClick={() => onClick()}>
        <FontAwesomeIcon 
          icon={icon({name: 'pencil'})} 
          size="2xl" 
          style={{ color: "#000000" }} 
          onMouseLeave={() => setIsHovered(false)}
          beatFade
        />
        </button>
      );
    } else {
      return (
        <button className="btn" onClick={() => onClick()}>
        <FontAwesomeIcon 
          icon={icon({name: 'pencil'})} 
          size="2xl" 
          style={{ color: "#000000" }} 
          onMouseEnter={() => setIsHovered(true)}
          
        />
        </button>
      );



  }
}



export const AddButton = ({onClick, label }) => {


    const [isHovered, setIsHovered] = useState(false);
  
    
    if (isHovered) {
      return (
        <button className="btn " onClick={() => onClick()}>

        <FontAwesomeIcon 
          icon={icon({name: 'plus'})} 
          size="2xl" 
          style={{ color: "#000000" }} 
          onMouseLeave={() => setIsHovered(false)}
          beatFade
        /> 
        {label}
        </button>
      );
    } else {
      return (
        <button className="btn" onClick={() => onClick()}>
        <FontAwesomeIcon 
          icon={icon({name: 'plus'})} 
          size="2xl" 
          style={{ color: "#000000" }} 
          onMouseEnter={() => setIsHovered(true)}
          
        />
        {label}
        </button>
      );
  }
}


export const OkButton = () => {


    const [isHovered, setIsHovered] = useState(false);
  
    
    if (isHovered) {
      return (
        <div className="btn ">

        <FontAwesomeIcon 
          icon={icon({name: 'check'})} 
          size="2xl" 
          style={{ color: "#000000" }} 
          onMouseLeave={() => setIsHovered(false)}
          beatFade
        /> 
    
        </div>
      );
    } else {
      return (
        <div className="btn" >
        <FontAwesomeIcon 
          icon={icon({name: 'check'})} 
          size="2xl" 
          style={{ color: "#000000" }} 
          onMouseEnter={() => setIsHovered(true)}
          
        />
    
        </div>
      );
  }
}



export const NoButton = () => {


    const [isHovered, setIsHovered] = useState(false);
  
    
    if (isHovered) {
      return (
        <div className="btn " >

        <FontAwesomeIcon 
          icon={icon({name: 'xmark'})} 
          size="2xl" 
          style={{ color: "#000000" }} 
          onMouseLeave={() => setIsHovered(false)}
          beatFade
        /> 
        </div>
      );
    } else {
      return (
        <div className="btn">
        <FontAwesomeIcon 
          icon={icon({name: 'xmark'})} 
          size="2xl" 
          style={{ color: "#000000" }} 
          onMouseEnter={() => setIsHovered(true)}
          
        />
        </div>
      );
  }
}


export const InfoButton = () => {


  const [isHovered, setIsHovered] = useState(false);

  
  if (isHovered) {
    return (
      <div className="" >

      <FontAwesomeIcon 
        icon={icon({name: 'circle-question'})} 
        size="2xl" 
        style={{ color: "#000000" }} 
        onMouseLeave={() => setIsHovered(false)}
        beatFade
      /> 
      </div>
    );
  } else {
    return (
      <div className="">
      <FontAwesomeIcon 
        icon={icon({name: 'circle-question'})} 
        size="2xl" 
        style={{ color: "#000000" }} 
        onMouseEnter={() => setIsHovered(true)}
        
      />
      </div>
    );
}
}





