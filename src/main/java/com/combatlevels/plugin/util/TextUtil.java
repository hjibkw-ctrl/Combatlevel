package com.combatlevels.plugin.util;

public class TextUtil {

    // تدرج الألوان اللي تلف عليها (أحمر - ذهبي - أصفر - أخضر - سماوي - بنفسجي فاتح)
    private static final char[] RAINBOW_COLORS = {'c', '6', 'e', 'a', 'b', 'd'};

    /**
     * يرجع نفس النص بس كل حرف فيه بلون مختلف (تأثير قوس قزح)، بدون ما يلون المسافات.
     */
    public static String rainbow(String text) {
        StringBuilder sb = new StringBuilder();
        int colorIndex = 0;

        for (char c : text.toCharArray()) {
            if (c == ' ') {
                sb.append(' ');
                continue;
            }
            sb.append('§').append(RAINBOW_COLORS[colorIndex % RAINBOW_COLORS.length]);
            sb.append(c);
            colorIndex++;
        }

        return sb.toString();
    }
}
