package core.util;

import javafx.event.ActionEvent;
import org.joda.time.DateTime;

public class PrintSelection {
    private String comboReportes;
    private String[] auditoriasA;
    private String[] serviciosA;
    private String[] clientesA;
    private String[] facturasA;
    private String[] usuariosA;

    public PrintSelection(String selectedCombo, String[] auditoriasA, String[] serviciosA, String[] clientesA, String[] facturasA, String[] usuariosA) {
        this.comboReportes = selectedCombo;
        this.auditoriasA = auditoriasA;
        this.serviciosA = serviciosA;
        this.clientesA = clientesA;
        this.facturasA = facturasA;
        this.usuariosA = usuariosA;
    }

    public void printAction(Print print) {
        DateTime d = new DateTime();
        String fecha = d.getDayOfMonth() + "-" + d.getMonthOfYear() + "-" + d.getYear();
        switch (comboReportes) {
            case "Auditoria":
                String aut = "Auditoria-";
                print.action(auditoriasA, aut + fecha + ".pdf", aut);
                break;
            case "Servicios":
                String ser = "Servicios-";
                print.action(serviciosA, ser + fecha + ".pdf", ser);
                break;
            case "Cliente":
                String cli = "Clientes-";
                print.action(clientesA, cli + fecha + ".pdf", cli);
                break;
            case "Factura":
                String fac = "Facturas-";
                print.action(facturasA, fac + fecha + ".pdf", fac);
                break;
            case "Usuario":
                String use = "Usuario-";
                print.action(usuariosA, use + fecha + ".pdf", use);
                break;
            case "Graficos":
                String gra = "Gráfico-";
                print.action(usuariosA, gra + fecha + ".pdf", gra);
                break;
        }
    }

    public interface Print {
        void action(String[] strings, String path, String name);
    }
}
