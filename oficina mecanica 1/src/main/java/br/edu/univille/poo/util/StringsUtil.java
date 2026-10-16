package br.edu.univille.poo.util;

import java.util.Objects;

public class StringsUtil {

    public static boolean isBlank(String string) {
        return Objects.isNull(string) || string.isBlank();
    }

    public static String somenteNumeros(String string) {
        return Objects.isNull(string) ? "" : string.replaceAll("\\D", "");
    }

}
