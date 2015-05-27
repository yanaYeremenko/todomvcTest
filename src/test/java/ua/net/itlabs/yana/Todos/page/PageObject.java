package ua.net.itlabs.yana.Todos.page;

import com.codeborne.selenide.ElementsCollection;
import org.openqa.selenium.WebElement;
import ru.yandex.qatools.allure.annotations.Step;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.hidden;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.actions;

public class PageObject {
    public ElementsCollection todos = $$("#todo-list>li");

    @Step
    public void addTask(String nameTask) {
        $("#new-todo").setValue(nameTask).pressEnter();
    }

    @Step
    public void toggle(String itemText) {
        todos.find(text(itemText)).find(".toggle").click();
    }

    @Step
    public void toggleAll() {
        $("#toggle-all").click();
    }

    @Step
    public void doubleClick(WebElement webElement) {
        actions().moveToElement(webElement).doubleClick().perform();
    }

    @Step
    public void edit(String nameTask, String newName) {
        doubleClick(todos.find(text(nameTask)).find("label"));
        $(".editing").find(".edit").setValue(newName).pressEnter();
    }

    @Step
    public void deleteTask(String nameTask) {
        todos.find(exactText(nameTask)).hover().find(".destroy").click();
    }

    @Step
    public void clearCompleted() {
        $("#clear-completed").click();
        $("#clearCompleted").shouldBe(hidden);
    }

    @Step
    public void filterActive() {
        $("a[href='#/active']").click();
    }

    @Step
    public void filterCompleted() {
        $("a[href='#/completed']").click();
    }

    @Step
    public void filterAll(){ $("a[href='#/']").click();
    }

    @Step
    public void assertItemsLeftCounter(Integer qty){
        $("#todo-count strong").shouldHave(text(qty.toString()));
    }
}
