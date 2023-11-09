package jatx.mybooks.ui.booklist

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.*
import jatx.mybooks.R
import jatx.mybooks.databinding.FragmentBookListBinding
import jatx.mybooks.util.Backup

class BookListFragment : Fragment() {

    private val viewModel: BookListViewModel by viewModels()

    private val binding: FragmentBookListBinding by viewBinding(createMethod = CreateMethod.INFLATE)

    private val adapter = BookListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.lifecycleOwner = this
        binding.viewmodel = viewModel

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
                    R.id.item_load_backup -> {
                        Backup.loadLauncher.launch(arrayOf("*/*"))
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }
}