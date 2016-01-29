package nazianoorani.myappportfolio;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
   private Button buttonApp1,buttonApp2, buttonApp3 ,buttonApp4 ,buttonApp5 , buttonApp6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonApp1 = (Button) findViewById(R.id.buttonApp1);
        buttonApp2 = (Button) findViewById(R.id.buttonApp2);
        buttonApp3 = (Button) findViewById(R.id.buttonApp3);
        buttonApp4 = (Button) findViewById(R.id.buttonApp4);
        buttonApp5 = (Button) findViewById(R.id.buttonApp5);
        buttonApp6 = (Button) findViewById(R.id.buttonApp6);
        buttonApp1.setOnClickListener(this);
        buttonApp2.setOnClickListener(this);
        buttonApp3.setOnClickListener(this);
        buttonApp4.setOnClickListener(this);
        buttonApp5.setOnClickListener(this);
        buttonApp6.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonApp1 : displayMessage(getString(R.string.app_1));
                break;
            case R.id.buttonApp2 : displayMessage(getString(R.string.app_2));
                break;
            case R.id.buttonApp3 : displayMessage(getString(R.string.app_3));
                break;
            case R.id.buttonApp4 : displayMessage(getString(R.string.app_4));
                break;
            case R.id.buttonApp5 : displayMessage(getString(R.string.app_5));
                break;
            case R.id.buttonApp6 : displayMessage(getString(R.string.app_6));
                break;

        }

    }

    private void displayMessage(String text) {
        String message ="This button will launch "+text;
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}
