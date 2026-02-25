// Create a function to define resources available based on cloud provider and environment
def getResources(cloudProvider, environment) {
    def resources = [
        aws: [
            dev: [ 'VPC', 'EC2', 'S3', 'Security_Group'],
            test: [ 'VPC', 'EC2', 'S3'],
            prod: [ 'VPC', 'EC2', 'S3', 'RDS', 'ALB' ],
        ],
        azure: [
            dev: ['VNET', 'VM', 'Storage_Account'],
            test: ['VNET', 'VM', 'Storage_Account', 'Network_Security_Group']
        ],
        both: [
            dev: ['VPC', 'EC2', 'S3', 'VNET', 'VM', 'Storage_Account'],
            test: ['VPC', 'EC2', 'S3', 'RDS', 'ALB', 'VNET', 'VM', 'Storage_Account', 'Network_Security_Group'],
            prod: ['VPC', 'EC2', 'S3', 'RDS', 'ALB']
        ]
    ]
    return resources[cloudProvider][environment] ?: []
}