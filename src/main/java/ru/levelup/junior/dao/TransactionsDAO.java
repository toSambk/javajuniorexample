package ru.levelup.junior.dao;

import ru.levelup.junior.entities.Account;
import ru.levelup.junior.entities.Transaction;

import javax.persistence.EntityManager;
import java.util.List;

public class TransactionsDAO {

    private final EntityManager manager;

    public TransactionsDAO(EntityManager manager) {
        this.manager = manager;
    }

    public void create(Transaction transaction) {
        if (transaction.getOrigin() == transaction.getReceiver()) {
            throw new IllegalArgumentException("Transaction receiver and origin accounts are the same");
        }
        if (transaction.getAmount() <= 0) {
            throw new IllegalArgumentException("Transaction amount is negative");
        }
        manager.persist(transaction);
    }

    public List<Transaction> findByOrigin(Account origin) {
        return manager.createQuery("from Transaction where origin.id = :p", Transaction.class)
                .setParameter("p", origin.getId())
                .getResultList();
    }

    public List<Transaction> findByReceiver(Account receiver) {
        return manager.createQuery("from Transaction where receiver.id = :p", Transaction.class)
                .setParameter("p", receiver.getId())
                .getResultList();
    }

    public List<Transaction> findByAccount(Account account) {
        return manager.createQuery("from Transaction where origin.id = :p or receiver.id = :p", Transaction.class)
                .setParameter("p", account.getId())
                .getResultList();
    }

}
