package com.airwallex.calc;

import com.airwallex.common.Constant;

import java.io.Console;

import static java.lang.System.exit;

public class RPNBootstrap {

    public static void main(String[] args) {
        Console console = System.console();
        if (console == null) {
            String err = "Unable to fetch console";
            System.out.println(err);
            return;
        }

        RPNCalculator rpn = new RPNCalculator();
        int exitCode = 0;
        while (exitCode == 0) {
            exitCode = rpn.exec(console.readLine());
        }
        if (exitCode != Constant.EXIT_CODE_NORMAL_QUIT) {
            exit(exitCode);
        }
    }
}
