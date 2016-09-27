#service mysql start && cd /opt && java -Dserver.port=8080 -jar target/yoAlert-0.0.1.jar
cd /opt && java -Dsendinblue_key=${SENDINBLUE_KEY} -Dserver.port=8080 -jar target/yoAlert-0.0.1.jar
