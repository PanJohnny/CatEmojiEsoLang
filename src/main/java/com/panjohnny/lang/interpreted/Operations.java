package com.panjohnny.lang.interpreted;

import com.panjohnny.lang.Main;
import com.panjohnny.lang.compiled.Token;

import java.util.HashMap;
import java.util.function.Consumer;

public final class Operations {
    public static final HashMap<Token, Consumer<StackProvider>> OPERATIONS = new HashMap<>();

    public static void register() {
        // 😺 x++
        OPERATIONS.put(Token.INCREMENT, Operations::increment);

        // 😿 x--
        OPERATIONS.put(Token.DECREMENT, Operations::decrement);

        // 😽 >>>
        OPERATIONS.put(Token.RIGHT, Operations::moveRight);

        // 🙀 <<<
        OPERATIONS.put(Token.LEFT, Operations::moveLeft);

        // 😻 x**2
        OPERATIONS.put(Token.POW, Operations::pow);

        // 😾 sqrt
        OPERATIONS.put(Token.SQRT, Operations::sqrt);

        // 😼 x = 0
        OPERATIONS.put(Token.NIL, Operations::zero);

        // 🐈 if(x > {i--} x {i++}) x = 1 : x = 0
        OPERATIONS.put(Token.LOGIC1, Operations::logic);

        // 😸 if(x == 1) {} : {skipTo(😹)}
        OPERATIONS.put(Token.LOGIC2, Operations::if1);

        // 😹 <end_of_logic>
        OPERATIONS.put(Token.END_OF_LOGIC, Operations::endOfLogic);

        OPERATIONS.put(Token.END, (stack) -> {
            if (stack.skipHint())
                return;
            Main.end(stack);
        });
    }

    public static void increment(StackProvider stack) {
        if (stack.skipHint())
            return;
        stack.set(stack.get()+1);
        if (Main.debug)
            System.out.println("+ (" + stack.get() + ")");
    }

    public static void decrement(StackProvider stack) {
        if (stack.skipHint())
            return;
        stack.set(stack.get()-1);
        if (Main.debug)
            System.out.println("- (" + stack.get() + ")");
    }

    public static void moveRight(StackProvider stack) {
        if (stack.skipHint())
            return;
        stack.moveRight();
        if (Main.debug)
            System.out.println("> (" + stack.get() + ")");
    }

    public static void moveLeft(StackProvider stack) {
        if (stack.skipHint())
            return;
        stack.moveLeft();
        if (Main.debug)
            System.out.println("< (" + stack.get() + ")");
    }

    public static void pow(StackProvider stack) {
        if (stack.skipHint())
            return;
        stack.set(stack.get()*stack.get());
        if (Main.debug)
            System.out.println("pow (" + stack.get() + ")");
    }

    public static void sqrt(StackProvider stack) {
        if (stack.skipHint())
            return;
        stack.set((int)Math.sqrt(stack.get()));
        if (Main.debug)
            System.out.println("sqrt (" + stack.get() + ")");
    }

    public static void zero(StackProvider stack) {
        if (stack.skipHint())
            return;
        stack.set(0);
        if (Main.debug)
            System.out.println("0 (" + stack.get() + ")");
    }

    public static void logic(StackProvider stack) {
        if (stack.skipHint())
            return;
        // if(x > {i--} x {i++}) x = 1 : x = 0

        int current = stack.get();
        stack.moveLeft();
        int before = stack.get();
        stack.moveRight();
        if (current > before)
            stack.set(1);
        else
            stack.set(0);
        if (Main.debug)
            System.out.println("LOGIC1 (" + stack.get() + ")");
    }

    public static void if1(StackProvider stack) {
        if (stack.skipHint())
            return;
        if (stack.get() != 1)
            stack.skipHint+=1;
        if (Main.debug)
            System.out.println("LOGIC2 (" + stack.get() + ")");
    }

    public static void endOfLogic(StackProvider stack) {
        if (!stack.skipHint())
            return;
        stack.skipHint-=1;
        if (Main.debug)
            System.out.println("END_OF_LOGIC (" + stack.get() + ")");
    }
}
