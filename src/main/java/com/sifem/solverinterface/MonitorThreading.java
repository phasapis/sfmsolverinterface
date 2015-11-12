package com.sifem.solverinterface;

import java.io.File;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSession;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.core.Response;
import management.configuration.JobCodeManagement;
import management.configuration.PropertiesManager;

public class MonitorThreading
{

    public Runnable createRunnable(final String sessionid)
    {
        Runnable aRunnable = new Runnable()
        {
            public void run()
            {
                    JobCodeManagement jobCodeManagement = JobCodeManagement.getJobCodeManagement();
                    PropertiesManager prop = PropertiesManager.getPropertiesManager();        
                    Response response = null;    
                    File pakFile = null;
                    File unvFile = null;
                    File endFlag = null;
                    
                    //pakFile = new File(prop.getPropertyValue("BaseSessionFolder") + sessionid + "/PAKC/Pak.dat");
                    //unvFile = new File(prop.getPropertyValue("BaseSessionFolder") + sessionid + "/PAKC/Pak.unv");
                    endFlag = new File(prop.getPropertyValue("BaseSessionFolder") + sessionid + "/PAKC/" + prop.getPropertyValue("EndFlag"));
                    
                    System.out.println("------------ "+ endFlag.getPath());

                    //while((!pakFile.exists()) && (!unvFile.exists()))
                    while(!endFlag.exists())
                    {
                        try {            
                            Thread.sleep(3000);
                            endFlag = new File(prop.getPropertyValue("BaseSessionFolder") + sessionid + "/PAKC/" + prop.getPropertyValue("EndFlag"));
                            System.out.println(" Still waiting for:" + endFlag.getAbsoluteFile());
                            
                        }
                        catch (InterruptedException ex)
                        {
                            Logger.getLogger(MonitorThreading.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

                    jobCodeManagement.nullifyCode(sessionid);

                    queueSession(sessionid);
            }
        };

        return aRunnable;
    }    
    
    protected void queueSession(String sessionid)
    {
        PropertiesManager prop = PropertiesManager.getPropertiesManager();                
        Properties properties = new Properties();
        TopicConnection topicConnection = null;
        properties.put("java.naming.factory.initial", prop.getPropertyValue("InitialContextFactory"));
        properties.put("connectionfactory.QueueConnectionFactory", prop.getPropertyValue("ConnectionString"));
        properties.put("topic." + prop.getPropertyValue("TopicName") + "." + sessionid, prop.getPropertyValue("TopicName") + "." + sessionid);

        try
        {
            System.out.println(" --------------- queue:" + prop.getPropertyValue("TopicName") + "." + sessionid);
            InitialContext ctx = new InitialContext(properties);
            TopicConnectionFactory topicConnectionFactory = (TopicConnectionFactory) ctx.lookup("QueueConnectionFactory");
            topicConnection = topicConnectionFactory.createTopicConnection();

            try
            {
              TopicSession topicSession = topicConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);

              Topic topic = (Topic) ctx.lookup(prop.getPropertyValue("TopicName") + "." + sessionid);
              javax.jms.TopicPublisher topicPublisher = topicSession.createPublisher(topic);

              String msg = sessionid;
              TextMessage textMessage = topicSession.createTextMessage(msg);

              topicPublisher.publish(textMessage);
              System.out.println("Publishing message " +textMessage);
              topicPublisher.close();
              topicSession.close();

              Thread.sleep(20);
            }
            catch (InterruptedException e)
            {
              e.printStackTrace();
            }

        }
        catch (JMSException e)
        {
            throw new RuntimeException("Error in JMS operations", e);
        }
        catch (NamingException e)
        {
            throw new RuntimeException("Error in initial context lookup", e);
        }
    }        

}
