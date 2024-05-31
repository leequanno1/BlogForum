const handleUnfollow = async function (flid) {

    const uid = parseInt(getCookie("SessionUserID"),10);
    let responseMessage;
    await (fetch("/api/user/unfollowUser", {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },body: JSON.stringify({
            uid: uid,
            flid: flid
        })
    })
        .then(response => response.json())
        .then(data => {
            responseMessage = data;
        })
    );
    if(responseMessage.message === "OK"){
        removeUser(flid);
    }

}

function getCookie(name) {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    if (parts.length === 2) {
        return parts.pop().split(';').shift();
    }
    return null;
}

const removeUser = function (flid) {
    console.log("user"+flid);
    document.getElementById("user"+flid).remove();
}