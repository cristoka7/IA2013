package py.pol.una.ia.tpfinal.vrptw.moacs;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import py.pol.una.ia.tpfinal.solucion.moacs.SolucionVRPTW;

/**
 *
 * @author Cristian Aceval Alvarez <cristian.aceval@gmail.com>
 * @author Deysi Leguizamon <deysi.leg@gmail.com>
 * @author Clara Lopez <clarita.diana@gmail.com>
 * @author Santiago Encina <cheito.en@gmail.com>
 */
public class VRPTW {

    private int capacidad;
    private Cliente clientes[];
    //cantidad de clientes
    protected int cantClientes;
    //matriz de adyacencia de los clientes
    protected double[][] matrizAdy;

    public VRPTW(String file) {
        cargarEstado(file);
    }
    
    public int getCantClientes() {
        return cantClientes;
    }

    public double getDistancia(int i, int j) {
        return matrizAdy[i][j];
    }

    private void cargarEstado(String file) {
        // El archivo file posee: la cantidad de customers, la capacidad de los camiones
        // y los datos de cada customer: coordenadas, demanda, ventana y tiempo de servicio
        int id = 0;
        double x = 0.0;
        double y = 0.0;
        double demanda = 0.0;
        double readyT = 0.0;
        double dueT = 0.0;
        double serviceT = 0.0;
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        String linea = "";
        try {
            reader.readLine(); //CUSTOMERS
            cantClientes = Integer.parseInt(reader.readLine());
            matrizAdy = new double[cantClientes][cantClientes];
            clientes = new Cliente[cantClientes];
            reader.readLine(); //CAPACITY
            capacidad = Integer.parseInt(reader.readLine());
            reader.readLine(); //CUST NO.  XCOORD.   YCOORD.    DEMAND   READY TIME  DUE DATE   SERVICE TIME
            int count = 0;
            String[] subCadena;
            while ((linea = reader.readLine()) != null) {
                if (count < cantClientes) {
                    subCadena = linea.split("\\s+");
                    for (int i = 0; i < subCadena.length; i++) {
                        switch (i) {
                            case 0:
                                id          = Integer.parseInt(subCadena[i]);
                                break;
                            case 1:
                                x           = Double.parseDouble(subCadena[i]);
                                break;
                            case 2:
                                y           = Double.parseDouble(subCadena[i]);
                                break;
                            case 3:
                                demanda     = Double.parseDouble(subCadena[i]);
                                break;
                            case 4:
                                readyT      = Double.parseDouble(subCadena[i]);
                                break;
                            case 5:
                                dueT        = Double.parseDouble(subCadena[i]);
                                break;
                            case 6:
                                serviceT    = Double.parseDouble(subCadena[i]);
                                break;
                            default:
                                ;
                        }
                    }
                    clientes[count] = new Cliente(id, x, y, serviceT, demanda, readyT, dueT);
                }
                count++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        generarMatrizAdyacencia();
        
        //imprimir();
    }

    private void generarMatrizAdyacencia() {
        double x;
        double y;
        double aux;

        for (int i = 0; i < cantClientes; i++) {
            for (int j = i + 1; j < cantClientes; j++) {
                //x^2
                x = Math.pow((clientes[i].getX() - clientes[j].getX()), 2);
                //y^2
                y = Math.pow((clientes[i].getY() - clientes[j].getY()), 2);
                //(x^2) + (y^2)
                aux = x + y;
                matrizAdy[i][j] = Math.sqrt(aux);
                matrizAdy[j][i] = matrizAdy[i][j];
            }
            matrizAdy[i][i] = 0;
        }
    }

    public double funcionObjetivo1(SolucionVRPTW sol) {
        return ((SolucionVRPTW) sol).getCamiones(); // devuelve la cantidad camiones
    }

    public double funcionObjetivo2(SolucionVRPTW solucion) {
        int i;
        double suma = 0;
        for (i = 0; i < solucion.getSizeActual() - 1; i++) {
            suma += matrizAdy[solucion.get(i)][solucion.get(i + 1)];
        }
        suma += matrizAdy[solucion.get(solucion.getSizeActual() - 1)][0];

        return suma; // devolver el "Total Travel Distance"
    }

    public double heuristica1(int estOrigen, int estDest) {
        return 1;
    }

    public double heuristica2(int estOrigen, int estDest) {
        return 1.0 / matrizAdy[estOrigen][estDest];
    }

    public int getCapacidad() {
        return capacidad;
    }

    public double getDemand(int customer) {
        return clientes[customer].getDemand();
    }

    public double getReadyTime(int customer) {
        return clientes[customer].getReadyTime();
    }

    public double getDueTime(int customer) {
        return clientes[customer].getDueTime();
    }

    public double getServiceTime(int customer) {
        return clientes[customer].getServiceTime();
    }

    public void imprimir() {
        System.out.print("Matriz Adyacencia:\n");
        for (int i = 0; i < cantClientes; i++) {
            for (int j = 0; j < cantClientes; j++) {
                System.out.print(matrizAdy[i][j] + " ");
            }
            System.out.print("\n");
        }
        System.out.println("Size: " + cantClientes);
        System.out.println("capacity: " + capacidad);
        for (int i = 0; i < 20; i++) {
            System.out.print("Customer: " + i + " ");
            System.out.print("Demanda: " + clientes[i].getDemand() + " ");
            System.out.print("Service Time: " + clientes[i].getServiceTime() + " ");
            System.out.print("Begin: " + clientes[i].getReadyTime() + " ");
            System.out.println("End: " + clientes[i].getDueTime() + " ");
        }
    }
}
