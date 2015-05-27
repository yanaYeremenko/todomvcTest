package ua.net.itlabs.yana.hw4;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ua.net.itlabs.yana.Todos.page.PageObject;
import static com.codeborne.selenide.CollectionCondition.texts;
import static com.codeborne.selenide.CollectionCondition.empty;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class TodosTest {
    PageObject pageObject;

    public TodosTest() {
        pageObject = new PageObject();
    }

    @BeforeClass
    public static void openTodoMVC() {
        open("http://todomvc.com/examples/troopjs_require/#/");
    }

    @Before
    public void clearData() {
        executeJavaScript("localStorage.clear()");
        open("http://todomvc.com");
        open("http://todomvc.com/examples/troopjs_require/#/");
    }

    @Test
    public void testAllFilters() {

        // create tasks
        pageObject.addTask("A");
        pageObject.addTask("B");
        pageObject.addTask("C");
        pageObject.addTask("D");
        pageObject.addTask("E");
        pageObject.todos.shouldHave(texts("A", "B", "C", "D", "E"));
        pageObject.assertItemsLeftCounter(5);

        // delete active task
        pageObject.deleteTask("C");
        pageObject.todos.shouldHave(texts("A", "B", "D", "E"));
        pageObject.assertItemsLeftCounter(4);

        // edit task
        pageObject.edit("A", "A edited");
        pageObject.todos.shouldHave(texts("A edited", "B", "D", "E"));

        // mark active task as completed
        pageObject.toggle("A edited");
        pageObject.toggle("B");
        pageObject.todos.filter(cssClass("completed")).shouldHave(texts("A edited", "B"));
        pageObject.assertItemsLeftCounter(2);

        // reopen task
        pageObject.toggle("B");
        pageObject.assertItemsLeftCounter(3);

        //clear completed task
        pageObject.clearCompleted();
        pageObject.todos.shouldHave(texts("B", "D", "E"));

        // complete all task & clear
        pageObject.toggleAll();
        pageObject.assertItemsLeftCounter(0);
        pageObject.clearCompleted();
        pageObject.todos.shouldBe(empty);
    }

    @Test
    public void testActiveFilters() {

        // precondition
        pageObject.addTask("A");
        pageObject.addTask("B");
        pageObject.addTask("C");
        pageObject.todos.shouldHave(texts("A", "B", "C"));
        pageObject.assertItemsLeftCounter(3);

        pageObject.toggle("C");

        // Filter active
        pageObject.filterActive();
        pageObject.todos.filter(visible).shouldHave(texts("A", "B"));

        //create task
        pageObject.addTask("D");
        pageObject.todos.filter(visible).shouldHave(texts("A", "B", "D"));
        pageObject.assertItemsLeftCounter(3);

        //filters
        pageObject.filterAll();
        pageObject.todos.filter(visible).shouldHave(texts("A", "B", "C", "D"));
        pageObject.filterActive();

        //edit task
        pageObject.edit("B", "B edited");
        pageObject.todos.filter(visible).shouldHave(texts("A", "B edited", "D"));

        //delete task
        pageObject.deleteTask("A");
        pageObject.todos.filter(visible).shouldHave(texts("B edited", "D"));

        pageObject.toggle("B edited");
        pageObject.todos.filter(visible).shouldHave(texts("D"));
    }

    @Test
    public void testCompletedFilters() {

        // precondition
        pageObject.addTask("A");
        pageObject.addTask("B");
        pageObject.addTask("C");
        pageObject.addTask("D");
        pageObject.todos.shouldHave(texts("A", "B", "C", "D"));

        pageObject.toggle("C");
        pageObject.toggle("A");
        pageObject.toggle("D");

        // Filter completed
        pageObject.filterCompleted();
        pageObject.todos.filter(visible).shouldHave(texts("A", "C", "D"));

        //delete task
        pageObject.deleteTask("C");
        pageObject.todos.filter(visible).shouldHave(texts("A", "D"));

        //reopen task
        pageObject.toggle("A");
        pageObject.todos.filter(visible).shouldHave(texts("D"));

        // clear
        pageObject.clearCompleted();
        pageObject.todos.filter(visible).shouldBe(empty);

        // filters
        pageObject.filterActive();
        pageObject.todos.filter(visible).shouldHave(texts("A", "B"));
        pageObject.assertItemsLeftCounter(2);
    }
}
