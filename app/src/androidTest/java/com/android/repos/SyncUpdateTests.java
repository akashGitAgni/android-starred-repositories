package com.android.repos;

import android.app.Application;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.android.repos.network.SyncRepositories;
import com.android.repos.network.SyncResponse;
import com.android.repos.repolist.ReposActivityViewModel;

import org.junit.Test;
import org.junit.runner.RunWith;

import io.realm.Realm;
import io.realm.RealmConfiguration;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class SyncUpdateTests {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.android.repositories", appContext.getPackageName());
    }

    @Test
    public void test() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        assertNotNull(appContext);

        RealmConfiguration testConfig = new RealmConfiguration.Builder().inMemory().name
                ("test-realm").build();
        Realm testRealm = Realm.getInstance(testConfig);
        assertNotNull(testRealm);
        SyncRepositories syncRepositories = new SyncRepositories();
        assertNotNull(syncRepositories);

        String response = syncRepositories.updateDatabase(appContext);

        assert (response.equals(SyncResponse.UPDATED));
    }
}
