/**
 * Toggles the visibility of the password field and changes the icon accordingly.
 * @param {HTMLElement} element - The element that triggers the toggle.
 */
export const togglePasswordVisibility = (element) => {
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
export function togglePasswordIcon(inputField) {
    const passwordToggle = inputField.nextElementSibling.querySelector('i');
    if (inputField.value.length > 0) {
        passwordToggle.style.display = 'block';
    } else {
        passwordToggle.style.display = 'none';
    }
}


/**
 * Validates if the form has all required fields filled.
 * @param {string} formSelector - The ID of the form to validate.
 * @return {boolean} - Returns true if the form is valid, otherwise false.
 */
export const isValidForm = (formSelector) => {
    const formElement = document.getElementById(formSelector);
    const formControls = document.querySelectorAll('.form-control');
    const validateMessage = document.querySelector('.validate-message');

    for (const formControl of formControls) {
        if (formControl.value === '') {
            validateMessage.style.display = 'block';
            validateMessage.innerText = 'Please enter complete information!';
            return false;
        }
    }

    return true;
}


/**
 * Creates a cookie with the specified name, value, and expiration days.
 * @param {string} name - The name of the cookie.
 * @param {string} value - The value of the cookie.
 * @param {number} days - The number of days until the cookie expires.
 */
export const setCookie = function (name, value, days) {
    let expires = "";
    if (days) {
        const date = new Date();
        date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
        expires = "; expires=" + date.toUTCString();
    }
    document.cookie = name + "=" + (value || "") + expires + "; path=/;";
}