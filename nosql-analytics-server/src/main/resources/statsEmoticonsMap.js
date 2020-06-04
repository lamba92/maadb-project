function () {
    if (this.sentiment === "%%%SENTIMENT%%%") {
        for (var key in this.emoticonsWithOccurrences) {
            if (this.emoticonsWithOccurrences.hasOwnProperty(key)) {
                emit(key, this.emoticonsWithOccurrences[key]);
            }
        }
    }
}
