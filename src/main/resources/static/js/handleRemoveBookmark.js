const handleRemoveBookmark = function (postId) {
    let messageBox = document.querySelector(`#messageBox`);
    messageBox.classList.remove("d-none");
    if(!messageBox.innerHTML) {
        messageBox.innerHTML +=
                `<span class="mb-3">Are you sure to remove this article?</span> 
                <div> 
                    <button onclick="removePost(${postId})" type="button" class="btn btn-danger">Remove</button> 
                    <button onclick="removeMessageBox()" type="button" class="btn btn-secondary">Cancel</button>
                </div> `;
    }
}

const removePost = async function (postId) {
    // fetch delete post api
    let apiURL = `/api/article/unbookmark`;
    await (
        fetch(apiURL, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                userId : getCookie("SessionUserID"),
                articleId : postId
            })
        })
            .then(data => console.log(data))
    )
    removeMessageBox();
    deletePostInUI(postId)
}

const removeMessageBox = function () {
    let messageBox = document.querySelector(`#messageBox`);
    messageBox.innerHTML = "";
    messageBox.classList.add("d-none");
}

function getCookie(name) {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    if (parts.length === 2) {
        return parts.pop().split(';').shift();
    }
    return null;
}

const deletePostInUI = function (postId) {
    document.querySelector(`#post${postId}`).remove();
}