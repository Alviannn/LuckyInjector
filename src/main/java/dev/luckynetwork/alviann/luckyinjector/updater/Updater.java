package dev.luckynetwork.alviann.luckyinjector.updater;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.luckynetwork.alviann.luckyinjector.closer.Closer;
import dev.luckynetwork.alviann.luckyinjector.loader.Loader;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.nio.file.Files;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

@RequiredArgsConstructor
@Getter
public class Updater {

    @Nullable private String latestVersion, latestDownloadUrl;
    @Getter private final Logger logger;

    /**
     * fetches for the new update asynchronously
     * <br><br>
     * to get it synchronously you can do it like this
     * <pre><code>
     *     Updater updater = ...;
     *     try {
     *         boolean hasUpdate = updater.fetchUpdate().join();
     *         // do other stuff
     *     } catch (Exception e) {
     *         e.printStackTrace();
     *     }
     * </code></pre>
     */
    public CompletableFuture<Boolean> fetchUpdate() {
        return CompletableFuture.supplyAsync(() -> {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(30L, TimeUnit.SECONDS)
                    .readTimeout(30L, TimeUnit.SECONDS)
                    .build();

            Request request = new Request.Builder()
                    .url("https://raw.githubusercontent.com/Alviannn/LuckyInjector/master/update-info.json")
                    .header("User-Agent", "Mozilla/5.0")
                    .get()
                    .build();

            boolean fetchedUpdateInfo = false;
            try (Closer closer = new Closer()) {
                Response response = client.newCall(request).execute();
                ResponseBody body = closer.add(response.body());

                if (body == null)
                    throw new NullPointerException("Body is null");

                Reader reader = closer.add(body.charStream());
                JsonParser parser = new JsonParser();
                JsonObject updateInfo = parser.parse(reader).getAsJsonObject();

                latestVersion = updateInfo.get("version").getAsString();
                latestDownloadUrl = updateInfo.get("download-url").getAsString();

                fetchedUpdateInfo = true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return fetchedUpdateInfo;
        });
    }

    /**
     * checks if the plugin can update with the new version
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean canUpdate() {
        ClassLoader loader = this.getClass().getClassLoader();
        String currentVersion = null;

        try (Closer closer = new Closer()) {
            InputStream stream = closer.add(loader.getResourceAsStream("version.info"));

            if (stream == null)
                throw new NullPointerException("Cannot find version.info file!");

            Scanner scanner = closer.add(new Scanner(stream));
            if (scanner.hasNext())
                currentVersion = scanner.nextLine();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (currentVersion == null)
            return true;
        if (latestVersion == null)
            return false;

        return !currentVersion.equals(latestVersion);
    }

    /**
     * starts updating the plugin
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public CompletableFuture<Void> initiateUpdate(File pluginsFolder) {
        return CompletableFuture.runAsync(() -> {
            boolean updateSuccess = false;

            if (latestDownloadUrl == null)
                throw new NullPointerException("Latest download URL is null");

            logger.warning("Found a new version of LuckyInjector (" + latestVersion + ")!");
            logger.warning("Downloading LuckyInjector version " + latestVersion + "!");

            try (Closer closer = new Closer()) {
                URL url = new URL(latestDownloadUrl);
                InputStream stream = closer.add(url.openStream());

                if (stream == null)
                    throw new NullPointerException("Cannot fetch the stream!");

                File updatedFile = new File(pluginsFolder, new File(url.getFile()).getName());
                if (!updatedFile.exists()) {
                    // creates the plugin folder if needed
                    if (!pluginsFolder.exists())
                        pluginsFolder.mkdir();

                    // downloads the file
                    Files.copy(stream, updatedFile.toPath());
                    updateSuccess = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (!updateSuccess)
                return;

            File pluginFile = Loader.getCurrentPluginFile();
            if (pluginFile == null || !pluginFile.exists())
                return;

            // deletes the current plugin file once the file is successfully updated
            pluginFile.delete();
        });
    }

}
