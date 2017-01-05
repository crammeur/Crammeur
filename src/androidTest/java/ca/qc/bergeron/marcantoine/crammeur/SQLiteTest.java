package ca.qc.bergeron.marcantoine.crammeur;

import android.support.test.InstrumentationRegistry;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import ca.qc.bergeron.marcantoine.crammeur.exceptions.repository.DeleteException;
import ca.qc.bergeron.marcantoine.crammeur.exceptions.repository.KeyException;
import ca.qc.bergeron.marcantoine.crammeur.model.entity.Company;
import ca.qc.bergeron.marcantoine.crammeur.model.entity.Product;
import ca.qc.bergeron.marcantoine.crammeur.repository.Repository;
import ca.qc.bergeron.marcantoine.crammeur.repository.crud.SQLite.SQLiteProduct;

/**
 * Created by Marc-Antoine on 2016-12-28.
 */
@RunWith(JUnit4.class)
public class SQLiteTest {

    @Test
    public void testSQLiteProduct() throws KeyException, DeleteException {
        Repository repo = new Repository(InstrumentationRegistry.getContext());
        SQLiteProduct sqlp = new SQLiteProduct(repo,InstrumentationRegistry.getContext());
        sqlp.save(new Product("Test",new Company("Test2"),"",0.1,100));
        sqlp.getAll();
    }
}
