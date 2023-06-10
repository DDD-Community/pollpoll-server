tasks.getByName("bootJar") {
    enabled = true
}

tasks.getByName("jar") {
    enabled = false
}

tasks.bootJar {
    val activeProfile: String = System.getProperty("spring.profiles.active") ?: "default"
    archiveFileName.set("${project.name}-$activeProfile.jar")
}

dependencies {
    // module
    implementation(project(":pollpoll-core"))
    implementation(project(":storage:db-pollpoll"))
    implementation(project(":support:logging"))

    // spring
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")

    // etc
    implementation("org.springdoc:springdoc-openapi-ui:1.6.14")
    implementation("io.jsonwebtoken:jjwt:0.9.1")
}
