import groovy.json.JsonSlurper

def call() {
    try {
        // Active Choices passes parameters into the 'binding'
        def selectedSub = binding.variables.get('SELECTED_SUBSCRIPTION')
        
        if (!selectedSub) return ["Select a Subscription first..."]

        // Extract ID from "Name (ID)" format
        def subId = selectedSub.contains("(") ? 
                    selectedSub.substring(selectedSub.lastIndexOf("(") + 1, selectedSub.lastIndexOf(")")) : 
                    selectedSub

        def cmd = ["/usr/bin/az", "storage", "account", "list", "--subscription", subId, "--query", "[].name", "--output", "json"]
        def proc = cmd.execute()
        def output = proc.text
        proc.waitFor()

        if (proc.exitValue() == 0) {
            return new JsonSlurper().parseText(output)
        } else {
            return ["Error fetching storage accounts"]
        }
    } catch (Exception e) {
        return ["Groovy Error: ${e.message}"]
    }
}