package pl.comp.generatorfaktur.model;

public class NumbersToWords {

    public static String convert(double amount) {

        String amountString = String.valueOf(amount);

        String[] parts = amountString.split("\\.");

        int beforeDot = parts[0].length();
        int afterDot = parts.length > 1 ? parts[1].length() : 0;

        return amountString;
    }
}
