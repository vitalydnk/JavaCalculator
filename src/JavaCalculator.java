import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.io.Serial;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

public class JavaCalculator extends JFrame  {
    @Serial
    private static final long serialVersionUID = 1L;

    private final StringBuffer inputBuffer = new StringBuffer();
    private String nextOperator = null;
    private int leftSide = 0;
    private final JLabel inputWindow;
    private final JLabel operatorPointer;

    private class DigitButtonAction extends AbstractAction {
        @Serial
        private static final long serialVersionUID = 1L;
        private final int digit;
        public DigitButtonAction(final int digit) {
            super(Integer.toString(digit));
            this.digit = digit;
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            enterDigit(digit);
        }
    }
    private class OperatorButtonAction extends AbstractAction {
        @Serial
        private static final long serialVersionUID = 1L;
        private final String operator;
        public OperatorButtonAction(final String operator) {
            super(operator);
            this.operator = operator;
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            makeOperation(operator);
        }
    }

    public JavaCalculator() {
        super("Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(300, 300);

        // Create display
        inputWindow = new JLabel();
        inputWindow.setHorizontalAlignment(JLabel.RIGHT);
        inputWindow.setText(Integer.toString(leftSide));

        operatorPointer = new JLabel();
        operatorPointer.setBorder(new EmptyBorder(0, 4, 0, 4));

        final JPanel display = new JPanel();
        display.setLayout(new BorderLayout());
        display.add(inputWindow, BorderLayout.CENTER);
        display.add(operatorPointer, BorderLayout.WEST);

        // Create number buttons
        final JPanel digitPanel = new JPanel();
        digitPanel.setLayout(new GridLayout(4,3));
        final int[] digitKeyOrder = new int[] { 7,8,9,4,5,6,1,2,3 };
        for (int digit : digitKeyOrder) {
            digitPanel.add(new JButton(new DigitButtonAction(digit)));
        }
        digitPanel.add(new JPanel()); // Blank spacer panel
        digitPanel.add(new JButton(new DigitButtonAction(0)));

        // Create operators
        final String[] OPERATORS = { "+","-","*","/","=" };
        final JPanel operatorPanel = new JPanel();
        operatorPanel.setLayout(new GridLayout(OPERATORS.length, 1));
        for (String op : OPERATORS) {
            operatorPanel.add(new JButton(new OperatorButtonAction(op)));
        }

        add(digitPanel, BorderLayout.CENTER);
        add(operatorPanel, BorderLayout.EAST);
        add(display, BorderLayout.NORTH);

        pack();
    }

    private void enterDigit(final int digit) {
        if (digit == 0 && inputBuffer.length() == 0) return;
        inputBuffer.append(digit);
        inputWindow.setText(inputBuffer.toString());
    }

    private int calculate(final int leftSide, final String operator, final int rightSide) {
        if (operator == null) return rightSide;
        else if ("+".equals(operator)) return leftSide + rightSide;
        else if ("-".equals(operator)) return leftSide - rightSide;
        else if ("*".equals(operator)) return leftSide * rightSide;
        else if ("/".equals(operator)) return leftSide / rightSide;
        else if ("=".equals(operator)) return rightSide;
        else {
            throw new IllegalStateException("Unrecognised operator " + operator);
        }
    }

    private void makeOperation(final String operator) {
        try {
            final int rightSide = Integer.parseInt(inputBuffer.toString());
            leftSide = calculate(leftSide, nextOperator, rightSide);
        } catch (NumberFormatException e) {
            // Ignore failure to parse inputBuffer to integer calculate() not called,
            // just carry on and clear the inputBuffer and queue a new operator
        } catch (ArithmeticException e) {
            // Divide by 0 in calculate()
            operatorPointer.setText("");
            inputWindow.setText(e.getMessage());
            nextOperator = null;
            return;
        } catch (IllegalStateException e) {
            // Unrecognised operator
            operatorPointer.setText("");
            inputWindow.setText(e.getMessage());
            nextOperator = null;
            return;
        }
        inputBuffer.setLength(0);
        nextOperator = operator;
        // Update display
        operatorPointer.setText(nextOperator);
        inputWindow.setText(Integer.toString(leftSide));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new JavaCalculator().setVisible(true);
            }
        });
    }
}