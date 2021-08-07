package com.stairwaytodev.project.ui

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.stairwaytodev.project.R
import com.stairwaytodev.project.ui.todo.todo_list.ToDoListFragment
import org.junit.Before
import org.junit.Test

class ToDoListFragmentInstrumentedTest {
    private lateinit var navController: TestNavHostController

    @Before
    fun setUp() {
        navController = TestNavHostController(ApplicationProvider.getApplicationContext())

        val fragmentScenario = launchFragmentInContainer(null, R.style.Theme_MaterialComponents) {
            ToDoListFragment()
        }

        fragmentScenario.onFragment { fragment ->
            navController.setGraph(R.navigation.nav_graph)
            Navigation.setViewNavController(fragment.requireView(), navController)
        }
    }

    @Test
    fun onViewCreated_isFragmentVisible() {
        onView(withText(R.string.fragment_todo_list_title)).check(matches(isDisplayed()))
    }

    @Test
    fun onAddToDoButtonClick_gotoAddToDoFragment() {
        onView(withId(R.id.fabAddToDo)).perform(click())
        assert(navController.currentDestination!!.id == R.id.addToDoFragment)
    }
}