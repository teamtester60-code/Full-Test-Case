package com.ferpfirstcode.utils.report;

import com.ferpfirstcode.utils.OSUtils;
import com.ferpfirstcode.utils.TerminalUtils;
import com.ferpfirstcode.utils.TimeManager;
import com.ferpfirstcode.utils.logs.LogsManager;
import org.apache.commons.io.FileUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static com.ferpfirstcode.utils.OSUtils.OSType.*;
import static com.ferpfirstcode.utils.dataReader.PropertyReader.getProperty;
import static com.ferpfirstcode.utils.report.AllureConstants.HISTORY_FOLDER;
import static com.ferpfirstcode.utils.report.AllureConstants.RESULTS_HISTORY_FOLDER;

public class AllureReportGenerator {
    //generate allure report

    public static void generateReports(boolean isSingleFile) {
        Path outputFolder = isSingleFile ? AllureConstants.REPORT_PATH : AllureConstants.FULL_REPORT_PATH;
        // allure generate -o reports --single-file --clean
        List<String> command = new ArrayList<>(List.of(
                AllureBinaryManger.getExecutable().toString(),
                "generate",
                AllureConstants.RESULTS_FOLDER.toString(),
                "-o", outputFolder.toString(),
                "--clean"
        ));
        if (isSingleFile) command.add("--single-file");
        TerminalUtils.executeTerminalCommand(command.toArray(new String[0]));
        LogsManager.info("🛠️ Running Allure generate command: " + String.join(" ", command));

    }


    //rename report file
    public static String renameReport() {
        String newFileName = AllureConstants.REPORT_PREFIX + TimeManager.gettimestamp() + AllureConstants.REPORT_EXTENSION; // AllureReport_20250720_211230.html
        com.ferpfirstcode.FileUtils.renameFile(AllureConstants.REPORT_PATH.resolve(AllureConstants.INDEX_HTML).toString(), newFileName);
        Path indexPath = AllureConstants.REPORT_PATH.resolve(AllureConstants.INDEX_HTML);
        if (!Files.exists(indexPath)) {
            LogsManager.error("❌ index.html not found. Report generation may have failed.");
        }

        return newFileName;
    }

    //open allure report

    public static void openReport(String reportFileName) {

        Path reportPath = AllureConstants.REPORT_PATH.resolve(reportFileName);
        switch (OSUtils.getOperatingSystemType()) {
            case WINDOWS -> TerminalUtils.executeTerminalCommand("cmd.exe", "/c", "start", reportPath.toString());
            case MAC, LINUX -> TerminalUtils.executeTerminalCommand("open", reportPath.toString());
            default -> LogsManager.warn("Opening Allure Report is not supported on this OS.");
        }
    }


    // copy history
    public static void copyHistory() {
        try {
            FileUtils.copyDirectory(HISTORY_FOLDER.toFile(), RESULTS_HISTORY_FOLDER.toFile());
        } catch (Exception e) {
            LogsManager.error("Error copying history files", e.getMessage());
        }
    }
}
