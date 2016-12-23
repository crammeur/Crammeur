package ca.qc.bergeron.marcantoine.crammeur;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import ca.qc.bergeron.marcantoine.crammeur.exceptions.repository.DeleteException;
import ca.qc.bergeron.marcantoine.crammeur.exceptions.repository.KeyException;
import ca.qc.bergeron.marcantoine.crammeur.model.entity.Company;
import ca.qc.bergeron.marcantoine.crammeur.model.entity.Product;
import ca.qc.bergeron.marcantoine.crammeur.repository.Repository;
import ca.qc.bergeron.marcantoine.crammeur.repository.i.DataFramework;

/**
 * Created by Marc-Antoine on 2016-12-05.
 */
@RunWith(AndroidJUnit4.class)
public class RepositoryTest {

    private Repository repository;

    @Before
    public void doBefore() {
        repository = new Repository(InstrumentationRegistry.getContext().getFilesDir());
    }

    @Test
    public void testRepositorySave() throws KeyException, DeleteException, ClassNotFoundException {
        Integer key = (Integer) repository.save(new Company(1, "Test"));
        Company company = (Company) repository.getByKey(Company.class, key);
        Assert.assertTrue(key != company.getId());
        Assert.assertTrue(key.equals(company.getId()));
    }

    @Test
    public void testRepositorySave2() throws KeyException, ClassNotFoundException, DeleteException {
        Integer key = (Integer) repository.save(new Company(2, "Test"));
        Assert.assertTrue(key == 2);
        Company company = (Company) repository.getByKey(Company.class, key);
        company.setName("Test2");
        Assert.assertTrue(company.getId() == repository.save(company));
        Assert.assertTrue(company.getName() != ((Company) repository.getByKey(Company.class, key)).getName());
        Assert.assertTrue(company.getName().equals(((Company) repository.getByKey(Company.class, key)).getName()));
    }

    @Test
    public void testRepositoryDelete() throws KeyException, DeleteException, ClassNotFoundException {
        Company c = new Company(1, "Test");
        repository.save(c);
        repository.delete(c);
        DataFramework<Company, Integer> df = (DataFramework<Company, Integer>) repository.getDataFramework(Company.class);
        Assert.assertTrue(!df.contains(c));
    }

    @Test
    public void testRepositoryDelete2() throws KeyException, DeleteException, ClassNotFoundException {
        Product p = new Product("Test", new Company(1, "Test"), "", 1.0, 100);
        repository.save(p);
        repository.delete(p.Company);
        DataFramework<Product, Integer> df = (DataFramework<Product, Integer>) repository.getDataFramework(Product.class);
        Assert.assertTrue(!df.contains(p));
    }

    @Test
    public void testRepositoryDelete3() throws KeyException, DeleteException, ClassNotFoundException {
        Product p = new Product("Test", new Company(1, "Test"), "", 1.0, 100);
        repository.save(p);
        repository.delete(p);
        DataFramework<Company, Integer> df = (DataFramework<Company, Integer>) repository.getDataFramework(Company.class);
        Assert.assertTrue(df.contains(p.Company));
    }
}