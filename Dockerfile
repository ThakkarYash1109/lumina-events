# ==========================================
# Lumina Integrated Marketplace - Dockerfile
# Multi-stage build for Java 21 Backend
# ==========================================

# Stage 1: Build Java Core & REST Engine
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

# Copy Java source tree
COPY backend-java/src ./src

# Create binary output directory and compile
RUN mkdir -p bin && javac -d bin $(find src -name "*.java")

# Stage 2: Minimal Production Runtime
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Maintain non-root secure execution
RUN addgroup -S lumina && adduser -S lumina -G lumina

# Copy compiled classes from build stage
COPY --from=build /app/bin ./bin
# Copy frontend assets to serve or bundle
COPY *.html *.css *.js ./
COPY lumina*.jpeg ./

USER lumina

EXPOSE 8080

ENV JAVA_OPTS="-Xms64m -Xmx256m"
ENV LUMINA_ENV="production"
ENV LUMINA_PORT=8080

HEALTHCHECK --interval=30s --timeout=5s --start-period=5s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/api/health || exit 1

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -cp bin com.lumina.server.LuminaApiServer"]

