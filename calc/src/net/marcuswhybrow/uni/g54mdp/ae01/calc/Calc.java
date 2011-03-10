package net.marcuswhybrow.uni.g54mdp.ae01.calc;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.widget.AdapterView.OnItemClickListener;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import android.util.Log;

public class Calc extends Activity
{
    private enum State { INITIAL, NUM1, OPERATION, NUM2, ANSWER }
    private enum Operation { DIVIDE, MULTIPLY, SUBTRACT, ADD }
    
    private State state = State.INITIAL;
    private Operation operation = null;
    
    private BigDecimal previous;
    private int previousLength = 0;
    private boolean hasPeriod = false;
    
    private Button digitButtons[];
    private Button operationButtons[];
    
    private Button operationEquals;
    private Button operationDivide;
    private Button operationMultiply;
    private Button operationSubtract;
    private Button operationAdd;
    
    private Button clear;
    
    private static final String TAG = "Calc";
    MathContext mathContext;
    
    private void setPrevious(CharSequence s) {
        if (s.length() > 0)
            this.previous = new BigDecimal(s.toString());
        this.previousLength = s.length();
    }
    
    private CharSequence clearDisplay(CharSequence t) {
        switch(state) {
            case INITIAL:
                // do nothing
                break;
            case OPERATION:
                operation = null;
                state = State.NUM1;
                // no break here!
                setPrevious("");
            case NUM1:
            case NUM2:
                if (t.toString().length() > 0) {
                    String s = t.toString().substring(0, t.length()-1);
                    if (state == State.NUM2) {
                        Character c = s.charAt(s.length() - 1);
                        if (!(Character.isDigit(c) || c == '.')) {
                            state = State.OPERATION;
                        }
                    }
                    return s;
                }
                break;
            case ANSWER:
                return "";
        }
        return t;
    }
    
    private CharSequence getAnswer(CharSequence cs) {
        if (state == State.NUM2) {
            Log.d(TAG, "display: " + cs);
            BigDecimal current = new BigDecimal(cs.toString().substring(previousLength+1));
            BigDecimal result = null;
            switch(operation) {
                case DIVIDE:
                    try {
                        result = previous.divide(current, this.mathContext);
                    } catch(ArithmeticException ae) {
                        state = State.INITIAL;
                        hasPeriod = false;
                        return "∞";
                    }
                    break;
                case MULTIPLY:
                    result = previous.multiply(current, this.mathContext);
                    break;
                case SUBTRACT:
                    result = previous.subtract(current, this.mathContext);
                    break;
                case ADD:
                    result = previous.add(current, this.mathContext);
                    break;
            }
            
            String visualResult;
            try {
                // An ArithmeticException is thrown if this BigDecimal has a 
                // nonzero fractional part.
                visualResult = result.toBigIntegerExact().toString();
            } catch(ArithmeticException ae) {
                visualResult = result.toString();
            }
            
            state = State.ANSWER;
            
            if ("Infinity".equals(visualResult)) {
                visualResult = "∞";
                state = State.INITIAL;
            }
            
            hasPeriod = false;
            return visualResult;
        }
        return cs;
    }
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        mathContext = new MathContext(7, RoundingMode.DOWN);
        
        final TextView display = (TextView) findViewById(R.id.display);
        
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
                    CharSequence s = b.getText();
                    if (! ".".equals(s) || hasPeriod == false) {
                        if (".".equals(s))
                            hasPeriod = true;
                        switch(state) {
                            case INITIAL:
                            case ANSWER:
                                display.setText(s);
                                break;
                            case OPERATION:
                                state = State.NUM2;
                                display.append(s);
                                break;
                            case NUM1:
                            case NUM2:
                                display.append(s);
                                break;
                        }
                        if (state != State.NUM1 && state != State.NUM2)
                            state = State.NUM1;
                    }
                }
            });
        }
        
        for (Button button : operationButtons) {
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Button b = (Button) v;
                    if (display.getText().length() > 0) {
                        CharSequence operator = b.getText();
                        
                        if (state == State.NUM2) {
                            display.setText(getAnswer(display.getText()));
                        }
                        
                        switch (state) {
                            case NUM1:
                            case NUM2:
                            case ANSWER:
                                setPrevious(display.getText());
                                display.append(operator);
                            
                                state = State.OPERATION;
                                break;
                            case OPERATION:
                                String p = display.getText().toString();
                                display.setText(p.substring(0, p.length()-1) + b.getText());
                                break;
                        }
                    
                        if (previousLength > 0) {
                            switch(operator.toString().charAt(0)) {
                                case '÷':
                                    operation = Operation.DIVIDE;
                                    break;
                                case '×':
                                    operation = Operation.MULTIPLY;
                                    break;
                                case '−':
                                    operation = Operation.SUBTRACT;
                                    break;
                                case '+':
                                    operation = Operation.ADD;
                                    break;
                            }
                        }
                    
                        hasPeriod = false;
                    }
                }
            });
        }
        
        operationEquals.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                display.setText(getAnswer(display.getText()));
            }
        });
        
        clear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                display.setText(clearDisplay(display.getText()));
            }
        });
    }
}
