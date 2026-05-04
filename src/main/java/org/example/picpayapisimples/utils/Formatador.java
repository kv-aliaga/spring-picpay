package org.example.picpayapisimples.utils;

import org.example.picpayapisimples.handler.exception.validation.input.InvalidCPFException;
import org.example.picpayapisimples.handler.exception.validation.input.InvalidDDDException;
import org.example.picpayapisimples.handler.exception.validation.input.InvalidPhoneException;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Set;

public class Formatador {
    public static NumberFormat moeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

    public static String formatarCpf(String cpf){
        if (cpf.length() != 11) throw new InvalidCPFException();
        return cpf.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
    }

    public static Set<String> DDDS_VALIDOS = Set.of(
        "11", "12", "13", "14", "15", "16", "17", "18", "19",
        "21", "22", "24", "27", "28",
        "31", "32", "33", "34", "35", "37", "38",
        "41", "42", "43", "44", "45", "46", "47", "48", "49",
        "51", "53", "54", "55",
        "61", "62", "63", "64", "65", "66", "67", "68", "69",
        "71", "73", "74", "75", "77", "79",
        "81", "82", "83", "84", "85", "86", "87", "88", "89",
        "91", "92", "93", "94", "95", "96", "97", "98", "99"
    );

    public static String formatarTelefone(String telefone) {
        if (telefone == null) return null;

        String tel = telefone.replaceAll("\\D", "");

        boolean temDdi = tel.startsWith("55");

        if (temDdi) {
            tel = tel.substring(2);
        }

        if (tel.length() != 10 && tel.length() != 11) {
            throw new InvalidPhoneException();
        }

        String ddd = tel.substring(0, 2);

        if (!DDDS_VALIDOS.contains(ddd)) {
            throw new InvalidDDDException();
        }

        if (temDdi) {
            if (tel.length() == 10) {
                return tel.replaceAll("(\\d{2})(\\d{4})(\\d{4})", "+55 ($1) $2-$3");
            }

            return tel.replaceAll("(\\d{2})(\\d{5})(\\d{4})", "+55 ($1) $2-$3");
        }

        if (tel.length() == 10) {
            return tel.replaceAll("(\\d{2})(\\d{4})(\\d{4})", "($1) $2-$3");
        }

        return tel.replaceAll("(\\d{2})(\\d{5})(\\d{4})", "($1) $2-$3");
    }
}
