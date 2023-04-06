package ro.pub.cs.systems.eim.colocviumodel1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewDebug;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Colocviu1MainActivity extends AppCompatActivity {

    Object serviceStatus = Constants.SERVICE_STOPPED;
    private TextView allTerms;
    private Button add;
    private Button compute;
    private EditText nextTerm;

    private boolean checkModify = false;
    String allSum;

    int valueFinal = 0;

    BroadcastReceiver broadcastReceiver = new PracticalTest01BroadcastReceiver();

    private IntentFilter intentFilter = new IntentFilter();

    private class PracticalTest01BroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(Constants.BROADCAST_RECEIVER_EXTRA, intent.getStringExtra(Constants.BROADCAST_RECEIVER_EXTRA));
           // Toast.makeText(this, "The activity returned with " + valueFinal, Toast.LENGTH_LONG).show();
            System.out.println("primesc");
            Toast.makeText(context, "Received from service " + intent.getStringExtra(Constants.BROADCAST_RECEIVER_EXTRA), Toast.LENGTH_LONG).show();
        }
    }

    private ButtonClickListenerAdd buttonClickListenerAdd = new ButtonClickListenerAdd();

    private class ButtonClickListenerAdd implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            String number = nextTerm.getText().toString();
            if (number != null) {
                checkModify = true;
                if (allSum == null)
                    allSum = number;
                else if (allSum != null) {
                    allSum = allSum + " + " + number;
                    allTerms.setText(allSum);
                }
                nextTerm.setText("");
            }
        }
    }

    private ButtonClickListenerCompute buttonClickListenerCompute = new ButtonClickListenerCompute();

    private class ButtonClickListenerCompute implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getApplicationContext(), SecondActivity.class);
            intent.putExtra(Constants.ALLTERMS, allSum);
            // astept rezultat
            startActivityForResult(intent, Constants.RequestCode);
        }
    }

    public void myStartService() {
       // System.out.println("ajung cu "+valueFinal);
        if(valueFinal>10 &&  serviceStatus == Constants.SERVICE_STOPPED){
            System.out.println("ajung cu "+valueFinal);
            Intent intent = new Intent(getApplicationContext(), ColocviuService.class);
            intent.putExtra(Constants.ALLTERMS, Integer.toString(valueFinal));
            getApplicationContext().startService(intent);
            serviceStatus = Constants.SERVICE_STARTED;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colocviu_main);

        allTerms = (TextView) findViewById(R.id.allTermsButton);
        nextTerm = (EditText) findViewById(R.id.nextTerm);

        add = (Button) findViewById(R.id.addButton);
        compute = (Button) findViewById(R.id.computeButton);
        compute.setOnClickListener(buttonClickListenerCompute);
        add.setOnClickListener(buttonClickListenerAdd);

        for (int index = 0; index < Constants.actionTypes.length; index++) {
            intentFilter.addAction(Constants.actionTypes[index]);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == Constants.RequestCode)
            if (resultCode == RESULT_OK) {

                valueFinal = intent.getExtras().getInt(Constants.ALLTERMSRESULT);
                Log.d(Constants.PROCESSING_THREAD_TAG, "Toast  " + valueFinal);
                Toast.makeText(this, "The activity returned with " + valueFinal, Toast.LENGTH_LONG).show();
                checkModify = false;
                allSum = Integer.toString(valueFinal);
                allTerms.setText(Integer.toString(valueFinal));
                myStartService();
            }
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // pereche key- value
        if (checkModify == true) {
            // salvez val calculata si val curenta si check
            savedInstanceState.putString(Constants.PREVAL, Integer.toString(valueFinal));
            savedInstanceState.putString(Constants.CHECK, Boolean.toString(checkModify));
            savedInstanceState.putString(Constants.CURRENTSUM, allTerms.getText().toString());
        } else {
            // salvez doar val calculata
            savedInstanceState.putString(Constants.PREVAL, Integer.toString(valueFinal));
            savedInstanceState.putString(Constants.CHECK, Boolean.toString(checkModify));
        }
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState.containsKey(Constants.CHECK)) {
            checkModify = Boolean.valueOf(savedInstanceState.getString(Constants.CHECK));

            if (checkModify == true) {
                // daca am modificat valoarea, atunci afisez valoarea calculata + valoarea curenta
                valueFinal = Integer.parseInt(savedInstanceState.getString(Constants.PREVAL));
                allTerms.setText(savedInstanceState.getString(Constants.CURRENTSUM));
            } else {
                // daca nu am modificat valoarea, atunci afisez doar valoarea calculata
                valueFinal = Integer.parseInt(savedInstanceState.getString(Constants.PREVAL));
                allTerms.setText(Integer.toString(valueFinal));
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent(getApplicationContext(), ColocviuService.class);
        getApplicationContext().stopService(intent);
    }


    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver, intentFilter);
    }
}
