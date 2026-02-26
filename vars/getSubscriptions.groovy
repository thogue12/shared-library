import groovy.json.JsonSlurper

def call() {
    try {
        // Use full path to az (usually /usr/bin/az or /usr/local/bin/az)
        def process = ['/usr/bin/az', 'account', 'list', '--query', '[].{name:name, id:id}', '--output', 'json'].execute()
        def output = process.text
        def error = process.err.text
        process.waitFor()

        if (process.exitValue() == 0 && output) {
            def data = new JsonSlurper().parseText(output)
            return data.collect { "${it.name} (${it.id})" }
        } else {
            // This will show up in your dropdown so you can see the actual error
            return ["Error: ${error.take(100)}"]
        }
    } catch (Exception e) {
        return ["Library Exception: ${e.message}"]
    }
}