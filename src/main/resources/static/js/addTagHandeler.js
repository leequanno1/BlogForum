let tagInput = document.getElementById("inputTag");
let scrollBar = document.getElementById("scrollBar");
let tagContainer = document.getElementById("tagContainer");
let tagListSize = 5;
// function
const showScrollBar = function () {
    scrollBar.style.display = "block";
}

const hideScrollBar = function () {
    scrollBar.style.display = "none";
}

const setScrollBarContent = function (htmlString) {
    scrollBar.innerHTML = htmlString;
}

const handelDeleteTag = function (id) {
    tagContainer.removeChild(document.getElementById(id));
}

const getElementString = function ( tagList ) {
    if(tagList.length > tagListSize) {
        tagList = tagList.slice(0, tagListSize);
    }
    let outputHTML = tagList.map(tag => {
        return `<span id="pretag_${tag.tagId}" onclick="handelDOMTag(${tag.tagId},'${tag.tagName}')">${tag.tagName}</span>\n`
    }).join("");
    return outputHTML;
}

const isTagExisted = function (id) {
    return !!document.getElementById(`tag_${id}`);
}

const handelDOMTag = function(id, name) {
    if(!isTagExisted(id)) {
        tagContainer.innerHTML +=
            `<span id="tag_${id}" class="tag">
                    <span>${name}</span>
                    <i class="fa-sharp fa-solid fa-circle-xmark" onclick="handelDeleteTag('tag_${id}')"></i>
                </span>`;
    }
    tagInput.value = "";
    hideScrollBar();
}

const debounce = (func, timeout = 300) => {
    let timer;
    return (...args) => {
        clearTimeout(timer);
        timer = setTimeout(() => {
            func.apply(this, args); }, timeout);
    };
}

const fetchGet = async function (url) {
    return (await fetch(url)).json();
}


tagInput.addEventListener("input",  debounce(async () => {
    let data = tagInput.value.trim();
    let tagList;
    if(data.charAt(0) === '#') {
        if(data.length < 2) {
            // get all tag api
            tagList = await fetchGet("http://localhost:8080/api/tag/getall");
        } else {
            // get tag has similar name
            tagList = await fetchGet(`http://localhost:8080/api/tag/getsimilar?name=${data.slice(1)}`);
        }
        // DOM data to scroll bar
        if(tagList.length > 0) {
            // show to scroll bar
            setScrollBarContent(getElementString(tagList))
            showScrollBar();
        } else {
            hideScrollBar();
        }
    } else {
        hideScrollBar();
    }
}));
