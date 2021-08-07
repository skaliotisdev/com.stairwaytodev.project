package com.stairwaytodev.project

import com.stairwaytodev.project.ui.AddToDoFragmentInstrumentedTest
import com.stairwaytodev.project.ui.EditToDoFragmentInstrumentedTest
import com.stairwaytodev.project.ui.ToDoListFragmentInstrumentedTest
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    MainActivityInstrumentedTest::class,
    ToDoListFragmentInstrumentedTest::class,
    AddToDoFragmentInstrumentedTest::class,
    EditToDoFragmentInstrumentedTest::class,
)

class AppInstrumentedTestSuite