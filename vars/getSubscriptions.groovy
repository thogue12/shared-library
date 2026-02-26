import groovy.json.JsonSlurper

def call() {
    try {
        def cmd = ['/bin/bash', '-c', '/usr/bin/az account list --query "[].{name:name, id:id}" --output json']
        def process = cmd.execute()
        def output = process.text
        process.waitFor()

        if (process.exitValue() == 0 && output) {
            def jsonSlurper = new groovy.json.JsonSlurper()
            def data = jsonSlurper.parseText(output)
            
            // Return the list for the dropdown
            return data.collect { sub -> "${sub.name} (${sub.id})" }
        } else {
            return ["No subscriptions found or CLI not logged in"]
        }
    } catch (Exception e) {
        return ["Error in Shared Library: ${e.message}"]
    }
}