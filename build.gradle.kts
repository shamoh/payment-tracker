plugins {
    java
    application
}

repositories {
    jcenter()
}

dependencies {
    implementation("com.google.guava:guava:26.0-jre")

    testImplementation("junit:junit:4.12")
    testImplementation("org.hamcrest:hamcrest-all:1.3")
    testImplementation("org.mockito:mockito-core:2.23.4")
    testImplementation("com.github.stefanbirkner:system-rules:1.19.0")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

application {
    mainClassName = "cz.kramolis.exercises.paymenttracker.Main"

}

(tasks.getAt("run") as JavaExec).apply {
    standardInput = System.`in`
    systemProperty("paymentTracker.exchangeRate.USD.RMB", "0.14557")
    systemProperty("paymentTracker.exchangeRate.USD.HKD", "0.12764")
}
