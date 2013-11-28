/**
 * JMException.java
 * 
 * @author Antonio J. Nebro
 * @version 1.0
 */
package py.pol.una.ia.tpfinal.vrptw.SPEA.util;

import java.io.Serializable;

import py.pol.una.ia.tpfinal.vrptw.SPEA.baseS.Configuration;

/**
 * jmetal exception class
 */
public class JMException extends Exception implements Serializable {
  
  /**
   * Constructor
   * @param Error message
   */
  public JMException (String message){
     super(message);      
  } // JmetalException
}
