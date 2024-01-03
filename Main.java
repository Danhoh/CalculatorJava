import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static final LinkedHashMap<String, String> romanMapping;
    private static final LinkedHashMap<String, String> arabicMapping;
    static {
        romanMapping = new LinkedHashMap<String, String>();
        romanMapping.put("1000", "M");
        romanMapping.put("900", "CM");
        romanMapping.put("500", "D");
        romanMapping.put("400", "CD");
        romanMapping.put("100", "C");
        romanMapping.put("90", "XC");
        romanMapping.put("50", "L");
        romanMapping.put("40", "XL");
        romanMapping.put("10", "X");
        romanMapping.put("9", "IX");
        romanMapping.put("5", "V");
        romanMapping.put("4", "IV");
        romanMapping.put("1", "I");

        arabicMapping = new LinkedHashMap<String, String>();
        arabicMapping.put("X", "10");
        arabicMapping.put("IX", "9");
        arabicMapping.put("VIII", "8");
        arabicMapping.put("VII", "7");
        arabicMapping.put("VI", "6");
        arabicMapping.put("V", "5");
        arabicMapping.put("IV", "4");
        arabicMapping.put("III", "3");
        arabicMapping.put("II", "2");
        arabicMapping.put("I", "1");
    }

    private static String romanize(String nStr) {
        int n = Integer.parseInt(nStr);
        ArrayList<String> orderedKeys = new ArrayList<>(romanMapping.keySet());
        String output = "";

        for (String keyStr : orderedKeys) {
            int key = Integer.parseInt(keyStr);
            while (n >= key) {
                output += romanMapping.get(keyStr);
                n -= key;
            }
        }

        return output;
    }

    private static boolean isArabic(String first, String second) { // true if both arabic and <= 10
        Pattern arabicRegex = Pattern.compile("[1-9]|10");
        boolean firstMatchFound = arabicRegex.matcher(first).matches();
        boolean secondMatchFound = arabicRegex.matcher(second).matches();

        if (firstMatchFound && secondMatchFound) {
            return true;
        }
        return false;
    }

    private static boolean isRoman(String first, String second) { // true if both roman and <= 10
        Pattern romanRegex = Pattern.compile("I|II|III|IV|V|VI|VII|VIII|IX|X");
        boolean firstMatchFound = romanRegex.matcher(first).matches();
        boolean secondMatchFound = romanRegex.matcher(second).matches();

        if (firstMatchFound && secondMatchFound) {
            return true;
        }
        return false;
    }

    private static String evalArabic(String first, String second, String sign) {
        int firstInt = Integer.parseInt(first);
        int secondInt = Integer.parseInt(second);

        switch (sign) {
            case "+": return Integer.toString(firstInt + secondInt);
            case "-": return Integer.toString(firstInt - secondInt);
            case "*": return Integer.toString(firstInt * secondInt);
            case "/": return Integer.toString(firstInt / secondInt);
            default: return "";
        }
    }

    private static String evalRoman(String first, String second, String sign) throws Exception {
        int firstInt = Integer.parseInt(arabicMapping.get(first));
        int secondInt = Integer.parseInt(arabicMapping.get(second));

        switch (sign) {
            case "+": return romanize(Integer.toString(firstInt + secondInt));
            case "-": {
                int result = firstInt - secondInt;

                if (result <= 0) {
                    throw new Exception("Roman operation <= 0");
                }

                return romanize(Integer.toString(result));
            }
            case "*": return romanize(Integer.toString(firstInt * secondInt));
            case "/": {
                int result = firstInt / secondInt;

                if (result <= 0) {
                    throw new Exception("Roman operation <= 0");
                }
                return romanize(Integer.toString(firstInt / secondInt));
            }
            default: return "";
        }
    }

    public static String calc(String input) throws Exception {
        Pattern exprRegex = Pattern.compile("(^[\\dIVX]+) ([\\-\\+\\/\\*]) ([\\dIVX]+)$");
        Matcher exprMatch = exprRegex.matcher(input);
        boolean matchFound = exprMatch.matches();

        if (matchFound) {
            String firstNum = exprMatch.group(1);
            String sign = exprMatch.group(2);
            String secondNum = exprMatch.group(3);

            if (isArabic(firstNum, secondNum)) {
                return evalArabic(firstNum, secondNum, sign);
            }

            if (isRoman(firstNum, secondNum)) {
                return evalRoman(firstNum, secondNum, sign);
            }
        }
        throw new Exception("Incorrect input");
    }

    public static void main(String[] args) {
        Scanner stdInScanner = new Scanner(System.in);

        while (true) {
            String s = stdInScanner.nextLine();
            try {
                System.out.println(calc(s));
            }
            catch(Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
