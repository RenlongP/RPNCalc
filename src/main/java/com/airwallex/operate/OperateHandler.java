package com.airwallex.operate;

import com.airwallex.mamo.MamoPad;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class OperateHandler {
    private static final List<Operator> STACK_OPERS = Arrays.asList(Operator.UNDO, Operator.CLEAR);

    private OperateHandler(){}
    public static Stack<BigDecimal> handle(Stack<BigDecimal> workingDir, MamoPad<Stack<BigDecimal>> mamoPad, Operator opr) {
        if(STACK_OPERS.contains(opr)){
            workingDir = opr.getResultStack(workingDir,mamoPad);
        }else {
            BigDecimal result = opr.apply(workingDir, mamoPad);
            if (result != null) {
                workingDir.push(result);
            }
        }
        return workingDir;
    }
}
