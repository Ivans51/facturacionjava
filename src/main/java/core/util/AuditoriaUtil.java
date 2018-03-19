package core.util;

import core.conexion.MyBatisConnection;
import core.dao.AuditoriaDAO;
import core.vo.Auditoria;
import org.joda.time.DateTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
            String hourOfDay = new DateTime().getHourOfDay() + ":" + new DateTime().getMinuteOfHour();
            SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
            SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
            Date _24HourDt = _24HourSDF.parse(hourOfDay);
            String format = _12HourSDF.format(_24HourDt);
            auditoria.setHora(format);
            auditoria.setNombreUsuario(nombreUsuario);
            auditoria.setUsuario_cedula(idUsuario);
            auditoriaDAO.insert(auditoria);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}