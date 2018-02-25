package core.dao;

import core.vo.Servicios;
import core.vo.SubServicios;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

public class SubServiciosDAO {

    private SqlSessionFactory sqlSessionFactory = null;

    public SubServiciosDAO(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    /**
     * Returns the list of all Auditoria instances from the database.
     *
     * @return the list of all Auditoria instances from the database.
     */
    @SuppressWarnings("unchecked")
    public List<SubServicios> selectAll() {
        List<SubServicios> list = null;
        SqlSession session = sqlSessionFactory.openSession();

        try {
            list = session.selectList("SubServicios.selectAll");
        } finally {
            session.close();
        }
        System.out.println("selectAll() --> " + list);
        return list;

    }

    /**
     * Select instance of SubServicios from the database.
     *
     * @param id the instance to be persisted.
     */
    public SubServicios selectById(int id) {
        SubServicios person = null;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            person = session.selectOne("SubServicios.selectById", id);
        } finally {
            session.close();
        }
        System.out.println("selectById(" + id + ") --> " + person);
        return person;
    }
    public SubServicios selectLastID() {
        SubServicios person;
        try (SqlSession session = sqlSessionFactory.openSession()) {
            person = session.selectOne("SubServicios.selectLastID");
        }
        return person;
    }

    public List<Servicios> selectJoinSub(String newValue) {
        List<Servicios> list;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            list = session.selectList("Servicios.selectJoinSub", newValue);
        } finally {
            session.close();
        }
        System.out.println("selectAll() --> " + list);
        return list;
    }

    public SubServicios selectByNombre(String id) {
        SubServicios person = null;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            person = session.selectOne("SubServicios.selectByNombre", id);
        } finally {
            session.close();
        }
        System.out.println("selectById(" + id + ") --> " + person);
        return person;
    }

    /**
     * Insert an instance of SubServicios into the database.
     *
     * @param usuario the instance to be persisted.
     */
    public int insert(SubServicios usuario) {
        int id = -1;
        SqlSession session = sqlSessionFactory.openSession();

        try {
            id = session.insert("SubServicios.insert", usuario);
        } finally {
            session.commit();
            session.close();
        }
        return id;
    }

    /**
     * Update an instance of SubServicios into the database.
     *
     * @param usuario the instance to be persisted.
     */
    public void update(SubServicios usuario) {
        int id = -1;
        SqlSession session = sqlSessionFactory.openSession();

        try {
            id = session.update("SubServicios.update", usuario);

        } finally {
            session.commit();
            session.close();
        }
        System.out.println("update(" + usuario + ") --> updated");
    }

    public void updateEstado(SubServicios usuario) {
        int id = -1;
        SqlSession session = sqlSessionFactory.openSession();

        try {
            id = session.update("SubServicios.updateEstado", usuario);

        } finally {
            session.commit();
            session.close();
        }
        System.out.println("update(" + usuario + ") --> updated");
    }

    /**
     * Delete an instance of SubServicios from the database.
     *
     * @param id value of the instance to be deleted.
     */
    public void delete(int id) {

        SqlSession session = sqlSessionFactory.openSession();

        try {
            session.delete("SubServicios.delete", id);
        } finally {
            session.commit();
            session.close();
        }
        System.out.println("delete(" + id + ")");

    }
}