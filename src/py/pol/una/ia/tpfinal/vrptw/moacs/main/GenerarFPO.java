package py.pol.una.ia.tpfinal.vrptw.moacs.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import py.pol.una.ia.tpfinal.vrptw.MOACS.pareto.ParetoMetricas;
import py.pol.una.ia.tpfinal.vrptw.MOACS.pareto.SolucionMetricas;

/**
 *
 * @author Cristian Aceval Alvarez <cristian.aceval@gmail.com>
 * @author Deysi Leguizamon <deysi.leg@gmail.com>
 * @author Clara Lopez <clarita.diana@gmail.com>
 * @author Santiago Encina <cheito.en@gmail.com>
 */
 
public class GenerarFPO {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String file = "instancias-parametros\\generado\\YTRUE-c101.txt.txt";
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        // El archivo file posee las dos matrices de adyacencia separadas por '\n'
        String linea;
        try {
            reader.readLine(); //Leer la 1ra linea de comentario
            String[] subCadena;
            ParetoMetricas pareto = new ParetoMetricas();
            while ((linea = reader.readLine()) != null) {
                subCadena = linea.split("\t");
                int f1 = Double.valueOf(subCadena[1]).intValue();
                Double valueOf = Double.valueOf(subCadena[0]);
                double f2 = valueOf.doubleValue();
                SolucionMetricas solucion = new SolucionMetricas(f1, f2);
                if (pareto.agregarNoDominado(solucion)) {
                    pareto.eliminarDominados(solucion);
                }
            }

            file = "instancias-parametros\\generado\\YTRUE-c101";
            try {
                FileWriter fstream = new FileWriter(file, true);
                BufferedWriter output = new BufferedWriter(fstream);
                for (SolucionMetricas solucionMetricas : pareto.getFrentePareto()) {
                    output.write(solucionMetricas.getF1() + "\t" + solucionMetricas.getF2());
                    output.newLine();
                }
                output.close();
            } catch (java.io.IOException e) {
                System.out.println("Error al leer archivo");
                e.printStackTrace();
            }

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
