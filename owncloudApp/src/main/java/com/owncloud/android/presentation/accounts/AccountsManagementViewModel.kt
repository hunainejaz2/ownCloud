/**
 * ownCloud Android client application
 *
 * @author Javier Rodríguez Pérez
 * @author Aitor Ballesteros Pavón
 *
 * Copyright (C) 2024 ownCloud GmbH.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2,
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.owncloud.android.presentation.accounts

import android.accounts.Account
import androidx.lifecycle.ViewModel
import com.owncloud.android.domain.utils.Event
import com.owncloud.android.extensions.ViewModelExt.runUseCaseWithResult
import com.owncloud.android.presentation.common.UIResult
import com.owncloud.android.providers.AccountProvider
import com.owncloud.android.providers.CoroutinesDispatcherProvider
import com.owncloud.android.usecases.files.RemoveLocalFilesForAccountUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AccountsManagementViewModel(
    private val accountProvider: AccountProvider,
    private val removeLocalFilesForAccountUseCase: RemoveLocalFilesForAccountUseCase,
    private val coroutinesDispatcherProvider: CoroutinesDispatcherProvider,
) : ViewModel() {

    private val _cleanAccountLocalStorageFlow = MutableStateFlow<Event<UIResult<Unit>>?>(null)
    val cleanAccountLocalStorageFlow: StateFlow<Event<UIResult<Unit>>?> = _cleanAccountLocalStorageFlow

    fun getLoggedAccounts(): Array<Account> {
        return accountProvider.getLoggedAccounts()
    }

    fun getCurrentAccount(): Account? {
        return accountProvider.getCurrentOwnCloudAccount()
    }

    fun cleanAccountLocalStorage(accountName: String) {
        runUseCaseWithResult(
            coroutineDispatcher = coroutinesDispatcherProvider.io,
            showLoading = true,
            flow = _cleanAccountLocalStorageFlow,
            useCase = removeLocalFilesForAccountUseCase,
            useCaseParams = RemoveLocalFilesForAccountUseCase.Params(accountName),
        )
    }
}
