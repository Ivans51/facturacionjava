package core.util;

import core.conexion.MyBatisConnection;
import core.dao.AuditoriaDAO;
import core.vo.Auditoria;

import java.text.ParseException;

public class AuditoriaUtil {

    private AuditoriaDAO auditoriaDAO;
    private Auditoria auditoria;
    private String nombreUsuario = Storage.getUsuario().getNombre();
    private int idUsuario = Storage.getUsuario().getCedula();

    public AuditoriaUtil(String nombreUsuario, int idUsuario) {
        this.nombreUsuario = nombreUsuario;
        this.idUsuario = idUsuario;
        auditoria = new Auditoria();
        auditoriaDAO = new AuditoriaDAO(MyBatisConnection.getSqlSessionFactory());
    }

    public void insertar(String accion) {
        try {
            auditoria.setAccion(accion);
            auditoria.setFecha(FechaUtil.getCurrentDate());
            auditoria.setHora(FechaUtil.getHourMinutes());
            auditoria.setNombreUsuario(nombreUsuario);
            auditoria.setFK_idUsuario(idUsuario);
            auditoriaDAO.insert(auditoria);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}