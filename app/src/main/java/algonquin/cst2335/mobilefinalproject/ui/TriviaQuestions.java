package algonquin.cst2335.mobilefinalproject.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import algonquin.cst2335.mobilefinalproject.MainActivity;
import algonquin.cst2335.mobilefinalproject.R;

public class TriviaQuestions extends AppCompatActivity {

    private ImageButton imageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trivia_questions);

        imageButton = findViewById(R.id.imgButton);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TriviaQuestions.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}