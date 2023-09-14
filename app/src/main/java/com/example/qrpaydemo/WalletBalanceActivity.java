package com.example.qrpaydemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class WalletBalanceActivity extends AppCompatActivity {

    private List<User> offlineTransactions;
   //private OfflineTransactionDatabaseHelper databaseHelper;


    private TextView textViewBalanceAmount;
    private EditText editTextAmount;
    private EditText editTextPin;

    private TextView TextuserId;

    private TextView Textusername;
    private Button buttonSendMoney;
    private ProgressBar progressBar;

    private ImageView imageViewSuccess;

    // Simulated user and wallet balance (you should replace this with your data model)
    private User currentUser;
    private double walletBalance = 100.0; // Replace with the user's actual balance

    // List to store pending offline transactions

    // Key for storing offline transactions in SharedPreferences
    private static final String OFFLINE_TRANSACTIONS_KEY = "offline_transactions";

    //private List<User> offlineTransactions;
    // Load offline transactions from SharedPreferences
    private List<User> loadOfflineTransactions() {
        List<User> transactions = new ArrayList<>();
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String transactionsJson = sharedPreferences.getString(OFFLINE_TRANSACTIONS_KEY, null);
        if (transactionsJson != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<User>>() {}.getType();
            transactions = gson.fromJson(transactionsJson, type);
        }
        return transactions;
    }

    // Save offline transactions to SharedPreferences
    private void saveOfflineTransactions(List<User> transactions) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String transactionsJson = gson.toJson(transactions);
        editor.putString(OFFLINE_TRANSACTIONS_KEY, transactionsJson);
        editor.apply();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_balance);

        // Get the context of the current activity
        Context context = this;


        // Initialize UI elements
        textViewBalanceAmount = findViewById(R.id.textViewBalanceAmount);
        editTextAmount = findViewById(R.id.editTextAmount);
        editTextPin = findViewById(R.id.editTextPin);
        buttonSendMoney = findViewById(R.id.buttonSendMoney);
        progressBar =  findViewById(R.id.progressBar);
        TextuserId = findViewById(R.id.TextuserId);
        Textusername = findViewById(R.id.Textusername);


        textViewBalanceAmount.setText("Balance: $" + walletBalance);

        // Initialize the list of offline transactions from local storage
        offlineTransactions = loadOfflineTransactions();


        // Get the user ID passed from the ScanQRcode activity
        String userId = getIntent().getStringExtra("userId");
        String username = getIntent().getStringExtra("username");
        TextuserId.setText(userId);
        Textusername.setText(username);

        // Handle the Send Money button click
        buttonSendMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the amount to transfer and PIN entered by the user
                String amountText = editTextAmount.getText().toString();
                String pinText = editTextPin.getText().toString();
                double amountTexts = Double.parseDouble(editTextAmount.getText().toString());
                int pinTexts = Integer.parseInt(editTextPin.getText().toString());
                String userId = TextuserId.getText().toString();
                String username = Textusername.getText().toString();



                offlineTransactionDatabaseHelper dbHelper = new offlineTransactionDatabaseHelper(context);

                User newUser = new User(userId,username,amountTexts,pinTexts,null);

                // Insert the transaction into the database
                long newRowId = dbHelper.insertTransaction(newUser);

                if (newRowId != -1) {
                    // Insertion was successful, show a success message

                    Toast.makeText(WalletBalanceActivity.this,"Transaction added successfully", Toast.LENGTH_SHORT).show();
                } else {
                    // Insertion failed, show an error message
                    Toast.makeText(WalletBalanceActivity.this, "Failed to add transaction", Toast.LENGTH_SHORT).show();
                }



                if (!amountText.isEmpty() && !pinText.isEmpty()) {
                    // Parse the amount
                    double transferAmount = Double.parseDouble(amountText);

                    // Check if the PIN is correct (replace '1234' with the user's actual PIN)
                    if (pinText.equals(newUser.getPin())) {
                        // Check if the wallet has sufficient balance
                        if (walletBalance >= transferAmount) {
                            // Deduct the transfer amount from the wallet balance
                            walletBalance -= transferAmount;

                            // Update the displayed balance
                            textViewBalanceAmount.setText("Balance: $" + walletBalance);

                            // Show a progress bar to indicate the transaction is processing
                            progressBar.setVisibility(View.VISIBLE);

                            // Simulate a delay to represent transaction processing (you should replace this)
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    // Hide the progress bar
                                    progressBar.setVisibility(View.GONE);
                                    // Show the success icon
                                    imageViewSuccess = findViewById(R.id.imageViewSuccess);
                                    imageViewSuccess.setVisibility(View.VISIBLE);

                                    // Create a new offline transaction
                                    User transaction = new User(currentUser.getUserId(), transferAmount);

                                    // Add the transaction to the list of offline transactions
                                    offlineTransactions.add(transaction);

                                    // Save the updated list of offline transactions to local storage
                                    saveOfflineTransactions(offlineTransactions);


                                    // Perform the money transfer to the receiver (you should implement this logic)
                                    transferMoneyToReceiver(transferAmount);

                                    // Clear input fields
                                    editTextAmount.setText("");
                                    editTextPin.setText("");

                                    // Show a success message to the user
                                    Toast.makeText(WalletBalanceActivity.this, "Successful!", Toast.LENGTH_SHORT).show();
                                    // Hide the success icon after a delay (e.g., 3 seconds)
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            imageViewSuccess.setVisibility(View.GONE);
                                        }

                                    }, 3000); // Simulated delay of 2 seconds (adjust as needed)

                                }

                            },2000);
                        }else {
                            // Show an error message: Insufficient balance
                            Toast.makeText(WalletBalanceActivity.this, "Insufficient balance!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Show an error message: Invalid PIN
                        Toast.makeText(WalletBalanceActivity.this, "Invalid pin", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Show an error message: Enter both amount and PIN
                    Toast.makeText(WalletBalanceActivity.this, "Please enter both amount and PIN.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private User loadUserInfoFromFile() {
        try {
            // Open the file for reading
            File file = new File(getFilesDir(), "userInfo.json"); // Use the same filename you used for saving

            // Read the JSON data from the file
            BufferedReader br = new BufferedReader(new FileReader(file));
            StringBuilder userJsonStringBuilder = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                userJsonStringBuilder.append(line);
            }

            br.close();

            // Parse the JSON data into a User object
            JSONObject userJson = new JSONObject(userJsonStringBuilder.toString());
            String userId = userJson.getString("userId");
            String username = userJson.getString("username");
            double amount = userJson.getDouble("amount");
            int pin = userJson.getInt("pin");

            return new User(userId, username, amount, pin, null); // Replace "null" with the actual QR code data
        } catch (JSONException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }








    // Implement the logic to transfer money to the receiver (you should customize this)
    private void transferMoneyToReceiver(double amount) {
        // Implement the transfer logic here
        // This could involve making API requests, updating server data, etc.
        // For the demo, you can print a log message.
        // You should implement your actual logic to handle money transfers.
        System.out.println("Transferred $" + amount + " to the receiver.");
    }




}


    