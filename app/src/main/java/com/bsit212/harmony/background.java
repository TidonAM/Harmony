package com.bsit212.harmony;

import org.linphone.core.Account;
import org.linphone.core.AccountParams;
import org.linphone.core.Core;
import org.linphone.core.Factory;

public class background {
    public static Core core;
    public Factory factory;
    public MainActivity ma;

    public void CoreIni() {

    }

    public static void unregister(Core core) {
        // Here we will disable the registration of our Account
        Account account = core.getDefaultAccount();
        if (account == null) {
            return;
        }
        AccountParams params = account.getParams();
        // Returned params object is const, so to make changes we first need to clone it
        AccountParams clonedParams = params.clone();
        // Now let's make our changes
        clonedParams.setRegisterEnabled(false);
        // And apply them
        account.setParams(clonedParams);
    }

    public static void delete(Core core) {
        Account account = core.getDefaultAccount();
        if (account == null) {
            return;
        }
        core.removeAccount(account);
        core.clearAccounts();
        core.clearAllAuthInfo();
    }
}
