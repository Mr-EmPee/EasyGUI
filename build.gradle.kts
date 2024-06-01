plugins {
  id("java-library")
  id("io.freefair.lombok") version "8.4"
  id("com.vanniktech.maven.publish") version "0.28.0"
}

version = findProperty("tag") ?: "0.0.3-SNAPSHOT"

dependencies {
  compileOnly("com.destroystokyo.paper:paper-api:1.12.2-R0.1-SNAPSHOT")
  compileOnly("org.jetbrains:annotations:24.1.0")
}

java.toolchain.languageVersion.set(JavaLanguageVersion.of(11))

mavenPublishing {
  coordinates("io.github.mr-empee", "easy-gui", version.toString())

  pom {
    name.set("EasyGUI")
    description.set("Create advanced GUIs in a simple manner")
    inceptionYear.set("2024")
    url.set("https://github.com/Mr-EmPee/EasyGUI")
    licenses {
      license {
        name.set("The Apache License, Version 2.0")
        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
        distribution.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
      }
    }

    developers {
      developer {
        id.set("mr-empee")
        name.set("Mr. EmPee")
        url.set("https://github.com/mr-empee/")
      }
    }

    scm {
      url.set("https://github.com/Mr-EmPee/EasyGUI")
      connection.set("scm:git:git://github.com/Mr-EmPee/EasyGUI.git")
      developerConnection.set("scm:git:ssh://git@github.com:Mr-EmPee/EasyGUI.git")
    }
  }
}