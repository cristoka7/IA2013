/**
 * BinaryTournament2.java
 * @author Juan J. Durillo
 * @version 1.0
 */

package py.pol.una.ia.tpfinal.vrptw.SPEA.operadores;

import java.util.Comparator;
import py.pol.una.ia.tpfinal.vrptw.SPEA.baseS.Solution;
import py.pol.una.ia.tpfinal.vrptw.SPEA.baseS.Operator;
import py.pol.una.ia.tpfinal.vrptw.SPEA.baseS.SolutionSet;
import py.pol.una.ia.tpfinal.vrptw.SPEA.operadores.DominanceComparator;
import py.pol.una.ia.tpfinal.vrptw.SPEA.util.PseudoRandom;

/**
 * This class implements an opertor for binary selections using the same code
 * in Deb's NSGA-II implementation
 */
public class BinaryTournament2 extends Operator{
  
  /**
   * dominance_ store the <code>Comparator</code> for check dominance_
   */
  private Comparator dominance_;
  
  /**
   * a_ stores a permutation of the solutions in the solutionSet used
   */
  private int a_[];
  
  /**
   *  index_ stores the actual index for selection
   */
  private int index_ = 0;
    
  /**
   * Constructor
   * Creates a new instance of the Binary tournament operator (Deb's
   * NSGA-II implementation version)
   */
  public BinaryTournament2()
  {
    dominance_ = new DominanceComparator();              
  } // BinaryTournament2
    
  /**
  * Performs the operation
  * @param object Object representing a SolutionSet
  * @return the selected solution
  */
  public Object execute(Object object)    
  {
    SolutionSet population = (SolutionSet)object;
    if (index_ == 0) //Create the permutation
    {
      a_= (new py.pol.una.ia.tpfinal.vrptw.SPEA.util.PermutationUtility()).intPermutation(population.size());
    }
            
        
    Solution solution1,solution2;
    solution1 = population.get(a_[index_]);
    solution2 = population.get(a_[index_+1]);
        
    index_ = (index_ + 2) % population.size();
        
    int flag = dominance_.compare(solution1,solution2);
    if (flag == -1)
      return solution1;
    else if (flag == 1)
      return solution2;
    else if (solution1.getCrowdingDistance() > solution2.getCrowdingDistance())
      return solution1;
    else if (solution2.getCrowdingDistance() > solution1.getCrowdingDistance())
      return solution2;
    else
      if (PseudoRandom.randDouble()<0.5)
        return solution1;
      else
        return solution2;        
  } // execute
} // BinaryTournament2
