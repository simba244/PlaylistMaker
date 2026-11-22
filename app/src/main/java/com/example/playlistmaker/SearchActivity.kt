package com.example.playlistmaker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SearchActivity : AppCompatActivity() {

    // Поле ввода — поле класса, чтобы достать его в onRestoreInstanceState
    private lateinit var inputSearch: EditText

    // Текущее значение поискового запроса
    private var searchQuery: String = SEARCH_QUERY_DEF

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val backButton = findViewById<ImageView>(R.id.button_back)
        backButton.setOnClickListener {
            finish()
        }

        inputSearch = findViewById(R.id.input_search)
        val clearButton = findViewById<ImageView>(R.id.button_clear)

        // Следим за текстом в поле ввода
        val searchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Пока не нужно
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Крестик виден только когда запрос не пустой
                clearButton.visibility = if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
                // TODO: здесь позже будет логика поиска
            }

            override fun afterTextChanged(s: Editable?) {
                // Запоминаем запрос, чтобы пережить пересоздание activity
                searchQuery = s?.toString() ?: SEARCH_QUERY_DEF
            }
        }
        inputSearch.addTextChangedListener(searchTextWatcher)

        // Крестик: очистить запрос, спрятать клавиатуру, крестик исчезнет сам через TextWatcher
        clearButton.setOnClickListener {
            inputSearch.setText("")
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(inputSearch.windowToken, 0)
        }
    }

    // Сохраняем запрос перед уничтожением activity (например, при повороте экрана)
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_QUERY_KEY, searchQuery)
    }

    // Достаём запрос при пересоздании и возвращаем его в поле ввода
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchQuery = savedInstanceState.getString(SEARCH_QUERY_KEY, SEARCH_QUERY_DEF)
        inputSearch.setText(searchQuery)
    }

    companion object {
        const val SEARCH_QUERY_KEY = "SEARCH_QUERY"
        const val SEARCH_QUERY_DEF = ""
    }
}
