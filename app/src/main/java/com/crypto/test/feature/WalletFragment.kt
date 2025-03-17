package com.crypto.test.feature

import android.annotation.SuppressLint
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.bumptech.glide.Glide
import com.crypto.test.R
import com.crypto.test.databinding.FragmentWalletBinding
import com.crypto.test.databinding.ViewItemCurrencyBinding
import kotlinx.coroutines.launch


class WalletFragment : Fragment() {

    private val viewBinding: FragmentWalletBinding by lazy {
        FragmentWalletBinding.inflate(layoutInflater)
    }

    private val viewModel: WalletViewModel by viewModels {
        WalletViewModel.Factory
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return viewBinding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = WalletCurrenciesAdapter()

        // setup recycler view
        with(viewBinding.fragmentWalletCurrencies) {
            this.adapter = adapter
            this.addItemDecoration(object : ItemDecoration() {
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {

                    val pos = parent.getChildAdapterPosition(view)
                    with(outRect) {
                        when (pos) {
                            0 -> {
                                top = ITEM_SPACING * 2
                                bottom = ITEM_SPACING
                            }

                            parent.adapter?.itemCount -> {
                                top = ITEM_SPACING
                                bottom = ITEM_SPACING * 2
                            }

                            else -> {
                                top = ITEM_SPACING
                                bottom = ITEM_SPACING
                            }
                        }
                    }
                }
            })
        }

        lifecycleScope.launch {

            // observe ui state change
            viewModel.uiState.collect { uiState ->

                Log.i(TAG, "onUiState: $uiState")
                viewBinding.fragmentWalletBalance.text =
                    "${uiState.symbol} ${uiState.balance} ${uiState.currency}"
                adapter.set(uiState.currenciesUiState)
            }
        }
    }

    inner class WalletCurrenciesAdapter :
        RecyclerView.Adapter<WalletCurrenciesAdapter.CurrencyViewHolder>() {

        private val data: MutableList<WalletCurrencyUiState> = mutableListOf()

        /**
         * depending on the data size
         * we may refactor this function to use [androidx.recyclerview.widget.DiffUtil]
         * to optimize the recyclerview performance
         */
        fun set(data: List<WalletCurrencyUiState>) {
            this.data.clear()
            this.data.addAll(data)
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
            val binding = ViewItemCurrencyBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            return CurrencyViewHolder((binding))
        }

        override fun getItemCount() = data.size

        override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
            holder.bind(position)
        }

        inner class CurrencyViewHolder(private val binding: ViewItemCurrencyBinding) :
            RecyclerView.ViewHolder(binding.root) {

            fun bind(position: Int) {

                val uiState = data.getOrNull(position)
                if (uiState == null) {
                    Log.w(TAG, "failed to get ui state at $position")
                    return
                }

                with(binding) {

                    Glide.with(viewItemCurrencyIcon)
                        .load(uiState.iconUrl)
                        .placeholder(R.drawable.ic_logo_cro)
                        .into(viewItemCurrencyIcon)

                    viewItemCurrencyName.text = uiState.name
                    viewItemCurrencyBalance.text = uiState.currencyBalance
                    viewItemCurrency.text = uiState.currency
                    viewItemCurrencyAnchorSymbol.text = uiState.anchorSymbol
                    viewItemCurrencyAnchorCurrencyBalance.text = uiState.anchorCurrencyBalance
                }
            }
        }
    }

    companion object {
        private val TAG = WalletFragment::class.simpleName
        private const val ITEM_SPACING = 30
    }
}
