package py.pol.una.ia.tpfinal.vrptw.moacs;

/**
 *
 * @author Cristian Aceval Alvarez <cristian.aceval@gmail.com>
 * @author Deysi Leguizamon <deysi.leg@gmail.com>
 * @author Clara Lopez <clarita.diana@gmail.com>
 * @author Santiago Encina <cheito.en@gmail.com>
 */
public class Cliente {

    // identificador del cliente
    private int id;
    // Coodenadas X
    private double x;
    // Coodenadas Y
    private double y;
    // Tiempo que se requiere para atender al cliente
    private double serviceTime;
    // Cantidad del producto demandado
    private double demand;
    // Tiempo inicial en el cual el cliente puede recibir los productos
    private double readyTime;
    // Tiempo final hasta el cual el cliente puede recibir los productos
    private double dueTime;

    public Cliente(int id, double x, double y, double serviceTime, double demand, double readyTime, double dueTime) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.serviceTime = serviceTime;
        this.demand = demand;
        this.readyTime = readyTime;
        this.dueTime = dueTime;
    }

    public int getId() {
        return id;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getServiceTime() {
        return serviceTime;
    }

    public double getDemand() {
        return demand;
    }

    public double getReadyTime() {
        return readyTime;
    }

    public double getDueTime() {
        return dueTime;
    }
}
