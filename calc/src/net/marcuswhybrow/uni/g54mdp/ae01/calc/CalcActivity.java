package net.marcuswhybrow.uni.g54mdp.ae01.calc;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;

public class CalcActivity extends Activity
{
    private Calc calc;
    
    private Button digitButtons[];
    private Button operationButtons[];
    
    private Button operationEquals;
    private Button operationDivide;
    private Button operationMultiply;
    private Button operationSubtract;
    private Button operationAdd;
    
    private Button clear;
    
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        final TextView display = (TextView) findViewById(R.id.display);
        calc = new Calc(display);
        
        digitButtons = new Button[] {
            (Button) findViewById(R.id.digit0),
            (Button) findViewById(R.id.digit1),
            (Button) findViewById(R.id.digit2),
            (Button) findViewById(R.id.digit3),
            (Button) findViewById(R.id.digit4),
            (Button) findViewById(R.id.digit5),
            (Button) findViewById(R.id.digit6),
            (Button) findViewById(R.id.digit7),
            (Button) findViewById(R.id.digit8),
            (Button) findViewById(R.id.digit9),
            (Button) findViewById(R.id.digitPeriod)
        };
        
        operationEquals = (Button) findViewById(R.id.operationEquals);
        operationDivide = (Button) findViewById(R.id.operationDivide);
        operationMultiply = (Button) findViewById(R.id.operationMultiply);
        operationSubtract = (Button) findViewById(R.id.operationSubtract);
        operationAdd = (Button) findViewById(R.id.operationAdd);
        
        clear = (Button) findViewById(R.id.clear);
        
        operationButtons = new Button[] {
            operationDivide,
            operationMultiply,
            operationSubtract,
            operationAdd
        };
        
        for (Button button : digitButtons) {
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Button b = (Button) v;
                    calc.selectDigit(b.getText().charAt(0));
                }
            });
        }
        
        for (Button button : operationButtons) {
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Button b = (Button) v;
                    calc.selectOperation(b.getText().charAt(0));
                }
            });
        }
        
        operationEquals.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                calc.selectEquals();
            }
        });
        
        clear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                calc.selectClear();
            }
        });
    }
}
