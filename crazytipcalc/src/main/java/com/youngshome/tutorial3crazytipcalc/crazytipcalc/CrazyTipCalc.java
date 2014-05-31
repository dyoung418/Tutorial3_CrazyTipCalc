package com.youngshome.tutorial3crazytipcalc.crazytipcalc;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;


public class CrazyTipCalc extends ActionBarActivity {

    private static boolean Locked = false;

    private static final String TOTAL_BILL = "TOTAL_BILL";
    private static final String CURRENT_TIP = "CURRENT_TIP";
    private static final String BILL_WITHOUT_TIP = "BILL_WITHOUT_TIP";

    private double billBeforeTip;
    private double tipAmount;
    private double finalBill;

    EditText billBeforeTipET;
    EditText tipAmountET;
    EditText finalBillET;
    SeekBar tipSeekBar;

    TextView tenResult;
    TextView fifteenResult;
    TextView eighteenResult;
    TextView twentyResult;
    TextView twentyFiveResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crazy_tip_calc);

        if(savedInstanceState == null){
            billBeforeTip = 0.0;
            tipAmount = .15;
            finalBill = 0.0;
        }else{
            billBeforeTip = savedInstanceState.getDouble(BILL_WITHOUT_TIP);
            tipAmount = savedInstanceState.getDouble(CURRENT_TIP);
            finalBill = savedInstanceState.getDouble(TOTAL_BILL);
        }
        billBeforeTipET = (EditText) findViewById(R.id.billEditText);
        tipAmountET = (EditText) findViewById(R.id.tipEditText);
        finalBillET = (EditText) findViewById(R.id.finalBilleditText);
        tipSeekBar = (SeekBar) findViewById(R.id.tipSeekBar);
        tenResult = (TextView) findViewById(R.id.tenResultTextView);
        fifteenResult = (TextView) findViewById(R.id.fifteenResultTextView);
        eighteenResult = (TextView) findViewById(R.id.eighteenResultTextView);
        twentyResult = (TextView) findViewById(R.id.twentyResultTextView);
        twentyFiveResult = (TextView) findViewById(R.id.twentyFiveResultTextView);

        tipAmountET.setText(String.format("%.02f", tipAmount));
        tipSeekBar.setProgress( ((int) (tipAmount * 100)) );
        billBeforeTipET.setText(String.format("%.02f", billBeforeTip));
        finalBillET.setText(String.format("%.02f", finalBill));

        updateExampleResults();

        billBeforeTipET.addTextChangedListener(billBeforeTipListener);
        tipAmountET.addTextChangedListener(tipListener);
        finalBillET.addTextChangedListener(finalBillListener);
        tipSeekBar.setOnSeekBarChangeListener(tipSeekBarListener);

    }

    private TextWatcher tipListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void afterTextChanged(Editable s) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(!Locked){
                try{
                    tipAmount = Double.parseDouble(s.toString());
                }
                catch(NumberFormatException e){
                    tipAmount = 0.15;
                }
                updateTipAndFinalBill();
            }
        }

    };

    private TextWatcher billBeforeTipListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void afterTextChanged(Editable s) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!Locked) {
                try{
                    billBeforeTip = Double.parseDouble(s.toString());
                }
                catch(NumberFormatException e){
                    billBeforeTip = 0.0;
                }
                updateTipAndFinalBill();
            }
        }

    };

    private TextWatcher finalBillListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void afterTextChanged(Editable s) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!Locked) {
                try{
                    finalBill = Double.parseDouble(s.toString());
                }
                catch(NumberFormatException e){
                    finalBill = billBeforeTip * tipAmount;
                }
                updateFinalBillAndTip();
            }
        }

    };


    private SeekBar.OnSeekBarChangeListener tipSeekBarListener = new SeekBar.OnSeekBarChangeListener(){
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if(!Locked){ // could also say if(fromUser) here...
                tipAmount = tipSeekBar.getProgress() * .01;
                tipAmountET.setText(String.format("%.02f", tipAmount));
                updateTipAndFinalBill();
            }
        }

    };

    private void updateTipAndFinalBill(){
        Locked = true;
        double tipAmount = Double.parseDouble(tipAmountET.getText().toString());
        double finalBill = billBeforeTip + (billBeforeTip * tipAmount);
        finalBillET.setText(String.format("%.02f", finalBill));
        tipSeekBar.setProgress( ((int) (tipAmount*100)) );
        updateExampleResults();
        Locked = false;
    }

    private void updateFinalBillAndTip(){
        Locked = true;
        double finalBill = Double.parseDouble(finalBillET.getText().toString());
        double tipAmount = (finalBill-billBeforeTip)/billBeforeTip;
        tipAmountET.setText(String.format("%.02f", tipAmount));
        tipSeekBar.setProgress(((int) (tipAmount * 100)));
        updateExampleResults();
        Locked = false;
    }

    private void updateExampleResults(){
        tenResult.setText( String.format("%.02f", billBeforeTip*1.10)  );
        fifteenResult.setText( String.format("%.02f", billBeforeTip*1.15)  );
        eighteenResult.setText( String.format("%.02f", billBeforeTip*1.18)  );
        twentyResult.setText( String.format("%.02f", billBeforeTip*1.20)  );
        twentyFiveResult.setText( String.format("%.02f", billBeforeTip*1.25)  );
    }

    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putDouble(TOTAL_BILL, finalBill);
        outState.putDouble(CURRENT_TIP, tipAmount);
        outState.putDouble(BILL_WITHOUT_TIP, billBeforeTip);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.crazy_tip_calc, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
