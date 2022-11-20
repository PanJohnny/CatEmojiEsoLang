package com.panjohnny.lang.compiled;

public enum Token {
    INCREMENT("\uD83D\uDE3A", "+"),
    DECREMENT("\uD83D\uDE3F", "-"),
    RIGHT("\uD83D\uDE3D", ">"),
    LEFT("\uD83D\uDE40", "<"),
    POW("\uD83D\uDE3B", "p"),
    SQRT("\uD83D\uDE3E", "s"),
    NIL("\uD83D\uDE3C", "0"),
    LOGIC1("\uD83D\uDC08", "("),
    LOGIC2("\uD83D\uDE38", ")"),
    END_OF_LOGIC("\uD83D\uDE38", "}"),
    END("\uD83D\uDCA9", "#");

    final String emoji;
    final String code;
    Token(String emoji, String code) {
        this.emoji = emoji;
        this.code = code;
    }

    public static Token fromEmoji(String emoji) {
        for (Token token : Token.values()) {
            if (token.emoji.equals(emoji)) {
                return token;
            }
        }

        return null;
    }

    public static Token fromCode(String code) {
        for (Token token : Token.values()) {
            if (token.code.equals(code)) {
                return token;
            }
        }

        return null;
    }

    @Override
    public String toString() {
        return code;
    }
}
