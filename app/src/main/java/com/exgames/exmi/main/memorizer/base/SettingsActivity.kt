package com.exgames.exmi.main.memorizer.base

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.exgames.exmi.main.memorizer.R
import com.exgames.exmi.main.memorizer.SharedPreferenceRepository
import com.exgames.exmi.main.memorizer.mvp.model.SettingsModel
import com.exgames.exmi.main.memorizer.mvp.presenter.SettingsPresenter
import com.exgames.exmi.main.memorizer.mvp.view.SettingsView
import com.exgames.exmi.main.utils.ActivityUtils
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : BaseActivity() {

    lateinit var presenter: SettingsPresenter

    companion object {
        fun getIntent(activity: Activity): Intent {

            return Intent(
                    activity,
                    SettingsActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val sharedPreferenceRepository = SharedPreferenceRepository(this)
        presenter = SettingsPresenter(SettingsView(this), SettingsModel(sharedPreferenceRepository))

        onBackButtonPressed()
        onMusicSwitchInteraction()
    }

    private fun onMusicSwitchInteraction() {
        switch_music.setOnCheckedChangeListener { _, checked ->
            presenter.onMusicSwitchCheckedChange(checked)
        }
    }

    private fun onBackButtonPressed() {
        settings_back_button.setOnClickListener {
            ActivityUtils.startActivityAndFinishFadeOutFadeIn(this, MainActivity.getIntent(this))
        }
    }

    override fun onBackPressed() {
        ActivityUtils.startActivityAndFinishFadeOutFadeIn(this, MainActivity.getIntent(this))
    }
}
