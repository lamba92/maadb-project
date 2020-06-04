function () {
    if (this.sentiment === "%%%SENTIMENT%%%") {
        for (var key in this.hashtagsWithOccurrences) {
            if (this.hashtagsWithOccurrences.hasOwnProperty(key)) {
                emit(key, this.hashtagsWithOccurrences[key]);
            }
        }
    }
}
