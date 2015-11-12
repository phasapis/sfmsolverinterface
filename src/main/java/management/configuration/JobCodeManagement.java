package management.configuration;

import java.util.Hashtable;

public class JobCodeManagement
{
    private static JobCodeManagement jobCodeManagement = null;
    private static Hashtable<String,JobStatesEnumeration> jobCodes = null;
    
    public static JobCodeManagement getJobCodeManagement()
    {
        if(jobCodeManagement==null)
        {
           jobCodeManagement = new JobCodeManagement();
           jobCodes = new Hashtable<String,JobStatesEnumeration>();
        }
        
        return jobCodeManagement;
    }
    
    public void addCode(String jobCode)
    {
        jobCodes.put(jobCode, JobStatesEnumeration.ONGOING);
    }
    
    public void nullifyCode(String jobCode)
    {
        jobCodes.put(jobCode, JobStatesEnumeration.FINISHED);
    }
    
    public JobStatesEnumeration getJobStatus(String jobCode)
    {
        return jobCodes.get(jobCode);
    }
}
