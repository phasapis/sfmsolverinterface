/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sifem.solverinterface;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author panos
 */
@javax.ws.rs.ApplicationPath("webresources")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(com.sifem.solverinterface.AccessResultsResource.class);
        resources.add(com.sifem.solverinterface.CADResource.class);
        resources.add(com.sifem.solverinterface.ConfigurationFileResource.class);

        resources.add(org.jboss.resteasy.plugins.providers.jaxb.CollectionProvider.class);
        resources.add(org.jboss.resteasy.plugins.providers.jaxb.JAXBElementProvider.class);
        resources.add(org.jboss.resteasy.plugins.providers.jaxb.JAXBXmlRootElementProvider.class);
        resources.add(org.jboss.resteasy.plugins.providers.jaxb.JAXBXmlSeeAlsoProvider.class);
        resources.add(org.jboss.resteasy.plugins.providers.jaxb.JAXBXmlTypeProvider.class);
        resources.add(org.jboss.resteasy.plugins.providers.jaxb.MapProvider.class);
        resources.add(org.jboss.resteasy.plugins.providers.jaxb.XmlJAXBContextFinder.class);
        resources.add(org.jboss.resteasy.plugins.providers.multipart.ListMultipartReader.class);
        resources.add(org.jboss.resteasy.plugins.providers.multipart.ListMultipartWriter.class);
        resources.add(org.jboss.resteasy.plugins.providers.multipart.MapMultipartFormDataReader.class);
        resources.add(org.jboss.resteasy.plugins.providers.multipart.MapMultipartFormDataWriter.class);
        resources.add(org.jboss.resteasy.plugins.providers.multipart.MimeMultipartProvider.class);
        resources.add(org.jboss.resteasy.plugins.providers.multipart.MultipartFormAnnotationReader.class);
        resources.add(org.jboss.resteasy.plugins.providers.multipart.MultipartFormAnnotationWriter.class);
        resources.add(org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataReader.class);
        resources.add(org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataWriter.class);
        resources.add(org.jboss.resteasy.plugins.providers.multipart.MultipartReader.class);
        resources.add(org.jboss.resteasy.plugins.providers.multipart.MultipartRelatedReader.class);
        resources.add(org.jboss.resteasy.plugins.providers.multipart.MultipartRelatedWriter.class);
        resources.add(org.jboss.resteasy.plugins.providers.multipart.MultipartWriter.class);
        resources.add(org.jboss.resteasy.plugins.providers.multipart.XopWithMultipartRelatedReader.class);
        resources.add(org.jboss.resteasy.plugins.providers.multipart.XopWithMultipartRelatedWriter.class);
        resources.add(org.jboss.resteasy.plugins.stats.RegistryStatsResource.class);
    }
    
}
