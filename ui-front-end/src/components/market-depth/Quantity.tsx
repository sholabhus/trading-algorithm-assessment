import React, { useRef, useEffect } from 'react';
export interface QuantityProps{
    quantity:number;
    color: string;
    }
export const Quantity =(props: QuantityProps)=>{
    const lastQuantityRef = useRef(props.quantity);
    //const [arrowPosition, setArrowPosition] =useState(0);
    const diff = props.quantity - lastQuantityRef.current;
    
    useEffect(() =>{
    // Log the price difference for debugging
            console.log(`Quantity: ${props.quantity}, Last Quantity: ${lastQuantityRef.current}, Diff: ${diff}`);

//update the reference after the log
lastQuantityRef.current =props.quantity;
    }, [diff, props.quantity]); // run when quantity changes

    {/*style */}
    const barStyle={
        backgroundColor :props.color,
        width: `${props.quantity ? props.quantity / 10 : 0}px`,
        height: '20px',
        marginLeft: '5px', // Add spacing
        display: 'flex', // Use flex to center the text
       alignItems: 'center',
    };  

return (
        <div className="quantity-container" style={{ display: 'flex', alignItems: 'center' }}>
            {/*render quantity bar */}
            <span className='color' style={barStyle}></span>
            
          {/*display quantity */}           
{props.quantity}

            </div>
        
    );
};
