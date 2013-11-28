package py.pol.una.ia.tpfinal.g13.MOACS.main;

import py.pol.una.ia.tpfinal.vrptw.moacs.VRPTW;
import py.pol.una.ia.tpfinal.moaco.moacs.MOACS;
import py.pol.una.ia.tpfinal.pareto.moacs.Metricas;
import py.pol.una.ia.tpfinal.pareto.moacs.ParetoMetricas;
import py.pol.una.ia.tpfinal.pareto.moacs.SolucionMetricas;
import py.pol.una.ia.tpfinal.solucion.moacs.SolucionVRPTW;

/**
 *
 * @author Cristian Aceval Alvarez <cristian.aceval@gmail.com>
 * @author Deysi Leguizamon <deysi.leg@gmail.com>
 * @author Clara Lopez <clarita.diana@gmail.com>
 * @author Santiago Encina <cheito.en@gmail.com>
 */
public class TP3IA2013 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int cantIteraciones = 10;
        String parametros = "";
        MOACS alg;
        VRPTW prob;
        String pr = "";
        String[] arrayArchivoProblema = {"c101.txt", "rc101.txt"};
        String ruta = "instancias-parametros\\";
        String subRuta = "generado\\";
        String archivoParametros = "parametros.txt";
        String archivoProblema;
        
        SolucionMetricas FPOc101 = new SolucionMetricas(9, 857.4002096877956);      //Frente Pareto Optimo para la instancia c101
        
        SolucionMetricas FPOrc101_1 = new SolucionMetricas(11, 1532.2782176724243);   //Frente Pareto Optimo para la instancia rc101
        SolucionMetricas FPOrc101_2 = new SolucionMetricas(12, 1520.4586161710367);   //Frente Pareto Optimo para la instancia rc101
        
        ParetoMetricas paretoC101 = new ParetoMetricas();
        paretoC101.agregarNoDominado(FPOc101);
        
        ParetoMetricas paretoRC101 = new ParetoMetricas();
        paretoRC101.agregarNoDominado(FPOrc101_1);
        paretoRC101.agregarNoDominado(FPOrc101_2);
        
        Metricas metricas = new Metricas();
        
        for (int m = 0; m < arrayArchivoProblema.length; m++) { //recorrer array de ArchivoProblema

            parametros = ruta + archivoParametros;
            pr = arrayArchivoProblema[m];
            archivoProblema = pr;
            
            System.out.println("****************************************************");
            System.out.println();
            System.out.println("archivoProblema = " + archivoProblema);

            String cadenaYtrue = ruta + subRuta + "YTRUE-" + pr + ".txt";

            for (int i = 0; i < cantIteraciones; i++) {
                System.out.println("Corrida: " + i);
                prob = new VRPTW(ruta + archivoProblema);
                alg = new MOACS(prob, parametros);
                alg.ejecutar();
                alg.pareto.agregarSolucionesVRP(prob, cadenaYtrue);
                ParetoMetricas paretoActual = new ParetoMetricas();
                
                for (int j = 0; j < alg.pareto.getCantSoluciones(); j++) {
                    SolucionVRPTW solucionVRP = alg.pareto.getSolucionVRP(j);
                    int camiones = solucionVRP.getCamiones();
                    double funcionObjetivo2 = alg.getVRPTW().funcionObjetivo2(solucionVRP);
                    SolucionMetricas actual = new SolucionMetricas(camiones, funcionObjetivo2);
                    System.out.println("\tSolucion "+j);
                    System.out.println("\tF1: "+camiones);
                    System.out.println("\tF2: "+funcionObjetivo2);
                    paretoActual.agregarNoDominado(actual);
                }
                double m1 = metricas.distanciaAlFPO((m==0 ? paretoC101 : paretoRC101), paretoActual);
                double m2 = metricas.distribucionFrente(paretoActual);
                double m3 = metricas.extensionFrente(paretoActual);
                
                System.out.println("M1: "+m1+"\nM2: "+m2+"\nM3: "+m3);
                
                System.out.println();
            }
        }
    }
}
