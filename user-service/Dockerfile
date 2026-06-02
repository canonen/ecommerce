# Build aşaması — Maven ile jar üret
FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /app
COPY pom.xml .

# Önce sadece dependency'leri indir — cache için
# Kod değişse bile dependency'ler tekrar indirilmez
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests

# Runtime aşaması — sadece jar'ı çalıştır
# builder'dan daha küçük image — production best practice
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]