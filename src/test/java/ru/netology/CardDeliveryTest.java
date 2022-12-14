package ru.netology;

import com.codeborne.selenide.Condition;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import utils.DataGenerator;


import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static java.time.Duration.ofSeconds;
import static utils.DataGenerator.Registration.generateDateOfMeeting;


public class CardDeliveryTest {

    @BeforeEach
    public void setUp() {
        open("http://localhost:9999");
    }

    @Test
    public void shouldRescheduleTheMeeting() {
        val user = DataGenerator.Registration.generateClientPersonalData("ru");
        val name = user.getFullName();
        val phoneNumber = user.getPhoneNumber();
        val city = user.getCity();

        String firstMeeting = generateDateOfMeeting(4);
        String secondMeeting = generateDateOfMeeting(10);


        $("[data-test-id='city'] input").setValue(city);
        $("[placeholder='Дата встречи']").sendKeys(Keys.chord(Keys.CONTROL, "a") + Keys.BACK_SPACE);
        $("[placeholder='Дата встречи']").setValue(firstMeeting);
        $("[data-test-id='name'] input").setValue(name);
        $("[data-test-id='phone'] input").setValue(phoneNumber);
        $("[data-test-id=agreement] .checkbox__box").click();
        $x("//*[text()=\"Запланировать\"]").click();
        $("[data-test-id='success-notification']").shouldBe(visible, ofSeconds(15));
        $("[data-test-id='success-notification']>.notification__content").shouldHave(text("Встреча успешно запланирована на " + firstMeeting));
        $("[placeholder=\"Дата встречи\"]").sendKeys(Keys.chord(Keys.CONTROL, "a") + Keys.BACK_SPACE);
        $("[placeholder=\"Дата встречи\"]").setValue(secondMeeting);
        $x("//*[text()=\"Запланировать\"]").click();
        $("[data-test-id='replan-notification']").shouldBe(visible);
        $("[data-test-id='replan-notification']>.notification__content").shouldHave(text("У вас уже запланирована встреча на другую дату. Перепланировать?"));
        $x("//*[text()=\"Перепланировать\"]").click();
        $("[data-test-id='success-notification']").shouldBe(Condition.visible);
        $("[data-test-id='success-notification']>.notification__content").shouldHave(exactText("Встреча успешно запланирована на " + secondMeeting));

    }

}
