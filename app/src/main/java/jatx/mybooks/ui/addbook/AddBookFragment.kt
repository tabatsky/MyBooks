package jatx.mybooks.ui.addbook

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.*
import jatx.mybooks.R
import jatx.mybooks.databinding.FragmentAddBookBinding
import jatx.mybooks.domain.models.BookType
import jatx.mybooks.util.*
import jatx.mybooks.util.Backup.tryToSaveBackup

class AddBookFragment : Fragment() {

    private val viewModel: AddBookViewModel by viewModels()

    private val binding: FragmentAddBookBinding by viewBinding(createMethod = CreateMethod.INFLATE)

    private val args: AddBookFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.lifecycleOwner = this
        binding.viewmodel = viewModel

        setListeners()

        viewModel.loadBookById(args.id)

        return binding.root
    }

    private fun afterSaving() {
        Log.e("AddBookFragment", "afterSaving")
        if (Build.VERSION.SDK_INT < 33) {
            tryToSaveBackup()
        } else {
            (requireActivity() as? AppCompatActivity)?.onSavePermissionGranted()
        }
        findNavController().popBackStack()
    }

    private fun setListeners() {
        binding.dateButton.setOnClickListener {
            val book = viewModel.book
            selectDate(book.date) { date ->
                viewModel.book.date = date
                viewModel.dateAsString.value = viewModel.book.dateAsString
            }
        }

        binding.deleteButton.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle(R.string.delete)
                .setMessage(R.string.delete_are_you_sure)
                .setPositiveButton(R.string.yes) { dialog, _ ->
                    viewModel.delete {
                        afterSaving()
                    }
                    dialog.dismiss()
                }.setNegativeButton(R.string.no) { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }

        binding.saveButton.setOnClickListener {
            val book = viewModel.book

            book.author = viewModel.author.value
            if (book.author.isEmpty()) {
                showToast(R.string.toast_empty_author)
                return@setOnClickListener
            }

            book.title = viewModel.title.value
            if (book.title.isEmpty()) {
                showToast(R.string.toast_empty_title)
                return@setOnClickListener
            }

            book.isAudioBook = viewModel.isAudioBook.value

            book.type = BookType.bookTypeByIndex(viewModel.bookType.value)

            viewModel.save {
                afterSaving()
            }
        }
    }
}