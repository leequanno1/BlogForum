import {togglePasswordVisibility, togglePasswordIcon, isValidForm} from './common.js';

// Function to validate email format using a regular expression
const isValidEmail = function (email) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
}


// Function to display validation messages
const showMessage = function (message) {
    validateMessageElement.classList.remove("success");
    validateMessageElement.style.display = 'block';
    validateMessageElement.innerText = message;
}


// Select DOM elements
const containTokenValue = document.querySelector(".input-group.mb-3.token-value-item");
const signUpForm = document.getElementById("signUpForm");

const passwordElement = document.getElementById('password');
const emailElement = document.getElementById('email');
const tokenValue = document.getElementById("tokenValue");
const confirmCreateAccountElement = document.getElementById("confirmCreateAccount")
const togglePassword = document.querySelector(".toggle-password");

const validateMessageElement = document.querySelector('.validate-message');
const btnSignUp = document.getElementById("btnSignUp");
const btnSendTokenValue = document.getElementById("btnSendTokenValue");
const btnContinue = document.getElementById('btnContinue');


// Add event listener to toggle password visibility
togglePassword.addEventListener('click', () => togglePasswordVisibility(togglePassword));

// Add event listener to toggle password icon visibility based on input
passwordElement.addEventListener('input', () => togglePasswordIcon(passwordElement));

// Add event listener to handle sending the token value for sign-up
btnSendTokenValue.addEventListener('click', async function (event) {

    event.preventDefault();

    const email = emailElement.value.trim();

    if (!isValidEmail(emailElement.value)) {
        showMessage("Invalid email!");
        return;
    }

    const response = await fetch("/api/user/sendTokenValueToSignUp", {
        method: "POST",
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            "Email": email
        })
    });

    const data = await response.json();

    if (response.ok) {
        validateMessageElement.classList.add("success");
        validateMessageElement.style.display = "block";
        validateMessageElement.innerText = "Send authentication code successfully!";
    } else {
        validateMessageElement.classList.remove("success");
        validateMessageElement.style.display = "block";
        validateMessageElement.innerText = data.message;
    }

});

/**
 * Handles the sign-up form submission.
 */
const handleSubmitSignUpForm = () => {

    btnSignUp.addEventListener('click', event => {
        event.preventDefault();

        if (!isValidForm('signUpForm')) {
            showMessage('Please enter complete information!');
            return;
        }

        if (!isValidEmail(emailElement.value)) {
            showMessage("Invalid email!");
            return;
        }

        if (!confirmCreateAccountElement.checked) {
            showMessage('Please accept all term and condition!');
            return;
        }

        signUpForm.submit();
        validateMessageElement.style.display = 'none';
    });
}

handleSubmitSignUpForm();


// Add event listener to handle email input changes
emailElement.addEventListener("input", function () {
    if (emailElement.value !== '') {
        containTokenValue.classList.remove('disable');
    } else {
        containTokenValue.classList.add('disable');
    }
});


// Add event listener to handle token value input changes
tokenValue.oninput = function () {
    validateMessageElement.style.display = "none";
}


// Add event listener to handle DOM content loaded event
document.addEventListener("DOMContentLoaded", function () {

    if (emailElement.value !== '') {
        containTokenValue.classList.remove('disable');
    }

    if (validateMessageElement.innerHTML === "Message default") {
        validateMessageElement.style.display = "none";
    } else if (validateMessageElement.innerHTML === "Create account successfully.") {
        const containMessageBox = document.querySelector('.contain-message-box');
        if (containMessageBox) {
            document.querySelector('.contain-message-box').style.display = 'flex';
        }
    } else {
        validateMessageElement.style.display = "block";
    }
});


// Add event listener to handle continue button click
btnContinue.addEventListener('click', function () {
    document.querySelector('.contain-message-box').style.display = 'flex';
    window.location.href = "/login";
});
