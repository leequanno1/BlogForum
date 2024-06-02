/**
 * Handles UI elements related to article interactions.
 */

let bookmarkButton = document.getElementById("bookmark");
let isBookmark = document.getElementById("isBookmark");

let follow = document.getElementById("follow");
let isFollow = document.getElementById("isFollow");

let upvote = document.getElementById("upvote");
let isUpvote = document.getElementById("isUpvote");

let downvote = document.getElementById("downvote");
let isDownvote = document.getElementById("isDownvote");

let vote = document.getElementById("vote");

let commentContent = document.getElementById("commentContent");
let sendCommentButton = document.getElementById("sendCommentButton");
let commentList = document.getElementById("commentList");

if(isUpvote.checked) {
    upvote.classList.add("btn-success");
    upvote.classList.remove("btn-outline-success");
}
if(isDownvote.checked) {
    downvote.classList.add("btn-danger");
    downvote.classList.remove("btn-outline-danger");
}


let upvoteCheck = () => {
    upvote.classList.add("btn-success");
    upvote.classList.remove("btn-outline-success");
    isUpvote.checked = true;
    vote.innerText = parseInt(vote.innerText) + 1;
}

let upvoteUncheck = () => {
    upvote.classList.add("btn-outline-success");
    upvote.classList.remove("btn-success");
    isUpvote.checked = false;
    vote.innerText = parseInt(vote.innerText) - 1;
}

let downvoteCheck = () => {
    downvote.classList.add("btn-danger");
    downvote.classList.remove("btn-outline-danger");
    isDownvote.checked = true;
    vote.innerText = parseInt(vote.innerText) - 1;
}

let downvoteUncheck = () => {
    downvote.classList.add("btn-outline-danger");
    downvote.classList.remove("btn-danger");
    isDownvote.checked = false;
    vote.innerText = parseInt(vote.innerText) + 1;
}

isBookmark.addEventListener("change", (e) => {
    let icon = document.getElementById("bookmarkIcon")
    if (isBookmark.checked) {
        icon.classList.add("fa-solid");
    } else {
        icon.classList.remove("fa-solid");
    }
})

isFollow.addEventListener("change", (e) => {
    if (isFollow.checked) {
        follow.innerText = "Đã theo dõi";
    } else {
        follow.innerText = "Theo dõi";
    }
})

isUpvote.addEventListener("change", (e) => {
    if (isUpvote.checked) {
        upvoteCheck();
        if (isDownvote.checked) {
            downvoteUncheck();
        }
    } else {
        upvoteUncheck();
    }
})

isDownvote.addEventListener("change", (e) => {
    if (isDownvote.checked) {
        downvoteCheck();
        if (isUpvote.checked) {
            upvoteUncheck();
        }
    } else {
        downvoteUncheck();
    }
})

// Function to retrieve cookie value by name
function getCookie(name) {
    let cookieArr = document.cookie.split(";");
    for (let i = 0; i < cookieArr.length; i++) {
        let cookiePair = cookieArr[i].split("=");
        if (name == cookiePair[0].trim()) {
            return decodeURIComponent(cookiePair[1]);
        }
    }
    return null;
}


// Event listener for bookmark button click
bookmarkButton.addEventListener("click", (e) => {
    let url = isBookmark.checked ? '/api/article/unbookmark' : '/api/article/addBookmark';
    fetch(url, {
        method: 'POST', headers: {
            'Content-Type': 'application/json'
        }, body: JSON.stringify({
            userId: getCookie("SessionUserID"), articleId: new URLSearchParams(window.location.search).get('id')
        })
    })
        .then(response => response.json())
        .then(data => {
            if (data.status === 'OK') {
                isBookmark.checked = !isBookmark.checked;
                console.log("Bookmark toggled successfully!");
            } else {
                console.log("Failed to toggle bookmark.");
            }
        })
});

// Event listener for upvote button click
upvote.addEventListener("click", (e) => {
    if (!isUpvote.checked) {
        fetch('/api/article/upvote', {
            method: 'POST', headers: {
                'Content-Type': 'application/json'
            }, body: JSON.stringify({
                userId: getCookie("SessionUserID"),
                articleId: new URLSearchParams(window.location.search).get('id')
            })
        })
            .then(response => {
                if (response.ok) {
                    console.log("Upvoted successfully!");
                } else {
                    console.log("Failed to upvote.");
                }
            })
    }
});

// Event listener for downvote button click
downvote.addEventListener("click", (e) => {
    if (!isDownvote.checked) {
        fetch('/api/article/downvote', {
            method: 'POST', headers: {
                'Content-Type': 'application/json'
            }, body: JSON.stringify({
                userId: getCookie("SessionUserID"),
                articleId: new URLSearchParams(window.location.search).get('id')
            })
        })
            .then(response => {
                if (response.ok) {
                    console.log("Downvoted successfully!");
                } else {
                    console.log("Failed to downvote.");
                }
            })
    }
});

// Event listener for follow button click
follow.addEventListener("click", (e) => {
    let url = isFollow.checked ? "api/user/unfollowUser" : "api/user/followUser";

    fetch(url, {
        method: 'POST', headers: {
            'Content-Type': 'application/json'
        }, body: JSON.stringify({
            uid: getCookie("SessionUserID"),
            flid: document.getElementById("userIDofArticle").value
        })
    })
        .then(response => {
            if (response.ok) {
                console.log("Follow successfully");
            } else {
                console.log("Failure successfully");
            }
        })

});

// Event listener for send comment button click
sendCommentButton.addEventListener("click", (e) => {
    if (commentContent.value) {
        let avt = document.querySelector(".profile-avatar");
        let visibleName = document.querySelector(".displayName").innerHTML;

        let comment = `<div class="comment mb-3">
                            <div class="d-flex align-items-center mb-3">
                                <img src="${avt.src}"
                                    alt="Avatar" class="avatar mr-3">
                                <div>
                                    <span class="font-weight-bold">${visibleName}</span>
                                </div>
                            </div>
                            <p>${commentContent.value}</p>
                        </div>`
        commentList.innerHTML = comment + commentList.innerHTML;
    }


    let urlParams = new URLSearchParams(window.location.search);

    // Call api
    fetch('/api/comment/add', {
        method: 'PUT', headers: {
            'Content-Type': 'application/json'
        }, body: JSON.stringify({
            userId: getCookie("SessionUserID"), articleId: urlParams.get('id'), content: commentContent.value
        })
    })
        .then(response => {
            if (response.ok) {
                console.log("Comment added successfully!");
            } else {
                console.log("Failed to add comment.");
            }
        })
});