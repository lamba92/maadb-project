function () {
    if (this.sentiment === "%%%SENTIMENT%%%") {
        for (var key in this.emojisWithOccurrences) {
            if (this.emojisWithOccurrences.hasOwnProperty(key)) {
                emit(key, this.emojisWithOccurrences[key])
            }
        }
    }
}
