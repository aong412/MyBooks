package com.trixiesoft.mybooks

import org.junit.Test
import org.junit.Assert.*
import org.junit.runner.RunWith;

import org.junit.runners.Parameterized;

import com.trixiesoft.mybooks.api.BookAPI

// Need to add DisplayName for test result clarity
// Need to upgrade to JUnit 5 for better readability/parameterization
// Need to categorize tests into suites for easier test running
// Need to mock OpenLibrary for faster response
// Results can be verified on https://openlibrary.org/search.json?title=

@RunWith(Parameterized::class)
class SearchBooksByTitleTest(val bookTitle : String, val numFound : Int) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun authors() : Collection<Array<Any>> {
            return listOf(
                arrayOf("qis", 2),              // Low number of books by title
                arrayOf("@", 17723838),         // High number of books title for limit testing
                arrayOf("tokien", 0),           // No books where title doesn't exist
                arrayOf("+", 0)                 // HTTP 500 Error
            )
        }
    }

    @Test
    fun testSearchBooksByTitle() {
        var result = BookAPI.instance.searchBooksByTitle(bookTitle)
        assertEquals(numFound, result.blockingGet().numFound)
    }
}