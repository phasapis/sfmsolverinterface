package com.sifem.solverinterface;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import eu.sifem.model.to.PAKCRestServiceTO;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.Path;
import javax.ws.rs.POST;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import management.configuration.PropertiesManager;
import management.configuration.UserAuthenticationManagenent;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;


@Path("/ConfigurationFile")
public class ConfigurationFileResource {

    @Context
    private UriInfo context;

    public ConfigurationFileResource() {
    }

	@POST
	@Path("/upload/{sessionid}")
	@Consumes("application/x-www-form-urlencoded")
	public Response uploadFile(@PathParam("sessionid") String sessionID, @FormParam("simulationInstance") String PAKCRestServiceJSON)
        {
                PropertiesManager prop = PropertiesManager.getPropertiesManager();
                UserAuthenticationManagenent userAuthenticationManagenent = new UserAuthenticationManagenent();
                
                try {
                    userAuthenticationManagenent.createSessionFolder(sessionID);
                } catch (IOException ex) {
                    Logger.getLogger(ConfigurationFileResource.class.getName()).log(Level.SEVERE, null, ex);
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();                    
                }

                String path = new String(prop.getPropertyValue("BaseSessionFolder") 
                                            + sessionID 
                                            + "/" + prop.getPropertyValue("ConfigurationPath"));                 

                System.out.println(" --------------------- " + sessionID + PAKCRestServiceJSON);
                Gson gson = new GsonBuilder().create();
                PAKCRestServiceTO simulationInstanceTO = gson.fromJson(PAKCRestServiceJSON, PAKCRestServiceTO.class);
                try {
                    System.out.println(IOUtils.toString(simulationInstanceTO.getCfgFile(), "UTF-8"));
                    FileUtils.writeByteArrayToFile(new File(path), simulationInstanceTO.getCfgFile());
                } catch (IOException ex) {
                    Logger.getLogger(ConfigurationFileResource.class.getName()).log(Level.SEVERE, null, ex);
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();                    
                }
                
            return Response.status(Response.Status.OK).build(); 
	}

	@POST
	@Path("/upload/head/{sessionid}")
	@Consumes("application/x-www-form-urlencoded")
	public Response uploadHeadFile(@PathParam("sessionid") String sessionID, @FormParam("simulationInstance") String PAKCRestServiceJSON)
        {
                PropertiesManager prop = PropertiesManager.getPropertiesManager();
                UserAuthenticationManagenent userAuthenticationManagenent = new UserAuthenticationManagenent();
                
                try {
                    userAuthenticationManagenent.createSessionHeadFolder(sessionID);
                } catch (IOException ex) {
                    Logger.getLogger(ConfigurationFileResource.class.getName()).log(Level.SEVERE, null, ex);
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();                    
                }

                String path = new String(prop.getPropertyValue("BaseSessionFolder") 
                                            + sessionID 
                                            + "/" + prop.getPropertyValue("ConfigurationPath"));                 

                System.out.println(" --------------------- " + sessionID + PAKCRestServiceJSON);
                Gson gson = new GsonBuilder().create();
                PAKCRestServiceTO simulationInstanceTO = gson.fromJson(PAKCRestServiceJSON, PAKCRestServiceTO.class);
                try {
                    System.out.println(IOUtils.toString(simulationInstanceTO.getCfgFile(), "UTF-8"));
                    FileUtils.writeByteArrayToFile(new File(path), simulationInstanceTO.getCfgFile());
                } catch (IOException ex) {
                    Logger.getLogger(ConfigurationFileResource.class.getName()).log(Level.SEVERE, null, ex);
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();                    
                }
                
            return Response.status(Response.Status.OK).build(); 
	}

        
        
	private void writeFile(byte[] content, String filename)
        throws IOException
        {
 
            File file = new File(filename);
 
            if (!file.exists())
                file.createNewFile();
 
            FileOutputStream fop = new FileOutputStream(file);
 
            fop.write(content);
            fop.flush();
            fop.close();
	}        

        
}
