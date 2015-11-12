package com.sifem.solverinterface;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import management.configuration.JobCodeManagement;
import management.configuration.PropertiesManager;
import management.configuration.UserAuthenticationManagenent;
import org.json.JSONObject;



@Path("/solver")
public class CADResource
{

    @Context
    private UriInfo context;

    public CADResource() {
    }
    
    @GET
    @Path("/authenticate/{username}/{password}")
    @Produces(MediaType.TEXT_PLAIN)
    public String authenticate(@PathParam("username") String username, @PathParam("password") String password)
    {
        String sessionID = null;
        UserAuthenticationManagenent userAuthenticationManagenent = new UserAuthenticationManagenent();
        sessionID = userAuthenticationManagenent.authenticateUser(username, password);
        
        return sessionID;
    }    
    
    @GET
    @Path("/cad/init/{modelname}/{sessionid}")
    @Produces(MediaType.APPLICATION_JSON)    
    public Response init(@PathParam("modelname") String modelName, @PathParam("sessionid") String sessionID)
    {
        PropertiesManager prop = PropertiesManager.getPropertiesManager();
        JobCodeManagement jobCodeManagement = JobCodeManagement.getJobCodeManagement();
        Response response = null;
        JSONObject responseID = null;
        
        Runtime rt = Runtime.getRuntime();
        try
        {
            String[] parameters = new String[2];
            parameters[0] = prop.getPropertyValue("BaseSessionFolder") 
                                + sessionID 
                                + "/" + prop.getPropertyValue("CADPath");
            
            //parameters[1] = new String("CoiledCochlea");
            parameters[1] = new String(modelName);
            
            System.out.println(" --- EXEPATH: " + parameters[0] + " " + parameters[1]);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                Logger.getLogger(CADResource.class.getName()).log(Level.SEVERE, null, ex);
            }
            Process pr = rt.exec(parameters);
            jobCodeManagement.addCode(sessionID);
            
            MonitorThreading monitorThreading = new MonitorThreading();
            Runnable myRunnable = monitorThreading.createRunnable(sessionID);
            Thread thread = new Thread(myRunnable);
            thread.start();
            
            response = Response.ok().build();
        }
        catch (IOException ex)
        {
            Logger.getLogger(CADResource.class.getName()).log(Level.SEVERE, null, ex);
            response = Response.status(Response.Status.NOT_ACCEPTABLE).build();
        }
                
        return response;
    }
    
    @GET
    @Path("/cad/status/{sessionid}")
    public Response getStatus(@PathParam("sessionid") String sessionID)
    {
        JobCodeManagement jobCodeManagement = JobCodeManagement.getJobCodeManagement();
        PropertiesManager prop = PropertiesManager.getPropertiesManager();        
        Response response = null;        
        File pakFile = null;
        File unvFile = null;
        
        pakFile = new File(prop.getPropertyValue("BaseSessionFolder") 
                            + sessionID 
                            + "/" + prop.getPropertyValue("ResultsFolder") 
                            + "/Pak.dat");
        unvFile = new File(prop.getPropertyValue("BaseSessionFolder") 
                            + sessionID 
                            + "/" + prop.getPropertyValue("ResultsFolder") 
                            + "/Pak.unv");
        
        if(pakFile.exists() && unvFile.exists())
        {
            response = Response.status(Response.Status.OK).build();
            jobCodeManagement.nullifyCode(sessionID);
        }
        else
            response = Response.status(Response.Status.NOT_FOUND).build();
        
        return response;
    }    
}
