package net.marcuswhybrow.uni.g54mdp.ae01.calc;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.widget.AdapterView.OnItemClickListener;

public class Calc extends Activity
{
    private enum State { INITIAL, NUMBER, OPERATION, ANSWER }
    private enum Operation { DIVIDE, MULTIPLY, SUBTRACT, ADD }
    
    private State state = State.INITIAL;
    private Operation operation = null;
    
    private float previous;
    private int previousLength;
    
    private Button digitButtons[];
    private Button operationButtons[];
    
    private Button operationEquals;
    private Button operationDivide;
    private Button operationMultiply;
    private Button operationSubtract;
    private Button operationAdd;
    
    private void setPrevious(CharSequence s) {
        this.previous = Float.valueOf(s.toString()).floatValue();
        this.previousLength = s.length();
    }
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
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
                    switch(state) {
                        case INITIAL:
                        case ANSWER:
                            display.setText(b.getText());
                            break;
                        case OPERATION:
                        case NUMBER:
                            display.append(b.getText());
                            break;
                    }
                    state = State.NUMBER;
                }
            });
        }
        
        for (Button button : operationButtons) {
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Button b = (Button) v;
                    setPrevious(display.getText());
                    CharSequence operator = b.getText();
                    display.append(operator);
                    
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
                    
                    state = State.OPERATION;
                }
            });
        }
        
        operationEquals.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (state == State.NUMBER) {
                    Button b = (Button) v;
                    float current = Float.valueOf(display.getText().toString().substring(previousLength+1)).floatValue();
                    float result = 0.0f;
                    switch(operation) {
                        case DIVIDE:
                            result = previous / current;
                            break;
                        case MULTIPLY:
                            result = previous * current;
                            break;
                        case SUBTRACT:
                            result = previous - current;
                            break;
                        case ADD:
                            result = previous + current;
                            break;
                    }
                    
                    String visualResult;
                    if ((int) result == result)
                        visualResult = Integer.toString((int) result);
                    else
                        visualResult = Float.toString(result);
                    
                    display.setText(visualResult);
                    state = State.ANSWER;
                }
            }
        });
    }
}
