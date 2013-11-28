/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package py.pol.una.ia.tpfinal.vrptw.SPEA.metricas;

import py.pol.una.ia.tpfinal.vrptw.SPEA.baseS.Solution;
import py.pol.una.ia.tpfinal.vrptw.SPEA.baseS.SolutionSet;

/**
 *
 * @author Internet Designer
 */
public class MetricsUtil_main {

    public static void main(String [] args) {



        MetricsUtil metricsUtils = new MetricsUtil();

        SolutionSet solutionSet = metricsUtils.readNonDominatedSolutionSet("src/files/algorithms/frontpareto/resultado_frente_tsp_spea.txt");


        ConjuntoPareto pareto = new ConjuntoPareto();

        for (int i = 0; i < solutionSet.size(); i++) {

                double solObjetivo1 = ((Solution) solutionSet.get(i)).getObjective(0);
                double solObjetivo2 = ((Solution) solutionSet.get(i)).getObjective(1);

                if (pareto.agregarNoDominado(solObjetivo1, solObjetivo2) == 1) {
                    pareto.eliminarDominados(solObjetivo1, solObjetivo2);
                }
                
            }

        pareto.solutionSet.printObjectivesToFile("src/files/algorithms/frontparetopromedio/frontparetopromediospeatsp.txt");

    }

    
}
