package algonquin.cst2335.mobilefinalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.logging.Logger;

public class DetailActivity extends AppCompatActivity {
    Logger log = Logger.getLogger(DetailActivity.class.getName());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent detailsIntent = getIntent();
        Bundle details = detailsIntent.getExtras();
        // show stuff in the next screen
        TextView flightNumber = findViewById(R.id.flightNumber);
        TextView destination = findViewById(R.id.destination);
        TextView terminal = findViewById(R.id.terminal);
        TextView gate = findViewById(R.id.gate);
        TextView delay = findViewById(R.id.delay);

        flightNumber.setText("Flight Number:   " + (String)details.get("flight_number"));
        destination.setText( "Destination:     " + (String)details.get("destination"));
        terminal.setText(    "Terminal:        " + (String)details.get("terminal"));
        gate.setText(        "Gate:            " + (String)details.get("gate"));
        delay.setText(       "Delay:           " + (String)details.get("delay"));

        // Buttons
        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AviationTracker.class);
                view.getContext().startActivity(intent);
            }
        });

        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                log.info("current flight detail should be saved. it's not implemented.");
            }
        });

    }
}