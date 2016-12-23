package ca.qc.bergeron.marcantoine.crammeur;

import android.support.test.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

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
    }

    @After
    public void doAfter() {

    }
}
