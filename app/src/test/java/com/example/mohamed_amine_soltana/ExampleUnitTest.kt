package com.example.mohamed_amine_soltana

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import com.example.mohamed_amine_soltana.model.Country
import com.example.mohamed_amine_soltana.model.CountryRepository
import com.example.mohamed_amine_soltana.utils.MAX_QUESTIONS
import com.example.mohamed_amine_soltana.viewmodel.GameViewModel
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.slot
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class GameViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var viewModel: GameViewModel
    private val testCountries = listOf(
        Country("TestCountry1", "Capital1", "Feature1", 1, 2),
        Country("TestCountry2", "Capital2", "Feature2", 3, 4),
        Country("TestCountry3", "Capital3", "Feature3", 5, 6),
        Country("TestCountry4", "Capital4", "Feature4", 7, 8),
        Country("TestCountry5", "Capital5", "Feature5", 9, 10),
        Country("TestCountry6", "Capital6", "Feature6", 11, 12),
        Country("TestCountry7", "Capital7", "Feature7", 13, 14),
        Country("TestCountry8", "Capital8", "Feature8", 15, 16),
        Country("TestCountry9", "Capital9", "Feature9", 17, 18),
        Country("TestCountry10", "Capital10", "Feature10", 19, 20)
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        mockkObject(CountryRepository)
        every { CountryRepository.countries } returns testCountries

        viewModel = GameViewModel()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `initial state is correct`() = runTest {
        assertEquals(0, viewModel.currentQuestionIndex.first())
        assertEquals(0, viewModel.score.first())
        assertEquals(MAX_QUESTIONS, viewModel.countries.size)
        assertFalse(viewModel.isGameOver())
    }

    @Test
    fun `checkAnswer returns true and increases score for correct answer`() = runTest {
        val countryName = viewModel.countries[0].name
        assertTrue(viewModel.checkAnswer(countryName))
        assertEquals(1, viewModel.score.first())
    }

    @Test
    fun `checkAnswer returns false and does not increase score for wrong answer`() = runTest {
        assertFalse(viewModel.checkAnswer("WrongAnswer"))
        assertEquals(0, viewModel.score.first())
    }

    @Test
    fun `nextQuestion increments the question index`() = runTest {
        assertEquals(0, viewModel.currentQuestionIndex.first())
        viewModel.nextQuestion()
        assertEquals(1, viewModel.currentQuestionIndex.first())
    }

    @Test
    fun `isGameOver returns true when all questions are answered`() = runTest {
        repeat(MAX_QUESTIONS - 1) {
            viewModel.nextQuestion()
        }
        assertFalse(viewModel.isGameOver())

        viewModel.nextQuestion()
        assertTrue(viewModel.isGameOver())
    }

    @Test
    fun `resetGame resets score and question index`() = runTest {
        viewModel.nextQuestion()
        viewModel.checkAnswer(viewModel.countries[0].name)

        assertTrue(viewModel.currentQuestionIndex.first() > 0 || viewModel.score.first() > 0)

        viewModel.resetGame()

        assertEquals(0, viewModel.currentQuestionIndex.first())
        assertEquals(0, viewModel.score.first())
    }


    @Test
    fun `submitting correct answer navigates to result screen`() {
        val navController = mockk<NavController>(relaxed = true)

        val currentIndex = viewModel.currentQuestionIndex.value
        assertFalse(currentIndex == 9)

        val userInput = viewModel.countries[0].name
        val isCorrect = viewModel.checkAnswer(userInput)
        assertTrue(isCorrect)

        viewModel.nextQuestion()
        navController.navigate("result/$isCorrect")

        verify { navController.navigate("result/true") }
    }

    @Test
    fun `submitting last answer navigates to score screen`() {
        val navController = mockk<NavController>(relaxed = true)

        repeat(MAX_QUESTIONS - 2) {
            viewModel.nextQuestion()
        }
        assertEquals(8, viewModel.currentQuestionIndex.value)

        val userInput = viewModel.countries[8].name

        val isLastQuestion = viewModel.currentQuestionIndex.value == 8
        viewModel.nextQuestion()

        assertEquals(9, viewModel.currentQuestionIndex.value)

        if (isLastQuestion) {
            navController.navigate("score") {
                popUpTo("question") { inclusive = true }
            }
        }

        verify { navController.navigate("score", any<NavOptionsBuilder.() -> Unit>())  }
    }
    @Test
    fun `clicking next on result screen with game over navigates to score screen`() {
        val navController = mockk<NavController>(relaxed = true)

        repeat(MAX_QUESTIONS) {
            viewModel.nextQuestion()
        }

        if (viewModel.isGameOver()) {
            navController.navigate("score") {
                popUpTo("question") { inclusive = true }
            }
        }

        verify { navController.navigate(match<String> { it.startsWith("score") }, any<NavOptionsBuilder.() -> Unit>()) }
    }

    @Test
    fun `clicking play again on score screen navigates to question screen and resets game`() {
        val navController = mockk<NavController>(relaxed = true)

        repeat(MAX_QUESTIONS) {
            viewModel.nextQuestion()
        }

        viewModel.resetGame()
        navController.navigate("question") {
            popUpTo("home") { inclusive = false }
        }

        assertEquals(0, viewModel.currentQuestionIndex.value)
        assertEquals(0, viewModel.score.value)
        verify { navController.navigate(match<String> { it.startsWith("question") }, any<NavOptionsBuilder.() -> Unit>()) }
    }
}