dependencies {
    implementation(project(":pollpoll-core"))

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    runtimeOnly("mysql:mysql-connector-java")
    runtimeOnly("com.h2database:h2")
}
