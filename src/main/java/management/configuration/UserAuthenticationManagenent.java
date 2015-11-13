package management.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserAuthenticationManagenent
{
    public void createSessionFolder(String sessionid)
    throws IOException
    {
        PropertiesManager prop = PropertiesManager.getPropertiesManager();
        File newFolder = new File(prop.getPropertyValue("BaseSessionFolder") + sessionid);
        
        newFolder.mkdir();
        
        copyFolder(new File(prop.getPropertyValue("BaseCADFolder")), newFolder);
    }

    public void createSessionHeadFolder(String sessionid)
    throws IOException
    {
        PropertiesManager prop = PropertiesManager.getPropertiesManager();
        File newFolder = new File(prop.getPropertyValue("BaseSessionFolder") + sessionid);
        
        newFolder.mkdir();
        
        copyFolder(new File(prop.getPropertyValue("BaseHeadFolder")), newFolder);
    }    
    
    protected void copyFolder(File src, File dest)
    throws IOException
    { 
    	if(src.isDirectory())
        {
            if(!dest.exists()){
    		   dest.mkdir();
            }
            String files[] = src.list();
 
            for (String file : files)
            {
    		   //construct the src and dest file structure
    		   File srcFile = new File(src, file);
    		   File destFile = new File(dest, file);
    		   //recursive copy
    		   copyFolder(srcFile,destFile);
            }
 
    	}
        else
        {
    		InputStream in = new FileInputStream(src);
    	        OutputStream out = new FileOutputStream(dest); 
 
    	        byte[] buffer = new byte[1024];
 
    	        int length;
    	        while ((length = in.read(buffer)) > 0){
    	    	   out.write(buffer, 0, length);
    	        }
 
    	        in.close();
    	        out.close();
    	}
    }    
    
    public String authenticateUser(String username, String pass)
    {
        String sessionID = null;
        PropertiesManager prop = PropertiesManager.getPropertiesManager();               
        String storedUsername = prop.getPropertyValue("username");
        String storedPassword = prop.getPropertyValue("password");

        if(storedPassword.endsWith(pass) && storedUsername.equals(username))            
            sessionID = UUID.randomUUID().toString().replaceAll("-", "");
                
        return sessionID;
    }
    
    public static void main(String[] args)
    {
        UserAuthenticationManagenent u = new UserAuthenticationManagenent();
        try {            
            u.createSessionFolder(u.authenticateUser("testusername", "password"));
        } catch (IOException ex) {
            Logger.getLogger(UserAuthenticationManagenent.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
