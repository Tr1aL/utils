package ru.tr1al.util;

/**
 * Класс переводит русский текст в транслит. Например, строка "Текст" будет
 * преобразована в "Tekst".
 */
public class TranslitUtil {

    private static final String[] charTable = new String[81];

    private static final char START_CHAR = 'Ё';

    static {
        charTable['А' - START_CHAR] = "A";
        charTable['Б' - START_CHAR] = "B";
        charTable['В' - START_CHAR] = "V";
        charTable['Г' - START_CHAR] = "G";
        charTable['Д' - START_CHAR] = "D";
        charTable['Е' - START_CHAR] = "E";
        charTable['Ё' - START_CHAR] = "E";
        charTable['Ж' - START_CHAR] = "ZH";
        charTable['З' - START_CHAR] = "Z";
        charTable['И' - START_CHAR] = "I";
        charTable['Й' - START_CHAR] = "I";
        charTable['К' - START_CHAR] = "K";
        charTable['Л' - START_CHAR] = "L";
        charTable['М' - START_CHAR] = "M";
        charTable['Н' - START_CHAR] = "N";
        charTable['О' - START_CHAR] = "O";
        charTable['П' - START_CHAR] = "P";
        charTable['Р' - START_CHAR] = "R";
        charTable['С' - START_CHAR] = "S";
        charTable['Т' - START_CHAR] = "T";
        charTable['У' - START_CHAR] = "U";
        charTable['Ф' - START_CHAR] = "F";
        charTable['Х' - START_CHAR] = "H";
        charTable['Ц' - START_CHAR] = "C";
        charTable['Ч' - START_CHAR] = "CH";
        charTable['Ш' - START_CHAR] = "SH";
        charTable['Щ' - START_CHAR] = "SH";
        charTable['Ъ' - START_CHAR] = "'";
        charTable['Ы' - START_CHAR] = "Y";
        charTable['Ь' - START_CHAR] = "'";
        charTable['Э' - START_CHAR] = "E";
        charTable['Ю' - START_CHAR] = "U";
        charTable['Я' - START_CHAR] = "YA";

        for (int i = 0; i < charTable.length; i++) {
            char idx = (char) ((char) i + START_CHAR);
            char lower = new String(new char[]{idx}).toLowerCase().charAt(0);
            if (charTable[i] != null) {
                charTable[lower - START_CHAR] = charTable[i].toLowerCase();
            }
        }
    }

    private static String[] RUS = {"а", "б", "в", "г", "д", "е", "з", "и", "й", "к", "л", "м", "н", "о", "п", "р", "с", "т", "у", "ф", "х", "ц", "ъ", "ы", "ь", "э"};
    private static String[] ENG = {"a", "b", "v", "g", "d", "e", "z", "i", "j", "k", "l", "m", "n", "o", "p", "r", "s", "t", "u", "f", "h", "c", "", "y", "", "e"};
    private static String[] RUS_SP = {"ё", "ж", "ч", "ш", "щ", "ю", "я"};
    private static String[] ENG_SP = {"jo", "zh", "ch", "sh", "sch", "ju", "ja"};
    private static String[] RUS_UP = {"А", "Б", "В", "Г", "Д", "Е", "З", "И", "Й", "К", "Л", "М", "Н", "О", "П", "Р", "С", "Т", "У", "Ф", "Х", "Ц", "Ъ", "Ы", "Ь", "Э"};
    private static String[] ENG_UP = {"A", "B", "V", "G", "D", "E", "Z", "I", "J", "K", "L", "M", "N", "O", "P", "R", "S", "T", "U", "F", "H", "C", "", "Y", "", "E"};
    private static String[] RUS_SP_UP = {"Ё", "Ж", "Ч", "Ш", "Щ", "Ю", "Я"};
    private static String[] ENG_SP_UP = {"JO", "ZH", "CH", "SH", "SCH", "JU", "JA"};


    /**
     * Переводит русский текст в транслит. В результирующей строке
     * каждая русская буква будет заменена на соответствующую английскую.
     * Не русские символы останутся прежними.
     *
     * @param text исходный текст с русскими символами
     * @return результат
     */
    public static String toTranslit(String text) {
        if (text == null) {
            return null;
        }
        char charBuffer[] = text.toCharArray();
        StringBuilder sb = new StringBuilder(text.length());
        for (char symbol : charBuffer) {
            int i = symbol - START_CHAR;
            if (i >= 0 && i < charTable.length) {
                String replace = charTable[i];
                sb.append(replace == null ? symbol : replace);
            } else {
                sb.append(symbol);
            }
        }
        return sb.toString();
    }

    public static String toTranslitForDNS(String text, String rpl) {
        text = toTranslit(text);
        if (text == null) {
            return null;
        }
        return text.replaceAll("[^-0-9a-zA-Z_ ]+", rpl);
    }

    public static String toTranslitForUrlPath(String text, String rpl) {
        text = toTranslit(text);
        if (text == null) {
            return null;
        }
        return text.replaceAll("[^-0-9a-zA-Z_]+", rpl);
    }

    public static String fullTranslit(String text) {
        String ret = "";
        for (String str : text.split(" ")) {
            if (isRussian(str)) {
                //to english
                for (int i = 0; i < RUS_SP.length; i++) {
                    str = str.replaceAll(RUS_SP[i], ENG_SP[i]);
                    str = str.replaceAll(RUS_SP_UP[i], ENG_SP_UP[i]);
                }
                for (int i = 0; i < RUS.length; i++) {
                    str = str.replaceAll(RUS[i], ENG[i]);
                    str = str.replaceAll(RUS_UP[i], ENG_UP[i]);
                }
            } else {
                //to russian
                for (int i = 0; i < ENG_SP.length; i++) {
                    str = str.replaceAll(ENG_SP[i], RUS_SP[i]);
                    str = str.replaceAll(ENG_SP_UP[i], RUS_SP_UP[i]);
                }
                for (int i = 0; i < ENG.length; i++) {
                    if (ENG[i] != null && !ENG[i].isEmpty()) {
                        str = str.replaceAll(ENG[i], RUS[i]);
                    }
                    if (ENG_UP[i] != null && !ENG_UP[i].isEmpty()) {
                        str = str.replaceAll(ENG_UP[i], RUS_UP[i]);
                    }
                }
            }
            ret += str + " ";
        }
        return ret.trim();
    }

    public static boolean isRussian(String text) {
        return getEnglishCount(text) == 0;
//        int rus = 0;
//        for (char c : text.toCharArray()) {
//            if (c >= 'а' && c <= 'я' || c >= 'А' && c <= 'Я'){
//                rus++;
//            }
//        }
//        return rus > text.length() / 4;
    }

    public static int getEnglishCount(String text) {
        int eng = 0;
        for (char c : text.toCharArray()) {
            if (c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z') {
                eng++;
            }
        }
        return eng;
    }

    public static int getRussianCount(String text) {
        int rus = 0;
        for (char c : text.toCharArray()) {
            if (c >= 'а' && c <= 'я' || c >= 'А' && c <= 'Я') {
                rus++;
            }
        }
        return rus;
    }

    private static String[] ENG_LAT2RUS = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
    private static String[] RUS_LAT2RUS = {"а", "б", "ц", "д", "е", "ф", "г", "х", "и", "й", "к", "л", "м", "н", "о", "п", "я", "р", "с", "т", "у", "в", "в", "х", "ы", "з"};

    public static String lat2Rus(String text) {
        String str = text;
        for (int i = 0; i < ENG_LAT2RUS.length; i++) {
            str = str.replaceAll(ENG_LAT2RUS[i], RUS_LAT2RUS[i]);
            str = str.replaceAll(ENG_LAT2RUS[i].toUpperCase(), RUS_LAT2RUS[i].toUpperCase());
        }
        return str;
    }

    public static void main(String[] args) {
//        final String test = "Привет, Мир. Это Длинная Строка с Разными символами русского алфавита.";
//        System.out.println("toTranslit(" + test + ") = " + toTranslit(test));

//        System.out.println(toTranslit(test));
//        System.out.println(fullTranslit(test));
//        System.out.println(fullTranslit(fullTranslit(test)));

//        System.out.println(lat2Rus("LEGO 9491 Стар Wарс Джеонозианская пушка"));

        for (int i = 'а'; i <= 'я'; i++) {
            System.out.println(String.valueOf((char) i) + " (" + i + ")");
        }
        for (int i = 'А'; i <= 'Я'; i++) {
            System.out.println(String.valueOf((char) i) + " (" + i + ")");
        }

//        int MAX_ITERATION = 1000000;
//        System.out.println("Test speed");
//        long time = System.currentTimeMillis();
//        for (int i = 0; i < MAX_ITERATION; i++) {
//            toTranslit(test);
//        }
//
//
//        System.out.println((System.currentTimeMillis() - time));
    }
}
