document.getElementById('avtImageInput').addEventListener('change', function() {
    let file = this.files[0];
    if (file) {
        let reader = new FileReader();
        reader.onload = function(event) {
            let base64String = event.target.result;
            document.getElementById('imagePreview').src = base64String;
            document.getElementById('base64Output').value = base64String;
        };
        reader.readAsDataURL(file);
    }
});