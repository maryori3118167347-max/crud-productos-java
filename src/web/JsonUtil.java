package web;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Parser JSON minimalista, suficiente para el objeto Producto que recibimos del frontend.
// No es un parser JSON de propósito general, solo entiende pares "clave": valor planos.
public class JsonUtil {

    public static String getString(String json, String clave) {
        Pattern pattern = Pattern.compile("\"" + clave + "\"\\s*:\\s*\"((?:[^\"\\\\]|\\\\.)*)\"");
        Matcher matcher = pattern.matcher(json);
        if (matcher.find()) {
            return matcher.group(1).replace("\\\"", "\"").replace("\\\\", "\\");
        }
        return null;
    }

    public static Double getNumber(String json, String clave) {
        Pattern pattern = Pattern.compile("\"" + clave + "\"\\s*:\\s*(-?\\d+(?:\\.\\d+)?)");
        Matcher matcher = pattern.matcher(json);
        if (matcher.find()) {
            return Double.parseDouble(matcher.group(1));
        }
        return null;
    }
}
