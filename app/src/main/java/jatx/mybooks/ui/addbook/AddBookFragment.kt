package jatx.mybooks.ui.addbook

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import jatx.mybooks.R
import jatx.mybooks.databinding.FragmentAddBookBinding
import jatx.mybooks.domain.models.BookType
import jatx.mybooks.util.*
import jatx.mybooks.util.Backup.tryToSaveBackup

class AddBookFragment : Fragment() {

    private val viewModel: AddBookViewModel by viewModels()

    private var _binding: FragmentAddBookBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    private val args: AddBookFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddBookBinding
            .inflate(layoutInflater, container, false)

        viewModel.loadBookById(args.id) {
            updateUi()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun afterSaving() {
        Log.e("AddBookFragment", "afterSaving")
        tryToSaveBackup()
        findNavController().popBackStack()
    }

    private fun updateUi() {
        binding.author.setText(viewModel.book.author)
        binding.title.setText(viewModel.book.title)

        binding.bookTypeSpinner.setItems(BookType.stringsForSpinner)
        binding.bookTypeSpinner.setSelection(BookType.indexOfBookType(viewModel.book.type))
        binding.bookTypeSpinner.setOnItemSelectedListener {
            viewModel.book.type = BookType.bookTypeByIndex(it)
        }

        binding.isAudioBook.isChecked = viewModel.book.isAudioBook

        binding.dateButton.text = viewModel.book.dateAsString
        binding.dateButton.setOnClickListener {
            val book = viewModel.book
            selectDate(book.date) { date ->
                viewModel.book.date = date
                binding.dateButton.text = viewModel.book.dateAsString
            }
        }

        binding.saveButton.setOnClickListener {
            val book = viewModel.book

            book.author = binding.author.text.toString().trim()
            if (book.author.isEmpty()) {
                showToast(R.string.toast_empty_author)
                return@setOnClickListener
            }

            book.title = binding.title.text.toString().trim()
            if (book.title.isEmpty()) {
                showToast(R.string.toast_empty_title)
                return@setOnClickListener
            }

            book.isAudioBook = binding.isAudioBook.isChecked

            viewModel.save {
                afterSaving()
            }
        }
    }
}