package ua.net.itlabs.yana.hw3;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static ua.net.itlabs.yana.Todos.page.PageStatic.*;
import static com.codeborne.selenide.CollectionCondition.texts;
import static com.codeborne.selenide.CollectionCondition.empty;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class TodosTest {

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
            addTask("A");
            addTask("B");
            addTask("C");
            addTask("D");
            addTask("E");
            todos.shouldHave(texts("A", "B", "C", "D", "E"));
            assertItemsLeftCounter(5);

            // delete active task
            deleteTask("C");
            todos.shouldHave(texts("A", "B", "D", "E"));
            assertItemsLeftCounter(4);

            // edit task
            edit("A","A edited");
            todos.shouldHave(texts("A edited", "B", "D", "E"));

            // mark active task as completed
            toggle("A edited");
            toggle("B");
            todos.filter(cssClass("completed")).shouldHave(texts("A edited", "B"));
            assertItemsLeftCounter(2);

            // reopen task
            toggle("B");
            assertItemsLeftCounter(3);

            //clear completed task
            clearCompleted();
            todos.shouldHave(texts("B","D","E"));

            // complete all task & clear
            toggleAll();
            assertItemsLeftCounter(0);
            clearCompleted();
            todos.shouldBe(empty);
    }

    @Test
    public void testActiveFilters() {

        // precondition
        addTask("A");
        addTask("B");
        addTask("C");
        todos.shouldHave(texts("A", "B", "C"));
        assertItemsLeftCounter(3);

        toggle("C");

        // Filter active
        filterActive();
        todos.filter(visible).shouldHave(texts("A", "B"));

        //create task
        addTask("D");
        todos.filter(visible).shouldHave(texts("A", "B", "D"));
        assertItemsLeftCounter(3);

        //filters
        filterAll();
        todos.filter(visible).shouldHave(texts("A","B","C","D"));
        filterActive();

        //edit task
        edit("B", "B edited");
        todos.filter(visible).shouldHave(texts("A", "B edited", "D"));

        //delete task
        deleteTask("A");
        todos.filter(visible).shouldHave(texts("B edited", "D"));

        toggle("B edited");
        todos.filter(visible).shouldHave(texts("D"));
    }

    @Test
    public void testCompletedFilters() {

        // precondition
        addTask("A");
        addTask("B");
        addTask("C");
        addTask("D");
        todos.shouldHave(texts("A", "B", "C", "D"));

        toggle("C");
        toggle("A");
        toggle("D");

        // Filter completed
        filterCompleted();
        todos.filter(visible).shouldHave(texts("A", "C", "D"));

        //delete task
        deleteTask("C");
        todos.filter(visible).shouldHave(texts("A", "D"));

        //reopen task
        toggle("A");
        todos.filter(visible).shouldHave(texts("D"));

        // clear
        clearCompleted();
        todos.filter(visible).shouldBe(empty);

       // filters
        filterActive();
        todos.filter(visible).shouldHave(texts("A","B"));
        assertItemsLeftCounter(2);
    }
}