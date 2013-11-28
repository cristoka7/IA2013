
package py.pol.una.ia.tpfinal.vrptw.SPEA.main;

import py.pol.una.ia.tpfinal.vrptw.SPEA.baseS.Problem;
import py.pol.una.ia.tpfinal.vrptw.SPEA.baseS.Operator;
import py.pol.una.ia.tpfinal.vrptw.SPEA.baseS.SolutionSet;
import py.pol.una.ia.tpfinal.vrptw.SPEA.baseS.Configuration;
import py.pol.una.ia.tpfinal.vrptw.SPEA.baseS.Algorithm;
import py.pol.una.ia.tpfinal.vrptw.SPEA.otros.Kursawe;
import py.pol.una.ia.tpfinal.vrptw.SPEA.otros.ProblemFactory;
import py.pol.una.ia.tpfinal.vrptw.SPEA.operadores.CrossoverFactory;
import py.pol.una.ia.tpfinal.vrptw.SPEA.operadores.MutationFactory;
import py.pol.una.ia.tpfinal.vrptw.SPEA.operadores.SelectionFactory;
import java.io.IOException;

import py.pol.una.ia.tpfinal.vrptw.SPEA.util.JMException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class SPEA2_main {
  public static Logger      logger_ ;      
  public static FileHandler fileHandler_ ; 
  
  
  public static void main(String [] args) throws JMException, IOException {
    Problem   problem   ;         // El problema a Resolver
    Algorithm algorithm ;         // Algoritmo A ser utilizado
    Operator  crossover ;         // Crossover 
    Operator  mutation  ;         // Mutacion
    Operator  selection ;         // Seleccion de operadores
        
    
    logger_      = Configuration.logger_ ;
    fileHandler_ = new FileHandler("SPEA2.log"); 
    logger_.addHandler(fileHandler_) ;
    
    if (args.length == 1) {
      Object [] params = {"Real"};
      problem = (new ProblemFactory()).getProblem(args[0],params);
    } // if
    else { 
      problem = new Kursawe(3, "Real"); 
     
    } 
    
    algorithm = new SPEA2(problem);
    
   
    algorithm.setInputParameter("Cantidad Poblacion",100);
    algorithm.setInputParameter("tamaño del archivo",100);
    algorithm.setInputParameter("cantidad maxima de evalucion",25000);
      
    // Mutation and Crossover for Real codification
    crossover = CrossoverFactory.getCrossoverOperator("SBXCrossover");                   
    crossover.setParameter("probabilidad",0.9);                   
    crossover.setParameter("indice de distribucion",20.0);
    mutation = MutationFactory.getMutationOperator("mutacionpolinomial");                    
    mutation.setParameter("probabilidad",1.0/problem.getNumberOfVariables());
    mutation.setParameter("distribucionindice",20.0);
    
   
 
    selection = SelectionFactory.getSelectionOperator("BinaryTournament") ;                           
    
 
    algorithm.addOperator("crossover",crossover);
    algorithm.addOperator("mutacion",mutation);
    algorithm.addOperator("seleccion",selection);
    
   
    long initTime = System.currentTimeMillis();
    SolutionSet population = algorithm.execute();
    long estimatedTime = System.currentTimeMillis() - initTime;

       
    logger_.info("Tiempo de Ejecucion Total: " + estimatedTime);
    logger_.info("Valores objetivos funcionales ");
    population.printObjectivesToFile("Funcionales");
    logger_.info("Valores de variablesVariables ");
    population.printVariablesToFile("VAR");      
  }
} 
