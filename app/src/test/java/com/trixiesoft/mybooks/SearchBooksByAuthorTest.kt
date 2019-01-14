package com.trixiesoft.mybooks

import org.junit.Test
import org.junit.Assert.*
import org.junit.runner.RunWith;

import org.junit.runners.Parameterized;

//import android.os.Bundle;
import com.trixiesoft.mybooks.api.BookAPI
//import org.mockito.Mockito;

// Need to add DisplayName for test result clarity
// Need to upgrade to JUnit 5 for better readability/parameterization
// Need to categorize tests into suites for easier test running
// Need to mock OpenLibrary for faster response
// Results can be verified on https://openlibrary.org/search.json?author=

@RunWith(Parameterized::class)
class SearchBooksByAuthorTest(val authorName : String, val numFound : Int) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun authors() : Collection<Array<Any>> {
            return listOf(
                arrayOf("qis", 1),              // Low number of books by author
                arrayOf("@", 17723838),         // High number of books by author for limit testing
                                                // Often failing because # of books increasing
                arrayOf("tokien", 0),           // No books author doesn't exist
                arrayOf("+", 0)                 // HTTP 500 Error
            )
        }
    }

    @Test
    fun testSearchBooksByAuthor() {
        var result = BookAPI.instance.searchBooksByAuthor(authorName)
        assertEquals(numFound, result.blockingGet().numFound)
    }

}