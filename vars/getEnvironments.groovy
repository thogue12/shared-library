def call(String cloudProvider) {
    if (cloudProvider == "aws") {
        return ["dev", "test", "prod"]
    } else if (cloudProvider == "azure") {
        return ["dev", "test"]
    } else {
        return ["dev", "test", "prod", "both"]
    }
}
