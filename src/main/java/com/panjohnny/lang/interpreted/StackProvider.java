package com.panjohnny.lang.interpreted;

import java.util.LinkedList;

public class StackProvider {
    LinkedList<Integer> stack;
    public int index = 0;
    public int skipHint;
    public StackProvider() {
        this.stack = new LinkedList<>();
        this.stack.addLast(0);
    }

    public void moveLeft() {
        index--;
        if (index < 0) {
            index = stack.size() - 1;
        }
    }

    public void moveRight() {
        index++;
        while (index >= stack.size()) {
            stack.addLast(0);
        }
    }

    public void set(int value) {
        stack.set(index, value);
    }

    public int get() {
        return stack.get(index);
    }

    @Override
    public String toString() {
        return stack.toString();
    }

    public boolean skipHint() {
        return skipHint > 0;
    }

    public String asString() {
        StringBuilder sb = new StringBuilder();
        for (int a : stack) {
            sb.append((char) a);
        }
        return sb.toString();
    }
}
