let postArticle = document.getElementById("postArticle");

postArticle.addEventListener('click' , async (e) => {
    let title = document.getElementById("articleTitle").value;
    let tagList = document.getElementById("tagList").value;
    let {editorContent, imgUrls} = handelArticleContentImageSrc(editor.getData());
    if (!(validateTitle() && validateTag() && validateContent())) {
        return;
    }
    let newArticleID = 0;
    loadingSpinner();
    await (fetch('/api/article/newbase64', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            title : title,
            content : editorContent,
            tags : tagList,
            images : imgUrls
        })
    })
        .then(response => response.json())
        .then(data => {
            newArticleID = parseInt(data.message);
        })
        .catch(error => {
            console.error('Error:', error);
        }));
    doneLoading();
    if (newArticleID !== 0) {
        document.location.href = `/content?id=${newArticleID}`;
    }
})

const loadingSpinner = function () {
    postArticle.innerHTML = `<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>
                Posting...`;
    postArticle.disabled = true;
}

const doneLoading = function () {
    postArticle.innerHTML = `<i class="fa-regular fa-circle-check"></i>
                Success`;
    postArticle.disabled = true;
}

const validateTitle = function () {
    let title = document.getElementById("articleTitle").value;
    if (title.trim().length > 0) {
        document.getElementById("alert1").classList.add("hide-alert");
        return true;
    } else {
        document.getElementById("alert1").classList.remove("hide-alert");
        return false;
    }
}

const validateTag = function () {
    let tagList = document.getElementById("tagList").value;
    if (tagList.trim().length > 0 && tagList.trim().split(" ").length < 6) {
        document.getElementById("alert2").classList.add("hide-alert");
        document.getElementById("alert3").classList.add("hide-alert");
        return true;
    }
    if (tagList.trim().length === 0) {
        document.getElementById("alert2").classList.remove("hide-alert");
        return false;
    }
    if (tagList.trim().split(" ").length > 5) {
        document.getElementById("alert3").classList.remove("hide-alert");
        return false;
    }
}

const validateContent = function () {
    let data = editor.getData();
    if (data.trim().length > 0) {
        // message
        document.getElementById("alert4").classList.add("hide-alert");
        return true;
    } else {
        document.getElementById("alert4").classList.remove("hide-alert");
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