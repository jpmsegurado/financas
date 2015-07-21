package com.example.joaopedro.minhasfinancas.async;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;


public class SyncService extends Service {
    public SyncService() {

    }

    private static final Object sSyncAdapterLock = new Object();
    private SyncAdapter bsa;
    @Override
    public void onCreate() {
        Log.d("SyncService", "onCreate - SyncService");
        synchronized (sSyncAdapterLock) {
            if (bsa == null) {
                bsa = new SyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return bsa.getSyncAdapterBinder();
    }
}
