package ua.net.itlabs.yana.hw4;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ua.net.itlabs.yana.Todos.page.TodoMVCPage;

import static com.codeborne.selenide.CollectionCondition.texts;
import static com.codeborne.selenide.CollectionCondition.empty;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class TodosTest {
    TodoMVCPage page;

    public TodosTest() {
        page = new TodoMVCPage();
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
        page.addTask("A");
        page.addTask("B");
        page.addTask("C");
        page.addTask("D");
        page.addTask("E");
        page.todos.shouldHave(texts("A", "B", "C", "D", "E"));
        page.assertItemsLeftCounter(5);

        // delete active task
        page.deleteTask("C");
        page.todos.shouldHave(texts("A", "B", "D", "E"));
        page.assertItemsLeftCounter(4);

        // edit task
        page.edit("A", "A edited");
        page.todos.shouldHave(texts("A edited", "B", "D", "E"));

        // mark active task as completed
        page.toggle("A edited");
        page.toggle("B");
        page.todos.filter(cssClass("completed")).shouldHave(texts("A edited", "B"));
        page.assertItemsLeftCounter(2);
        page.assertItemsCompletedCounter(2);

        // reopen task
        page.toggle("B");
        page.assertItemsLeftCounter(3);

        //clear completed task
        page.clearCompleted();
        page.todos.shouldHave(texts("B", "D", "E"));

        // complete all task & clear
        page.toggleAll();
        page.assertItemsLeftCounter(0);
        page.clearCompleted();
        page.todos.shouldBe(empty);
    }

    @Test
    public void testActiveFilters() {

        // precondition
        page.addTask("A");
        page.addTask("B");
        page.addTask("C");
        page.todos.shouldHave(texts("A", "B", "C"));
        page.assertItemsLeftCounter(3);

        page.toggle("C");

        // Filter active
        page.filterActive();
        page.todos.filter(visible).shouldHave(texts("A", "B"));

        //create task
        page.addTask("D");
        page.todos.filter(visible).shouldHave(texts("A", "B", "D"));
        page.assertItemsLeftCounter(3);

        //filters
        page.filterAll();
        page.todos.filter(visible).shouldHave(texts("A", "B", "C", "D"));
        page.filterActive();

        //edit task
        page.edit("B", "B edited");
        page.todos.filter(visible).shouldHave(texts("A", "B edited", "D"));

        //delete task
        page.deleteTask("A");
        page.todos.filter(visible).shouldHave(texts("B edited", "D"));

        page.toggle("B edited");
        page.todos.filter(visible).shouldHave(texts("D"));
    }

    @Test
    public void testCompletedFilters() {

        // precondition
        page.addTask("A");
        page.addTask("B");
        page.addTask("C");
        page.addTask("D");
        page.todos.shouldHave(texts("A", "B", "C", "D"));

        page.toggle("C");
        page.toggle("A");
        page.toggle("D");

        // Filter completed
        page.filterCompleted();
        page.todos.filter(visible).shouldHave(texts("A", "C", "D"));

        //delete task
        page.deleteTask("C");
        page.todos.filter(visible).shouldHave(texts("A", "D"));

        //reopen task
        page.toggle("A");
        page.todos.filter(visible).shouldHave(texts("D"));

        // clear
        page.clearCompleted();
        page.todos.filter(visible).shouldBe(empty);

        // filters
        page.filterActive();
        page.todos.filter(visible).shouldHave(texts("A", "B"));
        page.assertItemsLeftCounter(2);
    }
}
