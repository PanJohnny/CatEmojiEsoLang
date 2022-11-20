package com.panjohnny.lang.interpreted;

import com.panjohnny.lang.compiled.Compiler;
import com.panjohnny.lang.compiled.Token;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.function.Consumer;

public final class Interpreter {
    public static StackProvider run(String inp, @Nullable StackProvider stack) {
        if (stack == null) {
            stack = new StackProvider();
        }
        char[] results = inp.toCharArray();
        for (char s : results) {
            Token tk = Token.fromCode(s + "");
            Consumer<StackProvider> cons = Operations.OPERATIONS.get(tk);
            if (cons != null)
                cons.accept(stack);
        }
        return stack;
    }

    public static StackProvider eval(String inp, @Nullable StackProvider stack) {
        return run(Compiler.compile(inp), stack);
    }
}
