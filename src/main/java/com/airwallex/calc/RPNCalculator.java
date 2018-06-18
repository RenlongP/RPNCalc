package com.airwallex.calc;

import com.airwallex.common.Constant;
import com.airwallex.common.ErrorCode;
import com.airwallex.mamo.MamoPad;
import com.airwallex.mamo.MamoPadImpl;
import com.airwallex.operate.OperateHandler;
import com.airwallex.operate.Operator;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;

import static java.lang.System.*;

public class RPNCalculator {

    private Stack<BigDecimal> workingDir;
    private MamoPad<Stack<BigDecimal>> mamoPad;
    private DecimalFormat format;
    private static final String DISPLAY_FORMAT = "#.##########";
    private static final List<String> EXIT_CMD = Arrays.asList("quit", "q");

    public RPNCalculator() {
        this.workingDir = new Stack<>();
        this.mamoPad = new MamoPadImpl();
        this.format = new DecimalFormat(DISPLAY_FORMAT);
        format.setRoundingMode(RoundingMode.DOWN);
    }

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
            if (StringUtils.isNotEmpty(ele)) {
                Operator opr = Operator.find(ele);
                try {
                    if (opr != null) {
                        calc(opr, execPos);
                    } else {
                        getWorkingDir().push(new BigDecimal(ele));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return -1;
                }
                logHistory((Stack<BigDecimal>) getWorkingDir().clone());
                execPos += ele.length() + Constant.PARAM_DELIMITER.length();
            }
        }
        printStack();

        return 0;
    }

    private boolean isValidInputString(String input, List<String> resultMsg) {
        boolean isValid = true;
        if (StringUtils.isEmpty(input)) {
            isValid = false;
        } else {
            List<String> invalidEles = new LinkedList<>();
            for (String ele : input.split(Constant.PARAM_DELIMITER)) {
                if (StringUtils.isNotEmpty(ele) && !Operator.isValidOperator(ele) && !NumberUtils.isNumber(ele)) {
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

    private void printStack() {
        out.println("stack: " + getStackContents());
    }

    public String getStackContents() {
        StringBuilder sb = new StringBuilder();
        for (BigDecimal ele : getWorkingDir()) {
            sb.append(this.format.format(ele)).append(Constant.PARAM_DELIMITER);
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    public void calc(Operator opr, int execPos) {
        if (!opr.executable(getWorkingDir(), getMamoPad())) {
            String msg = ErrorCode.PARAMETER_INSUFFICIENT.getMessage(opr.getOperatorText(), execPos + 1);
            err.println(msg);
            throw new IllegalStateException(msg);
        } else {
            setWorkingDir(OperateHandler.handle(getWorkingDir(), getMamoPad(), opr));
        }
    }

    private void logHistory(Stack<BigDecimal> numbers) {
        getMamoPad().makeNote(numbers);
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
