// Select elements for the forgot password functionality
const forgotPasswordForm = document.getElementById("forgotPassForm");
const containSendCode =  document.querySelector(".input-group.mb-3.token-value-item");
const emailElement = document.getElementById("emailForgot");
const tokenValue = document.getElementById("tokenValue");
const validateMessageElement = document.querySelector(".validate-message");

const btnResetPassword = document.getElementById("btnResetPassword");
const btnSendCode = document.getElementById("btnSendTokenValue");


/**
 * Validates the email format using a regular expression.
 * @param {string} email - The email to validate.
 * @return {boolean} - Returns true if the email is valid, otherwise false.
 */
function isValidEmail(email) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
}

/**
 * Event listener for sending the authentication code.
 */
btnSendCode.addEventListener("click", async function (event) {
    event.preventDefault();

    if (emailElement.value === "") {
        validateMessageElement.style.display = "block";
        validateMessageElement.innerText = "Please enter email";
        return;
    }

    if (!isValidEmail(emailElement.value)) {
        validateMessageElement.style.display = "block";
        validateMessageElement.innerText = "Email invalidate";
        return;
    }

    const email = emailElement.value.trim();

    const response = await fetch("/api/user/sendTokenValueToForgotPassword", {
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
    }

    if (!response.ok) {
        validateMessageElement.style.display = "block";
        if (response.status === 404) {
            validateMessageElement.innerText = data.message;
        }

        if (response.status === 500) {
            validateMessageElement.innerText = data.message;
        }
    }

});


/**
 * Event listener for resetting the password.
 */
btnResetPassword.addEventListener("click", function (event) {
    event.preventDefault();

    validateMessageElement.classList.remove("success");

    if (!isValidEmail(emailElement.value)) {
        validateMessageElement.style.display = "block";
        validateMessageElement.innerText = "Email invalidate";
        return;
    }

    if (emailElement.value === "" || tokenValue.value === "") {
        validateMessageElement.style.display = "block";
        validateMessageElement.innerText = "Please enter complete information";
        return;
    }


    if (tokenValue.value.trim() === "") {
        validateMessageElement.style.display = "block";
        validateMessageElement.innerText = "Please enter authentication code";
        return;
    }

    document.getElementById("forgotPassForm").submit();
});


/**
 * Event listener for email input to enable/disable the send code button.
 */
emailElement.oninput = function () {
    if (emailElement.value.trim() !== "") {
        validateMessageElement.style.display = "none";
        containSendCode.classList.remove("disable");
    } else {
        containSendCode.classList.add("disable");
    }
};


/**
 * Event listener for token input to hide the validation message.
 */
tokenValue.oninput = function () {
    if (tokenValue.value.trim() !== "") {
        validateMessageElement.style.display = "none";
    }
};


/**
 * Initializes the form on page load.
 */
document.addEventListener("DOMContentLoaded", function () {

    if (emailElement.value !== "") {
        containSendCode.classList.remove("disable");
    }

    if (validateMessageElement.innerText === "Message default") {
        validateMessageElement.style.display = "none";
    } else {
        validateMessageElement.style.display = "block";
    }
});