
package org.teavm

package native
package os



import scala.jdk.CollectionConverters.{given}




class PlTest extends org.scalatest.funsuite.AnyFunSuite :

  test("GetProperties") :

    println(System.getProperties().asScala.toMap )

  test("GetEnv") :

    println(System.getenv().asScala.toMap )

  test("XADR") :

    println { AppData.getResolver().getAppDataRoot() }

    println { AppData.getResolver().getAppDataDir("ESBuild") }
    println { AppData.getResolver().getAppDataDir("Coursier") }
    println { AppData.getResolver().getAppDataDir("Electron") }

    println { AppData.getResolver().getAppDataDir(using crossDeviceShrYn = AppData.CROSSDEVICE_PROHIBITED ) ("OneDrvBackupSync") }
    println { AppData.getResolver().getAppDataDir(using crossDeviceShrYn = AppData.CROSSDEVICE_PROHIBITED ) ("ESBuild") }
    println { AppData.getResolver().getAppDataDir(using crossDeviceShrYn = AppData.CROSSDEVICE_PROHIBITED ) ("Coursier") }
    println { AppData.getResolver().getAppDataDir(using crossDeviceShrYn = AppData.CROSSDEVICE_PROHIBITED ) ("Electron") }

    println { AppData.getResolver().getAppDataDir(using crossDeviceShrYn = AppData.CROSSDEVICE_REQUIRED ) ("OneDrvBackups") }
    println { AppData.getResolver().getAppDataDir(using crossDeviceShrYn = AppData.CROSSDEVICE_REQUIRED ) ("ESBuild") }
    println { AppData.getResolver().getAppDataDir(using crossDeviceShrYn = AppData.CROSSDEVICE_REQUIRED ) ("Coursier") }
    println { AppData.getResolver().getAppDataDir(using crossDeviceShrYn = AppData.CROSSDEVICE_REQUIRED ) ("Electron") }

  test("XADR 2") :

    println { AppData.getResolver().getAppDataRoot() }

    println { AppData.getResolver().getAppDataDir("ESBuild") }
    println { AppData.getResolver().getAppDataDir("temp") }
    println { AppData.getResolver().getAppDataDir("Temp") }



