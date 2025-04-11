FROM openjdk:21
WORKDIR /app
COPY target/PI-IV-Gerenc-Vendas-Estoque-0.0.1-SNAPSHOT.jar .
ENTRYPOINT ["java", "-jar", "PI-IV-Gerenc-Vendas-Estoque-0.0.1-SNAPSHOT.jar"]