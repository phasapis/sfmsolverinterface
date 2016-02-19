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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

@Path("/accessResults")
public class AccessResultsResource {

    @Context
    private UriInfo context;

    public AccessResultsResource() {
    }

    @GET
    @Path("/screenshot/{sessionid}/{id}")
    @Produces("image/bmp")
    public Response getScreenshot(@PathParam("sessionid") String sessionid, @PathParam("id") String id)
    {
        PropertiesManager prop = PropertiesManager.getPropertiesManager();
        String filename = new String(prop.getPropertyValue("BaseSessionFolder") + sessionid + "/" + "ScreenShot" + id + ".bmp");  
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
    @Path("/simulation/headmodel/{sessionid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllHeadFiles(@PathParam("sessionid") String sessionid)
    {
        try {
            PropertiesManager prop = PropertiesManager.getPropertiesManager();
            /*
            String pakDATPath = new String(prop.getPropertyValue("BaseSessionFolder") + sessionid 
                            + "/" + prop.getPropertyValue("ResultsHeadPath") + "Pak.dat");
            String pakUNVPath = new String(prop.getPropertyValue("BaseSessionFolder") + sessionid 
                            + "/" + prop.getPropertyValue("ResultsHeadPath") + "Pak.unv");
                    */
            
            String zippedpakDATPath = new String(prop.getPropertyValue("BaseSessionFolder") + sessionid + "/PAKC/Pakunv.zip");            
            String pakDATPath = new String(prop.getPropertyValue("BaseSessionFolder") + sessionid + "/PAKC/Pak.dat");
            String pakUNVPath = new String(prop.getPropertyValue("BaseSessionFolder") + sessionid + "/PAKC/Pak.unv");
            
            
            System.err.println(pakUNVPath);
            File pakDATFile = new File(pakDATPath);
            File pakUNVFile  = new File(pakUNVPath);
            PAKCRestServiceTO simulationInstanceTO = new PAKCRestServiceTO();
            
            if(!pakDATFile.exists() || !pakUNVFile.exists())
            {            
                ResponseBuilder response = Response.status(Status.NOT_FOUND);
                return response.build();
            }
            
            byte[] pakcPakDatByteArr = org.apache.commons.io.FileUtils.readFileToByteArray(pakDATFile);
            
            ZipIt(pakUNVPath, zippedpakDATPath);
            
            File zipped = new File(zippedpakDATPath);
            byte[] pakcPakUnvByteArr = org.apache.commons.io.FileUtils.readFileToByteArray(zipped);            
            
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
    
    @GET
    @Path("/simulation/{sessionid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllFiles(@PathParam("sessionid") String sessionid)
    {
        try {
            PropertiesManager prop = PropertiesManager.getPropertiesManager();
            String pakDATPath = new String(prop.getPropertyValue("BaseSessionFolder") + sessionid + "/PAKC/Pak.dat");            
            String pakUNVPath = new String(prop.getPropertyValue("BaseSessionFolder") + sessionid + "/PAKC/Pak.unv");
            
            String centerlinePath = new String(prop.getPropertyValue("BaseSessionFolder") + sessionid + "/PAKC/Results/d_centerline.csv");
            String imagPath = new String(prop.getPropertyValue("BaseSessionFolder") + sessionid + "/PAKC/Results/p_imag.csv");
            String realPath = new String(prop.getPropertyValue("BaseSessionFolder") + sessionid + "/PAKC/Results/p_real.csv");
            String magnPath = new String(prop.getPropertyValue("BaseSessionFolder") + sessionid + "/PAKC/Results/v_magn.csv");
            String phasePath = new String(prop.getPropertyValue("BaseSessionFolder") + sessionid + "/PAKC/Results/v_phase.csv");
            
            File pakDATFile = new File(pakDATPath);
            File pakUNVFile  = new File(pakUNVPath);
            File centerlineFile  = new File(centerlinePath);
            File imagFile  = new File(imagPath);
            File realFile  = new File(realPath);
            File magnFile  = new File(magnPath);
            File phaseFile  = new File(phasePath);
            
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
            
            while(!centerlineFile.exists())
            {
                try {            
                    Thread.sleep(3000);                            
                }
                catch (InterruptedException ex)
                {
                    Logger.getLogger(AccessResultsResource.class.getName()).log(Level.SEVERE, null, ex);
                }
            }            
            
            if(centerlineFile.exists())
            {
                byte[] centerlineByteArr = org.apache.commons.io.FileUtils.readFileToByteArray(centerlineFile);
                byte[] imagByteArr = org.apache.commons.io.FileUtils.readFileToByteArray(imagFile);
                byte[] realByteArr = org.apache.commons.io.FileUtils.readFileToByteArray(realFile);
                byte[] magnByteArr = org.apache.commons.io.FileUtils.readFileToByteArray(magnFile);
                byte[] phaseByteArr = org.apache.commons.io.FileUtils.readFileToByteArray(phaseFile);

                simulationInstanceTO.setdCenterLineFile(centerlineByteArr);
                simulationInstanceTO.setpImagFile(imagByteArr);
                simulationInstanceTO.setvMagnFile(magnByteArr);
                simulationInstanceTO.setvPhaseFile(phaseByteArr);
                simulationInstanceTO.setpRealFile(realByteArr);
            }
            
            String JSONReturn = Util.getJsonStrFromObject(simulationInstanceTO);
            
            ResponseBuilder response = Response.ok(JSONReturn);
            return response.build();
            
        } catch (IOException ex) {
            Logger.getLogger(AccessResultsResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return Response.status(Status.NOT_FOUND).build();
    }    

    public void ZipIt(String inpath, String outpath)
    {
    	byte[] buffer = new byte[1024];
    	
    	try{
    		
    		FileOutputStream fos = new FileOutputStream(outpath);
    		ZipOutputStream zos = new ZipOutputStream(fos);
    		ZipEntry ze= new ZipEntry("Pak.unv");
    		zos.putNextEntry(ze);
    		FileInputStream in = new FileInputStream(inpath);
   	   
    		int len;
    		while ((len = in.read(buffer)) > 0) {
    			zos.write(buffer, 0, len);
    		}

    		in.close();
    		zos.closeEntry();
           
    		//remember close it
    		zos.close();
          
    		System.out.println("Done");

    	}catch(IOException ex){
    	   ex.printStackTrace();
    	}
    }
    
    
    public void unZipIt(String zipFile, String outputFolder){

     byte[] buffer = new byte[1024];
    	
     try{
    		
    	//create output directory is not exists
    	File folder = new File(outputFolder);
    	if(!folder.exists()){
    		folder.mkdir();
    	}
    		
    	//get the zip file content
    	ZipInputStream zis = 
    		new ZipInputStream(new FileInputStream(zipFile));
    	//get the zipped file list entry
    	ZipEntry ze = zis.getNextEntry();
    		
    	while(ze!=null){
    			
    	   String fileName = ze.getName();
           File newFile = new File(outputFolder + File.separator + fileName);
                
           System.out.println("file unzip : "+ newFile.getAbsoluteFile());
                
            //create all non exists folders
            //else you will hit FileNotFoundException for compressed folder
            new File(newFile.getParent()).mkdirs();
              
            FileOutputStream fos = new FileOutputStream(newFile);             

            int len;
            while ((len = zis.read(buffer)) > 0) {
       		fos.write(buffer, 0, len);
            }
        		
            fos.close();   
            ze = zis.getNextEntry();
    	}
    	
        zis.closeEntry();
    	zis.close();
    		
    	System.out.println("Done");
    		
    }catch(IOException ex){
       ex.printStackTrace(); 
    }
   }    
    
}
