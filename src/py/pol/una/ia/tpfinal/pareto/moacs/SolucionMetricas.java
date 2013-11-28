package py.pol.una.ia.tpfinal.pareto.moacs;

/**
 *
 * @author Cristian Aceval Alvarez <cristian.aceval@gmail.com>
 * @author Deysi Leguizamon <deysi.leg@gmail.com>
 * @author Clara Lopez <clarita.diana@gmail.com>
 * @author Santiago Encina <cheito.en@gmail.com>
 */
public class SolucionMetricas {
    private int f1;
    private double f2;

    public SolucionMetricas(int f1, double f2) {
        this.f1 = f1;
        this.f2 = f2;
    }

    public int getF1() {
        return f1;
    }

    public double getF2() {
        return f2;
    }

    @Override
    public String toString() {
        return "{" + "f1=" + f1 + ", f2=" + f2 + '}';
    }
    
    
}
