package ru.netology.tour.data;

import com.github.javafaker.CreditCardType;
import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Random;

public class DataHelper {
    private static final Faker faker = new Faker(new Locale("en"));
    private static final Faker fakerRus = new Faker(new Locale("ru"));

    private DataHelper() {
    }

    @Value
    @RequiredArgsConstructor
    public static class CardInfo {
        String cardNumber;
        String month;
        String year;
        String holder;
        String cvc;
    }

    public static String getApprovedCardNumber() {
        return ("1111 2222 3333 4444");
    }

    public static String getDeclinedCardNumber() {
        return ("5555 6666 7777 8888");
    }

    public static String getRandomCardNumber() {
        return faker.finance().creditCard(CreditCardType.VISA);
    }

    public static String getCardNumberWith15Numbers() {
        return ("1111 2222 3333 444");
    }

    public static String getCardNumberWithCyrillic() {
        return ("карта");
    }

    public static String getCardNumberWithLatin() {
        return ("card");
    }

    public static String getCardNumberWithSymbols() {
        return ("!#$%^&*");
    }

    public static String getMonth() {
        String[] month = new String[]{
                "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"
        };
        Random random = new Random();
        return month[random.nextInt(month.length)];
    }

    public static String getMonthWithZero() {
        return ("00");
    }

    public static String getMonthOverTwelve() {
        return ("13");
    }

    public static String getMonthWithLatin() {
        return ("december");
    }

    public static String getMonthWithCyrillic() {
        return ("май");
    }

    public static String getMonthWithSymbols() {
        return ("$%^&*");
    }

    public static String getMonthWithOneNumber() {
        return ("7");
    }

    public static String getSpecifiedMonth(int shift) {
        return LocalDate.now().plusMonths(shift).format(DateTimeFormatter.ofPattern("MM"));
    }

    public static String getYear(int shift) {
        return LocalDate.now().plusYears(shift).format(DateTimeFormatter.ofPattern("yy"));
    }

    public static String getYearWithZero() {
        return ("00");
    }

    public static String getYearWith1Number() {
        return ("6");
    }

    public static String getYearWithCyrillic() {
        return ("ноль");
    }

    public static String getYearWithLatin() {
        return ("twenty");
    }

    public static String getYearWithSymbols() {
        return ("$%^&*");
    }

    public static String getHolder() {
        return faker.name().firstName() + " " + faker.name().lastName();
    }

    public static String getHolderFirstNameWithDash() {
        return faker.name().firstName() + "-" + faker.name().firstName() + " " + faker.name().lastName();
    }

    public static String getHolderLastNameWithDash() {
        return faker.name().firstName() + " " + faker.name().lastName() + "-" + faker.name().lastName();
    }

    public static String getHolderNameWithDash() {
        return faker.name().firstName() + "-" + faker.name().firstName() + " " + faker.name().lastName() + "-" + faker.name().lastName();
    }

    public static String getHolderOnlyFirstName() {
        return faker.name().firstName();
    }

    public static String getHolderNameWithCyrillic() {
        return fakerRus.name().firstName() + " " + fakerRus.name().lastName();
    }

    public static String getHolderNameWithLatAndRus() {
        return faker.name().firstName() + " " + fakerRus.name().lastName();
    }

    public static String getHolderNameWithSymbols() {
        return ("!@#$%^&*()_+~");
    }

    public static String getHolderNameWithNumbers() {
        return ("1234567890");
    }

    public static String getHolderFirstNameWithSpace() {
        return faker.name().firstName() + " " + faker.name().firstName() + " " + faker.name().lastName();
    }

    public static String getCvc() {
        String[] cvc = new String[]{
                "123", "234", "456", "678", "789", "890", "098", "987", "876", "765", "654", "543",
                "432", "321", "111", "222", "333", "444", "555", "666", "777", "888", "999", "342",
                "583", "935", "178", "308", "925", "177", "834", "035", "273", "159", "482", "669"
        };
        Random random = new Random();
        return cvc[random.nextInt(cvc.length)];
    }

    public static String getCvcWith2Numbers() {
        return ("66");
    }

    public static String getCvcWith1Number() {
        return ("6");
    }

    public static String getCvcWithSymbols() {
        return ("$@~");
    }

    public static String getCvcWithCyrillic() {
        return ("два");
    }

    public static String getCvcWithLatin() {
        return ("one");
    }
}