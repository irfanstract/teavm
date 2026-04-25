
package org.teavm

package native
package os



import java.util.Locale







object AppData
{

  /**
   * the `AppDataResolver` instance for the current platform.
   */
  def getResolver(): AppDataResolver
  =
      (System.getProperty("os.name").toUpperCase(Locale.ROOT ) ).match
        case s"WIN${_}" => Win7AppDataResolver()

  transparent inline def CROSSDEVICE_PROHIBITED: CURRENTDEVICEONLY.type = CURRENTDEVICEONLY //
  transparent inline def CROSSDEVICE_REQUIRED  :   CROSSDEVICE.type     =   CROSSDEVICE     //
  case object CURRENTDEVICEONLY extends CrossDeviceSharingYn
  case object   CROSSDEVICE     extends CrossDeviceSharingYn
  trait CrossDeviceSharingYn private[teavm] ()
  object CrossDeviceSharingYn :
    transparent inline given CrossDeviceSharingYn = CROSSDEVICE_PROHIBITED

  // "file.separator"

} // AppData.

abstract class AppDataResolver private[teavm] ()
{ thisAppDataResolver =>

  /**
   * path-name-separator; `/` on POSIX, or `\` on Win32
   */
  def SLASH: String

  def getTempDir
    ( )
  : Path

  def getAppDataDir
    (using crossDeviceShrYn: AppData.CrossDeviceSharingYn )
    (k: AppName )
  : Path
  = (getAppDataRoot().toString() ).++(SLASH).++(k)

  def getAppDataRoot
    (using crossDeviceShrYn: AppData.CrossDeviceSharingYn )
    ( )
  : Path

  def getCpf
    ( )
  : Path

}

abstract class AppDataResolver07() extends AppDataResolver()
{ thisAppDataResolver =>

}

object Win7AppDataResolver :

  def apply(): AppDataResolver
  =
        new AppDataResolver07()
        { thisAppDataResolver =>

          override final val SLASH = "\\"

          override def getTempDir
            ( )
          = (getAppDataRoot(using crossDeviceShrYn = AppData.CURRENTDEVICEONLY )().toString() ).++(SLASH).++("Temp")

          override def getAppDataDir
            (using crossDeviceShrYn: AppData.CrossDeviceSharingYn )
            (k: AppName )
          : Path
          =

            if k.toUpperCase(Locale.ROOT) == "TEMP" then
              throw new IllegalArgumentException(s"'Temp' (here, '${k}') is not a valid app data directory name")

            (getAppDataRoot().toString() ).++(SLASH).++(k)

          override def getAppDataRoot
            (using crossDeviceShrYn: AppData.CrossDeviceSharingYn )
            ( )
          =
              crossDeviceShrYn.match
                case AppData.CROSSDEVICE       => System.getenv(     "APPDATA").toString()
                case AppData.CURRENTDEVICEONLY => System.getenv("LOCALAPPDATA").toString()

          override def getCpf
            ( )
          = System.getenv("CommonProgramFiles")

        }

  System.err.println :
    s"WARNING: Win7AppDataResolver EXPERIMENTAL"





object AppName

type AppName
>: String
<: String









