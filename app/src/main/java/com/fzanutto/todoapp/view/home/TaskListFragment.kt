package com.fzanutto.todoapp.view.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.fzanutto.todoapp.R
import com.fzanutto.todoapp.databinding.FragmentTaskListBinding


class TaskListFragment : Fragment(), TaskAdapter.ClickListener {

    private var _binding: FragmentTaskListBinding? = null
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
        _binding = FragmentTaskListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _adapter = TaskAdapter(arrayListOf(), this)

        binding.tasks.adapter = adapter

        viewModel.taskList.observe(viewLifecycleOwner) {
            adapter.updateTaskList(it)
        }

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }

        viewModel.createNotificationChannel(requireContext())

        viewModel.scheduleNotification(requireContext())
    }

    override fun onResume() {
        super.onResume()
        viewModel.requestTaskList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _adapter = null
    }

    override fun onClick(taskId: Int) {
        val action = TaskListFragmentDirections.actionFirstFragmentToSecondFragment(taskId)
        findNavController().navigate(action)
    }
}