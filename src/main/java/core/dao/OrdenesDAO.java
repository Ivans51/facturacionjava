package core.dao;

import core.vo.Cliente;
import core.vo.Ordenes;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

public class OrdenesDAO {

    private SqlSessionFactory sqlSessionFactory = null;

    public OrdenesDAO(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    /**
     * Returns the list of all Auditoria instances from the database.
     *
     * @return the list of all Auditoria instances from the database.
     */
    @SuppressWarnings("unchecked")
    public List<Ordenes> selectAll() {
        List<Ordenes> list = null;
        SqlSession session = sqlSessionFactory.openSession();

        try {
            list = session.selectList("Ordenes.selectAll");
        } finally {
            session.close();
        }
        System.out.println("selectAll() --> " + list);
        return list;

    }

    /**
     * Select instance of Ordenes from the database.
     *
     * @param id the instance to be persisted.
     */
    public Ordenes selectById(int id) {
        Ordenes person = null;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            person = session.selectOne("Ordenes.selectById", id);
        } finally {
            session.close();
        }
        System.out.println("selectById(" + id + ") --> " + person);
        return person;
    }

    /**
     * Insert an instance of Ordenes into the database.
     *
     * @param usuario the instance to be persisted.
     */
    public int insert(Ordenes usuario) {
        int id = -1;
        SqlSession session = sqlSessionFactory.openSession();

        try {
            id = session.insert("Ordenes.insert", usuario);
        } finally {
            session.commit();
            session.close();
        }
        return id;
    }

    /**
     * Update an instance of Ordenes into the database.
     *
     * @param usuario the instance to be persisted.
     */
    public void update(Ordenes usuario) {
        int id = -1;
        SqlSession session = sqlSessionFactory.openSession();

        try {
            id = session.update("Ordenes.update", usuario);

        } finally {
            session.commit();
            session.close();
        }
        System.out.println("update(" + usuario + ") --> updated");
    }

    /**
     * Delete an instance of Ordenes from the database.
     *
     * @param id value of the instance to be deleted.
     */
    public void delete(int id) {

        SqlSession session = sqlSessionFactory.openSession();

        try {
            session.delete("Ordenes.delete", id);
        } finally {
            session.commit();
            session.close();
        }
        System.out.println("delete(" + id + ")");

    }
}