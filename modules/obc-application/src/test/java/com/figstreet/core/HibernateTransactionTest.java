package com.figstreet.core;

import org.hibernate.Session;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.persistence.EntityManager;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class HibernateTransactionTest {

    @BeforeClass
    public static void initialize() throws MalformedURLException {
        Path h2DBConfig = Paths.get("src/test/resources/hibernate.test.xml");
        Path logConfig = Paths.get("src/test/resources/log4j.test.xml");
        Logging.initialize(logConfig.toAbsolutePath().toString());
        HibernateConfiguration.initialize(h2DBConfig.toUri().toURL());
    }

    @Test
    public void testHibernateTransactionopenOnceInAThread() throws SQLException {
        HibernateTransaction trans = HibernateTransaction.open();
        assertNotNull(trans);
        trans.close();
    }

    @Test
    public void testHibernateTransactionopenTwiceInAThread() throws SQLException {
        HibernateTransaction firstTrans = HibernateTransaction.open();
        Session firstSession = firstTrans.getSession();
        HibernateTransaction otherTrans = HibernateTransaction.open();
        assertEquals(firstTrans, otherTrans);
        assertEquals(firstSession, otherTrans.getSession());
        firstTrans.close();
        otherTrans.close();
    }

    @Test
    public void testHibernateTransactionopenTwiceClosingOneBeforeOpeningTheOtherInAThread() throws SQLException {
        HibernateTransaction firstTrans = HibernateTransaction.open();
        Session firstSession = firstTrans.getSession();
        firstTrans.close();
        HibernateTransaction otherTrans = HibernateTransaction.open();
        assertNotEquals(firstSession, otherTrans.getSession());
        firstTrans.close();
        otherTrans.close();
    }

    @Test
    public void testHibernateTransactionopenThreeTimesClosingTheSecondBeforeOpeningTheThirdInAThread() throws SQLException {
        HibernateTransaction firstTrans = HibernateTransaction.open();
        Session firstSession = firstTrans.getSession();
        HibernateTransaction secondTrans = HibernateTransaction.open();
        secondTrans.close();
        HibernateTransaction thirdTrans = HibernateTransaction.open();
        assertEquals(firstTrans, thirdTrans);
        assertEquals(firstSession, thirdTrans.getSession());
        firstTrans.close();
        thirdTrans.close();
    }

    @Test
    public void testHibernateTransactionopenThreeTimesClosingAllBeforeOpeningTheThirdInAThread() throws SQLException {
        HibernateTransaction firstTrans = HibernateTransaction.open();
        Session firstSession = firstTrans.getSession();
        HibernateTransaction secondTrans = HibernateTransaction.open();
        secondTrans.close();
        firstTrans.close();
        HibernateTransaction thirdTrans = HibernateTransaction.open();
        assertNotEquals(firstSession.getSession().hashCode(), thirdTrans.getSession().hashCode());
        thirdTrans.close();
    }

    @Test
    public void testGetEntityManager() throws SQLException {
        HibernateTransaction firstTrans = HibernateTransaction.open();
        EntityManager entityManager = firstTrans.getEntityManager();
        assertNotNull(entityManager);
        firstTrans.close();
    }

    //If you close a transaction with closeNoException a exception is not thrown out
    @Test
    public void testCloseNotException() throws SQLException {
        HibernateTransaction firstTrans = HibernateTransaction.open();
        firstTrans.getSession().close();
        firstTrans.closeNoException();
    }

    //If you close a transaction with its session closed it throws an exception
    @Test
    public void testCloseExceptionThrows() throws SQLException {
        try {
            HibernateTransaction firstTrans = HibernateTransaction.open();
            firstTrans.getSession().close();
            firstTrans.close();
        } catch (Exception e) {
            Assert.assertTrue(true);
            return;
        }
        Assert.assertTrue(false);
    }

    //This is to test randomString throws an exception when length > 130
    @Test
    public void testRandomStringThrowingException() {
        try {
            HibernateTransaction.randomString(131);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue(true);
            return;
        }
        Assert.assertTrue(false);
    }

    //This is to test you can open a transaction even if there are another in commit state
    @Test
    public void testOpenCommitedHibernateTransacion() throws SQLException {
        HibernateTransaction firstTrans = HibernateTransaction.open();
        firstTrans.commit();
        HibernateTransaction secondTrans = HibernateTransaction.open();
        firstTrans.close();
        secondTrans.close();
    }

    //This is to test you can close a transaction in rollback state
    @Test
    public void testRollback() throws SQLException {
        HibernateTransaction firstTrans = HibernateTransaction.open();
        firstTrans.rollback();
        firstTrans.close();
    }

    //This is to test you can close a transaction in commit state
    @Test
    public void testCloseCommitedTransaction() throws SQLException {
        HibernateTransaction firstTrans = HibernateTransaction.open();
        firstTrans.commit();
        firstTrans.close();
    }

    //This is to test the current contract is not broken. It is valid to try to close a closed connection
    @Test
    public void testCloseClosedTransaction() throws SQLException {
        HibernateTransaction firstTrans = HibernateTransaction.open();
        firstTrans.close();
        firstTrans.close();
    }


}
