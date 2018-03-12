package core.util;

import core.conexion.MyBatisConnection;
import core.dao.AuditoriaDAO;
import core.vo.Auditoria;
import org.joda.time.DateTime;

import java.text.ParseException;

public class AuditoriaUtil {

    private AuditoriaDAO auditoriaDAO;
    private Auditoria auditoria;
    private String nombreUsuario = Storage.getUsuario().getNombre();
    private int idUsuario = Storage.getUsuario().getCedula();

    public AuditoriaUtil() {
        auditoria = new Auditoria();
        auditoriaDAO = new AuditoriaDAO(MyBatisConnection.getSqlSessionFactory());
    }

    public void insertar(String accion) {
        try {
            auditoria.setAccion(accion);
            auditoria.setFecha(FechaUtil.getCurrentDate());
            auditoria.setHora(new DateTime().getHourOfDay());
            auditoria.setNombreUsuario(nombreUsuario);
            auditoria.setUsuario_cedula(idUsuario);
            auditoriaDAO.insert(auditoria);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}