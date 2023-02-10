# AuthlibInjectorWrapper
A tool which can help you use Authlib injector on Minecraft panels(Multicraft etc.), and even with more magical custom options

# Tip
I usually code in Python, that means I do not have a good master of Java, though I have learned a bit.<br>
But this project still works well, that's the most important :-)

# Long-in-short tutorial
1. Download the latest build jar file from the [release page](https://github.com/snnyyp/AuthlibInjectorWrapper/releases)
2. Upload the jar file to your server(through FTP software or online FTP manager)
3. If the server provider forces you to rename the jar file, please follow what he said
4. Go to your web panel page and start the server. When you see "Server will start after 5 seconds..." in the console log, stop the server immediately
5. Go back to your FTP software or online FTP manager, refresh it, then you can see "AuthlibInjectorWrapper.json" file, try to edit the config file and save it (More about the config file? Go to read the next section)
6. Restart the server, and you can see the final result

# More about the config file
There are few simple options in the config file, but make sure DO NOT MESS UP THE JSON FILE STRUCTURE
1. PrintWelcomeTitle[true, false]: print welcome title or not
2. PrintSystemDetail[true, false]: print system details or not, including OS, Java binary home(the location of binary "java" or "java.exe"), JVM bit, machine free memory, machine total memory and JVM arguments
3. JavaBinaryHome[String, default for "java", MUST]: the location of binary "java" or "java.exe". DO NOT EDIT IT IF YOU DO NOT KNOW WHAT THE HELL IS IT
4. JvmArguments[String, default for blank, with magical usage]: if this option left for blank, we will use the arguments which the panel gives.Otherwise, we will use the arguments you provide(though you can configure AuthlibInjector here, but we have provided a more graceful way to achieve that, so you'd better not add"-javaagent:authlib_injector.jar=https://example.com/api/yggdrasil/" here)
5. AuthlibInjectorPath[String, default for "authlib_injector.jar"]: the location of the Authlib Injector
6. YggdrasilUrl[String, default for "https://example.com/api/yggdrasil/", MUST]: please change it to your own Yggdrasil API url
7. ServerJar[String, default for "server.jar", MUST]: please change it to your own server jar location
8. ServerJarArguments[String ,default for blank]: The arguments for the server jar file, rarely used.DO NOT EDIT IT IF YOU DO NOT KNOW WHAT THE HELL IS IT
