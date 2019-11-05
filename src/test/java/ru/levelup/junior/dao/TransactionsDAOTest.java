package ru.levelup.junior.dao;


import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import ru.levelup.junior.entities.Account;
import ru.levelup.junior.entities.Transaction;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import java.util.Date;

import static org.junit.Assert.*;


public class TransactionsDAOTest {
    private EntityManagerFactory factory;
    private EntityManager manager;
    private TransactionsDAO dao;

    @Before
    public void setup() {
        factory = Persistence.createEntityManagerFactory("TestPersistenceUnit");
        manager = factory.createEntityManager();
        dao = new TransactionsDAO(manager);
    }

    @After
    public void cleanup() {
        if (manager!=null) {
            manager.close();
        }

        if(factory!=null){
            factory.close();
        }
    }

    @Test
    public void create() {

        Account origin = new Account("from", "123");
        Account receiver = new Account("to", "456");

        Transaction tx = new Transaction(new Date(), 1, origin, receiver);

        manager.getTransaction().begin();
        try {
            manager.persist(origin);
            manager.persist(receiver);
            dao.create(tx);
            manager.getTransaction().commit();
        } catch(Exception e) {
            manager.getTransaction().rollback();
            throw e;
        }

        Assert.assertNotNull(manager.find(Transaction.class, tx.getId()));

    }

    @Test
    public void findByOrigin() {

        Account origin = new Account("from", "123");
        Account receiver = new Account("to", "456");

        Transaction tx = new Transaction(new Date(), 1, origin, receiver);

        manager.getTransaction().begin();
        try {
            manager.persist(origin);
            manager.persist(receiver);
            dao.create(tx);
            manager.getTransaction().commit();
        } catch(Exception e) {
            manager.getTransaction().rollback();
            throw e;
        }

        Assert.assertEquals(tx.getId(), dao.findByOrigin(origin).get(0).getId());
        Assert.assertEquals(0, dao.findByOrigin(receiver).size());

    }

    @Test
    public void findByReceiver() {

        Account origin = new Account("from", "123");
        Account receiver = new Account("to", "456");

        Transaction tx = new Transaction(new Date(), 1, origin, receiver);

        manager.getTransaction().begin();
        try {
            manager.persist(origin);
            manager.persist(receiver);
            dao.create(tx);
            manager.getTransaction().commit();
        } catch(Exception e) {
            manager.getTransaction().rollback();
            throw e;
        }

        Assert.assertEquals(tx.getId(), dao.findByReceiver(receiver).get(0).getId());
        Assert.assertEquals(0, dao.findByReceiver(origin).size());

    }

    @Test
    public void findByAccount() {

        Account origin = new Account("from", "123");
        Account receiver = new Account("to", "456");

        Transaction tx = new Transaction(new Date(), 1, origin, receiver);

        manager.getTransaction().begin();
        try {
            manager.persist(origin);
            manager.persist(receiver);
            dao.create(tx);
            manager.getTransaction().commit();
        } catch(Exception e) {
            manager.getTransaction().rollback();
            throw e;
        }

        Assert.assertEquals(tx.getId(), dao.findByAccount(origin).get(0).getId());
        Assert.assertEquals(tx.getId(), dao.findByAccount(receiver).get(0).getId());

    }
}
