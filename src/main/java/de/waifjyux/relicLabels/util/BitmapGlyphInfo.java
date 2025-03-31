package de.waifjyux.relicLabels.util;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A collection of all available bitmap glyph infos containing the default character width.
 */
@SuppressWarnings("unused")
public enum BitmapGlyphInfo {
    DEFAULT(' ', 3),
    UNICODE_0020(' ', 3),
    UNICODE_0021('!', 1),
    UNICODE_0022('"', 3),
    UNICODE_0023('#', 5),
    UNICODE_0024('$', 5),
    UNICODE_0025('%', 5),
    UNICODE_0026('&', 5),
    UNICODE_0027('\'', 1),
    UNICODE_0028('(', 3),
    UNICODE_0029(')', 3),
    UNICODE_002A('*', 3),
    UNICODE_002B('+', 5),
    UNICODE_002C(',', 1),
    UNICODE_002D('-', 5),
    UNICODE_002E('.', 1),
    UNICODE_002F('/', 5),
    UNICODE_0030('0', 5),
    UNICODE_0031('1', 5),
    UNICODE_0032('2', 5),
    UNICODE_0033('3', 5),
    UNICODE_0034('4', 5),
    UNICODE_0035('5', 5),
    UNICODE_0036('6', 5),
    UNICODE_0037('7', 5),
    UNICODE_0038('8', 5),
    UNICODE_0039('9', 5),
    UNICODE_003A(':', 1),
    UNICODE_003B(';', 1),
    UNICODE_003C('<', 4),
    UNICODE_003D('=', 5),
    UNICODE_003E('>', 4),
    UNICODE_003F('?', 5),
    UNICODE_0040('@', 6),
    UNICODE_0041('A', 5),
    UNICODE_0042('B', 5),
    UNICODE_0043('C', 5),
    UNICODE_0044('D', 5),
    UNICODE_0045('E', 5),
    UNICODE_0046('F', 5),
    UNICODE_0047('G', 5),
    UNICODE_0048('H', 5),
    UNICODE_0049('I', 3),
    UNICODE_004A('J', 5),
    UNICODE_004B('K', 5),
    UNICODE_004C('L', 5),
    UNICODE_004D('M', 5),
    UNICODE_004E('N', 5),
    UNICODE_004F('O', 5),
    UNICODE_0050('P', 5),
    UNICODE_0051('Q', 5),
    UNICODE_0052('R', 5),
    UNICODE_0053('S', 5),
    UNICODE_0054('T', 5),
    UNICODE_0055('U', 5),
    UNICODE_0056('V', 5),
    UNICODE_0057('W', 5),
    UNICODE_0058('X', 5),
    UNICODE_0059('Y', 5),
    UNICODE_005A('Z', 5),
    UNICODE_005B('[', 3),
    UNICODE_005C('\\', 5),
    UNICODE_005D(']', 3),
    UNICODE_005E('^', 5),
    UNICODE_005F('_', 5),
    UNICODE_0060('`', 2),
    UNICODE_0061('a', 5),
    UNICODE_0062('b', 5),
    UNICODE_0063('c', 5),
    UNICODE_0064('d', 5),
    UNICODE_0065('e', 5),
    UNICODE_0066('f', 4),
    UNICODE_0067('g', 5),
    UNICODE_0068('h', 5),
    UNICODE_0069('i', 1),
    UNICODE_006A('j', 5),
    UNICODE_006B('k', 4),
    UNICODE_006C('l', 2),
    UNICODE_006D('m', 5),
    UNICODE_006E('n', 5),
    UNICODE_006F('o', 5),
    UNICODE_0070('p', 5),
    UNICODE_0071('q', 5),
    UNICODE_0072('r', 5),
    UNICODE_0073('s', 5),
    UNICODE_0074('t', 3),
    UNICODE_0075('u', 5),
    UNICODE_0076('v', 5),
    UNICODE_0077('w', 5),
    UNICODE_0078('x', 5),
    UNICODE_0079('y', 5),
    UNICODE_007A('z', 5),
    UNICODE_007B('{', 3),
    UNICODE_007C('|', 1),
    UNICODE_007D('}', 3),
    UNICODE_007E('~', 6),
    UNICODE_00E4('ä', 5),
    UNICODE_00C4('Ä', 5),
    UNICODE_00FC('ü', 5),
    UNICODE_00DC('Ü', 5),
    UNICODE_00F6('ö', 5),
    UNICODE_00D6('Ö', 5);

    public static List<BitmapGlyphInfo> list = new ArrayList<>(EnumSet.allOf(BitmapGlyphInfo.class));

    private int value;
    private char character;

    BitmapGlyphInfo(char c, int i) {
        this.value = i;
        this.character = c;
    }

    public int getValue() {
        return value;
    }

    public char getCharacter() {
        return character;
    }

    public static int getLengt(char character) {

        for(BitmapGlyphInfo all : list) if(character == all.getCharacter()) return all.getValue();

        return 0;

    }

    public static String[] getChatDistribution(String message, int max) {
        String[] original = message.split("(?<=\\s)|(?=\\n)");
        message = message.replaceAll("§[0-9a-f]", "");
        ArrayList<String> list = new ArrayList<>();
        String[] msg = message.split("(?<=\\s)|(?=\\n)");
        int currentLength = 0;
        String currentLine = "";

        for (int i = 0; i < msg.length; i++) {
            if (msg[i].equals("\n")) {
                list.add("§7" + currentLine);
                currentLine = "";
                currentLength = 0;
                continue;
            }

            int wordLength = 0;
            for (char all : msg[i].toCharArray()) {
                wordLength += BitmapGlyphInfo.getLengt(all) + 1;
            }

            if (currentLength + wordLength <= max) {
                currentLine = currentLine + original[i];
                currentLength += wordLength;
            } else {
                list.add("§7" + currentLine);
                currentLength = wordLength;
                currentLine = original[i];
            }
            if (i == msg.length - 1) list.add("§7" + currentLine);
        }

        return list.toArray(new String[0]);

    }

    public static String[] getExperimentalChatDistribution(String message, int max) {
        Pattern hexPattern = Pattern.compile("(?i)#[0-9A-F]{6}");
        Matcher matcher = hexPattern.matcher(message);
        while (matcher.find()) {
            String hex = matcher.group();
            StringBuilder legacy = new StringBuilder("§x");
            for (char c : hex.substring(1).toCharArray()) {
                legacy.append("§").append(c);
            }
            message = message.replace(hex, legacy.toString());
        }

        // Local lambda to retrieve the last valid color code from a string.
        java.util.function.Function<String, String> getLastColors = (text) -> {
            String last = "";
            for (int i = 0; i < text.length() - 1; i++) {
                if (text.charAt(i) == '§') {
                    char code = text.charAt(i + 1);
                    if ("0123456789AaBbCcDdEeFfKkLlMmNnOoRr".indexOf(code) > -1) {
                        last = "§" + code;
                    }
                }
            }
            return last;
        };

        // Split the original message into parts (preserving spaces and newlines)
        String[] original = message.split("(?<=\\s)|(?=\\n)");
        // Remove all color codes (including hex-converted ones) for length calculations.
        String strippedMessage = message.replaceAll("(?i)§[0-9A-FK-ORX]", "");
        ArrayList<String> list = new ArrayList<>();
        String[] msg = strippedMessage.split("(?<=\\s)|(?=\\n)");
        int currentLength = 0;
        String currentLine = "";

        for (int i = 0; i < msg.length; i++) {
            if (msg[i].equals("\n")) {
                String lastColors = getLastColors.apply(currentLine);
                list.add(lastColors + currentLine);
                currentLine = lastColors; // Carry over the last color for the new line.
                currentLength = 0;
                continue;
            }
            int wordLength = 0;
            for (char c : msg[i].toCharArray()) {
                wordLength += BitmapGlyphInfo.getLengt(c) + 1;
            }
            if (currentLength + wordLength <= max) {
                currentLine += original[i];
                currentLength += wordLength;
            } else {
                String lastColors = getLastColors.apply(currentLine);
                list.add(lastColors + currentLine);
                currentLine = lastColors + original[i];
                currentLength = wordLength;
            }
            if (i == msg.length - 1) {
                String lastColors = getLastColors.apply(currentLine);
                list.add(lastColors + currentLine);
            }
        }
        return list.toArray(new String[0]);
    }






}