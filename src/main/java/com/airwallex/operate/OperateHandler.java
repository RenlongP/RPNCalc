package com.airwallex.operate;

import com.airwallex.mamo.MamoPad;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

public class OperateHandler {
    private static final List<Operator> STACK_OPERS = Arrays.asList(Operator.UNDO, Operator.CLEAR);

    private OperateHandler(){}
    public static Deque<BigDecimal> handle(Deque<BigDecimal> workingDir, MamoPad<Deque<BigDecimal>> mamoPad, Operator opr) {
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
