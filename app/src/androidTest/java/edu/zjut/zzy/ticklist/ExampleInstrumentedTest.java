package edu.zjut.zzy.ticklist;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;

import androidx.datastore.core.DataStore;
import androidx.datastore.preferences.core.PreferenceDataStore;
import androidx.datastore.rxjava2.RxSharedPreferencesMigrationBuilder;
import androidx.datastore.rxjava3.RxDataStore;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("edu.zjut.zzy.ticklist", appContext.getPackageName());
    }
}