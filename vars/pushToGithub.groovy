def call(Map config =[:]){
    String credentialsId = config.creds
    String filePath = config.file
    String repoUrl = config.repo ?: 'github.com/thogue12/cloud-infrastructure.git'
    String branch = config.branch?: 'main'

    try {
        if (!fileExists(filePath)) {
            error "File ${filePath} not found. Exiting..."
        }
        withCredentials([usernamePassword(credentialsId: credentialsId, usernameVariable: 'GIT_USER', passwordVariable: 'GIT_PASS')]) {
        
            sh """
                git config user.email "thogue12@local.com"
                git config user.name  "thogue12"

                # 1. Fixed spelling to --porcelain
                # 2. Used \\\$ so the shell handles the variable, not Groovy
                if [ -n "\$(git status --porcelain ${filePath})" ]; then
                    echo "Changes detected in ${filePath}. Committing..."
                    git add ${filePath}
                    git commit -m "Automated update of ${filePath} from Jenkins build ${env.BUILD_NUMBER}"
                    git push https://\${GIT_USER}:\${GIT_PASS}@${repoUrl} HEAD:${branch}
                else
                    echo "No changes detected on ${filePath}. Skipping git push"
                fi 
            """
        }
        return true
    } catch (Exception e){
        error "Git operations failed: ${e.message}"
    }
}