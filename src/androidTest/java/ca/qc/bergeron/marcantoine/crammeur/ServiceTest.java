package ca.qc.bergeron.marcantoine.crammeur;

import android.support.test.InstrumentationRegistry;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;

import ca.qc.bergeron.marcantoine.crammeur.exceptions.repository.KeyException;
import ca.qc.bergeron.marcantoine.crammeur.model.entity.Company;
import ca.qc.bergeron.marcantoine.crammeur.model.entity.Product;
import ca.qc.bergeron.marcantoine.crammeur.service.Service;

/**
 * Created by Marc-Antoine on 2016-12-23.
 */
@RunWith(JUnit4.class)
public class ServiceTest {

    private Service service;

    @Before
    public void doBefore(){
        service = new Service(InstrumentationRegistry.getContext());
        Product p = new Product("Test",new Company("Test"),"",0.1,100);
        for (int i=0; i<200; i++) {
            try {
                service.Products.save(p);
                p.setId(null);
                p.Company.setId(null);
            } catch (KeyException e) {
                e.printStackTrace();
                Assert.fail();
            }
        }
    }

    @Test(timeout = 1000)
    public void testGetAll() {
        List pp = (List) service.Products.getAll();
        Assert.assertTrue(pp.size() == 100);
    }

    @Test
    public void testGetAll2() {
        service.Products.setGetAll(50);
        List pp = (List) service.Products.getAll();
        Assert.assertTrue(pp.size() == 50);
    }

    @After
    public void doAfter() {

    }
}
