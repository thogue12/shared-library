// vars/getStorageAccounts.groovy
import groovy.json.JsonSlurper

def call(String selectedSub) {
    if (!selectedSub || selectedSub.contains("Error")) {
        return ["Select a valid subscription..."]
    }

    try {
        // Extract ID
        def subId = selectedSub.contains("(") ? 
                    selectedSub.substring(selectedSub.lastIndexOf("(") + 1, selectedSub.lastIndexOf(")")) : 
                    selectedSub

        def cmd = ["/usr/bin/az", "storage", "account", "list", "--subscription", subId, "--query", "[].name", "--output", "json"]
        def proc = cmd.execute()
        def out = new StringBuilder(), err = new StringBuilder()
        proc.waitForProcessOutput(out, err)

        if (proc.exitValue() == 0) {
            return new JsonSlurper().parseText(out.toString())
        } else {
            return ["Error: " + err.toString().take(30)]
        }
    } catch (Exception e) {
        return ["System Error"]
    }
}