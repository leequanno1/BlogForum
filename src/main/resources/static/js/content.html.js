document.addEventListener("DOMContentLoaded", function () {

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

    let messageBoxForContentPage = document.getElementById("messageBoxForContentPage");
    let btnCloseMessageBox = document.getElementById("btnCloseMessageBox");

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

    if (isUpvote.checked) {
        upvote.classList.add("btn-success");
        upvote.classList.remove("btn-outline-success");
    }

    if (isDownvote.checked) {
        downvote.classList.add("btn-danger");
        downvote.classList.remove("btn-outline-danger");
    }

    isBookmark.addEventListener("change", (e) => {
        let icon = document.getElementById("bookmarkIcon")
        if (isBookmark.checked && getCookie("SessionUserID") != null) {
            icon.classList.add("fa-solid");
        } else {
            icon.classList.remove("fa-solid");
        }
    })

    if (isFollow != null) {
        isFollow.addEventListener("change", (e) => {
            if (isFollow.checked && getCookie("SessionUserID") != null) {
                follow.innerText = "Followed";
            } else {
                follow.innerText = "Follow";
            }
        })
    }

    isUpvote.addEventListener("change", (e) => {
        if (getCookie("SessionUserID") == null) {
            return;
        }

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
        if (getCookie("SessionUserID") == null) {
            return;
        }

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
    let getCookie = (name) => {
        let cookieArr = document.cookie.split(";");
        for (let i = 0; i < cookieArr.length; i++) {
            let cookiePair = cookieArr[i].split("=");
            if (name === cookiePair[0].trim()) {
                return decodeURIComponent(cookiePair[1]);
            }
        }
        return null;
    }

    let isActionValid = () => {
        let sessionUserIdCookie = getCookie("SessionUserID");
        if (sessionUserIdCookie == null) {
            messageBoxForContentPage.style.display = "flex";
            return false;
        }
        return true;
    };

    if (btnCloseMessageBox !== null) {
        btnCloseMessageBox.addEventListener("click", function () {
            messageBoxForContentPage.style.display = "none";
        });
    }

// Event listener for bookmark button click
    bookmarkButton.addEventListener("click", (e) => {
        if (!isActionValid()) {
            return;
        }

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

    let upVoteViaApi = () => {
        fetch('/api/article/upvote', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                userId: getCookie("SessionUserID"),
                articleId: new URLSearchParams(window.location.search).get('id'),
                checked: isUpvote.checked ? 1 : 0
            })
        })
            .then(response => {
                if (response.ok) {
                    console.log("Up vote successfully");
                } else {
                    console.log("Up vote failure");
                }
            })
    }

    let downVoteViaApi = () => {
        fetch('/api/article/downvote', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                userId: getCookie("SessionUserID"),
                articleId: new URLSearchParams(window.location.search).get('id'),
                checked: isDownvote.checked ? 1 : 0
            })
        })
            .then(response => {
                if (response.ok) {
                    console.log("Down vote successfully");
                } else {
                    console.log("Down vote failure");
                }
            })
    }

// Event listener for upvote button click
    upvote.addEventListener("click", (e) => {
        if (!isActionValid()) {
            return;
        }

        upVoteViaApi();
    });

// Event listener for downvote button click
    downvote.addEventListener("click", (e) => {
        if (!isActionValid()) {
            return;
        }

        downVoteViaApi();
    });


// Event listener for follow button click
    if (follow != null) {
        follow.addEventListener("click", (e) => {
            if (!isActionValid()) {
                return;
            }

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
                        console.log("Follow failure");
                    }
                })

        });
    }

// Event listener for send comment button click
    sendCommentButton.addEventListener("click", (e) => {
        if (!isActionValid()) {
            return;
        }

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

})