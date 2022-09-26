package jatx.mybooks.ui.booklist

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import jatx.mybooks.R
import jatx.mybooks.databinding.FragmentBookListBinding
import jatx.mybooks.domain.models.Book
import jatx.mybooks.domain.models.BookType
import jatx.mybooks.util.setItems
import jatx.mybooks.util.setOnItemSelectedListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BookListFragment : Fragment() {

    private val viewModel: BookListViewModel by viewModels()

    private var _binding: FragmentBookListBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    private val _spinnerPosition = MutableStateFlow(0)
    private val spinnerPosition = _spinnerPosition.asStateFlow()

    private val adapter = BookListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookListBinding
            .inflate(layoutInflater, container, false)

        binding.bookListView.layoutManager = LinearLayoutManager(requireContext())
        binding.bookListView.adapter = adapter

        adapter.onItemClick = {
            findNavController().navigate(BookListFragmentDirections.actionAddBook(it))
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost = requireActivity() as? MenuHost
        menuHost?.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.fragment_book_list, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.item_add_book -> {
                        findNavController().navigate(BookListFragmentDirections.actionAddBook(-1))
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        binding.filterSpinner.setItems(BookType.allStringsForSpinner)
        binding.filterSpinner.setSelection(0)
        binding.filterSpinner.setOnItemSelectedListener {
            _spinnerPosition.value = it
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                spinnerPosition.collect {
                    val list = viewModel.books.value.filterByType(it)
                    adapter.submitList(list)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.books.collect {
                    val list = it.filterByType(spinnerPosition.value)
                    adapter.submitList(list)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun List<Book>.filterByType(spinnerPosition: Int): List<Book> {
        val type = BookType.bookTypeByIndex(spinnerPosition - 1)
        return filter {
            (it.type == type).or(type == BookType.ALL)
        }
    }
}