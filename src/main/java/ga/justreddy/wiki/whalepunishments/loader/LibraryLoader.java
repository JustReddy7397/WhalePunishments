package ga.justreddy.wiki.whalepunishments.loader;


import com.alessiodp.libby.BukkitLibraryManager;
import com.alessiodp.libby.Library;
import com.alessiodp.libby.relocation.Relocation;
import org.bukkit.plugin.Plugin;

/**
 * @author JustReddy
 */
public class LibraryLoader {

    private final BukkitLibraryManager libraryManager;

    public LibraryLoader(Plugin plugin) {
        this.libraryManager = new BukkitLibraryManager(plugin);
    }

    public void load() {
        libraryManager.addMavenCentral();
        libraryManager.addJitPack();
        createLibrary(new InternalLibrary(
                        "Ormlite",
                        "com.j256.ormlite",
                        "ormlite-jdbc",
                        "6.1"
                ),
                "ga.justreddy.wiki.whalepunishments.libs.ormlite"
                );
        createLibrary(new InternalLibrary(
                "Lamp-Common",
                "com.github.Revxrsal.Lamp",
                "common",
                "3.2.1"
        ),
                "ga.justreddy.wiki.whalepunishments.libs.lamp"
                );
        createLibrary(new InternalLibrary(
                "Lamp-Bukkit",
                "com.github.Revxrsal.Lamp",
                "bukkit",
                "3.2.1"
        ),
                "ga.justreddy.wiki.whalepunishments.libs.lamp"
        );
        createLibrary(new InternalLibrary(
                        "Adventure-API",
                        "net.kyori",
                        "adventure-platform-bukkit",
                        "4.3.2"
                )
        );
        createLibrary(new InternalLibrary(
                        "Adventure-Minimessage",
                        "net.kyori",
                        "adventure-text-minimessage",
                        "4.16.0"
                )
        );
        createLibrary(new InternalLibrary(
                "Adventure-Aids",
                "net.kyori",
                "adventure-api",
                "4.16.0"
        ));
    }

    private void createLibrary(InternalLibrary internalLibrary) {
        final Library.Builder builder = Library.builder()
                .loaderId(internalLibrary.name)
                .groupId(internalLibrary.groupId)
                .artifactId(internalLibrary.artifactId)
                .version(internalLibrary.version);
        libraryManager.loadLibraries(builder.build());
    }

    private void createLibrary(InternalLibrary internalLibrary, String packageToRelocate) {
        final Library.Builder builder = Library.builder()
                .loaderId(internalLibrary.name)
                .groupId(internalLibrary.groupId)
                .artifactId(internalLibrary.artifactId)
                .version(internalLibrary.version)
                .relocate(packageToRelocate.replaceAll("\\.", "{}"),
                        "ga{}justreddy{}wiki{}whalepunishments{}libs{}" + packageToRelocate);
        libraryManager.loadLibraries(builder.build());
    }


    record InternalLibrary(
            String name,
            String groupId,
            String artifactId,
            String version
    ) {
    }
}
