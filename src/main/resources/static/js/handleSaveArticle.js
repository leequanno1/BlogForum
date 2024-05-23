let postArticle = document.getElementById("postArticle");

postArticle.addEventListener('click' , (e) => {
    let title = document.getElementById("articleTitle").value;
    let tagList = document.getElementById("tagList").value;
    let {editorContent, imgUrls} = handelArticleContentImageSrc(editor.getData());
    console.log(title);
    console.log(tagList);
    console.log(editorContent);
    console.log(imgUrls);
    // send formData to server; @RequestParam("title") String title,
    //                              @RequestParam("content") String content,
    //                              @RequestParam("tags") String tags,
    //                              @RequestParam("images") List<MultipartFile> images
    // /api/article/new

    fetch('/api/article/newbase64', {
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
            console.log(data);
        })
        .catch(error => {
            console.error('Error:', error);
        });
})

let handelArticleContentImageSrc = function (editorData) {
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