package core.dao;

import core.util.Myexception;
import core.vo.Factura;
import core.vo.Servicios;
import core.vo.Usuario;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

public class UsuarioDAO {

    private SqlSessionFactory sqlSessionFactory = null;

    public UsuarioDAO(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    /**
     * Returns the list of all Auditoria instances from the database.
     *
     * @return the list of all Auditoria instances from the database.
     */
    @SuppressWarnings("unchecked")
    public List<Usuario> selectAll() {
        List<Usuario> list = null;
        SqlSession session = sqlSessionFactory.openSession();

        try {
            list = session.selectList("Usuario.selectAll");
        } finally {
            session.close();
        }
        System.out.println("selectAll() --> " + list);
        return list;
    }

    public void selectStart() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.selectOne("Usuario.selectStart", 0);
        }
        System.out.println("First Connect");
    }

    /**
     * Select instance of Usuario from the database.
     *
     * @param id the instance to be persisted.
     */
    public Usuario selectById(int id) {
        Usuario person = null;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            person = session.selectOne("Usuario.selectById", id);
        } finally {
            session.close();
        }
        System.out.println("selectById(" + id + ") --> " + person);
        return person;
    }

    public Usuario selectByCorreo(String correo) {
        Usuario person = null;
        try (SqlSession session = sqlSessionFactory.openSession()) {
            person = session.selectOne("Usuario.selectByCorreo", correo);
        }
        return person;
    }

    public Usuario selectByClaveCorre(Usuario usuario) {
        Usuario person = null;
        try (SqlSession session = sqlSessionFactory.openSession()) {
            person = session.selectOne("Usuario.selectByClaveCorre", usuario);
        }
        return person;
    }

    public Usuario login(Usuario usuario) {
        Usuario person = null;
        try (SqlSession session = sqlSessionFactory.openSession()) {
            person = session.selectOne("Usuario.login", usuario);
        }
        return person;
    }

    public Usuario userExist(Usuario usuario){
        Usuario person = null;
        try (SqlSession session = sqlSessionFactory.openSession()) {
            person = session.selectOne("Usuario.userExist", usuario);
        }
        return person;
    }
    public List<Usuario> selectByDia(Usuario usuario) {
        List<Usuario> list = null;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            list = session.selectList("Usuario.selectByDia", usuario);
        } finally {
            session.close();
        }
        return list;
    }
    public List<Usuario> selectByRango(Usuario usuario) {
        List<Usuario> list = null;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            list = session.selectList("Usuario.selectByRango", usuario);
        } finally {
            session.close();
        }
        return list;
    }
    public List<Usuario> selectByMes(Usuario usuario) {
        List<Usuario> list = null;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            list = session.selectList("Usuario.selectByMes", usuario);
        } finally {
            session.close();
        }
        return list;
    }

    public void updateEstado(Usuario usuario) {
        int id = -1;
        SqlSession session = sqlSessionFactory.openSession();

        try {
            id = session.update("Usuario.updateEstado", usuario);

        } finally {
            session.commit();
            session.close();
        }
    }

    public void updateClave(Usuario usuario) {
        int id = -1;
        SqlSession session = sqlSessionFactory.openSession();

        try {
            id = session.update("Usuario.updateClave", usuario);

        } finally {
            session.commit();
            session.close();
        }
    }
    /**
     * Insert an instance of Usuario into the database.
     *
     * @param usuario the instance to be persisted.
     */
    public int insert(Usuario usuario) {
        int id = -1;
        SqlSession session = sqlSessionFactory.openSession();

        try {
            id = session.insert("Usuario.insert", usuario);
        } finally {
            session.commit();
            session.close();
        }
        System.out.println("insert(" + usuario + ") --> " + usuario.getCedula());
        return id;
    }

    /**
     * Update an instance of Usuario into the database.
     *
     * @param usuario the instance to be persisted.
     */
    public void update(Usuario usuario) {
        int id = -1;
        SqlSession session = sqlSessionFactory.openSession();

        try {
            id = session.update("Usuario.updateDatosUsuarios", usuario);

        } finally {
            session.commit();
            session.close();
        }
        System.out.println("update(" + usuario + ") --> updated");
    }

    /**
     * Delete an instance of Usuario from the database.
     *
     * @param id value of the instance to be deleted.
     */
    public void delete(int id) {

        SqlSession session = sqlSessionFactory.openSession();

        try {
            session.delete("Usuario.delete", id);
        } finally {
            session.commit();
            session.close();
        }
        System.out.println("delete(" + id + ")");

    }
}