/**
 * Solution.java
 * 
 * @author Juan J. Durillo
 * @author Antonio J. Nebro
 * @version 1.0 
 */
package py.pol.una.ia.tpfinal.vrptw.SPEA.baseS;

import java.io.Serializable;

import py.pol.una.ia.tpfinal.vrptw.SPEA.baseS.Configuration.* ;
import py.pol.una.ia.tpfinal.vrptw.SPEA.base.variable.Binary;

/**
 * Class representing a solution for a problem.
 */
public class Solution implements Serializable {        
  
  /**
   * Stores the type of the variable
   */	
  private SolutionType_ type_ ; 

  /**
   * Stores the decision variables of the solution.
   */
  private DecisionVariables decisionVariable_ ;

  /**
   * Stores the objectives values of the solution.
   */
  private double [] objective_ ;

  /**
   * Stores the number of objective values of the solution
   */
  private int numberOfObjectives_ ;

  /**
   * Stores the so called fitness value. Used in some metaheuristics
   */
  private double fitness_ ;

  /**
   * Used in algorithm AbYSS, this field is intended to be used to know
   * when a <code>Solution</code> is marked.
   */
  private boolean marked_ ;

  /**
   * Stores the so called rank of the solution. Used in NSGA-II
   */
  private int rank_ ;

  /**
   * Stores the overall constraint violation of the solution.
   */
  private double  overallConstraintViolation_ ;

  /**
   * Stores the number of constraints violated by the solution.
   */
  private int  numberOfViolatedConstraints_ ;

  /**
   * This field is intended to be used to know the location of
   * a solution into a <code>SolutionSet</code>. Used in MOCell
   */
  private int location_ ;

  /**
   * Stores the distance to his k-nearest neighbor into a 
   * <code>SolutionSet</code>. Used in SPEA2.
   */
  private double kDistance_ ; 

  /**
   * Stores the crowding distance of the the solution in a 
   * <code>SolutionSet</code>. Used in NSGA-II.
   */
  private double crowdingDistance_ ; 

  /**
   * Stores the distance between this solution and a <code>SolutionSet</code>.
   * Used in AbySS.
   */
  private double distanceToSolutionSet_ ;       

  /**
   * Constructor.
   */
  public Solution() {        
    marked_                       = false ;
    overallConstraintViolation_   = 0.0   ;
    numberOfViolatedConstraints_  = 0     ;  
    type_                         = SolutionType_.Undefined ;
    decisionVariable_             = null ;
    objective_                    = null ;
  } // Solution

  /**
   * Constructor
   * @param numberOfObjectives Number of objectives of the solution
   * 
   * This constructor is used mainly to read objective values from a file to
   * variables of a SolutionSet to apply quality indicators
   */
  public Solution(int numberOfObjectives) {
    numberOfObjectives_ = numberOfObjectives;
    objective_          = new double[numberOfObjectives];
  }
  
  /** 
   * Constructor.
   * @param problem The problem to solve
   */
  public Solution(Problem problem){
    //-> Initializing state variables and allocating memory
    type_ = problem.getSolutionType() ;
    numberOfObjectives_ = problem.getNumberOfObjectives() ;
    objective_          = new double[numberOfObjectives_] ;

    // Setting initial values
    fitness_              = 0.0 ;
    kDistance_            = 0.0 ;
    crowdingDistance_     = 0.0 ;        
    distanceToSolutionSet_ = Double.POSITIVE_INFINITY ;
    //<-

    decisionVariable_ = new DecisionVariables(problem);
  } // Solution
  
  static public Solution getNewSolution(Problem problem) {
    return new Solution(problem) ;
  }
  
  /** 
   * Constructor
   * @param problem The problem to solve
   */
  public Solution(Problem problem, DecisionVariables variables){
    //-> Initializing state variables and allocating memory
    type_ = problem.getSolutionType() ;
    numberOfObjectives_ = problem.getNumberOfObjectives() ;
    objective_          = new double[numberOfObjectives_] ;

    // Setting initial values
    fitness_              = 0.0 ;
    kDistance_            = 0.0 ;
    crowdingDistance_     = 0.0 ;        
    distanceToSolutionSet_ = Double.POSITIVE_INFINITY ;
    //<-

    decisionVariable_ = variables ;
  } // Constructor
  
  /** 
   * Copy constructor.
   * @param solution Solution to copy.
   */    
  public Solution(Solution solution) {            
    //-> Initializing state variables
    type_ = solution.type_;

    numberOfObjectives_ = solution.numberOfObjectives();
    objective_ = new double[numberOfObjectives_];
    for (int i = 0; i < objective_.length;i++) {
      objective_[i] = solution.getObjective(i);
    } // for
    //<-

    decisionVariable_ = new DecisionVariables(solution.getDecisionVariables());
    overallConstraintViolation_  = solution.getOverallConstraintViolation();
    numberOfViolatedConstraints_ = solution.getNumberOfViolatedConstraint();
    distanceToSolutionSet_ = solution.getDistanceToSolutionSet();
    crowdingDistance_     = solution.getCrowdingDistance();
    kDistance_            = solution.getKDistance();                
    fitness_              = solution.getFitness();
    marked_               = solution.isMarked();
    rank_                 = solution.getRank();
    location_             = solution.getLocation();
  } // Solution

  /**
   * Sets the distance between this solution and a <code>SolutionSet</code>.
   * The value is stored in <code>distanceToSolutionSet_</code>.
   * @param distance The distance to a solutionSet.
   */
  public void setDistanceToSolutionSet(double distance){
    distanceToSolutionSet_ = distance;
  } // SetDistanceToSolutionSet

  /**
   * Gets the distance from the solution to a <code>SolutionSet</code>. 
   * <b> REQUIRE </b>: this method has to be invoked after calling 
   * <code>setDistanceToPopulation</code>.
   * @return the distance to a specific solutionSet.
   */
  public double getDistanceToSolutionSet(){
    return distanceToSolutionSet_;
  } // getDistanceToSolutionSet


  /** 
   * Sets the distance between the solution and its k-nearest neighbor in 
   * a <code>SolutionSet</code>. The value is stored in <code>kDistance_</code>.
   * @param distance The distance to the k-nearest neighbor.
   */
  public void setKDistance(double distance){
    kDistance_ = distance;
  } // setKDistance

  /** 
   * Gets the distance from the solution to his k-nearest nighbor in a 
   * <code>SolutionSet</code>. Returns the value stored in
   * <code>kDistance_</code>. <b> REQUIRE </b>: this method has to be invoked 
   * after calling <code>setKDistance</code>.
   * @return the distance to k-nearest neighbor.
   */
  public double getKDistance(){
    return kDistance_;
  } // getKDistance

  /**
   * Sets the crowding distance of a solution in a <code>SolutionSet</code>.
   * The value is stored in <code>crowdingDistance_</code>.
   * @param distance The crowding distance of the solution.
   */  
  public void setCrowdingDistance(double distance){
    crowdingDistance_ = distance;
  } // setCrowdingDistance


  /** 
   * Gets the crowding distance of the solution into a <code>SolutionSet</code>.
   * Returns the value stored in <code>crowdingDistance_</code>.
   * <b> REQUIRE </b>: this method has to be invoked after calling 
   * <code>setCrowdingDistance</code>.
   * @return the distance crowding distance of the solution.
   */
  public double getCrowdingDistance(){
    return crowdingDistance_;
  } // getCrowdingDistance

  /**
   * Sets the fitness of a solution.
   * The value is stored in <code>fitness_</code>.
   * @param fitness The fitness of the solution.
   */
  public void setFitness(double fitness) {
    fitness_ = fitness;
  } // setFitness

  /**
   * Gets the fitness of the solution.
   * Returns the value of stored in the variable <code>fitness_</code>.
   * <b> REQUIRE </b>: This method has to be invoked after calling 
   * <code>setFitness()</code>.
   * @return the fitness.
   */
  public double getFitness() {
    return fitness_;
  } // getFitness

  /**
   * Sets the value of the i-th objective.
   * @param i The number identifying the objective.
   * @param value The value to be stored.
   */
  public void setObjective(int i, double value) {
    objective_[i] = value;
  } // setObjective

  /**
   * Returns the value of the i-th objective.
   * @param i The value of the objective.
   */
  public double getObjective(int i) {
    return objective_[i];
  } // getObjective

  /**
   * Returns the number of objectives.
   * @return The number of objectives.
   */
  public int numberOfObjectives() {
    if (objective_ == null)
      return 0 ;
    else
      return numberOfObjectives_;
  } // numberOfObjectives

  /**  
   * Returns the number of decision variables of the solution.
   * @return The number of decision variables.
   */
  public int numberOfVariables() {
    if (decisionVariable_ == null)
      return 0 ;
    else  
      return decisionVariable_.size();
  } // numberOfVariables

  /** 
   * Returns a string representing the solution.
   * @return The string.
   */
  public String toString() {
    String aux="";
    for (int i = 0; i < this.numberOfObjectives_; i++)
      aux = aux + this.getObjective(i) + "\t";

    return aux;
  } // toString
  public String toString(boolean comma) {
    String aux="";
    for (int i = 0; i < this.numberOfObjectives_; i++)
      aux = aux + this.getObjective(i) + ";";

    aux=aux.replace('.', ',');
    return aux;
  } // toString

  /**
   * Returns the decision variables of the solution.
   * @return the <code>DecisionVariables</code> object representing the decision
   * variables of the solution.
   */
  public DecisionVariables getDecisionVariables() {
    return this.decisionVariable_;
  } // getDecisionVariables

  /**
   * Sets the decision variables for the solution.
   * @param decisionVariables The <code>DecisionVariables</code> object 
   * representing the decision variables of the solution.
   */
  public void setDecisionVariables(DecisionVariables decisionVariables) {
    this.decisionVariable_ = decisionVariables;
  } // setDecisionVariables

  /**
   * Indicates if the solution is marked.
   * @return true if the method <code>marked</code> has been called and, after 
   * that, the method <code>unmarked</code> hasn't been called. False in other
   * case.
   */
  public boolean isMarked() {
    return this.marked_;
  } // isMarked

  /**
   * Establishes the solution as marked.
   */
  public void marked() {
    this.marked_ = true;
  } // marked

  /**
   * Established the solution as unmarked.
   */
  public void unMarked() {
    this.marked_ = false;
  } // unMarked

  /**  
   * Sets the rank of a solution. 
   * @param value The rank of the solution.
   */
  public void setRank(int value){
    this.rank_ = value;
  } // setRank

  /**
   * Gets the rank of the solution.
   * <b> REQUIRE </b>: This method has to be invoked after calling 
   * <code>setRank()</code>.
   * @return the rank of the solution.
   */
  public int getRank(){
    return this.rank_;
  } // getRank

  /**
   * Sets the overall constraints violated by the solution.
   * @param value The overall constraints violated by the solution.
   */
  public void setOverallConstraintViolation(double value) {
    this.overallConstraintViolation_ = value;
  } // setOverallConstraintViolation

  /**
   * Gets the overall constraint violated by the solution.
   * <b> REQUIRE </b>: This method has to be invoked after calling 
   * <code>overallConstraintViolation</code>.
   * @return the overall constraint violation by the solution.
   */
  public double getOverallConstraintViolation() {
    return this.overallConstraintViolation_;
  }  //getOverallConstraintViolation


  /**
   * Sets the number of constraints violated by the solution.
   * @param value The number of constraints violated by the solution.
   */
  public void setNumberOfViolatedConstraint(int value) {
    this.numberOfViolatedConstraints_ = value;
  } //setNumberOfViolatedConstraint

  /**
   * Gets the number of constraint violated by the solution.
   * <b> REQUIRE </b>: This method has to be invoked after calling
   * <code>setNumberOfViolatedConstraint</code>.
   * @return the number of constraints violated by the solution.
   */
  public int getNumberOfViolatedConstraint() {
    return this.numberOfViolatedConstraints_;
  } // getNumberOfViolatedConstraint

  /**
   * Sets the location of the solution into a solutionSet. 
   * @param location The location of the solution.
   */
  public void setLocation(int location) {
    this.location_ = location;
  } // setLocation

  /**
   * Gets the location of this solution in a <code>SolutionSet</code>.
   * <b> REQUIRE </b>: This method has to be invoked after calling
   * <code>setLocation</code>.
   * @return the location of the solution into a solutionSet
   */
  public int getLocation() {
    return this.location_;
  } // getLocation

  /**
   * Sets the type of the variable. 
   * @param type The type of the variable.
   */
  public void setType(String type) {
    type_ = Enum.valueOf(SolutionType_.class,type) ;
  } // setType

  /**
   * Sets the type of the variable. 
   * @param type The type of the variable.
   */
  public void setType(SolutionType_ type) {
    type_ = type ;
  } // setType

  /**
   * Gets the type of the variable
   * @return the type of the variable
   */
  public SolutionType_ getType() {
    return type_;
  } // getType



  /** 
   * Returns the aggregative value of the solution
   * @return The aggregative value.
   */
  public double getAggregativeValue() {
    double value = 0.0;                
    for (int i = 0; i < numberOfObjectives(); i++){            
      value += getObjective(i);
    }                
    return value;
  } // getAggregativeValue

  /**
   * Returns the number of bits of the chromosome in case of using a binary
   * representation
   * @return The number of bits if the case of binary variables, 0 otherwise
   */
  public int getNumberOfBits() {
    int bits = 0 ;
    
    for (int i = 0;  i < decisionVariable_.size()  ; i++)
      if ((decisionVariable_.variables_[i].getVariableType() == VariableType_.BinaryReal) ||
          (decisionVariable_.variables_[i].getVariableType() == VariableType_.Binary))
        bits += ((Binary)(decisionVariable_.variables_[i])).getNumberOfBits() ;
    
    return bits ;
  } // getNumberOfBits
} // Solution
