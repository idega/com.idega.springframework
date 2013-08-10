package com.idega.springframework;
 
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.web.context.support.XmlWebApplicationContext;
 
/**
 * Takes advantage of a spring hook to create a caching Bean Factory. Profiling identified the
 * high amount of reflection in spring 2.5.6 as a very large proportion of CPU time per request.
 * This class metadata should not change, so we cache it.
 */
public class CachingWebApplicationContext extends XmlWebApplicationContext {
 
    @Override
    protected DefaultListableBeanFactory createBeanFactory() {
        return new CachingByTypeBeanFactory();
    }
}