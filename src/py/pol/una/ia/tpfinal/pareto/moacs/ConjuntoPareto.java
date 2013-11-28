package py.pol.una.ia.tpfinal.pareto.moacs;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintStream;
import py.pol.una.ia.tpfinal.solucion.moacs.SolucionVRPTW;
import py.pol.una.ia.tpfinal.vrptw.moacs.VRPTW;

/**
 *
 * @author Cristian Aceval Alvarez <cristian.aceval@gmail.com>
 * @author Deysi Leguizamon <deysi.leg@gmail.com>
 * @author Clara Lopez <clarita.diana@gmail.com>
 * @author Santiago Encina <cheito.en@gmail.com>
 */
public class ConjuntoPareto {

    public static final int PARETO = 50;
    private int cantSoluciones; //cantidad actual de soluciones
    private int tamano; //tamano del array de soluciones
    // array que contiene las soluciones del frente pareto
    protected SolucionVRPTW[] soluciones;

    public ConjuntoPareto(int numSoluciones) {
        cantSoluciones = 0;
        tamano = numSoluciones;
        //lista = new Solucion[numSoluciones];
        soluciones = new SolucionVRPTW[numSoluciones];
    }
    
    public int agregarNoDominado(SolucionVRPTW sol, VRPTW prob) {
        double solfuncion1 = prob.funcionObjetivo1(sol); // Evaluacion de la solucion respecto
        double solfuncion2 = prob.funcionObjetivo2(sol); // a las funciones obetivo del problema

        double solauxfuncion1; // Evaluacion de los objetivos de alguna solucion del conjunto
        double solauxfuncion2;

        for (int i = 0; i < cantSoluciones; i++) {
            solauxfuncion1 = prob.funcionObjetivo1(soluciones[i]);
            solauxfuncion2 = prob.funcionObjetivo2(soluciones[i]);
            // ambas funciones objetivo siempre se minimizan
            if (solauxfuncion1 <= solfuncion1 && solauxfuncion2 <= solfuncion2) {
                return 0; //sol es dominada por una solucion del conjunto

            }
        }
        //Aumentar el tamaño del conjunto Pareto si este esta lleno
        if (cantSoluciones == tamano) {
            SolucionVRPTW[] listaAux = soluciones;
            tamano = tamano * 2;
            soluciones = new SolucionVRPTW[tamano];
            for (int i = 0; i < cantSoluciones; i++) {
                soluciones[i] = listaAux[i];
            }
        }
        if (soluciones[cantSoluciones] == null) {
            soluciones[cantSoluciones] = new SolucionVRPTW(sol.getSize());
        }
        soluciones[cantSoluciones].copiarSolucion(sol);
        cantSoluciones++;
        return 1;
    }

    public void eliminarDominados(SolucionVRPTW sol, VRPTW prob) {
        double solfuncion1 = prob.funcionObjetivo1(sol); // Evaluacion de la solucion respecto

        double solfuncion2 = prob.funcionObjetivo2(sol); // a las funciones obetivo del problema

        double solauxfuncion1; // Evaluacion de los objetivos de alguna solucion del conjunto

        double solauxfuncion2;
        //SolucionVRP *elim;

        for (int i = 0; i < cantSoluciones; i++) {
            solauxfuncion1 = prob.funcionObjetivo1(soluciones[i]);
            solauxfuncion2 = prob.funcionObjetivo2(soluciones[i]);
            // ambas funciones objetivo siempre se minimizan
            if ((solauxfuncion1 > solfuncion1 && solauxfuncion2 >= solfuncion2) || (solauxfuncion1 >= solfuncion1 && solauxfuncion2 > solfuncion2)) {
                //elim=soluciones[i];
                soluciones[i] = soluciones[cantSoluciones - 1];
                soluciones[cantSoluciones - 1] = null; //liberar puntero

                cantSoluciones--;
                i--;
                //elim->destruir();
            }
        }
    }

    public void listarSolucionesVRP(VRPTW prob, String file) {
        try {
            PrintStream output = new PrintStream(new FileOutputStream(file));
            output.println(cantSoluciones);
            for (int i = 0; i < cantSoluciones; i++) {
                output.println(prob.funcionObjetivo1(soluciones[i]) + "\t" + prob.funcionObjetivo2(soluciones[i]));
            }
            output.close();
        } catch (java.io.IOException e) {
            System.out.println("Error al leer archivo");
            e.printStackTrace();
        }
    }

    public void agregarSolucionesVRP(VRPTW prob, String file) {
        try {
            FileWriter fstream = new FileWriter(file, true);
            BufferedWriter output = new BufferedWriter(fstream);
            for (int i = 0; i < cantSoluciones; i++) {
                output.write(prob.funcionObjetivo2(soluciones[i]) + "\t" + prob.funcionObjetivo1(soluciones[i]));
                output.newLine();
            }
            output.close();
        } catch (java.io.IOException e) {
            System.out.println("Error al leer archivo");
            e.printStackTrace();
        }
    }

    public int getCantSoluciones() {
        return cantSoluciones;
    }

    public SolucionVRPTW getSolucionVRP(int i) {
        return soluciones[i];
    }

    @Override
    public String toString() {
        String ret = "ConjuntoPareto{" + "cantSoluciones=" + cantSoluciones + ", tamano=" + tamano + ", listaVRP=";
        ret = ret.concat("[");
        for (int i = 0; i < cantSoluciones; i++) {
            SolucionVRPTW solucionVRP = soluciones[i];
            ret = ret.concat(solucionVRP+" , ");
        }
        ret = ret.concat("]");
        ret = ret.concat(" }");
        return ret;
    }
    
    
}
