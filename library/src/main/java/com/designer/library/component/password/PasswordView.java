/*
 * Copyright (C) 2018,巨神科技有限公司 All rights reserved.
 * Project：Base
 * Author：两津勘吉
 * Date：12/31/18 2:33 AM
 */

package com.designer.library.component.password;

/**
 * @author Jungly
 * @mail jungly.ik@gmail.com
 * @date 15/3/21 16:20
 */
interface PasswordView {
  //void setError(String error);

  String getPassWord();

  void clearPassword();

  void setPassword(String password);

  void setPasswordVisibility(boolean visible);

  void togglePasswordVisibility();

  void setOnPasswordChangedListener(GridPasswordView.OnPasswordChangedListener listener);

  void setPasswordType(PasswordType passwordType);
}
