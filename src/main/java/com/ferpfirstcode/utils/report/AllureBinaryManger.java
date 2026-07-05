package com.ferpfirstcode.utils.report;

import com.ferpfirstcode.utils.OSUtils;
import com.ferpfirstcode.utils.TerminalUtils;
import com.ferpfirstcode.utils.logs.LogsManager;
import org.jsoup.Jsoup;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.OutputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class AllureBinaryManger {

    // 💡 Fallback version in case GitHub is down or network is disconnected
    private static final String FALLBACK_VERSION = "2.34.1";

    private static class LazyHolder {
        static final String VERSION = resolveVersion();

        private static String resolveVersion() {
            try {
                // Added a 5-second timeout to prevent the framework from hanging
                String url = Jsoup.connect("https://github.com/allure-framework/allure2/releases/latest")
                        .timeout(5000)
                        .followRedirects(true)
                        .execute()
                        .url()
                        .toString();
                return url.split("/tag/")[1];
            } catch (Exception e) {
                // 🔥 Instead of throwing an exception and crashing the test, use the fallback version
                LogsManager.error("GitHub is unreachable (Timeout/504). Falling back to stable version: " + FALLBACK_VERSION, e.getMessage());
                return FALLBACK_VERSION;
            }
        }
    }

    public static void downloadAndExtract() {
        try {
            String version = LazyHolder.VERSION;
            Path extractionDir = Paths.get(AllureConstants.EXTRACTION_DIR.toString(), "allure-" + version);

            if (Files.exists(extractionDir)) {
                LogsManager.info("Allure binaries already exist for version: " + version);
                return;
            }

            LogsManager.info("Starting download for Allure binaries version: " + version);
            Path zipPath = downloadZip(version);

            if (zipPath != null && Files.exists(zipPath)) {
                extractZip(zipPath);
                LogsManager.info("Allure binaries downloaded and extracted successfully.");

                // Give execute permissions to the binary file on Mac/Linux
                if (!OSUtils.getOperatingSystemType().equals(OSUtils.OSType.WINDOWS)) {
                    TerminalUtils.executeTerminalCommand("chmod", "u+x", getExecutable().toString());
                }

                // 🔥 Clean up the exact zip file directly (Cleaner and safer)
                Files.deleteIfExists(zipPath);
            }

        } catch (Exception e) {
            LogsManager.error("Error during downloading or extracting Allure binaries", e.getMessage());
        }
    }

    public static Path getExecutable() {
        String version = LazyHolder.VERSION;
        Path binaryPath = Paths.get(AllureConstants.EXTRACTION_DIR.toString(), "allure-" + version, "bin", "allure");

        return OSUtils.getOperatingSystemType() == OSUtils.OSType.WINDOWS
                ? binaryPath.resolveSibling(binaryPath.getFileName() + ".bat")
                : binaryPath;
    }

    // Download ZIP file for Allure
    private static Path downloadZip(String version) {
        try {
            String url = AllureConstants.ALLURE_ZIP_BASE_URL + version + "/allure-commandline-" + version + ".zip";
            Path zipFile = Paths.get(AllureConstants.EXTRACTION_DIR.toString(), "allure-" + version + ".zip");

            if (!Files.exists(zipFile)) {
                Files.createDirectories(AllureConstants.EXTRACTION_DIR);
                try (BufferedInputStream in = new BufferedInputStream(new URI(url).toURL().openStream());
                     OutputStream out = Files.newOutputStream(zipFile)) {
                    in.transferTo(out);
                }
            }
            return zipFile;
        } catch (Exception e) {
            LogsManager.error("Error downloading Allure zip file from URL", e.getMessage());
            return null;
        }
    }

    // Extract ZIP file for Allure
    private static void extractZip(Path zipPath) {
        try (ZipInputStream zipInputStream = new ZipInputStream(Files.newInputStream(zipPath))) {
            ZipEntry entry;
            while ((entry = zipInputStream.getNextEntry()) != null) {
                Path filePath = Paths.get(AllureConstants.EXTRACTION_DIR.toString(), entry.getName());

                if (entry.isDirectory()) {
                    Files.createDirectories(filePath);
                } else {
                    Files.createDirectories(filePath.getParent());
                    Files.copy(zipInputStream, filePath);
                }
            }
        } catch (Exception e) {
            LogsManager.error("Error extracting Allure zip file", e.getMessage());
        }
    }
}