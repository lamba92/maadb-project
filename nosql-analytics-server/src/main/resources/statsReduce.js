function (key, values) {
    return {
        word: key,
        occurrences: values.reduce((a, b) => a + b, 0)
    }
}
