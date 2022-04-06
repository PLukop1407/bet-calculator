//Bet Calculator designed and developed by Patrikas Lukosius, 40405699.
//Only contains three bet types, no Each Way bets or Dead Heats.
//Initially I planned to have all of this, however time constraints meant I had to limit the scope of this app and focus on getting main features working.

package com.example.betcalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.text.DecimalFormat;



public class MainActivity extends AppCompatActivity {
    String[] betTypes = {"Single", "Double", "Treble"}; //Array of strings used for the bet type spinner.
    String[] betOutcomes = {"Win", "Loser", "Void"}; //Array of strings used for the outcome spinners.
    String[] outcomes = {"Lose", "Lose", "Lose"}; //Initialising array of strings that stores the outcomes for every row.
    String selection; //Creating the selection variable here so that it can be accessed anywhere if necessary.
    Spinner spinBetType, spinOutcome1, spinOutcome2, spinOutcome3; //Creating spinners, so that I don't have to later.

    /*
    Above are the global variables required for the app to function. I tried to cut down on them, but I'm not too good at Java.
    In an ideal world, I'd have very few global variables, limiting most of them to the appropriate scope.
    Mostly they're here just to save me a lot of headaches.
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Bet Type spinner id is stored in spinBetType for easier use later when populating the spinner.
        spinBetType = findViewById(R.id.spnBetType);

        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, betTypes);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Set the Array Adapter aa to contain the values of the betTypes array.

        spinBetType.setAdapter(aa);
        //Set the Bet Type spinner to use the adapter aa, populating the spinner with the values from the betTypes array.


        /*
        The app does the same down here but for the bet outcome spinners. It does this four times as there are a maximum of four
        outcome spinners. I really don't like how this is done and wanted to improve it, but I didn't have much time left.
         */

        spinOutcome1 = findViewById(R.id.spnBetOutcome1);
        spinOutcome2 = findViewById(R.id.spnBetOutcome2);
        spinOutcome3 = findViewById(R.id.spnBetOutcome3);


        ArrayAdapter ab = new ArrayAdapter(this, android.R.layout.simple_spinner_item, betOutcomes);
        ab.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinOutcome1.setAdapter(ab);
        spinOutcome2.setAdapter(ab);
        spinOutcome3.setAdapter(ab);

        //Here the app creates a new event handler (onItemSelectedListener) for the bet types.
        //We need a handler specific to this spinner, as otherwise the app would only listen to one spinner.
        spinBetType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //By default, we hide all the rows for the selections.
                findViewById(R.id.row1).setVisibility(View.GONE);
                findViewById(R.id.row2).setVisibility(View.GONE);
                findViewById(R.id.row3).setVisibility(View.GONE);

                switch (position) {
                    case 0: //In the case of a "Single" bet, we reveal only one row as a single bet only has one selection.
                        findViewById(R.id.row1).setVisibility(View.VISIBLE);
                        selection = "Single";
                        break;
                    case 1: //In the case of a "Double" bet, we reveal two rows as there are two selections in a double bet.
                        findViewById(R.id.row1).setVisibility(View.VISIBLE);
                        findViewById(R.id.row2).setVisibility(View.VISIBLE);
                        selection = "Double";
                        break;
                    case 2: //Same as before, reveal three rows for a treble.
                        findViewById(R.id.row1).setVisibility(View.VISIBLE);
                        findViewById(R.id.row2).setVisibility(View.VISIBLE);
                        findViewById(R.id.row3).setVisibility(View.VISIBLE);
                        selection = "Treble";
                        break;
                    //Ideally, I'd create these rows dynamically instead of setting them to be "gone".
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                findViewById(R.id.row1).setVisibility(View.GONE);
                findViewById(R.id.row2).setVisibility(View.GONE);
                findViewById(R.id.row3).setVisibility(View.GONE);
            }
        });

        //Below we create more event handlers, this time for the bet outcome spinners.
        //It's vital to know what outcome is selected, as that changes how the bet is calculated entirely - hence the three handlers for three rows.
        spinOutcome1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        outcomes[0] = "Win";
                        break;
                    case 1:
                        outcomes[0] = "Lose";
                        break;
                    case 2:
                        outcomes[0] = "Void";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Do nothing
            }
        });

        spinOutcome2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        outcomes[1] = "Win";
                        break;
                    case 1:
                        outcomes[1] = "Lose";
                        break;
                    case 2:
                        outcomes[1] = "Void";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Do nothing
            }
        });

        spinOutcome3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        outcomes[2] = "Win";
                        break;
                    case 1:
                        outcomes[2] = "Lose";
                        break;
                    case 2:
                        outcomes[2] = "Void";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Do nothing
            }
        });


    }

    //Creating a format where our winnings float is rounded to two decimal places. (I couldn't find a better way of doing it, I hate Java.)
    private static final DecimalFormat winRounded = new DecimalFormat("0.00");

    /*
    CalcBet is a method called by the Calculate button when clicked.
    This method will get the bet type selection and switch it.
    Once bet type is switched, all the fields are run through an input validation method.
    If input validation returns true, we then call the methods for calculating the bets - outputting their return.
    */

    public void calcBet(View btnCalculate) {
        TextView txtWinnings = findViewById(R.id.txtWinnings);
        String str_winnings;
        float winnings;


        switch (selection) {
            case "Single":
                if (InputValidation(1).equals("true")) { //Single bet wants Row 1 + Stake to be checked in Input Validation.
                    winnings = CalcSingle(); //Store the return from CalcSingle() in winnings, so that we can display it in the correct format.
                    str_winnings = (winRounded.format(winnings));
                    txtWinnings.setText(str_winnings);
                } else  if (InputValidation(1).equals("false")){ //If the input validation check fails, we use Toast to display a message informing the user that something is missing.
                   Toast toast = Toast.makeText(getApplicationContext(), "Odds and/or Stake missing", Toast.LENGTH_SHORT);
                   toast.show();
                }
                break;

            case "Double": //Double bet needs "2" as a parameter, as we're checking Rows 1 and 2 + Stake.
                if (InputValidation(2).equals("true")) {
                    winnings = CalcDouble();
                    str_winnings = (winRounded.format(winnings));
                    txtWinnings.setText(str_winnings);
                } else if (InputValidation(2).equals("false")){ //If input validation fails
                    Toast toast = Toast.makeText(getApplicationContext(), "Odds and/or Stake missing", Toast.LENGTH_SHORT);
                    toast.show();
                }
                break;

            case "Treble": //Treble bet needs "3" as a parameter, as we're checking Rows 1, 2 and 3 + Stake.
                if (InputValidation(3).equals("true")) {
                    winnings = CalcTreble();
                    str_winnings = (winRounded.format(winnings));
                    txtWinnings.setText(str_winnings);
                } else if (InputValidation(3).equals("false")) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Odds and/or Stake missing", Toast.LENGTH_SHORT);
                    toast.show();
                }
                break;

        }

    }

    //Below is the CalcSingle method, it returns a float value which is later formatted to display a money amount correctly.
    public float CalcSingle() {
        EditText p1 = findViewById(R.id.inPrice1_1);
        EditText p2 = findViewById(R.id.inPrice1_2);
        EditText s1 = findViewById(R.id.inStake);
        //Initialise and store the Price and Stake fields for Row 1.

        if (outcomes[0].equals("Win")) { //outcomes[0] stores the data from the outcome spinner for Row 1, used to check what needs to be done with the bet.
            float price1 = Float.parseFloat(p1.getText().toString()); //Converting text from the fields to floats so they can be used in calculations.
            float price2 = Float.parseFloat(p2.getText().toString());
            float stake = Float.parseFloat(s1.getText().toString());

            return (((price1 / price2) * stake) + stake); //Single Bet calculation. ((Stake*Price)+Stake)=Win Amount
        } else if (outcomes[0].equals("Lose")) {
            return 0; //Return nothing if the outcome was a loser.
        } else {
            return Float.parseFloat(s1.getText().toString()); //Return stake if the outcome was void.
        }
    }
    //Below we have the CalcDouble method, it returns a float value as well.
    public float CalcDouble() {
        EditText p1 = findViewById(R.id.inPrice1_1);
        EditText p2 = findViewById(R.id.inPrice1_2);
        EditText p3 = findViewById(R.id.inPrice2_1);
        EditText p4 = findViewById(R.id.inPrice2_2);
        EditText s1 = findViewById(R.id.inStake);
        //Initialising and storing Price and Stake fields for Rows 1 and 2.

        float price1 = Float.parseFloat(p1.getText().toString());
        float price2 = Float.parseFloat(p2.getText().toString());
        float price3 = Float.parseFloat(p3.getText().toString());
        float price4 = Float.parseFloat(p4.getText().toString());
        float stake = Float.parseFloat(s1.getText().toString());
        //Converting to float for calculations.

        /*
            Double bets carry your winnings over onto the next selection, so they work as follows:
            £10 win on two selections at 9/4 and 2/1. ((10*9/4)+10)=32.50, ((32.50*2/1)+32.50)=97.50
            This makes things a lot more complicated.
         */



        if (outcomes[0].equals("Win") && outcomes[1].equals("Win")) { //If both selections win, we can calculate the amount normally.
            float stakeInterloper; //Need this little variable to store the new stake, as it changes for the second selection.

            stakeInterloper = (((price1/price2)*stake)+stake);
            return (((price3 / price4) * stakeInterloper) + stakeInterloper);

        } else if (outcomes[0].equals("Lose") || outcomes[1].equals("Lose")) { //If either selection loses, return 0.
            return 0;
        } else if (outcomes[0].equals("Win") && outcomes[1].equals("Void")){ //If either selection wins but the other voids, we calculate that as a single bet.
            return (((price1/price2)*stake)+stake);
        } else if (outcomes[0].equals("Void") && outcomes[1].equals("Win")){ //Checking for the same here, but swapping the outcomes.
           return (((price3/price4)*stake)+stake);
        } else { //If both selections void, return stake.
            return stake;
        }
    }

    //Below is the CalcTreble method for calculating a Treble bet.
    public float CalcTreble() {
        EditText p1 = findViewById(R.id.inPrice1_1);
        EditText p2 = findViewById(R.id.inPrice1_2);
        EditText p3 = findViewById(R.id.inPrice2_1);
        EditText p4 = findViewById(R.id.inPrice2_2);
        EditText s1 = findViewById(R.id.inStake);
        EditText p5 = findViewById(R.id.inPrice3_1);
        EditText p6 = findViewById(R.id.inPrice3_2);
        //Initialising and storing Price and Stake fields for Rows 1, 2 and 3.

        float stake = Float.parseFloat(s1.getText().toString());
        float price1 = Float.parseFloat(p1.getText().toString());
        float price2 = Float.parseFloat(p2.getText().toString());
        float price3 = Float.parseFloat(p3.getText().toString());
        float price4 = Float.parseFloat(p4.getText().toString());
        float price5 = Float.parseFloat(p5.getText().toString());
        float price6 = Float.parseFloat(p6.getText().toString());
        //Converting text from fields into floats for calculations.

        /*
        Treble bets have three selections, which makes things a lot more annoying.
        The winnings carry over from selection 1 to 2 and then 3.
        £10 on three selections at 9/4, 2/1, 2/1. ((10*9/4)+10)=32.50, ((32.50*2/1)+32.50)=97.50, ((97.50*2/1)+97.50)=292.50
        */


        if (outcomes[0].equals("Win") && outcomes[1].equals("Win") && outcomes[2].equals("Win")) { //If all three selections win, we calculate the bet normally.
            float stakeInterloper1;
            float stakeInterloper2;

            stakeInterloper1=(((price1/price2)*stake)+stake);
            stakeInterloper2=(((price3/price4)*stakeInterloper1)+stakeInterloper1);
            return (((price5/price6)*stakeInterloper2)+stakeInterloper2);

            //I hate this 'nested if' mess, but it's necessary. Couldn't figure out a better way of doing this - arrays might've made it a bit better.

        } else if (outcomes[0].equals("Lose") || outcomes[1].equals("Lose") || outcomes[2].equals("Lose")) { //If any of them lose, return 0.
            return 0;
        } else if (outcomes[0].equals("Win") && outcomes[1].equals("Void") && outcomes[2].equals("Void")) { //If Selection 1 wins, calculate that as a single.
            return (((price1/price2)*stake)+stake);
        } else if (outcomes[0].equals("Void") && outcomes[1].equals("Win") &&  outcomes[2].equals("Void")) { //Single bet on selection 2.
            return (((price3/price4)*stake)+stake);
        } else if (outcomes[0].equals("Void") && outcomes[1].equals("Void") &&  outcomes[2].equals("Win")) { //Single bet on Selection 3.
            return (((price5/price6)*stake)+stake);
        } else if (outcomes[0].equals("Win") && outcomes[1].equals("Win") && outcomes[2].equals("Void")) { //If Selections 1 and 2 win, calculate that as a double.
            float stakeInterloper1=(((price1/price2)*stake)+stake);
            return (((price3/price4)*stakeInterloper1)+stakeInterloper1);
        } else if (outcomes[0].equals("Void") && outcomes[1].equals("Win") && outcomes[2].equals("Win")) { //Double bet on Selections 2 and 3.
            float stakeInterloper1=(((price3/price4)*stake)+stake);
            return (((price5/price6)*stakeInterloper1)+stakeInterloper1);
        } else if (outcomes[0].equals("Win") && outcomes[1].equals("Void") && outcomes[2].equals("Win")) { //Double bet on Selections 1 and 3.
            float stakeInterloper1=(((price1/price2)*stake)+stake);
            return (((price5/price6)*stakeInterloper1)+stakeInterloper1);
        } else { //Otherwise return the stake.
            return stake;
        }
    }


    //Input Validation method below, checks if all the EditText fields contain values. Without this, the app crashes trying to do calculations with null variables.
    public String InputValidation(int rows) {
        String valid;
        switch (rows){
            case 1: //Check all the fields in Row 1.
                EditText p1 = findViewById(R.id.inPrice1_1);
                EditText p2 = findViewById(R.id.inPrice1_2);
                EditText s1 = findViewById(R.id.inStake);

                if (    TextUtils.isEmpty(p1.getText().toString()) ||
                        TextUtils.isEmpty(p2.getText().toString()) ||
                        TextUtils.isEmpty(s1.getText().toString())
                ) {
                    valid="false"; //If any field is empty, valid is set to false.
                } else {
                    valid="true"; //If all fields are populated, valid is set to true.
                }
                break;

            case 2: //Check all the fields in Rows 1 and 2.
                p1 = findViewById(R.id.inPrice1_1);
                p2 = findViewById(R.id.inPrice1_2);
                EditText p3 =  findViewById(R.id.inPrice2_1);
                EditText p4 =  findViewById(R.id.inPrice2_2);
                s1 = findViewById(R.id.inStake);


                if (    TextUtils.isEmpty(p1.getText().toString()) ||
                        TextUtils.isEmpty(p2.getText().toString()) ||
                        TextUtils.isEmpty(s1.getText().toString()) ||
                        TextUtils.isEmpty(p3.getText().toString()) ||
                        TextUtils.isEmpty(p4.getText().toString())
                ) {
                    valid = "false"; //If any field is empty, valid is set to false.
                } else {
                    valid = "true"; //If all fields are populated, valid is set to true.
                }
                break;

            case 3: //Check all the fields in Rows 1, 2 and 3.
                p1 = findViewById(R.id.inPrice1_1);
                p2 = findViewById(R.id.inPrice1_2);
                p3 = findViewById(R.id.inPrice2_1);
                p4 = findViewById(R.id.inPrice2_2);
                EditText p5 = findViewById(R.id.inPrice3_1);
                EditText p6 = findViewById(R.id.inPrice3_2);
                s1 = findViewById(R.id.inStake);

                if (    TextUtils.isEmpty(p1.getText().toString()) ||
                        TextUtils.isEmpty(p2.getText().toString()) ||
                        TextUtils.isEmpty(s1.getText().toString()) ||
                        TextUtils.isEmpty(p3.getText().toString()) ||
                        TextUtils.isEmpty(p4.getText().toString()) ||
                        TextUtils.isEmpty(p5.getText().toString()) ||
                        TextUtils.isEmpty(p6.getText().toString())
                ) {
                    valid = "false"; //If any field is empty, valid is set to false.
                } else {
                    valid = "true"; //If all fields are populated, valid is set to true.
                }
                break;


            default:
                throw new IllegalStateException("Unexpected value: " + rows);
        }
        return valid;
    }

}