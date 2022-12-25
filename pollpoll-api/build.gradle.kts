tasks.getByName("bootJar") {
    enabled = true
}

tasks.getByName("jar") {
    enabled = false
}

dependencies {
    // module
    implementation(project(":pollpoll-core"))
    implementation(project(":storage:db-pollpoll"))
    implementation(project(":support:logging"))

    // spring
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    // etc
    implementation("org.springdoc:springdoc-openapi-ui:1.6.14")
}
