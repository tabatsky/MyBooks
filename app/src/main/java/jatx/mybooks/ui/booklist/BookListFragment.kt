package jatx.mybooks.ui.booklist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import jatx.mybooks.databinding.FragmentBookListBinding
import kotlinx.coroutines.launch

class BookListFragment : Fragment() {

    private val viewModel: BookListViewModel by viewModels()

    private var _binding: FragmentBookListBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

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

        binding.addBookButton.setOnClickListener {
            findNavController().navigate(BookListFragmentDirections.actionAddBook(-1))
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.books.collect {
                    adapter.submitList(it)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}