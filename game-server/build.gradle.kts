plugins {
    kotlin("jvm")
    application
    id("org.zeroturnaround.gradle.jrebel")
    id("com.github.johnrengelman.shadow")
}

application {
    mainClass.set("com.osroyale.Main")
    applicationDefaultJvmArgs += arrayOf(
        "-XX:-OmitStackTraceInFastThrow",
        "--enable-preview",
        "-XX:+UseZGC",
        "-Xmx8g",
        "-Xms4g",
        "-XX:MaxGCPauseMillis=100"
    )
}

kotlin {
    jvmToolchain(21)
}

dependencies {
    implementation("io.netty", "netty-all", "4.1.96.Final")
    implementation("joda-time", "joda-time", "2.12.5")
    implementation("org.quartz-scheduler", "quartz", "2.3.2")
    implementation("com.jcabi", "jcabi-jdbc", "0.19.0")
    implementation("com.google.code.gson", "gson", "2.10.1")
    implementation("com.google.guava", "guava", "32.1.2-jre")
    implementation("org.jsoup", "jsoup", "1.16.1")
    implementation("org.apache.commons", "commons-compress", "1.23.0")
    implementation("com.moandjiezana.toml", "toml4j", "0.7.2")
    implementation("com.mysql:mysql-connector-j:8.0.33")
    implementation("com.zaxxer", "HikariCP", "5.0.1")
    implementation("org.postgresql:postgresql:42.6.0")
    implementation("org.apache.logging.log4j", "log4j-core", "2.20.0")
    implementation("org.apache.ant", "ant", "1.10.14")
    implementation("org.jctools", "jctools-core", "4.0.1")
    implementation("io.github.classgraph", "classgraph", "4.8.162")
    implementation("com.discord4j", "discord4j-core", "3.2.4")
    implementation("it.unimi.dsi", "fastutil", "8.5.12")
    implementation("net.dv8tion", "JDA", "5.0.0-beta.13")
    implementation("com.hankcs:aho-corasick-double-array-trie:1.2.3")
    implementation("org.mindrot", "jbcrypt", "0.4")

    val slf4jVersion = "2.0.7"
    implementation("org.slf4j", "slf4j-api", slf4jVersion)
    runtimeOnly("org.slf4j", "slf4j-simple", slf4jVersion)

    implementation("de.mkammerer:argon2-jvm:2.11")
    implementation("net.openhft:affinity:3.23.3")
}

sourceSets["main"].java {
    srcDir("plugins")
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.compilerArgs = mutableListOf("--enable-preview")
}