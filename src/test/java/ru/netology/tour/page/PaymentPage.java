package ru.netology.tour.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import ru.netology.tour.data.DataHelper;


import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class PaymentPage {
    private final SelenideElement buyButton = $(byText("Купить"));
    private final SelenideElement formName = $(byText("Оплата по карте"));
    private final SelenideElement cardNumberField = $("[placeholder='0000 0000 0000 0000']");
    private final SelenideElement monthField = $("[placeholder='08']");
    private final SelenideElement yearField = $("[placeholder='22']");
    private final SelenideElement holderField = $$("[class='input__control']").get(3);
    private final SelenideElement cvcField = $("[placeholder='999']");
    private final SelenideElement continueButton = $(byText("Продолжить"));

    private final ElementsCollection errorTextField = $$(".input_invalid .input__sub");

    private final SelenideElement successNotification = $$(".notification__content").get(0);;
    private final SelenideElement errorNotification = $$(".notification__content"). get(1);
    private final SelenideElement blockNotification = $$(".notification__content"). get(2);

    public void verifyFormName() {
        formName.shouldBe(visible);
    }

    public void verifyErrorNotification(int index) {
        errorTextField.get(index).shouldBe(visible);
    }

    public void choosePayment() {
        buyButton.click();
    }

    public void fillForm(DataHelper.CardInfo cardInfo) {
        cardNumberField.setValue(cardInfo.getCardNumber());
        monthField.setValue(cardInfo.getMonth());
        yearField.setValue(cardInfo.getYear());
        holderField.setValue(cardInfo.getHolder());
        cvcField.setValue(cardInfo.getCvc());
        continueButton.click();
    }

    public void sendEmptyForm() {
        continueButton.click();
    }

    public void verifyNotificationWrongFormat(int index) {
        errorTextField.get(index).shouldHave(exactText("Неверный формат")).shouldBe(visible);
    }

    public void verifyNotificationRequiredField(int index) {
        errorTextField.get(index).shouldHave(exactText("Поле обязательно для заполнения"))
                .shouldBe(visible);
    }

    public void verifyNotificationCardDateIsIncorrect(int index) {
        errorTextField.get(index).shouldHave(exactText("Неверно указан срок действия карты"))
                .shouldBe(visible);
    }

    public void verifyNotificationCardExpired(int index) {
        errorTextField.get(index).shouldHave(exactText("Истёк срок действия карты"))
                .shouldBe(visible);
    }

    public void verifyNotificationSuccessPayment() {
        successNotification.shouldHave(exactText("Операция одобрена Банком."), Duration.ofSeconds(20))
                .shouldBe(visible);
    }

    public void verifyNotificationErrorPayment() {
        errorNotification.shouldHave(exactText("Ошибка! Банк отказал в проведении операции."), Duration.ofSeconds(20))
                .shouldBe(visible);
    }

    public void verifyNotificationDeclinedCard() {
        blockNotification.shouldHave(exactText("Карта заблокирована. Попробуйте использовать другую карту или обратиться в службу поддержки Банка"), Duration.ofSeconds(20))
                .shouldBe(visible);
    }
}
