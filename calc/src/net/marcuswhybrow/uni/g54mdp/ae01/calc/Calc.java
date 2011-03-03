package net.marcuswhybrow.uni.g54mdp.ae01.calc;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.widget.AdapterView.OnItemClickListener;

public class Calc extends Activity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        final TextView display = (TextView) findViewById(R.id.display);
        
        Button digitButtons[] = {
            (Button) findViewById(R.id.digit0),
            (Button) findViewById(R.id.digit1),
            (Button) findViewById(R.id.digit2),
            (Button) findViewById(R.id.digit3),
            (Button) findViewById(R.id.digit4),
            (Button) findViewById(R.id.digit5),
            (Button) findViewById(R.id.digit6),
            (Button) findViewById(R.id.digit7),
            (Button) findViewById(R.id.digit8),
            (Button) findViewById(R.id.digit9)
        };
        
        Button digitPeriod = (Button) findViewById(R.id.digitPeriod);
        Button operationEquals = (Button) findViewById(R.id.operationEquals);
        Button operationDivide = (Button) findViewById(R.id.operationDivide);
        Button operationMultiply = (Button) findViewById(R.id.operationMultiply);
        Button operationSubtract = (Button) findViewById(R.id.operationSubtract);
        Button operationAdd = (Button) findViewById(R.id.operationAdd);
        
        for (Button button : digitButtons) {
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    display.append(((Button) v).getText());
                }
            });
        }
    }
}
