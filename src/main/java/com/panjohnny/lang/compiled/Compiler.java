package com.panjohnny.lang.compiled;

import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

public class Compiler {
    public static String compile(String source) {
        List<String> emojis = Pattern.compile("\\P{M}\\p{M}*+").matcher(source)
                .results()
                .map(MatchResult::group).toList();
        StringBuilder sb = new StringBuilder();
        for (String emoji : emojis) {
            Token tk = Token.fromEmoji(emoji);
            if (tk != null) {
                sb.append(tk);
            }
        }

        return sb.toString();
    }

    public static String decompile(String str) {
        char[] tokens = str.toCharArray();

        StringBuilder sb = new StringBuilder();
        for (char s : tokens) {
            Token t = Token.fromCode(s + "");
            if (t!=null)
                sb.append(t.emoji);
        }

        return sb.toString();
    }
}