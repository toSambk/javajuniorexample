package ru.levelup.junior.dao;


import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.levelup.junior.entities.Account;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;

public class AccountsDAOTest {
    private EntityManagerFactory factory;
    private EntityManager manager;
    private AccountsDAO dao;

    @Before
    public void setup() {
        factory = Persistence.createEntityManagerFactory("TestPersistenceUnit");
        manager = factory.createEntityManager();
        dao = new AccountsDAO(manager);
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
        manager.getTransaction().begin();
        Account account = new Account();
        try {
            account.setLogin("test1");
            account.setPassword("123");
            dao.create(account);
            manager.getTransaction().commit();
        } catch (Exception e) {
            manager.getTransaction().rollback();
            throw e;
        }
        Assert.assertNotNull(manager.find(Account.class, account.getId()));
    }

    @Test
    public void findByLogin() {

        manager.getTransaction().begin();
        Account account = new Account();
        try {
            account.setLogin("test1");
            account.setPassword("123");
            manager.persist(account);
            manager.getTransaction().commit();
        } catch (Exception e) {
            manager.getTransaction().rollback();
            throw e;
        }

        Account found = dao.findByLogin("test1");
        Assert.assertNotNull(found);
        Assert.assertEquals(account.getId(), found.getId());

        try {
            dao.findByLogin("test2");
            Assert.fail("User test2 shouldn't be found");
        } catch(NoResultException expected) {
        }

    }

    @Test
    public void findByLoginAndPassword() {

        manager.getTransaction().begin();
        Account account = new Account();
        try {
            account.setLogin("test1");
            account.setPassword("123");
            manager.persist(account);
            manager.getTransaction().commit();
        } catch (Exception e) {
            manager.getTransaction().rollback();
            throw e;
        }

        Account found = dao.findByLoginAndPassword("test1", "123");
        Assert.assertNotNull(found);
        Assert.assertEquals(account.getId(), found.getId());

        try {
            dao.findByLoginAndPassword("test1", "456");
            Assert.fail("User test1 shouldn't be found");
        } catch(NoResultException expected) {
        }

    }
}
