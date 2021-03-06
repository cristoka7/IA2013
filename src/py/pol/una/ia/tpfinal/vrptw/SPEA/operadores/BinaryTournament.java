/**
 * BinaryTournament.java
 * @author Juan J. Durillo
 * @version 1.0
 */
package py.pol.una.ia.tpfinal.vrptw.SPEA.operadores;


import py.pol.una.ia.tpfinal.vrptw.SPEA.operadores.BinaryTournamentComparator;
import java.util.Comparator;
import py.pol.una.ia.tpfinal.vrptw.SPEA.baseS.Solution;
import py.pol.una.ia.tpfinal.vrptw.SPEA.baseS.Operator;
import py.pol.una.ia.tpfinal.vrptw.SPEA.baseS.SolutionSet;
import py.pol.una.ia.tpfinal.vrptw.SPEA.util.PseudoRandom;

/**
 * This class implements an opertor for binary selections
 */
public class BinaryTournament extends Operator{

  /**
   * Stores the <code>Comparator</code> used to compare two
   * solutions
   */
  private Comparator comparator_;

  /**
   * Constructor
   * Creates a new Binary tournament operator using a BinaryTournamentComparator
   */
  public BinaryTournament(){
    comparator_ = new BinaryTournamentComparator();
  } // BinaryTournament

  
  /**
  * Constructor
  * Creates a new Binary tournament with a specific <code>Comparator</code>
  * @param comparator The comparator
  */
  public BinaryTournament(Comparator comparator) {
    comparator_ = comparator;
  } // Constructor

  
  /**
  * Performs the operation
  * @param object Object representing a SolutionSet
  * @return the selected solution
  */
  public Object execute(Object object){
    SolutionSet SolutionSet = (SolutionSet)object;
    Solution solution1,solution2;
    solution1 = SolutionSet.get(PseudoRandom.randInt(0,SolutionSet.size()-1));
    solution2 = SolutionSet.get(PseudoRandom.randInt(0,SolutionSet.size()-1));

    int flag = comparator_.compare(solution1,solution2);
    if (flag == -1)
      return solution1;
    else if (flag == 1)
      return solution2;
    else
      if (PseudoRandom.randDouble()<0.5)
        return solution1;
      else
        return solution2;                       
  } // execute
} // BinaryTournament
