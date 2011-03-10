package net.marcuswhybrow.uni.g54mdp.ae01.calc;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import android.view.View;
import android.widget.TextView;

public class Calc {
    
    private static final Calc calc = null;
    
    private enum State { INITIAL, NUM1, OPERATION, NUM2, ANSWER }
    
    private State state = State.INITIAL;
    
    private BigDecimal previous;
    private int previousLength = 0;
    private boolean hasPeriod = false;
    
    MathContext mathContext = new MathContext(7, RoundingMode.DOWN);
    
    final TextView display;
    
    private String num1String = "";
    private String num2String = "";
    
    private BigDecimal num1 = null;
    private BinaryOperation operation = null;
    private BigDecimal num2 = null;
    
    private boolean currentNumHasDecimalPoint = false;
    
    
    public Calc(TextView display) {
        this.display = display;
    }
    
    
    private void updateDisplay() {
        String operationString = (operation != null) ? operation.toString() : "";
        display.setText(num1String + operationString + num2String);
    }
    
    private void changeNum1(String num1String) {
        this.num1String = num1String;
        try {
            this.num1 = new BigDecimal(num1String);
        } catch(NumberFormatException nfe) {
            this.num1 = null;
        }
    }
    
    private void changeNum1(BigDecimal num1) {
        this.num1 = num1;
        try {
            this.num1String = num1.toBigIntegerExact().toString();
        } catch(ArithmeticException ae) {
            this.num1String = num1.toString();
        }
    }
    
    private boolean isNum1Valid() {
        try {
            new BigDecimal(num1String);
            return true;
        } catch(NumberFormatException nfe) {
            return false;
        }
    }
    
    private void changeNum2(String num2String) {
        this.num2String = num2String;
        try {
            this.num2 = new BigDecimal(num2String);
        } catch(NumberFormatException nfe) {
            this.num2 = null;
        }
    }
    
    private void changeNum2(BigDecimal num2) {
        this.num2 = num2;
        try {
            this.num2String = num2.toBigIntegerExact().toString();
        } catch(ArithmeticException ae) {
            this.num2String = num2.toString();
        }
    }
    
    private boolean isNum2Valid() {
        try {
            new BigDecimal(num2String);
            return true;
        } catch(NumberFormatException nfe) {
            return false;
        }
    }
    
    private void changeOperation(BinaryOperation operation) {
        this.operation = operation;
    }
    
    
    private BigDecimal getAnswer() {
        return this.operation.evaluate(this.num1, this.num2);
    }
    
    
    private void setPrevious(CharSequence s) {
        if (s.length() > 0)
            this.previous = new BigDecimal(s.toString());
        this.previousLength = s.length();
    }
    
    public void selectOperation(char operation) {
        selectOperation(OperationFactory.get().getOperation(operation));
    }
    
    public void selectOperation(BinaryOperation operation) {
        if (state != State.INITIAL && this.isNum1Valid()) {
            if (state == State.NUM2 && this.isNum2Valid()) {
                this.changeNum1(this.getAnswer());
                this.changeNum2("");
                this.currentNumHasDecimalPoint = false;
            }
        
            this.changeOperation(operation);
            state = State.OPERATION;
            this.updateDisplay();
        }
    }
    
    
    public void selectDigit(char c) {
        switch(state) {
            case INITIAL:
            case ANSWER:
                state = State.NUM1;
                this.changeNum1("");
                this.currentNumHasDecimalPoint = false;
            case NUM1:
                if (c != '.' || this.currentNumHasDecimalPoint == false)
                    this.changeNum1(this.num1String + Character.toString(c));
                if (c == '.')
                    this.currentNumHasDecimalPoint = true;
                break;
            case OPERATION:
                state = State.NUM2;
                this.currentNumHasDecimalPoint = false;
            case NUM2:
                if (c != '.' || this.currentNumHasDecimalPoint == false)
                    this.changeNum2(this.num2String + Character.toString(c));
                if (c == '.')
                    this.currentNumHasDecimalPoint = true;
                break;
        }
        
        this.updateDisplay();
    }
    
    public void selectClear() {
        switch(state) {
            case NUM1:
                if (this.num1String.length() > 0) {
                    char removedChar = this.num1String.charAt(this.num1String.length() - 1);
                    this.changeNum1(this.num1String.substring(0, this.num1String.length() - 1));
                    
                    if (removedChar == '.')
                        this.currentNumHasDecimalPoint = false;
                }   
                break;
            case NUM2:
                if (this.num2String.length() > 1) {
                    char removedChar = this.num2String.charAt(this.num2String.length() - 1);
                    this.changeNum2(this.num2String.substring(0, this.num2String.length() - 1));
                    
                    if (removedChar == '.')
                        this.currentNumHasDecimalPoint = false;
                } else {
                    this.changeNum2("");
                    this.currentNumHasDecimalPoint = false;
                    state = State.OPERATION;
                }
                break;
            case OPERATION:
                this.changeOperation(null);
                state = State.NUM1;
                break;
            case ANSWER:
                this.changeNum1("");
                this.currentNumHasDecimalPoint = false;
                state = State.NUM1;
        }
        this.updateDisplay();
    }
    
    public void selectEquals() {
        if (state == State.NUM2 && this.isNum2Valid()) {
            BigDecimal answer = this.getAnswer();
            this.changeNum1(answer);
            this.changeNum2("");
            this.currentNumHasDecimalPoint = false;
            this.changeOperation(null);
            if (answer instanceof BinaryOperation.BigDecimalInfinity)
                state = State.INITIAL;
            else
                state = State.ANSWER;
            this.updateDisplay();
        }
    }
}