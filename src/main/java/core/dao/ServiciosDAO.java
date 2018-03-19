package core.dao;

import core.vo.Servicios;
import core.vo.Servicios;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

public class ServiciosDAO {

    private SqlSessionFactory sqlSessionFactory = null;

    public ServiciosDAO(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    /**
     * Returns the list of all Auditoria instances from the database.
     *
     * @return the list of all Auditoria instances from the database.
     */
    @SuppressWarnings("unchecked")
    public List<Servicios> selectAll() {
        List<Servicios> list = null;
        SqlSession session = sqlSessionFactory.openSession();

        try {
            list = session.selectList("Servicios.selectAll");
        } finally {
            session.close();
        }
        System.out.println("selectAll() --> " + list);
        return list;
    }
    public List<Servicios> selectAllFilter() {
        List<Servicios> list = null;
        SqlSession session = sqlSessionFactory.openSession();

        try {
            list = session.selectList("Servicios.selectAllFilter");
        } finally {
            session.close();
        }
        System.out.println("selectAll() --> " + list);
        return list;
    }

    public Servicios selectAllNombres(String newValue) {
        Servicios list = null;
        SqlSession session = sqlSessionFactory.openSession();

        try {
            list = session.selectOne("Servicios.selectAllNombres", newValue);
        } finally {
            session.close();
        }
        System.out.println("selectAll() --> " + list);
        return list;

    }

    public Servicios selectLastID() {
        Servicios list;
        try (SqlSession session = sqlSessionFactory.openSession()) {
            list = session.selectOne("Servicios.selectLastID");
        }
        return list;
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

    /**
     * Select instance of Servicios from the database.
     *
     * @param id the instance to be persisted.
     */
    public Servicios selectById(int id) {
        Servicios person = null;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            person = session.selectOne("Servicios.selectById", id);
        } finally {
            session.close();
        }
        System.out.println("selectById(" + id + ") --> " + person);
        return person;
    }

    public Servicios selectByNombre(String id) {
        Servicios person = null;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            person = session.selectOne("Servicios.selectByNombre", id);
        } finally {
            session.close();
        }
        System.out.println("selectById(" + id + ") --> " + person);
        return person;
    }

    public List<Servicios> selectNombres() {
        List<Servicios> person = null;
        try (SqlSession session = sqlSessionFactory.openSession()) {
            person = session.selectList("Servicios.selectByNombres");
        }
        return person;
    }
    public List<Servicios> selectByDia(Servicios usuario) {
        List<Servicios> list = null;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            list = session.selectList("Servicios.selectByDia", usuario);
        } finally {
            session.close();
        }
        return list;
    }
    public List<Servicios> selectByRango(Servicios usuario) {
        List<Servicios> list = null;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            list = session.selectList("Servicios.selectByRango", usuario);
        } finally {
            session.close();
        }
        return list;
    }
    public List<Servicios> selectByMes(Servicios usuario) {
        List<Servicios> list = null;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            list = session.selectList("Servicios.selectByMes", usuario);
        } finally {
            session.close();
        }
        return list;
    }

    /**
     * Insert an instance of Servicios into the database.
     *
     * @param usuario the instance to be persisted.
     */
    public int insert(Servicios usuario) {
        int id = -1;
        SqlSession session = sqlSessionFactory.openSession();

        try {
            id = session.insert("Servicios.insert", usuario);
        } finally {
            session.commit();
            session.close();
        }
        return id;
    }

    /**
     * Update an instance of Servicios into the database.
     *
     * @param usuario the instance to be persisted.
     */
    public void updateEstado(Servicios usuario) {
        int id = -1;
        SqlSession session = sqlSessionFactory.openSession();

        try {
            id = session.update("Servicios.updateEstado", usuario);

        } finally {
            session.commit();
            session.close();
        }
    }

    public void update(Servicios usuario) {
        int id = -1;
        SqlSession session = sqlSessionFactory.openSession();

        try {
            id = session.update("Servicios.update", usuario);

        } finally {
            session.commit();
            session.close();
        }
        System.out.println("update(" + usuario + ") --> updated");
    }

    /**
     * Delete an instance of Servicios from the database.
     *
     * @param id value of the instance to be deleted.
     */
    public void delete(int id) {

        SqlSession session = sqlSessionFactory.openSession();

        try {
            session.delete("Servicios.delete", id);
        } finally {
            session.commit();
            session.close();
        }
        System.out.println("delete(" + id + ")");

    }
}