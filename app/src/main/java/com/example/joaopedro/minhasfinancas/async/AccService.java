package com.example.joaopedro.minhasfinancas.async;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AccService extends Service {

    private FinancasAuthenticator fa;

    @Override
    public void onCreate() {
        fa = new FinancasAuthenticator(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return fa.getIBinder();
    }
}
