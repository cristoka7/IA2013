/**
 * CrossoverFactory.java
 *
 * @author Juanjo Durillo
 * @version 1.1
 */

package py.pol.una.ia.tpfinal.vrptw.SPEA.operadores;

import py.pol.una.ia.tpfinal.vrptw.SPEA.operadores.BitFlipMutation;
import py.pol.una.ia.tpfinal.vrptw.SPEA.baseS.Configuration;
import py.pol.una.ia.tpfinal.vrptw.SPEA.baseS.Operator;
import py.pol.una.ia.tpfinal.vrptw.SPEA.util.JMException;

/**
 * Class implementing a mutation factory.
 */
public class MutationFactory {
  
  /**
   * Gets a crossover operator through its name.
   * @param name of the operator
   * @return the operator
   * @throws JMException 
   */
  public static Operator getMutationOperator(String name) throws JMException{
  
    if (name.equalsIgnoreCase("PolynomialMutation"))
      return new PolynomialMutation(20);
    else if (name.equalsIgnoreCase("BitFlipMutation"))
      return new BitFlipMutation();
    else if (name.equalsIgnoreCase("SwapMutation"))
      return new SwapMutation();
    else
    {
      Configuration.logger_.severe("Operator '" + name + "' not found ");
      Class cls = java.lang.String.class;
      String name2 = cls.getName() ;    
      throw new JMException("Exception in " + name2 + ".getMutationOperator()") ;
    }        
  } // getMutationOperator
} // MutationFactory
