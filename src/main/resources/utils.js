var List = Java.type("java.util.LinkedList")
function getImages() {
    var list = new List();
    for (var key in chapterImages) {
        list.add(chapterImages[key])
    }
    return list;
}
function getPageTitle() {
    return pageTitle;
}
function getChapterPath() {
    return chapterPath;
}
function getChapterPath() {
    return chapterPath;
}