package com.fzanutto.todoapp.view.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.fzanutto.todoapp.R
import com.fzanutto.todoapp.databinding.FragmentFirstBinding
import com.fzanutto.todoapp.databinding.TaskRowItemBinding
import com.google.android.material.snackbar.Snackbar


class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private var _adapter: TaskAdapter? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val adapter get() = _adapter!!

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _adapter = TaskAdapter(arrayListOf())

        binding.tasks.adapter = adapter

        viewModel.requestTaskList()

        viewModel.taskList.observe(viewLifecycleOwner) {
            adapter.updateTaskList(it)
        }


        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }

        binding.fab.setOnClickListener { fabView ->
            Snackbar.make(fabView, "This should create new task", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _adapter = null
    }
}