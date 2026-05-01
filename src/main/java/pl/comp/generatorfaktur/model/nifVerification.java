package pl.comp.generatorfaktur.model;

public class nifVerification {
    public static boolean verify(String gettingNip) {
        String nip = gettingNip.toUpperCase().trim();
        if (nip.matches("^[0-9]{8}[A-Z]$")) {
            //NIF
            int nipNumbers = Integer.parseInt(nip.substring(0, 8));
            int index = nipNumbers % 23;
            String controlChar = "TRWAGMYFPDXBNJZSQVHLCKE";

            if (controlChar.charAt(index) == nip.charAt(8)){
                System.out.println("NIF valid");
                return true;
            } else {
                System.out.println("NIF/CIF/NIE is incorrect mathematically");
                System.out.println(index);
                return false;
            }
        }
        else if (nip.matches("^[XYZ][0-9]{7}[A-Z]$")){
            //NIE
            int nipNumbers = 0;
            String mapFirstLetter;
            if (nip.charAt(0) == 'X') {
                mapFirstLetter = "0";
                String check = mapFirstLetter + nip.substring(1, 8);
                nipNumbers = Integer.parseInt(check.substring(0, 8));
            } else if (nip.charAt(0) == 'Y') {
                mapFirstLetter = "1";
                String check = mapFirstLetter + nip.substring(1, 8);
                nipNumbers = Integer.parseInt(check.substring(0, 8));
            } else if (nip.charAt(0) == 'Z') {
                mapFirstLetter = "2";
                String check = mapFirstLetter + nip.substring(1, 8);
                nipNumbers = Integer.parseInt(check.substring(0, 8));
            }

            int index = nipNumbers % 23;
            String controlChar = "TRWAGMYFPDXBNJZSQVHLCKE";

            if (controlChar.charAt(index) == nip.charAt(8)){
                System.out.println("NIE valid");
                return true;
            } else {
                System.out.println("NIF/CIF/NIE is incorrect mathematically");
                System.out.println(index);
                return false;
            }
        }

        else if (nip.matches("^[A-Z][0-9]{7}[A-Z0-9]$")) {
            // CIF

            String CONTROL_DIGITS = "JABCDEFGHI";

            char firstLetter = nip.charAt(0);
            String digits = nip.substring(1, 8);
            char controlChar = nip.charAt(8);

            int sum = 0;

            for (int i = 0; i < digits.length(); i++) {
                int digit = digits.charAt(i) - '0';

                boolean shouldDouble = ((i + 1) % 2 == 0);

                if (shouldDouble) {
                    digit *= 2;
                    digit = (digit / 10) + (digit % 10);
                }

                sum += digit;
            }

            int controlDigit = (10 - (sum % 10)) % 10;

            char expectedControlLetter = CONTROL_DIGITS.charAt(controlDigit);
            char expectedControlDigit = Character.forDigit(controlDigit, 10);

            boolean numericControl = "ABEH".indexOf(firstLetter) >= 0;

            boolean isValid =
                    numericControl
                            ? controlChar == expectedControlDigit
                            : controlChar == expectedControlLetter;

            return isValid;
        }
        else {
            System.out.println("NIF/CIF/NIE is incorrect mathematically");
            return false;
        }
    }
}
