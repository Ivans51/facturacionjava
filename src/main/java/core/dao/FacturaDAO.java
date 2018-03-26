package core.dao;

import core.vo.Factura;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

public class FacturaDAO {

    private SqlSessionFactory sqlSessionFactory = null;

    public FacturaDAO(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    /**
     * Returns the list of all Auditoria instances from the database.
     *
     * @return the list of all Auditoria instances from the database.
     */
    @SuppressWarnings("unchecked")
    public List<Factura> selectAll() {
        List<Factura> list = null;
        SqlSession session = sqlSessionFactory.openSession();

        try {
            list = session.selectList("Factura.selectAll");
        } finally {
            session.close();
        }
        System.out.println("selectAll() --> " + list);
        return list;

    }

    public Factura selectLastID() {
        Factura person = null;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            person = session.selectOne("Factura.selectLastID");
        } finally {
            session.close();
        }
        return person;
    }

    public List<Factura> joinFacturaCliente() {
        List<Factura> person = null;
        try (SqlSession session = sqlSessionFactory.openSession()) {
            person = session.selectList("Factura.joinFacturaCliente");
        }
        return person;
    }

    /**
     * Select instance of Factura from the database.
     *
     * @param id the instance to be persisted.
     */
    public Factura selectById(int id) {
        Factura person = null;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            person = session.selectOne("Factura.selectById", id);
        } finally {
            session.close();
        }
        System.out.println("selectById(" + id + ") --> " + person);
        return person;
    }
    public List<Factura> selectByDia(Factura usuario) {
        List<Factura> list = null;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            list = session.selectList("Factura.selectByDia", usuario);
        } finally {
            session.close();
        }
        return list;
    }
    public List<Factura> selectByRango(Factura usuario) {
        List<Factura> list = null;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            list = session.selectList("Factura.selectByRango", usuario);
        } finally {
            session.close();
        }
        return list;
    }
    public List<Factura> selectByMes(Factura usuario) {
        List<Factura> list = null;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            list = session.selectList("Factura.selectByMes", usuario);
        } finally {
            session.close();
        }
        return list;
    }

    /**
     * Insert an instance of Factura into the database.
     *
     * @param usuario the instance to be persisted.
     */
    public int insert(Factura usuario) {
        int id = -1;
        SqlSession session = sqlSessionFactory.openSession();

        try {
            id = session.insert("Factura.insert", usuario);
        } finally {
            session.commit();
            session.close();
        }
        return id;
    }

    /**
     * Update an instance of Factura into the database.
     *
     * @param usuario the instance to be persisted.
     */
    public void update(Factura usuario) {
        int id = -1;
        SqlSession session = sqlSessionFactory.openSession();

        try {
            id = session.update("Factura.update", usuario);

        } finally {
            session.commit();
            session.close();
        }
        System.out.println("update(" + usuario + ") --> updated");
    }

    /**
     * Delete an instance of Factura from the database.
     *
     * @param id value of the instance to be deleted.
     */
    public void delete(int id) {

        SqlSession session = sqlSessionFactory.openSession();

        try {
            session.delete("Factura.delete", id);
        } finally {
            session.commit();
            session.close();
        }
        System.out.println("delete(" + id + ")");

    }
}