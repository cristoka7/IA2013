/**
 * OKA2.java
 *
 * @author Antonio J. Nebro
 * @version 1.0
 */
package py.pol.una.ia.tpfinal.vrptw.SPEA.otros;

import py.pol.una.ia.tpfinal.vrptw.SPEA.baseS.Problem;
import py.pol.una.ia.tpfinal.vrptw.SPEA.baseS.Solution;
import py.pol.una.ia.tpfinal.vrptw.SPEA.baseS.DecisionVariables;
import py.pol.una.ia.tpfinal.vrptw.SPEA.baseS.Configuration.*;
import py.pol.una.ia.tpfinal.vrptw.SPEA.util.JMException;

/**
 * Class representing problem Kursawe
 */
public class OKA2 extends Problem {  
   
  
  /** 
   * Constructor.
   * Creates a new instance of the OKA2 problem.
   * @param solutionType The solution type must "Real" or "BinaryReal".
   */
  public OKA2(String solutionType) {
    numberOfVariables_   = 3  ;
    numberOfObjectives_  = 2  ;
    numberOfConstraints_ = 0  ;
    problemName_         = "OKA2"                    ;
        
    upperLimit_ = new double[numberOfVariables_] ;
    lowerLimit_ = new double[numberOfVariables_] ;
       
    lowerLimit_[0] = -Math.PI ;
    upperLimit_[0] = Math.PI  ;    
    for (int i = 1; i < numberOfVariables_; i++) {
      lowerLimit_[i] = -5.0 ;
      upperLimit_[i] = 5.0  ;
    } // for
        
    solutionType_ = Enum.valueOf(SolutionType_.class, solutionType) ; 
    
    // All the variables are of the same type, so the solutionType name is the
    // same than the variableType name
    variableType_ = new VariableType_[numberOfVariables_];
    for (int var = 0; var < numberOfVariables_; var++){
      variableType_[var] = Enum.valueOf(VariableType_.class, solutionType) ;    
    } // for
  } // OKA2
    
  /** 
  * Evaluates a solution 
  * @param solution The solution to evaluate
   * @throws JMException 
  */
  public void evaluate(Solution solution) throws JMException {
    DecisionVariables decisionVariables  = solution.getDecisionVariables();
        
    double [] fx   = new double[numberOfObjectives_] ; // 2 functions
    double [] x    = new double[numberOfVariables_]  ; // 3 variables
   
    for (int i = 0; i < numberOfVariables_; i++)
      x[i] = decisionVariables.variables_[i].getValue() ;
    
    fx[0] = x[0] ; 
    
    fx[1] = 1 -Math.pow((x[0]+Math.PI),2)/(4*Math.pow(Math.PI,2)) + 
               Math.pow(Math.abs(x[1] - 5*Math.cos(x[0])),1.0/3.0)+ 
               Math.pow(Math.abs(x[2] - 5*Math.sin(x[0])),1.0/3.0);
        
    solution.setObjective(0, fx[0]);
    solution.setObjective(1, fx[1]);
  } // evaluate
} // OKA2
