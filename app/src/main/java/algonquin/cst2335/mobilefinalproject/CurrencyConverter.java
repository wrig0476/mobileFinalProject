package algonquin.cst2335.mobilefinalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class CurrencyConverter extends AppCompatActivity {

    private ImageButton imageButton;
    private Spinner fromSpinner;
    private Spinner toSpinner;
    private EditText amountEditText;
    private EditText convertAmount;
    private Button convertButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_converter);

        imageButton = findViewById(R.id.imgButton);
        fromSpinner = findViewById(R.id.fromSpinner);
        toSpinner = findViewById(R.id.toSpinner);
        amountEditText = findViewById(R.id.amountEditText);
        convertAmount = findViewById(R.id.convertAmount);
        convertButton = findViewById(R.id.convertButton);

        imageButton.setOnClickListener(v -> {
            Intent intent = new Intent(CurrencyConverter.this, MainActivity.class);
            startActivity(intent);
        });

        // Define the currency options
        String[] currencyOptions = {"AUD", "CAD", "USD"};

        // Create ArrayAdapter instances with the currency options
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, currencyOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set the ArrayAdapter instances to the Spinners
        fromSpinner.setAdapter(adapter);
        toSpinner.setAdapter(adapter);


        convertButton.setOnClickListener(v -> {
            performCurrencyConversion();
        });

    }
    private void performCurrencyConversion () {
        String fromCurrency = fromSpinner.getSelectedItem().toString();
        String toCurrency = toSpinner.getSelectedItem().toString();
        double currencyAmount; // Initialize amount to a default value

        currencyAmount = Double.parseDouble(amountEditText.getText().toString());

        // Construct the API URL with the selected currencies and amount
        String apiUrl = "https://api.getgeoapi.com/v2/currency/convert?format=json"
                + "&from=" + fromCurrency
                + "&to=" + toCurrency
                + "&amount=" + currencyAmount
                + "&api_key=2011c0442fd66c69fb987e9e3a20f85e05d0a37a"
                + "&format=json";


        // Make the API request using Volley
        // Handle API request errors here
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, apiUrl, null,
                response -> {
                    // Handle the API response here
                    try {
                        // Parse the JSON response and extract the converted amount
                        JSONObject rates = response.getJSONObject("rates");

                        if (toCurrency == "AUD") {
                            JSONObject AUD = rates.getJSONObject("AUD");
                            String rateForAmountString = AUD.getString("rate_for_amount");
                            convertAmount.setText(String.format(rateForAmountString));
                            Toast.makeText(CurrencyConverter.this, "converted amount: " + rateForAmountString, Toast.LENGTH_LONG).show();
                        } else if (toCurrency == "CAD") {
                            JSONObject CAD = rates.getJSONObject("CAD");
                            String rateForAmountString = CAD.getString("rate_for_amount");
                            convertAmount.setText(String.format(rateForAmountString));
                            Toast.makeText(CurrencyConverter.this, "converted amount: " + rateForAmountString, Toast.LENGTH_LONG).show();
                        } else {
                            JSONObject USD = rates.getJSONObject("USD");
                            String rateForAmountString = USD.getString("rate_for_amount");
                            convertAmount.setText(String.format(rateForAmountString));
                            Toast.makeText(CurrencyConverter.this, "converted amount: " + rateForAmountString, Toast.LENGTH_LONG).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                Throwable::printStackTrace);

        // Add the request to the RequestQueue (Volley handles it automatically)
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

}
