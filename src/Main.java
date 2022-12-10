public class Main {
    static enum NumberType{
        Arabic,
        Roman
    }

    public static void main(String[] args) {

        var scanner = new java.util.Scanner(System.in);
        System.out.println(calc(scanner.nextLine()));

    }

    //Возвращает значение римской цифры
    static int getValue(char c) {
        char[] roamn_array = {'I', 'V', 'X'};
        int[] arabic_array = {1, 5, 10 };
        for(int i = 0; i < roamn_array.length; i++) {
            if(c == roamn_array[i]) {
                return arabic_array[i];
            }
        }
        return -1;
    }

    static int romanToArabic(String number) {
        if(!number.matches("[IVX]+") || number.equals("") || number == null) {
            throw new IllegalArgumentException("Incorrectly entered data");
        }
        int length = number.length();
        int result = 0;
        for(int i = 0; i < length; i++) {
            if(getValue(number.charAt(i)) == -1) {
                throw new IllegalArgumentException("Illegal Character");
            }
            if(i < length - 3) {
                if(number.charAt(i) == number.charAt(i + 1) && number.charAt(i) == number.charAt(i + 2) && number.charAt(i) == number.charAt(i + 3)) {
                    throw new IllegalArgumentException("More than 3 same Characters successively");
                }
            }


            //Применение правил
            if(i < length - 1) {
                int currentChar = getValue(number.charAt(i));
                int nextChar = getValue(number.charAt(i + 1));
                if(currentChar < nextChar) {
                    result = result + nextChar - currentChar;
                    i++;
                }
                else {
                    result = result + currentChar;
                }
            }
            else {
                result = result + getValue(number.charAt(i));
            }
        }
        return result;
    }

    static String arabicToRoman(int number) {
        //
        if(number < 1) throw new IllegalArgumentException("В римской системе нет отрицательных чисел.");
       // if(number > 10) throw new IllegalArgumentException("Поддерживает только числа от 1 до 10.");

        String result = "";
        int[] values = {10, 9, 5, 4, 1};
        String[] romanNumerals = {"X", "IX", "V", "IV", "I"};
        int i = 0;

        //Здесь происходит главная задача
        while(i < values.length) {
            if(number >= values[i]) {
                result += romanNumerals[i];
                number = number - values[i];
            }
            else {
                i++;
            }
        }
        return result;
    }

    static boolean likelyNumeric(char digit) {
        if ((digit >= '0' & digit <= '9') | (digit >= 'A' & digit <= 'Z'))
            return true;

        return false;
    }

    static int tryParseNumber(String number, NumberType number_type) throws NullPointerException, IllegalArgumentException {
        if (number == null | number_type == null)
            throw new NullPointerException();

        if (number.length() == 0)
            throw new IllegalArgumentException("Строка не является математической операцией.");

        if (number_type == NumberType.Roman)
            return romanToArabic(number);

        try {
            return Integer.parseInt(number);
        }
        catch (NumberFormatException e) {
            throw new IllegalArgumentException("Строка не является математической операцией.");
        }
    }

    static int tryOperation(int a, int b, char operation) throws IllegalArgumentException {
        if (a > 10)
            throw new IllegalArgumentException("Числа вне диапазона: " + a + "> 10.");

        if (b > 10)
            throw new IllegalArgumentException("Числа вне диапазона: " + b + "> 10.");

        switch (operation) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                return a / b;
        }

        throw new IllegalArgumentException();
    }

    static String calc(String input) throws NullPointerException, IllegalArgumentException {
        if (input == null)
            throw new NullPointerException();

        String a = "";
        String b = "";
        char operation = 0;

        int i = 0;

        // Поиск начала первого числа: останавливается в начале числа
        for (; i < input.length(); i++)
            if (likelyNumeric(input.charAt(i)))
                break;

        // Чтение первого числа
        for (; i < input.length(); i++)
            if (likelyNumeric(input.charAt(i)))
                a += input.charAt(i);
            else
                break;

        // Поиск символа операции
        for (; i < input.length(); i++)
            if ("+-/*".indexOf(input.charAt(i)) > -1) {
                operation = input.charAt(i);
                i++;
                break;
            }

        // Нахождение начала второго числа
        for (; i < input.length(); i++)
            if (likelyNumeric(input.charAt(i)))
                break;

        //Чтение второго числа
        for (; i < input.length(); i++)
            if (likelyNumeric(input.charAt(i)))
                b += input.charAt(i);
            else
                break;

        if (i != input.length())
            throw new IllegalArgumentException("Содержит более двух операндов");

        NumberType node_type = NumberType.Roman;

        if ((a.charAt(0) >= '0') & (a.charAt(0) <= '9'))
            node_type = NumberType.Arabic;



        int ans = tryOperation(tryParseNumber(a, node_type), tryParseNumber(b, node_type), operation);

        if (node_type == NumberType.Roman)
            return arabicToRoman(ans);

        return Integer.toString(ans);
    }
}