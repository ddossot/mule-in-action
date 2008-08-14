package com.muleinaction.cxf.jaxws;

import javax.jws.WebService; 
import javax.jws.WebMethod; 

@WebService 
public interface GreetingService { 

    @WebMethod(operationName = "sayGreeting") 
    String getGreeting(String name); 

}
