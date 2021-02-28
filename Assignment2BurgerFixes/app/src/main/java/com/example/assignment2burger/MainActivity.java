package com.example.assignment2burger;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private RadioGroup myRadio; //Declare radio group once
    private RadioButton radio; //Declare radio button once because Radios cannot be selected more than once
    private CheckBox letBox, tomBox, picBox, onBox, cheeseBox, mayoBox, musBox; //Declare checkboxes
    private CheckBox[] checkboxesFiberArray; //Create Checkbox Array for Fiber
    private CheckBox[] checkboxesFatArray; //Create Checkbox Array for Fat
    private HashMap<String, Double> priceMap,sizePrice; //Create 2 HashMaps named priceMap and sizePrice
    private TextView priceText; //Declare Text View for Price
    private Button ord, res; //Declare buttons.
    private double priceTotal; //Variable for Total Price
    private int fiberTotalSelections = 0; //Initialise int for Total Fiber Selections
    private double proteinPrice = 0.0; //Initialise double for protein price
    private double fatPrice = 0.0; //Initialise double for Fat price
    private double fiberPrice=0.0; //Initialise double for Fiber price
    private double multiplier =0.0; //Initialise double for multiplier.
    @Override
    protected void onCreate(Bundle savedInstanceState) { //On Create function
        super.onCreate(savedInstanceState);
        View mybg;
        mybg=this.getWindow().getDecorView();
        mybg.setBackgroundResource(R.color.colorBg);
        setContentView(R.layout.activity_main);
        priceText = findViewById(R.id.textView_price);
        initUI();
        Selected();
    }

    public void initUI() { //Initialise everything

        priceText = findViewById(R.id.textView_price); //TextView
        String priceTextString = priceText.getText().toString(); //String Value of Total Price
        priceTotal = Double.parseDouble(priceTextString);
        ord = findViewById(R.id.order); //Buttons
        res = findViewById(R.id.reset);

        myRadio = findViewById(R.id.radioGroup);

        letBox = findViewById(R.id.lettuce);
        tomBox = findViewById(R.id.tomato);
        picBox = findViewById(R.id.pickle);
        cheeseBox = findViewById(R.id.cheese);
        mayoBox = findViewById(R.id.mayo);
        musBox = findViewById(R.id.mustard);
        onBox = findViewById(R.id.onion);

        checkboxesFiberArray = new CheckBox[]{letBox, tomBox, picBox, onBox};
        checkboxesFatArray = new CheckBox[]{cheeseBox, mayoBox, musBox};

        priceMap = new HashMap<String, Double>(); //Initialise Hashmap priceMap
        priceMap.put("Beef", 4.50);
        priceMap.put("Chicken", 3.00);
        priceMap.put("Fish", 4.00);
        priceMap.put("Egg", 2.00);
        priceMap.put("Lettuce", 0.50);
        priceMap.put("Tomato", 0.50);
        priceMap.put("Pickle", 0.50);
        priceMap.put("Onion", 0.50);
        priceMap.put("Cheese", 1.00);
        priceMap.put("Mayonaise", 0.50);
        priceMap.put("Mustard", 0.70);

        sizePrice = new HashMap<String, Double>(); //Initliase Hashmap sizePrice (For multiplier later)
        sizePrice.put("Small",0.0);
        sizePrice.put("Regular",0.20);
        sizePrice.put("Large",0.30);
        sizePrice.put("Gigantic",0.50);

    }

    public void calculate()
    {
        priceTotal = (fatPrice+proteinPrice+fiberPrice)+ (fatPrice+proteinPrice+fiberPrice) * multiplier; //Dont Get It init bruv
        priceText.setText(String.valueOf(priceTotal));

    }
    public void Selected()
    {

        myRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i)
                {
                    radio = (RadioButton) findViewById(i);
                    proteinPrice = priceMap.get(radio.getText()); //Check which protein will get selected according to Text "Beef" or whatever
                    calculate();
                }
        });


        for (final CheckBox checkBox : checkboxesFiberArray) //Fiber
        {
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {

                    if (fiberTotalSelections == 3 && bChecked) //If 3 are checked.
                    {
                          compoundButton.setChecked(false);
                            fiberPrice=1.00; //Cap the price to $1
                            calculate();
                    }

                    else if (bChecked) //If a box is checked
                    {
                        if (fiberTotalSelections<=2) //if fiberTotalSecltions is less than equal to 2
                        {
                            fiberPrice += priceMap.get(checkBox.getText().toString()); //fiberPrice += to the check box
                            fiberTotalSelections++; //Increment totalSelections by 1
                            calculate(); //Call Calculate function
                        }
                    }

                    else if (!bChecked ) //If Unchecked
                    {
                        if ( fiberTotalSelections<=2) //And selections are less than equal to 2
                        {
                            fiberPrice -= priceMap.get(checkBox.getText().toString()); //fiberPrice -= the price of that checkbox
                            calculate(); //Calculate
                        }
                        fiberTotalSelections--; // decrement the box
                    }

                    if(fiberTotalSelections==3) //if total fiber selections == 3
                    {
                        fiberPrice=1.00; //Cap the price to $1
                        calculate();
                    }
                }
            });
        }

        for (final CheckBox checkBox : checkboxesFatArray) //Fat
        {
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                    if (bChecked) //If box for fat checked
                    {
                        fatPrice += priceMap.get(checkBox.getText().toString()); //fatPrice += to whatever is checked
                        calculate(); //Call calculate function
                    }
                    else if (!bChecked) //Once you uncheck
                    {

                        fatPrice -= priceMap.get(checkBox.getText().toString()); //Deduct
                        calculate();
                    }
                }
            });
        }

        Spinner spinner  = findViewById(R.id.spinner1); //Declare Spinner
        String[] sizes = {"Small","Regular","Large","Gigantic"}; //String Array for sizes
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>( //New Array Adapter, initialise with sizes array
                this,android.R.layout.simple_spinner_item,sizes);

        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(spinnerArrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l)
            {
                System.out.println("Hello");
                String item = parent.getItemAtPosition(i).toString();

                switch(item)
                {
                    case ("Small"):
                        multiplier = sizePrice.get("Small");
                        calculate();
                        break;
                    case ("Regular"):
                        multiplier = sizePrice.get("Regular");
                        calculate();
                        break;

                    case ("Large"):

                        multiplier = sizePrice.get("Large");
                        calculate();
                        break;

                    case ("Gigantic"):
                       multiplier = sizePrice.get("Gigantic");
                        calculate();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });
        ord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if (myRadio.getCheckedRadioButtonId() == -1) //If nothing checked
                {
                    Toast toast=Toast.makeText(getApplicationContext(),"No selection made in Protein section",Toast.LENGTH_SHORT);
                    View view_2 =toast.getView();
                    view_2.setBackgroundColor(Color.GRAY);
                    toast.show();                  }
                else
                {
                    Toast toast=Toast.makeText(getApplicationContext(),"Total price is " + priceTotal,Toast.LENGTH_SHORT);
                    View view_2 =toast.getView();
                    view_2.setBackgroundColor(Color.GRAY);
                    toast.show();
                }
            }
        });

        res.setOnClickListener(new View.OnClickListener()
        { //Reset Button
            @Override
            public void onClick(View view)
            {
                cheeseBox.setChecked(false);
                letBox.setChecked(false);
                mayoBox.setChecked(false);
                onBox.setChecked(false);
                picBox.setChecked(false);
                tomBox.setChecked(false);
                musBox.setChecked(false);

                 //radio.setSelected(false);
                //radio.setChecked(false);
                myRadio.setOnCheckedChangeListener(null);
                myRadio.clearCheck();
                myRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
                {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int i)
                    {
                        radio = (RadioButton) findViewById(i);
                        proteinPrice = priceMap.get(radio.getText());

                        calculate();
                    }
                });
                proteinPrice = 0.0;
                calculate();
            }
        });
    }
}



