const navList = ["navPosts", "navBookmark", "navFollowedPosts", "navFollowedUsers", "navFollowers"];
const pathnames = window.location.pathname.split("/");

console.log(document.cookie);
const activeNav = function (id) {
    document.getElementById(id).classList.add("active");
}

if (pathnames.length > 2) {
    let page = pathnames[2];
    for (let i = 0; i < navList.length; i++) {
        if("nav" + page === navList[i].toLowerCase()) {
            activeNav(navList[i]);
            break;
        }
    }
} else {
    activeNav("navPosts");
}


