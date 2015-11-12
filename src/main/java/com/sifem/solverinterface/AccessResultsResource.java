package com.sifem.solverinterface;

import eu.sifem.model.to.PAKCRestServiceTO;
import java.io.File;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import management.configuration.PropertiesManager;
import eu.sifem.utils.Util;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/accessResults")
public class AccessResultsResource {

    @Context
    private UriInfo context;

    public AccessResultsResource() {
    }

    @GET
    @Path("/screenshot/{id}")
    @Produces("image/bmp")
    public Response getScreenshot(@PathParam("id") String id)
    {
        PropertiesManager prop = PropertiesManager.getPropertiesManager();
        String filename = new String(prop.getPropertyValue("BaseSessionFolder") + "ScreenShot" + id + ".bmp");  
        File f = new File(filename);
        
        if(!f.exists())
        {
            ResponseBuilder response = Response.status(Status.NOT_FOUND);
            return response.build();
        }
         
        File file = new File(filename);
         
        ResponseBuilder response = Response.ok((Object) file);
        response.header("Content-Disposition", "attachment; filename=\"screenshot" + id + ".bmp\"");
        return response.build();        
    }

    @GET
    @Path("/unv/{sessionid}")
    @Produces("application/octet-stream")
    public Response getUNVFile(@PathParam("sessionid") String sessionid)
    {
        PropertiesManager prop = PropertiesManager.getPropertiesManager();
        String filename = new String(prop.getPropertyValue("BaseSessionFolder") + sessionid + "/Pak.unv");  
        File f = new File(filename);
        
        if(!f.exists())
        {
            ResponseBuilder response = Response.status(Status.NOT_FOUND);
            return response.build();
        }
         
        File file = new File(filename);
         
        ResponseBuilder response = Response.ok((Object) file);
        response.header("Content-Disposition", "attachment; filename=\"Pak.unv\"");
        return response.build();        
    }    

    @GET
    @Path("/dat/{sessionid}")
    @Produces("application/octet-stream")
    public Response getDATFile(@PathParam("sessionid") String sessionid)
    {
        PropertiesManager prop = PropertiesManager.getPropertiesManager();
        String filename = new String(prop.getPropertyValue("BaseSessionFolder") + sessionid + "/Pak.dat");  
        File f = new File(filename);
        
        if(!f.exists())
        {
            ResponseBuilder response = Response.status(Status.NOT_FOUND);
            return response.build();
        }
         
        File file = new File(filename);
         
        ResponseBuilder response = Response.ok((Object) file);
        response.header("Content-Disposition", "attachment; filename=\"Pak.dat\"");
        return response.build();        
    }     
    
    @GET
    @Path("/simulation/{sessionid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllFiles(@PathParam("sessionid") String sessionid)
    {
        try {
            PropertiesManager prop = PropertiesManager.getPropertiesManager();
            String pakDATPath = new String(prop.getPropertyValue("BaseSessionFolder") + sessionid + "/PAKC/Pak.dat");
            String pakUNVPath = new String(prop.getPropertyValue("BaseSessionFolder") + sessionid + "/PAKC/Pak.unv");
            File pakDATFile = new File(pakDATPath);
            File pakUNVFile  = new File(pakUNVPath);
            PAKCRestServiceTO simulationInstanceTO = new PAKCRestServiceTO();
            
            if(!pakDATFile.exists() || !pakUNVFile.exists())
            {            
                ResponseBuilder response = Response.status(Status.NOT_FOUND);
                return response.build();
            }
            
            byte[] pakcPakDatByteArr = org.apache.commons.io.FileUtils.readFileToByteArray(pakDATFile);
            byte[] pakcPakUnvByteArr = org.apache.commons.io.FileUtils.readFileToByteArray(pakUNVFile);
            
            simulationInstanceTO.setDatFile(pakcPakDatByteArr);
            simulationInstanceTO.setUnvFile(pakcPakUnvByteArr);
            String JSONReturn = Util.getJsonStrFromObject(simulationInstanceTO);
            
            ResponseBuilder response = Response.ok(JSONReturn);
            return response.build();   
        } catch (IOException ex) {
            Logger.getLogger(AccessResultsResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return Response.status(Status.NOT_FOUND).build();
    }    
    
}
