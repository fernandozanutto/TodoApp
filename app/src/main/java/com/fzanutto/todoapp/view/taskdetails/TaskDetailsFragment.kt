package com.fzanutto.todoapp.view.taskdetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.fzanutto.todoapp.R
import com.fzanutto.todoapp.databinding.FragmentTaskDetailsBinding
import com.fzanutto.todoapp.models.Task

class TaskDetailsFragment : Fragment() {

    private var _binding: FragmentTaskDetailsBinding? = null
    private val viewModel: TaskDetailsViewModel by viewModels()
    private val args by navArgs<TaskDetailsFragmentArgs>()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskDetailsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (args.taskId == 0) {
            (activity as AppCompatActivity).supportActionBar?.title = context?.getString(R.string.create_task)
        }

        binding.buttonSave.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }

        viewModel.task.observe(viewLifecycleOwner) {
            updateUI(it)
        }
    }

    override fun onResume() {
        super.onResume()

        viewModel.requestTask(args.taskId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateUI(task: Task) {
        binding.apply {
            title.text = task.title
            description.text = task.description
        }
    }
}