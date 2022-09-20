package com.valmiraguiar.wefit.presentation.githubrepo

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.valmiraguiar.wefit.R
import com.valmiraguiar.wefit.databinding.FragmentGithubRepoListBinding
import com.valmiraguiar.wefit.domain.model.GitRepoModel
import com.valmiraguiar.wefit.gitRepositoryListItem


class GitHubRepoListFragment : Fragment() {
    private val vm by viewModels<GitHubRepoListViewModel>()
    private lateinit var binding: FragmentGithubRepoListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGithubRepoListBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vm.loadRepos("appswefit")

        vm.response.observe(viewLifecycleOwner) { response ->
            when (response) {
                is GitHubRepoResponse.Loading -> {
                    binding.listProgressBar.visibility = View.VISIBLE
                }
                is GitHubRepoResponse.Success -> {
                    binding.listProgressBar.visibility = View.GONE

                    modelBuilder(response.githubRepoList)
                }
                is GitHubRepoResponse.Error -> {
                    binding.listProgressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), response.e.message, Toast.LENGTH_LONG).show()
                }
            }
        }

        val btnConfiguration = requireActivity().findViewById<ImageButton>(R.id.config_button)
        btnConfiguration.setOnClickListener {
            showDialog()
        }
    }

    private fun showDialog() {

        val dialog: Dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.change_user_bottom_dialog)

        val txtSelectedUser: TextInputEditText = dialog.findViewById(R.id.txt_selected_user)
        val saveButton: Button = dialog.findViewById(R.id.save_button)
        val closeButton: Button = dialog.findViewById(R.id.close_dialog_button)

        dialog.show()
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
        dialog.window!!.setGravity(Gravity.BOTTOM)

        saveButton.setOnClickListener {
            vm.loadRepos(txtSelectedUser.text.toString())
            dialog.dismiss()
        }

        closeButton.setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun modelBuilder(data: List<GitRepoModel>) {
        binding.githubRepoList.withModels {
            data.forEachIndexed { index, model ->
                gitRepositoryListItem {

                    id(model.id)
                    val username = model.fullName.substringBefore("/") + "/"
                    val repositoryName = model.fullName.substringAfter("/")

                    username(username)
                    repositoryName(repositoryName)
                    description(model.description)

                    if (model.stargazersCount > 99)
                        favorites(getString(R.string.hundred_more_favorite))
                    else
                        favorites(model.stargazersCount.toString())

                    language(model.language)

                    val colorLanguageId = getColorFromLanguage(model.language)


                    onBind { _, view, _ ->
                        if (model.language.isNotEmpty()) {
                            val languageStatus =
                                view.dataBinding.root.findViewById<ImageView>(R.id.language_status)
                            languageStatus.visibility = View.VISIBLE
                            languageStatus.setColorFilter(
                                ContextCompat.getColor(
                                    requireContext(),
                                    colorLanguageId
                                ), android.graphics.PorterDuff.Mode.MULTIPLY
                            )
                        }

                        val favoriteButton =
                            view.dataBinding.root.findViewById<MaterialButton>(R.id.favorite_button)
                        favoriteButton.setOnClickListener {
                            //TODO
                        }
                    }

                    clickListener { _, _, _, _ ->
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.data = Uri.parse(model.htmlUrl)
                        startActivity(intent)
                    }
                }
            }
        }
    }

    private fun getColorFromLanguage(language: String): Int {
        return when (language) {
            "JavaScript" -> {
                R.color.javascript
            }
            "Java" -> {
                R.color.java
            }
            "C" -> {
                R.color.c
            }
            "Kotlin" -> {
                R.color.kotlin
            }
            "CSS" -> {
                R.color.css
            }
            "Elixir" -> {
                R.color.elixir
            }
            "Go" -> {
                R.color.go
            }
            "PHP" -> {
                R.color.php
            }
            "Python" -> {
                R.color.python
            }
            "Ruby" -> {
                R.color.ruby
            }
            "Scala" -> {
                R.color.scala
            }
            else -> {
                R.color.unknow
            }
        }
    }
}