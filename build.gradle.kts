import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm") version "1.4.31"
    id("com.github.johnrengelman.shadow") version "5.1.0"
}

group = "com.wandisco.support"
version = "1.0-SNAPSHOT"

val hadoopArtifactVersion = "2.6.0-cdh5.16.0"
val hiveArtifactVersion = "1.1.0-cdh5.16.0"

repositories {
    maven("https://repository.cloudera.com/artifactory/cloudera-repos/")
    mavenCentral()
}

dependencies {
    implementation("org.apache.hadoop:hadoop-common:$hadoopArtifactVersion")
    implementation("org.apache.hive:hive-common:$hiveArtifactVersion")
    implementation("org.apache.hive:hive-metastore:$hiveArtifactVersion")
}


tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.withType<ShadowJar> {
    isZip64 = true
}

configurations {
    runtime {
        exclude(module="hadoop-common")
        exclude(module="hive-common")
        exclude(module="hive-metastore")
    }
}
