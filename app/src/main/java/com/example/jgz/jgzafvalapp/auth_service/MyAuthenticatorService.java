package com.example.jgz.jgzafvalapp.auth_service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by Boy on 13-12-2016.
 */

public class MyAuthenticatorService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        MyAuthenticator myAuthenticator = new MyAuthenticator(this);
        return myAuthenticator.getIBinder();
    }
}
