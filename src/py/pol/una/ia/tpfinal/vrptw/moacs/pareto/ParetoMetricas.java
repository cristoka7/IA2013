package py.pol.una.ia.tpfinal.vrptw.MOACS.pareto;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Cristian Aceval Alvarez <cristian.aceval@gmail.com>
 * @author Deysi Leguizamon <deysi.leg@gmail.com>
 * @author Clara Lopez <clarita.diana@gmail.com>
 * @author Santiago Encina <cheito.en@gmail.com>
 */
public class ParetoMetricas {
    private List<SolucionMetricas> frentePareto = new ArrayList<SolucionMetricas>();
    
    public boolean agregarNoDominado(SolucionMetricas nuevo) {
        for (SolucionMetricas actual : frentePareto) {
            if(actual.getF1() <= nuevo.getF1() && actual.getF2() <= nuevo.getF2()) {
                return false;
            }
        }
        frentePareto.add(nuevo);
        return true;
    }
    
    public void eliminarDominados(SolucionMetricas nuevo) {
        Iterator<SolucionMetricas> iterator = frentePareto.iterator();
        while (iterator.hasNext()) {
            SolucionMetricas actual = iterator.next();
            if((actual.getF1() > nuevo.getF1() && actual.getF2() >= nuevo.getF2()) || (actual.getF1() >= nuevo.getF1() && actual.getF2() > nuevo.getF2())) {
                iterator.remove();
            }
        }
    }

    public List<SolucionMetricas> getFrentePareto() {
        return frentePareto;
    }
}
