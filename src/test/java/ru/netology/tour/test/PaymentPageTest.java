package ru.netology.tour.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.tour.data.DataHelper;
import ru.netology.tour.page.PaymentPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.tour.data.DataHelper.*;
import static ru.netology.tour.data.SQLHelper.cleanDatabase;
import static ru.netology.tour.data.SQLHelper.getPaymentStatus;

public class PaymentPageTest {
    PaymentPage paymentPage;

    @AfterAll
    static void tearDownAll() {
        cleanDatabase();
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void setUp() {
        paymentPage = open("http://localhost:8080", PaymentPage.class);

        paymentPage.choosePayment();
    }

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @Test
    @DisplayName("1. Отправка формы оплаты по активной карте с заполнением всех полей валидными данными")
    public void shouldPaymentWhenAllDataValid() {
        paymentPage.verifyFormName();
        paymentPage.fillForm(new DataHelper.CardInfo(getApprovedCardNumber(),
                getMonth(), getYear(3), getHolder(), getCvc()));
        paymentPage.verifyNotificationSuccessPayment();
        assertEquals("APPROVED", getPaymentStatus());
    }

    @Test
    @DisplayName("2. Отправка формы оплаты по активной карте с заполнением всех полей валидными данными. Имя через тире")
    public void shouldPaymentWhenFirstNameWithDash() {
        paymentPage.verifyFormName();
        paymentPage.fillForm(new DataHelper.CardInfo(getApprovedCardNumber(),
                getMonth(), getYear(2), getHolderFirstNameWithDash(), getCvc()));
        paymentPage.verifyNotificationSuccessPayment();
        assertEquals("APPROVED", getPaymentStatus());
    }

    @Test
    @DisplayName("3. Отправка формы оплаты по активной карте с заполнением всех полей валидными данными. Фамилия через тире")
    public void shouldPaymentWhenLastNameWithDash() {
        paymentPage.verifyFormName();
        paymentPage.fillForm(new DataHelper.CardInfo(getApprovedCardNumber(),
                getMonth(), getYear(1), getHolderLastNameWithDash(), getCvc()));
        paymentPage.verifyNotificationSuccessPayment();
        assertEquals("APPROVED", getPaymentStatus());
    }

    @Test
    @DisplayName("4. Отправка формы оплаты по активной карте с заполнением всех полей валидными данными. Имя и фамилия через тире")
    public void shouldPaymentWhenNameWithDash() {
        paymentPage.verifyFormName();
        paymentPage.fillForm(new DataHelper.CardInfo(getApprovedCardNumber(),
                getMonth(), getYear(2), getHolderNameWithDash(), getCvc()));
        paymentPage.verifyNotificationSuccessPayment();
        assertEquals("APPROVED", getPaymentStatus());
    }

    @Test
    @DisplayName("5. Отправка формы оплаты по карте без заполнения полей")
    public void sendEmptyForm() {
        paymentPage.verifyFormName();
        paymentPage.sendEmptyForm();
        paymentPage.verifyErrorNotification(0);
        paymentPage.verifyErrorNotification(1);
        paymentPage.verifyErrorNotification(2);
        paymentPage.verifyErrorNotification(3);
        paymentPage.verifyErrorNotification(4);
    }

    @Test
    @DisplayName("6. Отправка формы оплаты по заблокированной карте с заполнением всех полей валидными данными")
    public void shouldNotPaymentWhenCardDeclined() {
        paymentPage.verifyFormName();
        paymentPage.fillForm(new DataHelper.CardInfo(getDeclinedCardNumber(),
                getMonth(), getYear(1), getHolder(), getCvc()));
        paymentPage.verifyNotificationDeclinedCard();
        assertEquals("DECLINED", getPaymentStatus());
    }

    @Test
    @DisplayName("7. Отправка формы оплаты по активной карте с пустым полем номер карты")
    public void shouldNotPaymentWhenCardNumberIsEmpty() {
        paymentPage.verifyFormName();
        paymentPage.fillForm(new DataHelper.CardInfo("",
                getMonth(), getYear(2), getHolder(), getCvc()));
        paymentPage.verifyNotificationRequiredField(0);
    }

    @Test
    @DisplayName("8. Отправка формы оплаты по активной карте с пустым полем месяц")
    public void shouldNotPaymentWhenMonthIsEmpty() {
        paymentPage.verifyFormName();
        paymentPage.fillForm(new DataHelper.CardInfo(getApprovedCardNumber(),
                "", getYear(3), getHolder(), getCvc()));
        paymentPage.verifyNotificationRequiredField(0);
    }

    @Test
    @DisplayName("9. Отправка формы оплаты по активной карте с пустым полем год")
    public void shouldNotPaymentWhenYearIsEmpty() {
        paymentPage.verifyFormName();
        paymentPage.fillForm(new DataHelper.CardInfo(getApprovedCardNumber(),
                getMonth(), "", getHolder(), getCvc()));
        paymentPage.verifyNotificationRequiredField(0);
    }

    @Test
    @DisplayName("10. Отправка формы оплаты по активной карте с пустым полем владелец")
    public void shouldNotPaymentWhenHolderIsEmpty() {
        paymentPage.verifyFormName();
        paymentPage.fillForm(new DataHelper.CardInfo(getApprovedCardNumber(),
                getMonth(), getYear(3), "", getCvc()));
        paymentPage.verifyNotificationRequiredField(0);
    }

    @Test
    @DisplayName("11. Отправка формы оплаты по активной карте с пустым полем cvc/cvv")
    public void shouldNotPaymentWhenCvcIsEmpty() {
        paymentPage.verifyFormName();
        paymentPage.fillForm(new DataHelper.CardInfo(getApprovedCardNumber(),
                getMonth(), getYear(2), getHolder(), null));
        paymentPage.verifyNotificationRequiredField(1);
    }

    @Test
    @DisplayName("12. Появление сообщения под полем номер карты при вводе невалидных данных (15 цифр)")
    public void errorMsgWhenEnterInTheCardNumberField15Digits() {
        paymentPage.verifyFormName();
        paymentPage.fillForm(new DataHelper.CardInfo(getCardNumberWith15Numbers(),
                getMonth(), getYear(1), getHolder(), getCvc()));
        paymentPage.verifyNotificationWrongFormat(0);
    }

    @Test
    @DisplayName("13. Отправка формы оплаты по рандомной карте с заполнением всех полей")
    public void shouldNotPaymentWithDefunctCard() {
        paymentPage.verifyFormName();
        paymentPage.fillForm(new DataHelper.CardInfo(getRandomCardNumber(),
                getMonth(), getYear(2), getHolder(), getCvc()));
        paymentPage.verifyNotificationErrorPayment();
    }

    @Test
    @DisplayName("14. Появление сообщения под полем номер карты при вводе невалидных данных (кириллица)")
    public void errorMsgWhenEnterInTheCardNumberFieldCyrillic() {
        paymentPage.verifyFormName();
        paymentPage.fillForm(new DataHelper.CardInfo(getCardNumberWithCyrillic(),
                getMonth(), getYear(1), getHolder(), getCvc()));
        paymentPage.verifyNotificationRequiredField(0);
    }

    @Test
    @DisplayName("15. Появление сообщения под полем номер карты при вводе невалидных данных (латиница)")
    public void errorMsgWhenEnterInTheCardNumberFieldLatin() {
        paymentPage.verifyFormName();
        paymentPage.fillForm(new DataHelper.CardInfo(getCardNumberWithLatin(),
                getMonth(), getYear(3), getHolder(), getCvc()));
        paymentPage.verifyNotificationRequiredField(0);
    }

    @Test
    @DisplayName("16. Появление сообщения под полем номер карты при вводе невалидных данных (спецсимволы)")
    public void errorMsgWhenEnterInTheCardNumberFieldSymbols() {
        paymentPage.verifyFormName();
        paymentPage.fillForm(new DataHelper.CardInfo(getCardNumberWithSymbols(),
                getMonth(), getYear(1), getHolder(), getCvc()));
        paymentPage.verifyNotificationRequiredField(0);
    }

    @Test
    @DisplayName("17. Появление сообщения под полем месяц при вводе невалидных данных (два нуля)")
    public void errorMsgWhenEnterInTheMonthField2Zero() {
        paymentPage.verifyFormName();
        paymentPage.fillForm(new DataHelper.CardInfo(getApprovedCardNumber(),
                getMonthWithZero(), getYear(3), getHolder(), getCvc()));
        paymentPage.verifyNotificationCardDateIsIncorrect(0);
    }

    @Test
    @DisplayName("18. Появление сообщения под полем месяц при вводе невалидных данных (число 13)")
    public void errorMsgWhenEnterInTheMonthField13Number() {
        paymentPage.verifyFormName();
        paymentPage.fillForm(new DataHelper.CardInfo(getApprovedCardNumber(),
                getMonthOverTwelve(), getYear(4), getHolder(), getCvc()));
        paymentPage.verifyNotificationCardDateIsIncorrect(0);
    }

    @Test
    @DisplayName("19. Появление сообщения под полем месяц при вводе невалидных данных (латиница)")
    public void errorMsgWhenEnterInTheMonthFieldLatin() {
        paymentPage.verifyFormName();
        paymentPage.fillForm(new DataHelper.CardInfo(getApprovedCardNumber(),
                getMonthWithLatin(), getYear(2), getHolder(), getCvc()));
        paymentPage.verifyNotificationRequiredField(0);
    }

    @Test
    @DisplayName("20. Появление сообщения под полем месяц при вводе невалидных данных (кириллица)")
    public void errorMsgWhenEnterInTheMonthFieldCyrillic() {
        paymentPage.verifyFormName();
        paymentPage.fillForm(new DataHelper.CardInfo(getApprovedCardNumber(),
                getMonthWithCyrillic(), getYear(3), getHolder(), getCvc()));
        paymentPage.verifyNotificationRequiredField(0);
    }

    @Test
    @DisplayName("21. Появление сообщения под полем месяц при вводе невалидных данных (спецсимволы)")
    public void errorMsgWhenEnterInTheMonthFieldSymbols() {
        paymentPage.verifyFormName();
        paymentPage.fillForm(new DataHelper.CardInfo(getApprovedCardNumber(),
                getMonthWithSymbols(), getYear(2), getHolder(), getCvc()));
        paymentPage.verifyNotificationRequiredField(0);
    }

    @Test
    @DisplayName("22. Появление сообщения под полем месяц при вводе невалидных данных (одна цифра)")
    public void errorMsgWhenEnterInTheMonthFieldOneDigit() {
        paymentPage.verifyFormName();
        paymentPage.fillForm(new DataHelper.CardInfo(getApprovedCardNumber(),
                getMonthWithOneNumber(), getYear(4), getHolder(), getCvc()));
        paymentPage.verifyNotificationWrongFormat(0);
    }

    @Test
    @DisplayName("23. Появление сообщения под полем год при вводе невалидных данных (прошедший год)")
    public void errorMsgWhenEnterInTheYearFieldLastYear() {
        paymentPage.verifyFormName();
        paymentPage.fillForm(new DataHelper.CardInfo(getApprovedCardNumber(),
                getMonth(), getYear(-1), getHolder(), getCvc()));
        paymentPage.verifyNotificationCardExpired(0);
    }

    @Test
    @DisplayName("24. Появление сообщения под полем год при вводе невалидных данных (два нуля)")
    public void errorMsgWhenEnterInTheYearField2Zero() {
        paymentPage.verifyFormName();
        paymentPage.fillForm(new DataHelper.CardInfo(getApprovedCardNumber(),
                getMonth(), getYearWithZero(), getHolder(), getCvc()));
        paymentPage.verifyNotificationCardExpired(0);
    }

    @Test
    @DisplayName("25. Появление сообщения под полем год при вводе невалидных данных (свыше срока действия карты)")
    public void errorMsgWhenEnterInTheYearFieldOverExpDate() {
        paymentPage.verifyFormName();
        paymentPage.fillForm(new DataHelper.CardInfo(getApprovedCardNumber(),
                getMonth(), getYear(6), getHolder(), getCvc()));
        paymentPage.verifyNotificationCardDateIsIncorrect(0);
    }

    @Test
    @DisplayName("26. Появление сообщения под полем год при вводе невалидных данных (одна цифра)")
    public void errorMsgWhenEnterInTheYearFieldOneDigit() {
        paymentPage.verifyFormName();
        paymentPage.fillForm(new DataHelper.CardInfo(getApprovedCardNumber(),
                getMonth(), getYearWith1Number(), getHolder(), getCvc()));
        paymentPage.verifyNotificationWrongFormat(0);
    }

    @Test
    @DisplayName("27. Появление сообщения под полем год при вводе невалидных данных (кириллица)")
    public void errorMsgWhenEnterInTheYearFieldCyrillic() {
        paymentPage.verifyFormName();
        paymentPage.fillForm(new DataHelper.CardInfo(getApprovedCardNumber(),
                getMonth(), getYearWithCyrillic(), getHolder(), getCvc()));
        paymentPage.verifyNotificationRequiredField(0);
    }

    @Test
    @DisplayName("28. Появление сообщения под полем год при вводе невалидных данных (латиница)")
    public void errorMsgWhenEnterInTheYearFieldLatin() {
        paymentPage.verifyFormName();
        paymentPage.fillForm(new DataHelper.CardInfo(getApprovedCardNumber(),
                getMonth(), getYearWithLatin(), getHolder(), getCvc()));
        paymentPage.verifyNotificationRequiredField(0);
    }

    @Test
    @DisplayName("29. Появление сообщения под полем год при вводе невалидных данных (спецсимволы)")
    public void errorMsgWhenEnterInTheYearFieldSymbols() {
        paymentPage.verifyFormName();
        paymentPage.fillForm(new DataHelper.CardInfo(getApprovedCardNumber(),
                getMonth(), getYearWithSymbols(), getHolder(), getCvc()));
        paymentPage.verifyNotificationRequiredField(0);
    }

    @Test
    @DisplayName("30. Появление сообщения под полем владелец при вводе невалидных данных (только имя или фамилия)")
    public void errorMsgWhenEnterInTheHolderFieldOnlyFirstName() {
        paymentPage.verifyFormName();
        paymentPage.fillForm(new DataHelper.CardInfo(getApprovedCardNumber(),
                getMonth(), getYear(3), getHolderOnlyFirstName(), getCvc()));
        paymentPage.verifyNotificationWrongFormat(0);
    }

    @Test
    @DisplayName("31. Появление сообщения под полем владелец при вводе невалидных данных (кириллица)")
    public void errorMsgWhenEnterInTheHolderFieldCyrillic() {
        paymentPage.verifyFormName();
        paymentPage.fillForm(new DataHelper.CardInfo(getApprovedCardNumber(),
                getMonth(), getYear(4), getHolderNameWithCyrillic(), getCvc()));
        paymentPage.verifyNotificationWrongFormat(0);
    }

    @Test
    @DisplayName("32. Появление сообщения под полем владелец при вводе невалидных данных (кириллица+латиница)")
    public void errorMsgWhenEnterInTheHolderFieldCyrillicAndLatin() {
        paymentPage.verifyFormName();
        paymentPage.fillForm(new DataHelper.CardInfo(getApprovedCardNumber(),
                getMonth(), getYear(2), getHolderNameWithLatAndRus(), getCvc()));
        paymentPage.verifyNotificationWrongFormat(0);
    }

    @Test
    @DisplayName("33. Появление сообщения под полем владелец при вводе невалидных данных (спецсимволы)")
    public void errorMsgWhenEnterInTheHolderFieldSymbols() {
        paymentPage.verifyFormName();
        paymentPage.fillForm(new DataHelper.CardInfo(getApprovedCardNumber(),
                getMonth(), getYear(1), getHolderNameWithSymbols(), getCvc()));
        paymentPage.verifyNotificationWrongFormat(0);
    }

    @Test
    @DisplayName("34. Появление сообщения под полем владелец при вводе невалидных данных (цифры)")
    public void errorMsgWhenEnterInTheHolderFieldDigits() {
        paymentPage.verifyFormName();
        paymentPage.fillForm(new DataHelper.CardInfo(getApprovedCardNumber(),
                getMonth(), getYear(2), getHolderNameWithNumbers(), getCvc()));
        paymentPage.verifyNotificationWrongFormat(0);
    }

    @Test
    @DisplayName("35. Появление сообщения под полем владелец при вводе невалидных данных (двойное имя с пробелом)")
    public void errorMsgWhenEnterInTheHolderFieldExists2Space() {
        paymentPage.verifyFormName();
        paymentPage.fillForm(new DataHelper.CardInfo(getApprovedCardNumber(),
                getMonth(), getYear(3), getHolderFirstNameWithSpace(), getCvc()));
        paymentPage.verifyNotificationWrongFormat(0);
    }

    @Test
    @DisplayName("36. Появление сообщения под полем CCV/CVV при вводе невалидных данных (2 цифры)")
    public void errorMsgWhenEnterInTheCvcFieldTwoDigits() {
        paymentPage.verifyFormName();
        paymentPage.fillForm(new DataHelper.CardInfo(getApprovedCardNumber(),
                getMonth(), getYear(2), getHolder(), getCvcWith2Numbers()));
        paymentPage.verifyNotificationWrongFormat(0);
    }

    @Test
    @DisplayName("37. Появление сообщения под полем CCV/CVV при вводе невалидных данных (1 цифра)")
    public void errorMsgWhenEnterInTheCvcFieldOneDigits() {
        paymentPage.verifyFormName();
        paymentPage.fillForm(new DataHelper.CardInfo(getApprovedCardNumber(),
                getMonth(), getYear(2), getHolder(), getCvcWith1Number()));
        paymentPage.verifyNotificationWrongFormat(0);
    }

    @Test
    @DisplayName("38. Появление сообщения под полем CCV/CVV при вводе невалидных данных (спецсимволы)")
    public void errorMsgWhenEnterInTheCvcFieldOneSymbols() {
        paymentPage.verifyFormName();
        paymentPage.fillForm(new DataHelper.CardInfo(getApprovedCardNumber(),
                getMonth(), getYear(2), getHolder(), getCvcWithSymbols()));
        paymentPage.verifyNotificationRequiredField(1);
    }

    @Test
    @DisplayName("39. Появление сообщения под полем CCV/CVV при вводе невалидных данных (кириллица)")
    public void errorMsgWhenEnterInTheCvcFieldCyrillic() {
        paymentPage.verifyFormName();
        paymentPage.fillForm(new DataHelper.CardInfo(getApprovedCardNumber(),
                getMonth(), getYear(4), getHolder(), getCvcWithCyrillic()));
        paymentPage.verifyNotificationRequiredField(1);
    }

    @Test
    @DisplayName("40. Появление сообщения под полем CCV/CVV при вводе невалидных данных (латиница)")
    public void errorMsgWhenEnterInTheCvcFieldLatin() {
        paymentPage.verifyFormName();
        paymentPage.fillForm(new DataHelper.CardInfo(getApprovedCardNumber(),
                getMonth(), getYear(2), getHolder(), getCvcWithLatin()));
        paymentPage.verifyNotificationRequiredField(1);
    }

    @Test
    @DisplayName("41. Отправка формы оплаты по активной карте с указанием предыдущего месяца текущего года")
    public void shouldNotPaymentWhenCardDateIncorrect() {
        paymentPage.verifyFormName();
        paymentPage.fillForm(new DataHelper.CardInfo(getApprovedCardNumber(),
                getCurrentMonth(-1), getYear(0), getHolder(), getCvc()));
        paymentPage.verifyNotificationCardDateIsIncorrect(0);
    }

    @Test
    @DisplayName("42. Отправка формы оплаты по активной карте с указанием текущего месяца и года")
    public void shouldPaymentWhenCardDateIsCurrent() {
        paymentPage.verifyFormName();
        paymentPage.fillForm(new DataHelper.CardInfo(getApprovedCardNumber(),
                getCurrentMonth(0), getYear(0), getHolder(), getCvc()));
        paymentPage.verifyNotificationSuccessPayment();
    }

    @Test
    @DisplayName("43. Отправка формы оплаты по активной карте с указанием следующего (от текущего) месяца и текущего года")
    public void shouldPaymentWhenCardDateIsNextCurrent() {
        paymentPage.verifyFormName();
        paymentPage.fillForm(new DataHelper.CardInfo(getApprovedCardNumber(),
                getCurrentMonth(1), getYear(0), getHolder(), getCvc()));
        paymentPage.verifyNotificationSuccessPayment();
    }
}
