/**
 * Toggles the visibility of the password field and changes the icon accordingly.
 */
const togglePasswordVisibility = (element) => {
    const target = element.getAttribute('data-target');
    const passwordField = document.querySelector(target);
    const passwordToggle = element.querySelector('i');

    if (passwordField.type === 'password') {
        passwordField.type = 'text';
        passwordToggle.classList.remove('fa-eye');
        passwordToggle.classList.add('fa-eye-slash');
    } else {
        passwordField.type = 'password';
        passwordToggle.classList.remove('fa-eye-slash');
        passwordToggle.classList.add('fa-eye');
    }
}


/**
 * Function to toggle password icon visibility based on input field value.
 * @param {HTMLElement} inputField - The input field element to check for value.
 */
function togglePasswordIcon(inputField) {
    const passwordToggle = inputField.nextElementSibling.querySelector('i');
    if (inputField.value.length > 0) {
        passwordToggle.style.display = 'block';
    } else {
        passwordToggle.style.display = 'none';
    }
}

// Select elements for the reset password functionality
const btnConfirmResetPassword = document.getElementById("btnConfirmResetPassword");
const passwordElement = document.getElementById("password");
const rePasswordElement = document.getElementById("rePassword");
const validateMessageElement = document.querySelector(".validate-message");
const btnContinue = document.getElementById('btnContinue');

/**
 * The function is used to handle when the user clicks the btnConfirmResetPassword button.
 */
btnConfirmResetPassword.addEventListener("click", function (event) {
    event.preventDefault();

    if (passwordElement.value === "" || rePasswordElement.value === "") {
        validateMessageElement.style.display = 'block';
        validateMessageElement.innerText = 'Please enter complete information!';
        return;
    }

    if (passwordElement.value !== rePasswordElement.value) {
        validateMessageElement.style.display = 'block';
        validateMessageElement.innerText = 'Passwords do not match!';
        return;
    }

    document.getElementById("resetPassForm").submit();
})


// Handle the continue button click
if (btnContinue) {
    btnContinue.addEventListener('click', function () {
        document.querySelector('.contain-message-box').style.display = 'flex';
        window.location.href = "/login";
    });
}



// Handle the document loaded event
document.addEventListener("DOMContentLoaded", function () {
    if (validateMessageElement.innerText === "Message default") {
        validateMessageElement.style.display = "none";
    } else if (validateMessageElement.innerText === "Update password successfully.") {
        const containMessageBox = document.querySelector('.contain-message-box');
        if (containMessageBox) {
            containMessageBox.style.display = 'flex';
        }
    } else {
        validateMessageElement.style.display = "block";
    }
});