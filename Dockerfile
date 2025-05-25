FROM eclipse-temurin:23-jdk-alpine

WORKDIR /app

#jarファイルのコピー(先にビルドが必要！！)
COPY target/nkjwebapp-0.0.1-SNAPSHOT.jar app.jar

#ポート開放
EXPOSE 9136

#アプリ起動
ENTRYPOINT [ "java", "-jar", "app.jar" ]