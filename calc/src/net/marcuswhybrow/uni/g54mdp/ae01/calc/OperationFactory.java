package net.marcuswhybrow.uni.g54mdp.ae01.calc;

public class OperationFactory {
    private static OperationFactory operationFactory = null;
    
    private OperationFactory() {};
    
    public static OperationFactory get() {
        if (operationFactory == null)
            operationFactory = new OperationFactory();
        return operationFactory;
    }
    
    public BinaryOperation getOperation(char c) {
        switch(c) {
            case BinaryOperation.DIVIDE:
                return new BinaryOperation.DivideBinaryOperation();
            case BinaryOperation.MULTIPLY:
                return new BinaryOperation.MultiplyBinaryOperation();
            case BinaryOperation.SUBTRACT:
                return new BinaryOperation.SubtractBinaryOperation();
            case BinaryOperation.ADD:
                return new BinaryOperation.AddBinaryOperation();
        }
        return null;
    }
}