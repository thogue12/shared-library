def call(Map config =[:]){
    String credentialsId = config.creds
    String filePath = config.file
    String repoUrl = config.repo ?: 'github.com/thogue12/cloud-infrastructure.git'
    String branch = config.branch?: 'main'

    try{
        if (!fileExists(filePath)) {
            error "File ${filePath} nofound. Exiting..."
        }
        withCredentials([usernamePassword(credentialsId: credentialsId, usernameVariable: 'GIT_USER', passwordVariable: 'GIT_PASS')]) {
        
            sh """
                git config user.email "thogue12@local.com"
                git config user.name  "thogue12"

                # only commit if the file changed
                if [ -n "\$(git status --porcelan ${filePath})" ]; then
                    echo "Changes detected in ${filePath}. Committing..."
                    git add ${filePath}
                    git commit -m "Automated update of ${filePath} from Jenkins"
                    git push https://${GIT_USER}:${GIT_PASS}@${repoUrl} HEAD:${branch}

                else
                    echo "No changes detected on ${filePath}. Skipping git push"
            """
        }
        return true
    }catch (Exception e){
        error "Git operations failed: ${e.message}"
    }
}

