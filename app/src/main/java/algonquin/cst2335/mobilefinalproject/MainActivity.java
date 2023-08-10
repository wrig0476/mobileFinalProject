package algonquin.cst2335.mobilefinalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    private Button aviationTracker;
    private Button currencyConverter;
    private Button triviaQestions;
    private Button BearImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        aviationTracker = findViewById(R.id.aviationTracker);
        currencyConverter = findViewById(R.id.converter);
        triviaQestions = findViewById(R.id.trivia);
        BearImages = findViewById(R.id.bearImg);

        aviationTracker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AviationTracker.class );
                startActivity(intent);
            }
        });

        currencyConverter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CurrencyConverter.class );
                startActivity(intent);
            }
        });

        triviaQestions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TriviaQuestions.class );
                startActivity(intent);
            }
        });

        BearImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BearImage.class );
                startActivity(intent);
            }
        });
    }

}