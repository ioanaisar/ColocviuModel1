package ro.pub.cs.systems.eim.colocviumodel1;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class ColocviuService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // return super.onStartCommand(intent, flags, startId);
        int sum = Integer.parseInt(intent.getStringExtra(Constants.ALLTERMS));
        System.out.println("Service primeste suma: " + sum);
        // creez thread si start
        ProcessingThread processingThread = new ProcessingThread(this, sum);
        processingThread.start();
        return Service.START_REDELIVER_INTENT;
    }
}
