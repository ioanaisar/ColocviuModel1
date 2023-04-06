package ro.pub.cs.systems.eim.colocviumodel1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.Objects;


public class SecondActivity extends AppCompatActivity {

    private int numberFinal;
    private String stringFinal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);


        if(savedInstanceState==null){
            Bundle extras = getIntent().getExtras();
            if(extras==null) {
             //   numberFinal.setText("0");
            }else{
                stringFinal = extras.getString(Constants.ALLTERMS);

                // compute sum
                String[] numbers = stringFinal.split(" ",0);
                for (String number : numbers) {
                    if(!Objects.equals(number, "+")) {
                        Log.d(Constants.PROCESSING_THREAD_TAG, "Thread has stopped! " + number);
                        numberFinal += Integer.parseInt(number);
                    }
                    //numberFinal += Integer.parseInt(number);
                }

                Intent intent = new Intent();
                Log.d(Constants.PROCESSING_THREAD_TAG, "Thread has stopped! " + numberFinal);
                intent.putExtra(Constants.ALLTERMSRESULT,numberFinal);
                setResult(RESULT_OK, intent);
                finish();

                //numberFinal.setText(String.valueOf(left+right));
                //numberFinal.setText(extras.getString(Constants.LEFT_TEXT) + " " + extras.getString(Constants.RIGHT_TEXT));
            }


        }
    }
}