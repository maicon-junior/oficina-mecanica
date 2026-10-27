package br.edu.univille.poo.oficina.util;

public class StringsUtil {

    private StringsUtil() {}

    public static boolean isBlank(String texto) {
        return texto == null || texto.isBlank();
    }

    public static String somenteNumeros(String texto) {
        return texto == null ? "" : texto.replaceAll("\\D", "");
    }

    // 12345678900 -> 123.456.789-00
    public static String formatarCpf(String cpf) {
        String n = somenteNumeros(cpf);
        if (n.length() != 11) {
            return cpf;
        }
        return n.substring(0, 3) + "." + n.substring(3, 6) + "." + n.substring(6, 9) + "-" + n.substring(9);
    }

    public static String limpar(String texto) {
        return texto == null ? null : texto.trim();
    }
}
