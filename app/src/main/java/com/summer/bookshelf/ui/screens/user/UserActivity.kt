package com.summer.bookshelf.ui.screens.user

import android.os.Bundle
import com.summer.bookshelf.R
import com.summer.bookshelf.base.ui.BaseActivity
import com.summer.bookshelf.databinding.ActivityUserBinding

class UserActivity : BaseActivity<ActivityUserBinding>() {


    override val layoutResId: Int
        get() = R.layout.activity_user

    override fun onActivityReady(savedInstanceState: Bundle?) {}

}