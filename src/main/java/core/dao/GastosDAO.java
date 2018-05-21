package core.dao;

import core.vo.Gastos;
import core.vo.Servicios;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

public class GastosDAO {

    private SqlSessionFactory sqlSessionFactory = null;

    public GastosDAO(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    /**
     * Returns the list of all Auditoria instances from the database.
     *
     * @return the list of all Auditoria instances from the database.
     */
    @SuppressWarnings("unchecked")
    public List<Gastos> selectAll() {
        List<Gastos> list = null;
        SqlSession session = sqlSessionFactory.openSession();

        try {
            list = session.selectList("Gastos.selectAll");
        } finally {
            session.close();
        }
        System.out.println("selectAll() --> " + list);
        return list;

    }

    public Gastos selectLastID() {
        Gastos list;
        try (SqlSession session = sqlSessionFactory.openSession()) {
            list = session.selectOne("Gastos.selectLastID");
        }
        return list;
    }

    /**
     * Select instance of Gastos from the database.
     *
     * @param id the instance to be persisted.
     */
    public Gastos selectById(int id) {
        Gastos person = null;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            person = session.selectOne("Gastos.selectById", id);
        } finally {
            session.close();
        }
        System.out.println("selectById(" + id + ") --> " + person);
        return person;
    }
    public List<Gastos> selectByDia(Gastos usuario) {
        List<Gastos> list = null;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            list = session.selectList("Gastos.selectByDia", usuario);
        } finally {
            session.close();
        }
        return list;
    }
    public List<Gastos> selectByRango(Gastos usuario) {
        List<Gastos> list = null;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            list = session.selectList("Gastos.selectByRango", usuario);
        } finally {
            session.close();
        }
        return list;
    }
    public List<Gastos> selectByMes(Gastos usuario) {
        List<Gastos> list = null;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            list = session.selectList("Gastos.selectByMes", usuario);
        } finally {
            session.close();
        }
        return list;
    }

    public void updateEstado(Gastos usuario) {
        int id = -1;
        SqlSession session = sqlSessionFactory.openSession();

        try {
            id = session.update("Gastos.updateEstado", usuario);

        } finally {
            session.commit();
            session.close();
        }
    }

    /**
     * Insert an instance of Gastos into the database.
     *
     * @param usuario the instance to be persisted.
     */
    public int insert(Gastos usuario) {
        int id = -1;
        SqlSession session = sqlSessionFactory.openSession();

        try {
            id = session.insert("Gastos.insert", usuario);
        } finally {
            session.commit();
            session.close();
        }
        return id;
    }

    /**
     * Update an instance of Gastos into the database.
     *
     * @param usuario the instance to be persisted.
     */
    public void update(Gastos usuario) {
        int id = -1;
        SqlSession session = sqlSessionFactory.openSession();

        try {
            id = session.update("Gastos.update", usuario);

        } finally {
            session.commit();
            session.close();
        }
        System.out.println("update(" + usuario + ") --> updated");
    }

    /**
     * Delete an instance of Gastos from the database.
     *
     * @param id value of the instance to be deleted.
     */
    public void delete(int id) {

        SqlSession session = sqlSessionFactory.openSession();

        try {
            session.delete("Gastos.delete", id);
        } finally {
            session.commit();
            session.close();
        }
        System.out.println("delete(" + id + ")");

    }
}