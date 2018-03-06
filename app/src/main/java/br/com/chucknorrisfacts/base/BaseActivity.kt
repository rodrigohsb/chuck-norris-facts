package br.com.chucknorrisfacts.base

import android.support.v7.app.AppCompatActivity
import br.com.chucknorrisfacts.screenbehaviors.BehaviorsScreen

/**
 * @rodrigohsb
 */
abstract class BaseActivity: AppCompatActivity(), BehaviorsScreen {

    override fun clearView() {
        hideEmptyView()
        hideGenericErrorView()
        hideLoadingView()
        hideNoConnectionView()
        hideTimeoutView()
    }
}