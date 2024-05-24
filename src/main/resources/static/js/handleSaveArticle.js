let postArticle = document.getElementById("postArticle");

postArticle.addEventListener('click' , (e) => {
    let title = document.getElementById("articleTitle").value;
    let tagList = document.getElementById("tagList").value;
    let {editorContent, imgUrls} = handelArticleContentImageSrc(editor.getData());
    if (!(validateTitle() && validateTag() && validateContent())) {
        return;
    }
    loadingSpinner();
    // fetch('/api/article/newbase64', {
    //     method: 'POST',
    //     headers: {
    //         'Content-Type': 'application/json'
    //     },
    //     body: JSON.stringify({
    //         title : title,
    //         content : editorContent,
    //         tags : tagList,
    //         images : imgUrls
    //     })
    // })
    //     .then(response => response.json())
    //     .then(data => {
    //         console.log(data);
    //     })
    //     .catch(error => {
    //         console.error('Error:', error);
    //     });
})

const loadingSpinner = function () {
    postArticle.innerHTML = `<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>
                Loading...`;
    postArticle.disabled = true;
}

const doneLoading = function () {

}

const validateTitle = function () {
    let title = document.getElementById("articleTitle").value;
    if (title.trim().length > 0) {
        // message
        return true;
    } else {
        return false;
    }
}

const validateTag = function () {
    let tagList = document.getElementById("tagList").value;
    if (tagList.trim().length > 0) {
        // message
        return true;
    } else {
        return false;
    }
}

const validateContent = function () {
    let data = editor.getData();
    if (data.trim().length > 0) {
        // message
        return true;
    } else {
        return false;
    }
}

const handelArticleContentImageSrc = function (editorData) {
    const apiSourceString = "/api/coundinary/sourceImage/"
    const htmlString = editorData;
    const parser = new DOMParser();
    const doc = parser.parseFromString(htmlString, 'text/html');

    const imgElements  = doc.querySelectorAll('img');
    const imgUrls = Array.from(imgElements).map(img => img.src);

    imgElements .forEach((img, index) => {
        img.src = `[?]`;
    });

    const serializer = new XMLSerializer();
    let editorContent = serializer.serializeToString(doc);
    editorContent = editorContent.slice(62);
    editorContent = editorContent.slice(0, -14);
    return {editorContent, imgUrls};
}