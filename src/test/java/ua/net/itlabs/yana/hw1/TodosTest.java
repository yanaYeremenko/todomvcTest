package ua.net.itlabs.yana.hw1;

import com.codeborne.selenide.ElementsCollection;
import org.junit.Before;
import org.junit.Test;

import static com.codeborne.selenide.CollectionCondition.texts;
import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.CollectionCondition.empty;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;

public class TodosTest {
    void addTask (String param) {
        $("#new-todo").setValue(param).pressEnter();
    }

    @Before
    public void clearData() {
        executeJavaScript("localStorage.clear()");
        open("http://todomvc.com");
        open("http://todomvc.com/examples/troopjs_require/#/");
    }
    @Test
    public void testCreatTask(){
        ElementsCollection todoTasks =  $$("#todo-list>li");

        open("http://todomvc.com/examples/troopjs_require/#/");
        todoTasks.shouldBe(empty);

        addTask("Task 1");
        addTask("Task 2");
        addTask("Task 3");
        addTask("Task 4");
        todoTasks.shouldHave(texts("task 1", "task 2", "task 3", "task 4"));

        todoTasks.find(text("2")).hover();
        todoTasks.find(text("2")).find(".destroy").click();
        todoTasks.shouldHave(texts("task 1", "task 3", "task 4"));

        todoTasks.find(text("4")).find(".toggle").click();
        todoTasks.filter(cssClass("completed")).shouldHave(texts("4"));
        $("#clear-completed").click();
        todoTasks.shouldHave(texts("task 1", "task 3"));

        $("#toggle-all").click();
        $("#clear-completed").click();
        todoTasks.shouldBe(empty);
    }
}
