package ua.net.itlabs.yana.Todos.page;

import com.codeborne.selenide.ElementsCollection;
import ru.yandex.qatools.allure.annotations.Step;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.hidden;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class TodoMVC {

    public static ElementsCollection todos = $$("#todo-list>li");

    @Step
    public static void addTask(String nameTask) {
        $("#new-todo").setValue(nameTask).pressEnter();
    }

    @Step
    public static void toggle(String itemText) {
        todos.find(text(itemText)).find(".toggle").click();
    }

    @Step
   public static void toggleAll() {
        $("#toggle-all").click();
    }

    @Step
    public static void edit(String nameTask, String newName) {
        todos.find(text(nameTask)).find("label").doubleClick();
        $(".editing").find(".edit").setValue(newName).pressEnter();
    }

    @Step
    public static void deleteTask(String nameTask) {
        todos.find(exactText(nameTask)).hover().find(".destroy").click();
    }

    @Step
    public static void clearCompleted() {
        $("#clear-completed").click();
        $("#clear-completed").shouldBe(hidden);
    }

    @Step
    public static void filterActive() {
        $("a[href='#/active']").click();
    }

    @Step
    public static void filterCompleted() {
        $("a[href='#/completed']").click();
    }

    @Step
    public static void filterAll() {
        $("a[href='#/']").click();
    }

    @Step
    public static void assertItemsLeftCounter(Integer quantity){
        $("#todo-count strong").shouldHave(text(quantity.toString()));
    }

    @Step
    public static void assertItemsCompletedCounter(Integer quantity) {
            $("#clear-completed").shouldHave(text(quantity.toString()));
        }
    }

