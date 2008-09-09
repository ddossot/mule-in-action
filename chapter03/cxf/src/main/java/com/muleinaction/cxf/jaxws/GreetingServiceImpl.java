package com.muleinaction.cxf.jaxws;

import javax.jws.WebService; 
import javax.jws.WebMethod; 

@WebService(endpointInterface = "com.muleinaction.cxf.jaxws.GreetingService", 
serviceName = "GreetingService") 
public class GreetingServiceImpl implements GreetingService { 

    @WebMethod(operationName = "sayGreeting") 
    public String getGreeting(String name) { 
        return "Hello, " + name; 
    } 
} 

