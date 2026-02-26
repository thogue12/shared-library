import groovy.json.JsonSlurper

def call(){
    try {
        if (SELECTED_SUBSCRIPTION == null || SELECTED_SUBSCRIPTION.trim().isEmpty()) {
            return ["Select a Subscription first..."]
        }

        def subId = SELECTED_SUBSCRIPTION.contains("(") ? 
                    SELECTED_SUBSCRIPTION.substring(SELECTED_SUBSCRIPTION.lastIndexOf("(") + 1, SELECTED_SUBSCRIPTION.lastIndexOf(")")) : 
                    SELECTED_SUBSCRIPTION

        def command = "/usr/bin/az account set --subscription ${subId} && /usr/bin/az storage account list --query '[].name' --output json 2>&1"
        def proc = ["/bin/bash", "-c", command].execute()
        def output = proc.text.trim()
        proc.waitFor()

        if (proc.exitValue() != 0) {
            return ["AZ CLI Error: " + output.take(50)] 
        }

        if (!output || output == "[]") {
            return ["ERROR: No storage accounts found in this sub"]
        }

        def data = new groovy.json.JsonSlurper().parseText(output)
        return data
                             
    } catch (Exception e) {
        return ["GROOVY ERROR: " + e.getMessage()]
    }
}