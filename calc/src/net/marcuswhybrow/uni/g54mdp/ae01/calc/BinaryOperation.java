package net.marcuswhybrow.uni.g54mdp.ae01.calc;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public abstract class BinaryOperation {
    public static final char DIVIDE = '÷';
    public static final char MULTIPLY = '×';
    public static final char ADD = '+';
    public static final char SUBTRACT = '−';
    
    public static final char INFINITY = '\u221E';
    
    private static final MathContext MATH_CONTEXT = new MathContext(7, RoundingMode.DOWN);
    private final char representation;

    public BinaryOperation(char representation) {
        this.representation = representation;
    }

    @Override
    public String toString() {
        return Character.toString(this.representation);
    }

    public char getRepresentation() {
        return this.representation;
    }

    public abstract BigDecimal evaluate(BigDecimal num1, BigDecimal num2);


    public static final class DivideBinaryOperation extends BinaryOperation {
        public DivideBinaryOperation() {
            super(DIVIDE);
        }
    
        @Override
        public BigDecimal evaluate(BigDecimal num1, BigDecimal num2) {
            try {
                return num1.divide(num2, MATH_CONTEXT);
            } catch(ArithmeticException ae) {
                return new BigDecimalInfinity();
            }
        }
    }

    public static final class MultiplyBinaryOperation extends BinaryOperation {
        public MultiplyBinaryOperation() {
            super(MULTIPLY);
        }
    
        @Override
        public BigDecimal evaluate(BigDecimal num1, BigDecimal num2) {
            return num1.multiply(num2, MATH_CONTEXT);
        }
    }

    public static final class AddBinaryOperation extends BinaryOperation {
        public AddBinaryOperation() {
            super(ADD);
        }
    
        @Override
        public BigDecimal evaluate(BigDecimal num1, BigDecimal num2) {
            return num1.add(num2, MATH_CONTEXT);
        }
    }

    public static final class SubtractBinaryOperation extends BinaryOperation {
        public SubtractBinaryOperation() {
            super(SUBTRACT);
        }
    
        @Override
        public BigDecimal evaluate(BigDecimal num1, BigDecimal num2) {
            return num1.subtract(num2, MATH_CONTEXT);
        }
    }
    
    public static class BigDecimalInfinity extends BigDecimal {
        public BigDecimalInfinity() {
            // This must have a decimal point
            super(1.5);
        }
        
        @Override
        public String toString() {
            return Character.toString(INFINITY);
        }
    }
}