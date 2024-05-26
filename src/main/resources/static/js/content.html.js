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
    if(isBookmark.checked) {
        icon.classList.add("fa-solid");
    } else {
        icon.classList.remove("fa-solid");
    }
})

isFollow.addEventListener("change", (e) => {
    if(isFollow.checked) {
        follow.innerText = "Đã theo dõi";
    } else {
        follow.innerText = "Theo dõi";
    }
})

isUpvote.addEventListener("change", (e) => {
    if(isUpvote.checked) {
        upvoteCheck();
        if(isDownvote.checked){
            downvoteUncheck();
        }
    } else {
        upvoteUncheck();
    }
})

isDownvote.addEventListener("change", (e) => {
    if(isDownvote.checked) {
        downvoteCheck();
        if(isUpvote.checked) {
            upvoteUncheck();
        }
    } else {
        downvoteUncheck();
    }
})

sendCommentButton.addEventListener("click", (e) => {
    if(commentContent.value) {
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
        commentContent.value = "";
    }
})