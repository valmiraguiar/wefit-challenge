package com.valmiraguiar.wefit.presentation.favorite

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.valmiraguiar.wefit.R
import com.valmiraguiar.wefit.databinding.FragmentFavoriteBinding
import com.valmiraguiar.wefit.domain.model.GitRepoModel
import com.valmiraguiar.wefit.gitRepositoryListItem
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteFragment : Fragment() {

    private val vm: FavoriteViewModel by viewModel()
    private lateinit var binding: FragmentFavoriteBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val user = sharedPreferences.getString("username", "appswefit")

        vm.loadFavorites(user!!)

        vm.response.observe(viewLifecycleOwner) { response ->
            when (response) {
                is FavoriteResponse.Loading -> {
                    binding.listProgressBar.visibility = View.VISIBLE
                }
                is FavoriteResponse.Success -> {
                    binding.listProgressBar.visibility = View.GONE

                    modelBuilder(response.favoriteList)
                }
                is FavoriteResponse.Error -> {
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

    private fun modelBuilder(data: List<GitRepoModel>) {
        binding.repoFavoriteList.withModels {
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
                            view.dataBinding.root.findViewById<Button>(R.id.favorite_button)
                        favoriteButton.visibility = View.GONE

                        val imgView =
                            view.dataBinding.root.findViewById<ImageView>(R.id.user_avatar_img)
                        val userImgProgressBar =
                            view.dataBinding.root.findViewById<ProgressBar>(R.id.user_img_progress_bar)
                        Picasso.get()
                            .load(model.ownerAvatarUrl)
                            .error(R.drawable.ic_account_circle)
                            .into(imgView, object : Callback {
                                override fun onSuccess() {
                                    userImgProgressBar.visibility = View.GONE
                                }

                                override fun onError(e: Exception?) {
                                    userImgProgressBar.visibility = View.GONE
                                }
                            })
                    }

                    onUnbind { _, view ->
                        Picasso.get().invalidate(model.ownerAvatarUrl)
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

    private fun showDialog() {

        val dialog: Dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.change_user_bottom_dialog)

        val sharedPrefs = requireActivity().getPreferences(Context.MODE_PRIVATE)

        val txtSelectedUser: TextInputEditText = dialog.findViewById(R.id.txt_selected_user)
        val saveButton: Button = dialog.findViewById(R.id.save_button)
        val closeButton: Button = dialog.findViewById(R.id.close_dialog_button)

        val username = sharedPrefs.getString("username", "appswefit")
        txtSelectedUser.setText(username!!)

        dialog.show()
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
        dialog.window!!.setGravity(Gravity.BOTTOM)

        saveButton.setOnClickListener {
            vm.loadFavorites(txtSelectedUser.text.toString())

            sharedPrefs.edit().putString("username", txtSelectedUser.text.toString()).apply()
            dialog.dismiss()
        }

        closeButton.setOnClickListener {
            dialog.dismiss()
        }
    }
}