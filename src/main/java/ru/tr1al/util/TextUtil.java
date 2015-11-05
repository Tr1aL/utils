package ru.tr1al.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TextUtil {

    public static String encodeHTML(String s) {
        if (s == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (char c : s.toCharArray()) {
            switch (c) {
                case '<':
                    sb.append("&lt;");
                    break;
                case '>':
                    sb.append("&gt;");
                    break;
                case '&':
                    sb.append("&amp;");
                    break;
                case '\"':
                    sb.append("&quot;");
                    break;
                default:
                    sb.append(c);
            }
        }
        return sb.toString();
    }

    public static Integer getIntegerOrNull(String s) {
        try {
            return Integer.parseInt(s.replaceAll(" ", ""));
        } catch (Exception e) {
            return null;
        }
    }

    public static Double getDoubleOrNull(String s) {
        try {
            s = s.replaceAll(" ", "").replaceAll(",", ".");
            return Double.parseDouble(s);
        } catch (NumberFormatException e) {
            try {
                s = s.replaceAll(" ", "").replaceAll("\\.", ",");
                return Double.parseDouble(s);
            } catch (NumberFormatException e1) {
                return null;
            }
        }
    }

    public static Long getLongOrNull(String s) {
        try {
            return Long.parseLong(s);
        } catch (Exception e) {
            return null;
        }
    }

    public static Boolean getBooleanOrNull(String s) {
        try {
            return Boolean.parseBoolean(s);
        } catch (Exception e) {
            return null;
        }
    }

    public static String emptyToNull(String s) {
        if (s == null || s.trim().length() == 0)
            return null;
        else return s;
    }

    public static String nullToEmpty(String s) {
        if (s == null) {
            return "";
        } else {
            return s;
        }
    }

    public static boolean isNotNull(String value) {
        return value != null && !value.isEmpty();
    }

    public static boolean isNotSpaces(String value) {
        return value != null && !value.trim().isEmpty();
    }

    public static boolean isNull(String value) {
        return value == null || value.trim().isEmpty();
    }

    public static boolean equals2Objects(Object s1, Object s2) {
        if (s1 == null && s2 == null) {
            return true;
        }
        if (s1 == null) {
            return false;
        }
        if (s2 == null) {
            return false;
        }
        return s1.equals(s2);
    }


    public static String escapeForCSV(String o) {
        if (TextUtil.isNotNull(o)) {
            return o.replaceAll("\"", "\"\"");
        }
        return o;
    }

    /**
     * This methods converts array of string to list of integer
     *
     * @param value input array of string
     * @return list of integer
     */
    public static List<Integer> convertArrayStringToListInteger(String value) {
        List<Integer> list = new ArrayList<>();
        if (value != null) {
            String[] array = value.split(",");
            for (String line : array) {
                Integer intValue = getIntegerOrNull(line.trim());
                if (intValue != null) {
                    list.add(intValue);
                }
            }
        }
        return list;
    }

    /**
     * This methods converts a list of integer to a string
     *
     * @param ids input list of ints
     * @return string
     */
    public static String convertListIntegerToString(Collection<Integer> ids) {
        StringBuilder sb = new StringBuilder();
        for (Integer id : ids) {
            if (sb.length() > 0) sb.append(",");
            sb.append(id);
        }
        return sb.toString();
    }

    public static Integer getIntegerValueOrDef(Object o, Integer def) {
        if (o == null) {
            return def;
        }
        if (o instanceof Long) {
            return ((Long) o).intValue();
        } else if (o instanceof Integer) {
            return (Integer) o;
        } else if (o instanceof Double) {
            return ((Double) o).intValue();
        } else if (o instanceof BigInteger) {
            return ((BigInteger) o).intValue();
        } else if (o instanceof BigDecimal) {
            return ((BigDecimal) o).intValue();
        }
        return def;
    }

    public static Double getDoubleValueOrDef(Object o, Double def) {
        if (o == null) {
            return def;
        }
        if (o instanceof Long) {
            return ((Long) o).doubleValue();
        } else if (o instanceof Integer) {
            return ((Integer) o).doubleValue();
        } else if (o instanceof Double) {
            return (Double) o;
        } else if (o instanceof BigInteger) {
            return ((BigInteger) o).doubleValue();
        } else if (o instanceof BigDecimal) {
            return ((BigDecimal) o).doubleValue();
        }
        return def;
    }

    public static Long getLongValueOrDef(Object o, Long def) {
        if (o == null) {
            return def;
        }
        if (o instanceof Long) {
            return (Long) o;
        } else if (o instanceof Integer) {
            return ((Integer) o).longValue();
        } else if (o instanceof Double) {
            return ((Double) o).longValue();
        } else if (o instanceof BigInteger) {
            return ((BigInteger) o).longValue();
        } else if (o instanceof BigDecimal) {
            return ((BigDecimal) o).longValue();
        }
        return def;
    }

    public static Timestamp getTimestampValueOrDef(Object o, Timestamp def) {
        if (o == null) {
            return def;
        }
        if (o instanceof Timestamp) {
            return (Timestamp) o;
        }
        return def;
    }

    public static boolean isTrue(Boolean b) {
        return b != null && b;
    }

    public static <T> T getDefIfNull(T obj, T def) {
        if (obj == null) {
            return def;
        }
        return obj;
    }

    public static String getShortText(String text, Integer length) {
        if (isNotNull(text)) {
            if (length == null || length < 1) {
                length = 100;
            }
            if (text.length() > length) {
                text = text.substring(0, length - 3) + "...";
            }
        }
        return text;
    }

    @SafeVarargs
    public static <T> T coalesce(T... arr) {
        for (T obj : arr) {
            if (obj != null) {
                return obj;
            }
        }
        return null;
    }
}
