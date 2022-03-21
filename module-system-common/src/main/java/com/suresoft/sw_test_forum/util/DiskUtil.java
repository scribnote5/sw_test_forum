package com.suresoft.sw_test_forum.util;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class DiskUtil {
    private String driverName;

    private String totalSize;

    private String usedSize;

    private String availableSize;

    @Builder
    public DiskUtil(String driverName, String totalSize, String usedSize, String availableSize) {
        this.driverName = driverName;
        this.totalSize = totalSize;
        this.usedSize = usedSize;
        this.availableSize = availableSize;
    }

    public static List<DiskUtil> getDiskUtil() {
        List<DiskUtil> result = new ArrayList<>();
        double totalSize = 0;
        double usedSize = 0;
        double availableSize = 0;

        File[] drives = File.listRoots();

        for (File drive : drives) {
            totalSize = drive.getTotalSpace() / Math.pow(1024, 3);
            usedSize = drive.getUsableSpace() / Math.pow(1024, 3);
            availableSize = totalSize - usedSize;

            result.add(DiskUtil.builder()
                    .driverName(drive.getAbsolutePath())
                    .totalSize(String.format("%.2f", totalSize) + "GB")
                    .usedSize(String.format("%.2f", usedSize) + "GB")
                    .availableSize(String.format("%.2f", availableSize) + "GB")
                    .build());
        }

        return result;
    }
}