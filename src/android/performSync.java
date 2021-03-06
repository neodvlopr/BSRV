package you.got.it.plugin;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.os.Handler;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.LogRecord;


/**
 * Created by Phoenix on 22/07/2018.
 */

public class performSync extends Service {
    public static Runnable r = null;
    public int c = 0;
    ExecutorService threadPoolExecutor = Executors.newSingleThreadExecutor();
    Future longRunningTaskFuture;
    public Handler dataHandler = null;
    private class MyHandlerThread extends HandlerThread {

        Handler handler;

        public MyHandlerThread(String name, int p) {

            super(name, p);
        }

        @Override
        protected void onLooperPrepared() {
            Log.d("MyService", "Looper Prepared");
            Looper l = getLooper();
            dataHandler = new Handler(l) {
                public void handleMessage(Message msg) {
                    Log.d("MyService", "Handler function");
                    String nombre = msg.getData().getString("nombre");
                    String mensaje = msg.getData().getString("mensaje");
                    showNotification();
                   /* String myData = (String) msg.obj;
                    Toast.makeText(getApplicationContext(), ""+myData+"",
                            Toast.LENGTH_LONG).show();*/

                }
            };
            r = new Runnable() {
                public void run() {
                    try {
                        // send to our Handler
                        Message msg = new Message();
                        Bundle b = new Bundle();
                        b.putString("nombre", "Marce");
                        b.putString("mensaje", "Hello World");
                        msg.setData(b);

                        c++;

                        dataHandler.sendMessage(msg);

                        dataHandler.postDelayed(this, 10000);
                        if(c == 4){
                            dataHandler.removeCallbacks(r);
                        }
                    } catch (Exception e) {
                        // wait 30 seconds
                        try {
                            Thread.sleep(30000);
                        } catch (InterruptedException ie) {
                            // do nothing
                        }
                        // try again

                    }


                }
            };
            dataHandler.postDelayed(r, 10000);
        }
    }
    public void showNotification() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setComponent(new ComponentName("you.got.it", "you.got.it.MainActivity"));
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
        Resources r = getResources();
        Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        String title = "Title";
        String text = "asdadaasdasdadadasasdasdasd";
        int icon = android.R.drawable.ic_delete;
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
                builder.setTicker("Ticker");
                builder.setSmallIcon(icon);
                builder.setContentTitle(title);
                builder.setContentText(text);
                builder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });
                builder.setSound(uri);
                builder.setContentIntent(pi);
                builder.setAutoCancel(true);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(c, builder.build());
    }
    public performSync(){
        Log.d("MyService", "perform");
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        Log.d("MyService", "service bindeded");
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.w("MyService", "onStartCommand callback called");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        MyHandlerThread thread = new MyHandlerThread("ServiceStartArguments", Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("MyService", "onDestroy callback called");
    }

}
