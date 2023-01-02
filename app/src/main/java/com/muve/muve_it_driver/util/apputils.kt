package com.muve.muve_it_driver.util

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.muve.muve_it_driver.databinding.ActivityCreatePasswordBinding
import com.muve.muve_it_driver.databinding.FragmentVerifyEmailBinding
import com.muve.muve_it_driver.databinding.VerifyCodeFragmentBinding
import kotlinx.android.synthetic.main.verify_code_fragment.*
import java.util.regex.Pattern


var isLogInButtonClicked : Boolean = false
var isEditUserDone : Boolean = false



var gender : String = ""

fun EditText.onChange(cb: (String) -> Unit) {
    this.addTextChangedListener(object: TextWatcher {
        override fun afterTextChanged(s: Editable?) {

            cb(s.toString())

        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    })
}

fun Context.toast(message: String){

    Toast.makeText(this,message, Toast.LENGTH_SHORT).show()
}

// for disabling copy paste in edit text
fun TextView.disableCopyPaste() {
    isLongClickable = false
    setTextIsSelectable(false)
    customSelectionActionModeCallback = object : ActionMode.Callback {
        override fun onCreateActionMode(mode: ActionMode?, menu: Menu): Boolean {
            return false
        }

        override fun onPrepareActionMode(mode: ActionMode?, menu: Menu): Boolean {
            return false
        }

        override fun onActionItemClicked(mode: ActionMode?, item: MenuItem): Boolean {
            return false
        }

        override fun onDestroyActionMode(mode: ActionMode?) {}
    }
}


fun isValidPassword(data: String, updateUI: Boolean = true): Boolean {
    val str = data
    var valid = true

    // Password policy check
    // Password should be minimum minimum 8 characters long
    if (str.length < 8) {
        valid = false
    }
    // Password should contain at least one number
    var exp = ".*[0-9].*"
    var pattern = Pattern.compile(exp, Pattern.CASE_INSENSITIVE)
    var matcher = pattern.matcher(str)
    if (!matcher.matches()) {
        valid = false
    }

    //Password should contain letters
    exp = ".*[A-Z,a-z].*"
    pattern = Pattern.compile(exp)
    matcher = pattern.matcher(str)
    if (!matcher.matches()) {
        valid = false
    }


   /* // Password should contain at least one capital letter
    exp = ".*[A-Z].*"
    pattern = Pattern.compile(exp)
    matcher = pattern.matcher(str)
    if (!matcher.matches()) {
        valid = false
    }

    // Password should contain at least one small letter
    exp = ".*[a-z].*"
    pattern = Pattern.compile(exp)
    matcher = pattern.matcher(str)
    if (!matcher.matches()) {
        valid = false
    }*/

    // Password should contain at least one special character
    // Allowed special characters : "~!@#$%^&*()-_=+|/,."';:{}[]<>?"
    exp = ".*[~!@#\$%\\^&*()\\-_=+\\|\\[{\\]};:'\",<.>/?].*"
    pattern = Pattern.compile(exp)
    matcher = pattern.matcher(str)
    if (!matcher.matches()) {
        valid = false
    }

    // Set error if required
 /*   if (updateUI) {
        val error: String? = if (valid) null else PASSWORD_POLICY
        setError(data, error)
    }*/

    return valid
}

fun clickOnPinViewCreate(vBinding: ActivityCreatePasswordBinding) {

    vBinding.edtPassword1.disableCopyPaste()
    vBinding.edtPassword2.disableCopyPaste()
    vBinding.edtPassword3.disableCopyPaste()
    vBinding.edtPassword4.disableCopyPaste()


    //edit text1
    vBinding.edtPassword1.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {

        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            if(s?.length == 1){

                vBinding.edtPassword2.requestFocus()
            }
        }
    })

    //edit text2
    vBinding.edtPassword2.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {

        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            if(s?.length == 1){

                vBinding.edtPassword3.requestFocus()
            }else {

                vBinding.edtPassword1.requestFocus()
            }
        }
    })

    //edit text1
    vBinding.edtPassword3.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {

        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            if(s?.length == 1){

                vBinding.edtPassword4.requestFocus()
            }else {
                vBinding.edtPassword2.requestFocus()
            }
        }
    })

    //edit text1
    vBinding.edtPassword4.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {

        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            if(s?.length == 0){

                vBinding.edtPassword3.requestFocus()
            }
        }
    })



}


 fun clickOnPinView(vBinding: VerifyCodeFragmentBinding) {

    vBinding.edtPassword1.disableCopyPaste()
    vBinding.edtPassword2.disableCopyPaste()
    vBinding.edtPassword3.disableCopyPaste()
    vBinding.edtPassword4.disableCopyPaste()


    //edit text1
    vBinding.edtPassword1.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {

        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            if(s?.length == 1){

                vBinding.edtPassword2.requestFocus()
            }
        }
    })

    //edit text2
    vBinding.edtPassword2.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {

        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            if(s?.length == 1){

                vBinding.edtPassword3.requestFocus()
            }else {

                vBinding.edtPassword1.requestFocus()
            }
        }
    })

    //edit text1
    vBinding.edtPassword3.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {

        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            if(s?.length == 1){

                vBinding.edtPassword4.requestFocus()
            }else {
                vBinding.edtPassword2.requestFocus()
            }
        }
    })

    //edit text1
    vBinding.edtPassword4.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {

        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            if(s?.length == 0){

                vBinding.edtPassword3.requestFocus()
            }
        }
    })



}

fun clickOnPinView(vBinding: FragmentVerifyEmailBinding) {

    vBinding.edtPassword1.disableCopyPaste()
    vBinding.edtPassword2.disableCopyPaste()
    vBinding.edtPassword3.disableCopyPaste()
    vBinding.edtPassword4.disableCopyPaste()


    //edit text1
    vBinding.edtPassword1.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {

        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            if(s?.length == 1){

                vBinding.edtPassword2.requestFocus()
            }
        }
    })

    //edit text2
    vBinding.edtPassword2.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {

        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            if(s?.length == 1){

                vBinding.edtPassword3.requestFocus()
            }else {

                vBinding.edtPassword1.requestFocus()
            }
        }
    })

    //edit text1
    vBinding.edtPassword3.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {

        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            if(s?.length == 1){

                vBinding.edtPassword4.requestFocus()
            }else {
                vBinding.edtPassword2.requestFocus()
            }
        }
    })

    //edit text1
    vBinding.edtPassword4.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {

        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            if(s?.length == 0){

                vBinding.edtPassword3.requestFocus()
            }
        }
    })



}


fun View.hideKeyboard(inputMethodManager: InputMethodManager) {
    inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
}


