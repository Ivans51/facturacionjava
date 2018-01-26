package core.util;

import core.conexion.MyBatisConnection;
import core.dao.UsuarioDAO;
import core.vo.Usuario;

public class LoginUser {

    public Usuario iniciarSession(String nombreUsuario, String clave) throws Myexception {
        UsuarioDAO usuarioDAO = new UsuarioDAO(MyBatisConnection.getSqlSessionFactory());
        Usuario usuario = new Usuario();
        usuario.setNombreUsuario(nombreUsuario);
        usuario.setClave(clave);
        usuario = usuarioDAO.login(usuario);
        if (usuario == null)
            throw new Myexception("No existe el usuario");
        else
            return usuario;
    }
}