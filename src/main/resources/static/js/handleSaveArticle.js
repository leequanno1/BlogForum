let postArticle = document.getElementById("postArticle");

postArticle.addEventListener('click' , (e) => {
    let title = document.getElementById("articleTitle").value;
    let tagList = document.getElementById("tagList").value;
    let {editorContent, imgUrls} = handelArticleContentImageSrc(editor.getData());
    console.log(title);
    console.log(tagList);
    console.log(editorContent);
    console.log(imgUrls);
})

let handelArticleContentImageSrc = function (editorData) {
    const apiSourceString = "/api/coundinary/sourceImage/"
    const htmlString = editorData;
    const parser = new DOMParser();
    const doc = parser.parseFromString(htmlString, 'text/html');

    const imgElements  = doc.querySelectorAll('img');
    const imgUrls = Array.from(imgElements).map(img => img.src);

    imgElements .forEach((img, index) => {
        img.src = `${apiSourceString}${index + 1}.png`;
    });

    const serializer = new XMLSerializer();
    let editorContent = serializer.serializeToString(doc);
    editorContent = editorContent.slice(62);
    editorContent = editorContent.slice(0, -14);
    return {editorContent, imgUrls};
}