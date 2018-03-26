package core.util;

import core.conexion.MyBatisConnection;
import core.dao.UsuarioDAO;
import core.vo.Usuario;

public class LoginUser {

    public Usuario iniciarSession(String nombreUsuario, String clave) throws Myexception {
        UsuarioDAO usuarioDAO = new UsuarioDAO(MyBatisConnection.getSqlSessionFactory());
        Usuario usuario = new Usuario();
        usuario.setNombre(nombreUsuario);
        usuario.setClave(clave);
        Usuario userExist = usuarioDAO.userExist(usuario);
        Usuario user = usuarioDAO.login(usuario);
        if (user == null)
            throw new Myexception(userExist == null ? "No existe el usuario" : "Contraseña incorrecta");
        else
            return user;
    }
}