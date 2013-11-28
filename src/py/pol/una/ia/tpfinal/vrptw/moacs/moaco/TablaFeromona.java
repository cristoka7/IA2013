package py.pol.una.ia.tpfinal.vrptw.MOACS.moaco;

/**
 *
 * @author Cristian Aceval Alvarez <cristian.aceval@gmail.com>
 * @author Deysi Leguizamon <deysi.leg@gmail.com>
 * @author Clara Lopez <clarita.diana@gmail.com>
 * @author Santiago Encina <cheito.en@gmail.com>
 */
public class TablaFeromona {

    private int cantClientes;
    private double[][] tabla;

    public TablaFeromona(int tam) {
        cantClientes = tam;
        tabla = new double[tam][tam];
    }

    public double obtenerValor(int estOrigen, int estDestino) {
        return tabla[estOrigen][estDestino];
    }

    public void actualizar(int estOrigen, int estDestino, double tau) {
        tabla[estOrigen][estDestino] = tau;
    }

    public void reiniciar(double tau0) {
        for (int i = 0; i < cantClientes; i++) {
            for (int j = 0; j < cantClientes; j++) {
                tabla[i][j] = tau0;
            }
        }
    }

    public void imprimir() {
        for (int i = 0; i < cantClientes; i++) {
            for (int j = 0; j < cantClientes; j++) {
                System.out.printf("%lf ", tabla[i][j]);
            }
            System.out.print("\n");
        }
    }
}
