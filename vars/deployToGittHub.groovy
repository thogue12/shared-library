def call(String credentialsId, String filePath) {
    try {

        if (fileExists(filePath)) {
            echo "File ${filePath} found!!"
        } else {
            error "File ${filePath} not found. Exiting..."
        }

        withCredentials([usernamePassword(credentialsId: credentialsId, usernameVariable: 'GIT_USER', passwordVariable: 'GIT_PASS')]) {
            
            echo "Github credentials bound. Proceeding..."

            sh "git add ${filePath}"
            sh "git commit -m 'a commit from jenkins'"
            sh "git push https://${GIT_USER}:${GIT_PASS}@github.com/your-repo.git HEAD:main"
        }
        
        return true

    } catch (Exception e) {
         error "Git operations failed: ${e.message}"
    }
}