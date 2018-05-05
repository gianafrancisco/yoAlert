if [ -z ${DB_HOST} ]
then
    DB_HOST="alertas.chmathi3vejv.us-west-2.rds.amazonaws.com"
fi

if [ -z ${DB_NAME} ]
then
    DB_HOST="alertas"
fi

if [ -z ${DB_NAME} ]
then
    DB_USERNAME="alertas"
fi


cd /opt && java -Dsendinblue_key=${SENDINBLUE_KEY} \
                -Dspring.datasource.password=${DB_PASSWORD} \
                -Dspring.datasource.url=jdbc:mysql://${DB_HOST}:3306/${DB_NAME} \
                -Dspring.datasource.username=${DB_USERNAME} \
                -Dserver.port=8080 \
                -Dleads_token=${LEADS_TOKEN} \
                -DemailTo=${EMAIL_TO} \
                -Dfb.appId=${FB_APP_ID} \
                -Dfb.appSecret=${FB_APP_SECRET} \
                -jar target/yoAlert-0.0.1.jar
