let avtToggle = document.getElementById("smallAvt");
let personalBox = document.getElementById("personalBox");

avtToggle.addEventListener("click", ()=> {
    if (avtToggle.checked) {
        personalBox.classList.remove("personal-box-hide");
    } else {
        personalBox.classList.add("personal-box-hide");
    }
})