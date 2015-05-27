package ua.net.itlabs.yana.hw2;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebElement;
import ru.yandex.qatools.allure.annotations.Step;

import static com.codeborne.selenide.CollectionCondition.texts;
import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.CollectionCondition.empty;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class TodosTest {

    ElementsCollection todos = $$("#todo-list>li");
    SelenideElement listWithActive = $("a[href='#/active']");
    SelenideElement listWithCompleted = $("a[href='#/completed']");
    SelenideElement listWithAll = $("a[href='#/']");

    @Step
    void addTask (String nameTask) {
        $("#new-todo").setValue(nameTask).pressEnter();
    }

    @Step
    void toggle (String itemText) {
        todos.find(text(itemText)).find(".toggle").click();
    }

    @Step
    void doubleClick (WebElement webElement){
        actions().moveToElement(webElement).doubleClick().perform();
    }

    @Step
    void  edit (String nameTask, String newName) {
        doubleClick(todos.find(text(nameTask)).find("label"));
        $("input.edit").setValue(newName).pressEnter();
    }

    @Before
    public void clearData() {
        executeJavaScript("localStorage.clear()");
        open("http://todomvc.com");
        open("http://todomvc.com/examples/troopjs_require/#/");
    }
    @Test
    public void testCreateTask(){
        open("http://todomvc.com/examples/troopjs_require/#/");
        todos.shouldBe(empty);

        // create tasks
        addTask("A");
        addTask("B");
        addTask("C");
        addTask("D");
        addTask("E");
        todos.shouldHave(texts("A", "B", "C", "D", "E"));

        // delete active task
        todos.find(text("C")).hover();
        todos.find(text("C")).find(".destroy").click();
        todos.shouldHave(texts("A", "B", "D", "E"));

        // mark active tasks as completed
        toggle("B");
        toggle("D");
        todos.filter(cssClass("completed")).shouldHave(texts("B", "D"));

        // go to list with active tasks
        listWithActive.click();
        todos.filter(visible).shouldHave(texts("A", "E"));

        // edit active task in active list
        edit("A", "A edited");
        todos.filter(visible).shouldHave(texts("A edited", "E"));

        // create new task in the active list
        addTask("J from active");
        todos.filter(visible).shouldHave(texts("A edited", "E", "J from active"));

        // go to list with completed tasks
        listWithCompleted.click();
        todos.filter(visible).shouldHave(texts("B", "D"));

        // delete completed task
        todos.filter(visible).find(text("D")).hover();
        todos.filter(visible).find(text("D")).find(".destroy").click();
        todos.filter(visible).shouldHave(texts("B"));

        // change completed task on active task
        todos.filter(visible).find(text("B")).find(".toggle").click();
        todos.filter(visible).shouldBe(empty);

        // go to list with all tasks & toddle all & clear
        listWithAll.click();
        $("#toggle-all").click();
        $("#clear-completed").click();
        todos.shouldBe(empty);
    }
}
