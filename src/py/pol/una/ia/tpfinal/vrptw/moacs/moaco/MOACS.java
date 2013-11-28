package py.pol.una.ia.tpfinal.vrptw.MOACS.moaco;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import py.pol.una.ia.tpfinal.vrptw.MOACS.pareto.ConjuntoPareto;
import py.pol.una.ia.tpfinal.vrptw.MOACS.solucion.SolucionVRPTW;
import py.pol.una.ia.tpfinal.vrptw.moacs.VRPTW;

/**
 *
 * @author Cristian Aceval Alvarez <cristian.aceval@gmail.com>
 * @author Deysi Leguizamon <deysi.leg@gmail.com>
 * @author Clara Lopez <clarita.diana@gmail.com>
 * @author Santiago Encina <cheito.en@gmail.com>
 */
public class MOACS {

    private TablaFeromona feromonas; //Matrices de feromonas
    private double alfa; // exponente para las feromonas
    private double beta; // exponente para la visibilidad
    private double rho; // Learning step (coeficiente de evaporacion)
    private double taoInicial; // valor inicial para las tablas de feromonas
    private double tao;
    private double q0;
    private double F1MAX; // utilizados para normalizacion
    private double F2MAX;
    private double NORM1;
    private double NORM2;
    private int hormigaActual; // utilizado para calcular los pesos lambda
    private int noLambdas;
    private static int CANT_SOLUCIONES = 30;
    public static final int RAND_MAX = 2147483647; // (2^31) - 1
    private VRPTW vrptw;
    private int criterio;
    private int tiempoTotal;
    private int maxIteraciones;
    private int hormigas;
    public ConjuntoPareto pareto;

    public MOACS(VRPTW p, String file) {
        this.vrptw = p;
        this.pareto = new ConjuntoPareto(CANT_SOLUCIONES);
        this.NORM1 = 1; //por defecto
        this.NORM2 = 1; //por defecto
        this.F1MAX = 1; //por defecto
        this.F2MAX = 1; //por defecto
        inicializarParametros(file);
        inicializar_tabla();
        this.noLambdas = 0;
    }

    private void inicializar_tabla() {
        this.feromonas = new TablaFeromona(this.vrptw.getCantClientes());
        this.feromonas.reiniciar(this.taoInicial);
    }

    private void inicializarParametros(String file) {
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
            while ((linea = reader.readLine()) != null) {
                subCadena = linea.split("=");
                double VALOR = Double.valueOf(subCadena[1]).doubleValue();
                if (subCadena[0].equalsIgnoreCase("criterio")) {
                    this.criterio = (int) VALOR;
                } else if (subCadena[0].equalsIgnoreCase("valor")) {
                    if (this.criterio == 1) {
                        this.tiempoTotal = (int) VALOR;
                    } else {
                        this.maxIteraciones = (int) VALOR;
                    }
                } else if (subCadena[0].equalsIgnoreCase("hormigas")) {
                    this.hormigas = (int) VALOR;
                } else if (subCadena[0].equalsIgnoreCase("alfa")) {
                    this.alfa = VALOR;
                } else if (subCadena[0].equalsIgnoreCase("beta")) {
                    this.beta = VALOR;
                } else if (subCadena[0].equalsIgnoreCase("rho")) {
                    this.rho = VALOR;
                } else if (subCadena[0].equalsIgnoreCase("tau0")) {
                    this.taoInicial = VALOR;
                } else if (subCadena[0].equalsIgnoreCase("q0")) {               // Utilizado para la regla Pseudo-Aleatoria
                    this.q0 = VALOR; 
                } else if (subCadena[0].equalsIgnoreCase("f1max")) {            // promedio de la evaluacion del conjunto pareto del objetivo 1
                    this.F1MAX = VALOR;
                } else if (subCadena[0].equalsIgnoreCase("f2max")) {            // promedio de la evaluacion del conjunto pareto del objetivo 2
                    this.F2MAX = VALOR;
                } else if (subCadena[0].equalsIgnoreCase("d1max")) {            // Valor para normalizar el objetivo 1
                    this.NORM1 = VALOR;
                } else if (subCadena[0].equalsIgnoreCase("d2max")) {            // Valor para normalizar el objetivo 2
                    this.NORM2 = VALOR;
                }

            }

        } catch (Exception e) {
            System.err.println("Error de conversion");
        }

    }

    private void actualizarFeromonas(SolucionVRPTW solucion, int tamanhoSolucion, double deltaTau) {
        int j;
        int k;
        for (int i = 0; i < tamanhoSolucion - 1; i++) { // actualizar ambas tablas en una cantidad 1/cantNoDominados
            j = solucion.get(i);
            k = solucion.get(i + 1);
            this.feromonas.actualizar(j, k, this.feromonas.obtenerValor(j, k) + deltaTau);
        }
    }

    private int seleccionarMayor(int estOrigen, int[] visitados) {
        int sgteEstado = 0;
        double mayorValor = -1; // inicializar a un valor peque�o
        double heuristica1;
        double heuristica2;
        double valorActual;
        double lambda1;
        double lambda2;
        double random;
        int[] sinPorcion = new int[this.vrptw.getCantClientes()];
        int cantSinPorcion = 0;
        if (this.noLambdas != 0) {
            lambda1 = lambda2 = 1;
        } else {
            lambda1 = this.hormigaActual; // peso de la hormiga actual para el objetivo 1
            lambda2 = this.hormigas - lambda1 + 1; // peso para el objetivo 2
        }
        for (int i = 0; i < this.vrptw.getCantClientes(); i++) {
            if (visitados[i] == 0) // estado i no visitado
            {
                heuristica1 = this.vrptw.heuristica1(estOrigen, i) * this.NORM1; // normalizado
                heuristica2 = this.vrptw.heuristica2(estOrigen, i) * this.NORM2; // normalizado
                valorActual = Math.pow(this.feromonas.obtenerValor(estOrigen, i), this.alfa) * Math.pow(heuristica1, lambda1 * this.beta) * Math.pow(heuristica2, lambda2 * this.beta);
                if (valorActual > mayorValor) {
                    mayorValor = valorActual;
                    sgteEstado = i;
                }
                sinPorcion[cantSinPorcion++] = i;
            }
        }
        if (mayorValor == -1) {
            random = rand() % cantSinPorcion;
            sgteEstado = sinPorcion[(int) random];
        }
        return sgteEstado;
    }

    private int seleccionarProbabilistico(int estOrigen, int[] visitados) {
        int sgteEstado = 0;
        double heuristica1;
        double heuristica2;

        double[] productos = new double[this.vrptw.getCantClientes()];
        double random;
        double suma = 0;
        double acum = 0;
        double lambda1;
        double lambda2;
        int[] sinPorcion = new int[this.vrptw.getCantClientes()];
        int cantSinPorcion = 0;
        if (this.noLambdas != 0) {
            lambda1 = lambda2 = 1;
        } else {
            lambda1 = this.hormigaActual; // peso de la hormiga actual para el objetivo 1
            lambda2 = this.hormigas - lambda1 + 1; // peso para el objetivo 2
        }
        random = rand() / (double) RAND_MAX; // escoger un valor entre 0 y 1
        // hallar la suma y los productos
        for (int i = 0; i < this.vrptw.getCantClientes(); i++) {
            if (visitados[i] == 0) {
                heuristica1 = this.vrptw.heuristica1(estOrigen, i) * this.NORM1; // normalizado
                heuristica2 = this.vrptw.heuristica2(estOrigen, i) * this.NORM2; // normalizado
                productos[i] = Math.pow(this.feromonas.obtenerValor(estOrigen, i), this.alfa) * Math.pow(heuristica1, lambda1 * this.beta) * Math.pow(heuristica2, lambda2 * this.beta);
                suma += productos[i];
                sinPorcion[cantSinPorcion++] = i;
            }
        }
        if (suma == 0) {
            random = rand() % cantSinPorcion;
            sgteEstado = sinPorcion[(int) random];
        } else {
            // aplicar ruleta
            for (int i = 0; i < this.vrptw.getCantClientes(); i++) {
                if (visitados[i] == 0) // estado i no visitado
                {
                    acum += productos[i] / suma;
                    if (acum >= random) {
                        sgteEstado = i;
                        break;
                    }
                }
            }
        }
        return sgteEstado;
    }

    private double calcularDeltaTao(SolucionVRPTW sol) {
        double delta;

        delta = 1.0 / ((this.vrptw.funcionObjetivo1(sol) / this.F1MAX) + (this.vrptw.funcionObjetivo2(sol) / this.F2MAX)); //normalizados
        
        return delta;
    }

    private double calcularTaoPrima(double avr1, double avr2) {
        return (1.0 / (avr1 * avr2));
    }

    private double calcularPromedioPareto(int objetivo) {
        double promedio = 0;
        for (int i = 0; i < this.pareto.getCantSoluciones(); i++) {
            if (objetivo == 1) {
                promedio += this.vrptw.funcionObjetivo1(this.pareto.getSolucionVRP(i));
            } else {
                promedio += this.vrptw.funcionObjetivo2(this.pareto.getSolucionVRP(i));
            }
        }

        return (promedio / (double) this.pareto.getCantSoluciones());
    }

    public void calcularTaoIJ(int origen, int destino) {
        double tau;
        tau = (1 - this.rho) * this.feromonas.obtenerValor(origen, destino) + this.rho * this.taoInicial; // (1-RHO)*TAUij + RHO*TAO0
        this.feromonas.actualizar(origen, destino, tau);
    }

    public int siguienteEstado(int estOrigen, SolucionVRPTW sol, double currentTime, double cargaActual) {
        int sgteEstado = 0;
        int[] visitados = new int[this.vrptw.getCantClientes()];
        SolucionVRPTW soluc = (SolucionVRPTW) sol;
        VRPTW problem = (VRPTW) this.vrptw;
        int totalVisitados = 1; // necesariamente se visito el deposito 1 vez
        double distancia;
        double q;
        // hallar el vecindario
        for (int i = 0; i < soluc.getSizeActual(); i++) {
            visitados[soluc.get(i)] = 1;
            if (soluc.get(i) != 0) { // estado 0 ya se contabilizo
                totalVisitados++;
            }
        }
        for (int i = 0; i < problem.getCantClientes(); i++) {
            if (visitados[i] == 0) { // controlar si se cumplira la ventana, la capacidad y si se podra volver a tiempo al deposito si fuera necesario
                distancia = Math.max(currentTime + problem.getDistancia(estOrigen, i), problem.getReadyTime(i));
                if (    cargaActual + problem.getDemand(i) > problem.getCapacidad() ||                  // controlar que la demanda no supera la capacidad del camion
                        currentTime + problem.getDistancia(estOrigen, i) > problem.getDueTime(i) ||     // controlar que el camion llegue al cliente en la ventana de tiempo
                        distancia + problem.getDistancia(i, 0) > problem.getDueTime(0)) {               // controlar que el tiempo de retorno al deposito no sea mayor al establecido en el deposito
                    visitados[i] = 1; // marcar como no vecino
                    totalVisitados++;
                }
            }
        }

        if (totalVisitados >= problem.getCantClientes()) {
            sgteEstado = 0; // ir al deposito
        } else {
            // Regla Proporcional Pseudo-Aleatoria
            q = rand() / (double) RAND_MAX;
            if (q <= this.q0) {
                sgteEstado = seleccionarMayor(estOrigen, visitados);
            } else {
                sgteEstado = seleccionarProbabilistico(estOrigen, visitados);
            }
        }
        return sgteEstado;
    }

    public void ejecutar() {
        int generacion = 0;
        int estOrigen;
        double deltaTao;
        double taoPrima;
        long start;
        long end;
        SolucionVRPTW[] sols = new SolucionVRPTW[this.hormigas];
        for (int i = 0; i < this.hormigas; i++) {
            sols[i] = new SolucionVRPTW(this.vrptw.getCantClientes() * 2);
        }
        start = System.currentTimeMillis();
        end = start;
        this.tao = -1;
        this.noLambdas = 1;

        while (condicionParada(generacion, start, end)) {
            generacion++;
            for (int i = 0; i < this.hormigas; i++) {
                estOrigen = 0; // colocar a la hormiga en un estado inicial
                this.hormigaActual = i + 1; // utilizado en seleccionar_sgte_estado
                construirSolucionVRP(estOrigen, this, 1, sols[i]);
            }

            for (int i = 0; i < this.hormigas; i++) {
                if (this.pareto.agregarNoDominado(sols[i], this.vrptw) == 1) {
                    this.pareto.eliminarDominados(sols[i], this.vrptw);
                }
                //else
                sols[i].resetear();
            }

            taoPrima = calcularTaoPrima(calcularPromedioPareto(1), calcularPromedioPareto(2));

            if (taoPrima > this.tao) {
                // reiniciar tabla de feromonas
                this.tao = taoPrima;
                this.feromonas.reiniciar(this.tao);
            } else {
                // actualizan la tabla las soluciones del frente Pareto
                for (int i = 0; i < this.pareto.getCantSoluciones(); i++) {
                    deltaTao = calcularDeltaTao(this.pareto.getSolucionVRP(i));

                    actualizarFeromonas(this.pareto.getSolucionVRP(i), this.pareto.getSolucionVRP(i).getSizeActual(), deltaTao);
                }
            }
            end = System.currentTimeMillis();
        }
    }

    private void construirSolucionVRP(int estOrigen, MOACS moacs, int taoIJ, SolucionVRPTW solucion) {
        int estVisitados = 0;
        int sgteEstado;
        int estActual = estOrigen;
        double cargaActual;
        double currentTime;

        VRPTW vrp = (VRPTW) moacs.getVRPTW();

        solucion.add(estOrigen);
        estVisitados++;
        currentTime = 0;
        cargaActual = 0;
        while (estVisitados < vrp.getCantClientes()) {
            sgteEstado = moacs.siguienteEstado(estActual, solucion, currentTime, cargaActual);
            solucion.add(sgteEstado);
            if (sgteEstado != 0) { //0 representa el deposito, no ir al deposito
                estVisitados++;
                currentTime = Math.max((currentTime + vrp.getDistancia(estActual, sgteEstado)), vrp.getReadyTime(sgteEstado));
                cargaActual += vrp.getDemand(sgteEstado);
                if (taoIJ != 0) {
                    moacs.calcularTaoIJ(estActual, sgteEstado);
                }
            } else { // ir al deposito
                currentTime = 0;
                cargaActual = 0;
                solucion.incrementarCamiones();
            }
            estActual = sgteEstado;
        }
        solucion.add(estOrigen); // volver al deposito
    }

    public boolean condicionParada(int generacion, long start, long end) {
        if (this.criterio == 1) {
            long elapsedTimeMillis = end - start;
            float elapsedTimeSec = elapsedTimeMillis / 1000F;

            if (elapsedTimeSec < this.tiempoTotal) {
                return true;
            }
        } else if (generacion < this.maxIteraciones) {
            return true;
        }
        return false;
    }

    public VRPTW getVRPTW() {
        return this.vrptw;
    }

    public int rand() {
        double aleat = Math.random() * RAND_MAX;
        aleat = Math.floor(aleat);
        return (int) aleat;
    }
}
