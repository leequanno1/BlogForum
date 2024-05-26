import {togglePasswordVisibility, togglePasswordIcon} from './common.js';


// Get the toggle password element and the password input element
const togglePassword = document.querySelector(".toggle-password");
const passwordElement = document.getElementById('password');


// Add click event to toggle password visibility
togglePassword.addEventListener('click', () => togglePasswordVisibility(togglePassword));

// Add input event to toggle password icon based on the input value
passwordElement.addEventListener('input', () => togglePasswordIcon(passwordElement));

/*
 * Handles the login form submission.
 */
const handleSubmitLoginForm = () => {
    const btnLogin = document.getElementById('btnLogin');
    if (!btnLogin) {
        return;
    }

    // Add click event to the login button
    btnLogin.addEventListener('click', event => {
        const usernameElement = document.getElementById('username');
        const passwordElement = document.getElementById('password');
        const validateMessageElement = document.querySelector('.validate-message');

        // Check if username or password is empty
        if (!usernameElement.value || !passwordElement.value) {
            validateMessageElement.style.display = 'block';
            validateMessageElement.innerText = 'Please enter complete information!';
            event.preventDefault();
        } else {
            validateMessageElement.style.display = 'none';
        }
    });
}


// Call the function to handle login form submission
handleSubmitLoginForm();


// Add event listener for when the document is fully loaded
document.addEventListener("DOMContentLoaded", function () {
    const validateMessage = document.querySelector(".validate-message");

    if (validateMessage.innerHTML === "Message default") {
        validateMessage.style.display = "none";
    } else {
        validateMessage.style.display = "block";
    }
});



