package com.crypto.test

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.fragment.app.FragmentActivity
import com.crypto.test.databinding.ActivityMainBinding
import com.crypto.test.feature.WalletFragment

class MainActivity : FragmentActivity() {

    private val viewBinding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(viewBinding.root)
        supportFragmentManager.beginTransaction()
            .replace(
                viewBinding.activityMainFragmentContainer.id,
                WalletFragment::class.java,
                null
            )
            .commit()
    }
}
