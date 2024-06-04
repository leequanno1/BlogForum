const isFollow = document.getElementById("isFollowed");
const follow = document.getElementById("followButton");

follow.addEventListener("click", (e) => {
    let url = isFollow.checked ? "api/user/unfollowUser" : "api/user/followUser";

    fetch(url, {
        method: 'POST', headers: {
            'Content-Type': 'application/json'
        }, body: JSON.stringify({
            uid: getCookie("SessionUserID"),
            flid: document.getElementById("userId").value
        })
    })
        .then(response => {
            if (response.ok) {
                console.log("Follow successfully");
                if(follow.innerText === "Follow") {
                    follow.innerText = "Followed";
                } else {
                    follow.innerText = "Follow";
                }
            } else {
                console.log("Failure successfully");
            }
        })

});