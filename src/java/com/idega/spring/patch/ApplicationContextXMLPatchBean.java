/*
 * Created on 2.12.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.idega.spring.patch;

/**
 * 
 * This Object does nothing, it only has to exist because the xml parser that 
 * parses applicationContext.xml sets the dtd condition of (description?,bean+) 
 * and not (description?,import*,bean*) as it should according to the dtd configured
 * in the header of applicationContext.xml.
 * 
 * 
 *  Last modified: $$Date: 2004/12/02 15:14:28 $$ by $$Author: gummi $$
 * 
 * @author <a href="mailto:gummi@idega.com">Gudmundur Agust Saemundsson</a>
 * @version $$Revision: 1.1 $$
 */
public class ApplicationContextXMLPatchBean {
	

}
