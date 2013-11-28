package py.pol.una.ia.tpfinal.solucion.moacs;

import java.io.PrintStream;

/**
 *
 * @author Cristian Aceval Alvarez <cristian.aceval@gmail.com>
 * @author Deysi Leguizamon <deysi.leg@gmail.com>
 * @author Clara Lopez <clarita.diana@gmail.com>
 * @author Santiago Encina <cheito.en@gmail.com>
 */
public class SolucionVRPTW {

    private int camiones;
    private int sizeActual;
    private int clientes[];
    private int size;

    public SolucionVRPTW(int tam) {
        
        clientes = new int[tam];
        for (int i = 0; i < tam; i++) {
            clientes[i] = -1;
        }
        size = tam;
        
        camiones = 1;
        sizeActual = 0;
    }

    public void incrementarCamiones() {
        camiones++;
    }

    public int getCamiones() {
        return camiones;
    }

    public void add(int valor) {
        if (sizeActual + 1 >= size) {
            duplicarTamanho();
        }
        clientes[sizeActual] = valor;
        sizeActual++;
    }

    public int getSizeActual() {
        return sizeActual;
    }

    public void imprimir(PrintStream f) {
        for (int i = 0; i < sizeActual - 1; i++) {
            f.print(clientes[i] + "-");
        }
        f.print(clientes[sizeActual - 1] + "-");
    }

    public void resetear() {
        for (int i = 0; i < size; i++) {
            clientes[i] = -1;
        }
        sizeActual = 0;
        camiones = 0;
    }

    public void copiarSolucion(SolucionVRPTW sol) {
        for (int i = 0; i < sol.getSizeActual(); i++) {
            clientes[i] = sol.get(i);
        }
        for (int i = sol.getSizeActual(); i < size; i++) {
            clientes[i] = -1;
        }
        sizeActual = sol.getSizeActual();
        camiones = sol.getCamiones();
    }

    private void duplicarTamanho() {
        int arrayAnterior[] = clientes;
        clientes = new int[size * 2];
        for (int i = 0; i < size; i++) {
            clientes[i] = arrayAnterior[i];
        }
        for (int i = size; i < size * 2; i++) {
            clientes[i] = -1;
        }
        size *= 2;
    }

    @Override
    public String toString() {
        String ret = "SolucionVRP{" + "camiones=" + camiones + ", sizeActual=" + sizeActual;
        ret = ret.concat(", array=[");
        for (int i = 0; i < sizeActual; i++) {
            ret = ret.concat(clientes[i]+";");
        }
        ret = ret.concat("]");
        return ret;
    }
    
    public void set(int pos, int valor) {
        clientes[pos] = valor;
    }
    
    public int get(int pos) {
        return clientes[pos];
    }
    
    public int getSize() {
        return size;
    }

}
