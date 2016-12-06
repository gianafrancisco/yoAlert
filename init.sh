#service mysql start && cd /opt && java -Dserver.port=8080 -jar target/yoAlert-0.0.1.jar
cd /opt && java -Dsendinblue_key=${SENDINBLUE_KEY} \
                -Dspring.datasource.password=${DB_PASSWORD} \
                -Dserver.port=8080 \
                -Dleads_token=${LEADS_TOKEN} \
                -jar target/yoAlert-0.0.1.jar
