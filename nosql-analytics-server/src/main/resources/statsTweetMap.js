function () {
    if (this.sentiment === "%%%SENTIMENT%%%") {
        for (var key in this.stemmedTweetWithOccurrences) {
            if (this.stemmedTweetWithOccurrences.hasOwnProperty(key)) {
                emit(key, this.stemmedTweetWithOccurrences[key]);
            }
        }
    }
}
