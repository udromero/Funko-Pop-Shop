package com.dannydevs.funkopopshop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PaymentPage extends AppCompatActivity {

    TextView mPaymentDisplay;

    EditText mCreditCardNumberField;
    EditText mCVNumberField;
    EditText mStreetAddressField;
    EditText mCityField;
    EditText mZipcodeField;
    EditText mStateField;

    Button mPayButton;
    Button mBackButton;

    String mCreditCardNumber;
    String mCVNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        wireUpDisplay();

        mPayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getInfoFromDisplay();
                // TODO: check valid card and cv num, wipe display and set message
                if(checkValidPayment()){
                    hideDisplay();
                    mPaymentDisplay.setText("Payment Processed Successfully");
                } else {
                    Toast.makeText(PaymentPage.this, "Please Enter Valid Payment", Toast.LENGTH_SHORT).show();
                }

            }
        });

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LandingPage.class);
                startActivity(intent);
            }
        });
    }

    private void wireUpDisplay(){
        mPaymentDisplay = findViewById(R.id.checkoutDisplayText);

//        mCreditCardNumberField = findViewById(R.id.checkoutCardNumber);
//        mCVNumberField = findViewById(R.id.checkoutCVNumber);
//        mStreetAddressField = findViewById(R.id.checkoutStreet);
//        mCityField = findViewById(R.id.checkoutCity);
//        mZipcodeField = findViewById(R.id.checkOutZipCode);
//        mStateField =findViewById(R.id.checkoutState);

        mPayButton = findViewById(R.id.checkoutPayButton);
        mBackButton = findViewById(R.id.checkoutBackButton);
    }

    private void getInfoFromDisplay()
    {
        mCreditCardNumber = mCreditCardNumberField.getText().toString();
        mCVNumber = mCVNumberField.getText().toString();
    }
    private boolean checkValidPayment(){
        return mCreditCardNumber.length() > 8 && mCreditCardNumber.length() <= 16 && mCVNumber.length() <= 4 && mCVNumber.length() >= 3;
    }

    private void hideDisplay(){
//        mCreditCardNumberField.setVisibility(View.INVISIBLE);
//        mCVNumberField.setVisibility(View.INVISIBLE);
//        mStreetAddressField.setVisibility(View.INVISIBLE);
//        mCityField.setVisibility(View.INVISIBLE);
//        mZipcodeField.setVisibility(View.INVISIBLE);
//        mStateField.setVisibility(View.INVISIBLE);
//        mPayButton.setVisibility(View.INVISIBLE);
    }
}
