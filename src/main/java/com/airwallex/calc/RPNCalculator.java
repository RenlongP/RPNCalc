package com.airwallex.calc;

import com.airwallex.common.Constant;
import com.airwallex.common.ErrorCode;
import com.airwallex.mamo.MamoPad;
import com.airwallex.mamo.MamoPadImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;

import static java.lang.System.err;
import static java.lang.System.out;

public class RPNCalculator {

    private Stack<BigDecimal> workingDir = new Stack<>();
    private MamoPad<Stack<BigDecimal>> mamoPad = new MamoPadImpl();
    private static final String DISPLAY_FORMAT = "#.##########";
    private static final List<String> EXIT_CMD = Arrays.asList("quit", "q");

    public int exec(String input) {

        if (EXIT_CMD.contains(input.toLowerCase())) {
            out.println("Exiting RPN Calculator. Bye!");
            return Constant.EXIT_CODE_NORMAL_QUIT;
        }

        List<String> errorMsgs = new LinkedList<>();
        if (!isValidInputString(input, errorMsgs)) {
            for (String errorMsg : errorMsgs) {
                out.println(errorMsg);
            }
            return 0;
        }

        int execPos = 0;
        for (String ele : input.split(Constant.PARAM_DELIMITER)) {
            Operator opr = Operator.find(ele);
            try {
                if (opr != null) {
                    calc(workingDir, opr, execPos);
                } else {
                    workingDir.push(new BigDecimal(ele));
                }
            } catch (Exception e) {
                e.printStackTrace();
                return -1;
            }
            logHistory((Stack<BigDecimal>) workingDir.clone());
            execPos += ele.length() + Constant.PARAM_DELIMITER.length();
        }
        printstack();

        return 0;
    }

    private boolean isValidInputString(String input, List<String> resultMsg) {
        boolean isValid = true;
        if (StringUtils.isEmpty(input)) {
            isValid = false;
        } else {
            List<String> invalidEles = new LinkedList<>();
            for (String ele : input.split(Constant.PARAM_DELIMITER)) {
                if (!Operator.isValidOperator(ele) && !NumberUtils.isNumber(ele)) {
                    invalidEles.add(ele);
                }
            }
            if (!invalidEles.isEmpty()) {
                resultMsg.add(ErrorCode.WRONG_INPUT.getMessage(invalidEles));
                isValid = false;
            }
        }
        return isValid;
    }

    private void printstack() {
        out.println("stack: "+ getStackContents());
    }

    public String getStackContents() {
        StringBuilder sb = new StringBuilder();
        for (BigDecimal ele : getWorkingDir()) {
            if (ele instanceof BigDecimal) {
                BigDecimal d = new BigDecimal(ele.toString());
                DecimalFormat format = new DecimalFormat(DISPLAY_FORMAT);
                sb.append(format.format(d.setScale(10, RoundingMode.DOWN)));
            } else {
                sb.append(ele.toString());
            }
            sb.append(Constant.PARAM_DELIMITER);
        }
        if(sb.length()>0){
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    public Stack<BigDecimal> calc(Stack<BigDecimal> numbers, Operator opr, int execPos) {
        List<BigDecimal> calcArgs = new ArrayList<>();
        if (numbers.size() < opr.getOperand()) {
            String err = ErrorCode.PARAMETER_INSUFFICIENT.getMessage(opr.getOperatorText(), execPos + 1);
            out.println(err);
            throw new IllegalStateException(err);
        }
        for (int i = 0; i < opr.getOperand(); i++) {
            calcArgs.add(numbers.pop());
        }
        if (opr.equals(Operator.UNDO)) {
            undo();
        } else if (opr.equals(Operator.CLEAR)) {
            numbers.clear();
        } else {
            numbers.push(opr.apply(calcArgs));
        }
        return numbers;
    }

    private void logHistory(Stack<BigDecimal> numbers) {
        getMamoPad().makeNote(numbers);
    }

    private void undo() {
        if (getMamoPad().getSize() < 2) {
            err.println("Undo operation failed since no enough record");
            return;
        }

        this.setWorkingDir(getMamoPad().readLatest());
    }

    public Stack<BigDecimal> getWorkingDir() {
        return workingDir;
    }

    public void setWorkingDir(Stack<BigDecimal> workingDir) {
        this.workingDir = workingDir;
    }

    public MamoPad<Stack<BigDecimal>> getMamoPad() {
        return mamoPad;
    }
}
