//Declarative pipeline
pipeline{
    agent any
    parameters {
        string(name: 'SERVERIP', defaultValue: '', description: '')
         string(name: 'BUILDNUM', defaultValue: '', description: '')
          string(name: 'BRANCH', defaultValue: '', description: '') 
}
stages{
    stage("Multiple sever deployment"){
        steps{

            sh '''
            aws s3 cp s3://mamuu/pandu/${BRANCH}/${BUILDNUM}/hello-${BUILDNUM}.war .
            ls -l
            IFS=, read -ra input <<< "${SERVERIP}"
            for ip in "${input[@]}"
            do
            echo "$ip"
            scp -o StrictHostKeyChecking=no -i /tmp/mamu1031.pem hello-${BUILDNUM}.war ec2-user@$ip:/var/lib/tomcat/webapps
            ssh -o StrictHostKeyChecking=no -i /tmp/mamu1031.pem ec2-user@$ip "hostname"
            done
            '''
        }
    }
}
}