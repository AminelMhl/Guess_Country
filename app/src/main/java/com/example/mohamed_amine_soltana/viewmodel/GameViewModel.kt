package com.example.mohamed_amine_soltana.viewmodel

import androidx.lifecycle.ViewModel
import com.example.mohamed_amine_soltana.model.Country
import com.example.mohamed_amine_soltana.model.CountryRepository
import com.example.mohamed_amine_soltana.utils.MAX_QUESTIONS
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class GameViewModel : ViewModel() {

    private val _currentQuestionIndex = MutableStateFlow(0)
    val currentQuestionIndex: StateFlow<Int> = _currentQuestionIndex

    private val _score = MutableStateFlow(0)
    val score: StateFlow<Int> = _score

    private val _countries = CountryRepository.countries.shuffled().take(MAX_QUESTIONS)
    val countries: List<Country> = _countries

    fun checkAnswer(selected: String): Boolean {
        val correct = countries[_currentQuestionIndex.value].name == selected
        if (correct) _score.value++
        return correct
    }

    fun nextQuestion() {
        _currentQuestionIndex.value++
    }

    fun isGameOver(): Boolean {
        return _currentQuestionIndex.value >= MAX_QUESTIONS
    }

    fun resetGame() {
        _score.value = 0
        _currentQuestionIndex.value = 0
    }
}

