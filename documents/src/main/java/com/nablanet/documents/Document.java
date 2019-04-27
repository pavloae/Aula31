package com.nablanet.documents;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.Objects;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Document {

    public static boolean deleteFolder(File folderToBeDeleted) {
        if (folderToBeDeleted == null) return false;
        if (folderToBeDeleted.listFiles() != null)
            for (File file : Objects.requireNonNull(folderToBeDeleted.listFiles()))
                deleteFolder(file);
        return folderToBeDeleted.delete();
    }

    public static void copyFile(File src, File dst) throws IOException {

        BufferedInputStream bufferedInputStream = null;
        BufferedOutputStream bufferedOutputStream = null;
        try {
            bufferedInputStream = new BufferedInputStream(new FileInputStream(src));
            bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(dst));
            int b;
            while ((b = bufferedInputStream.read()) != -1)
                bufferedOutputStream.write(b);
        } finally {
            if (bufferedOutputStream != null)
                bufferedOutputStream.close();
            if (bufferedInputStream != null)
                bufferedInputStream.close();
        }

    }

    public static void copyInputStreamToFile(InputStream inputStream, File file) throws IOException {

        BufferedInputStream bufferedInputStream = null;
        BufferedOutputStream bufferedOutputStream = null;
        try {
            bufferedInputStream = new BufferedInputStream(inputStream);
            bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
            int b;
            while ((b = bufferedInputStream.read()) != -1)
                bufferedOutputStream.write(b);
        } finally {
            if (bufferedOutputStream != null)
                bufferedOutputStream.close();
            if (bufferedInputStream != null)
                bufferedInputStream.close();
        }

    }

    public static void unzip(File file) throws IOException {
        // Nos aseguramos de que exista un directorio vacio donde descomprimir
        File destDir = new File(file.getParent(), "unziped");
        if (destDir.exists() && !deleteFolder(destDir) || !destDir.mkdirs())
            return;
        ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(file));
        ZipEntry zipEntry;
        BufferedInputStream bufferedInputStream = new BufferedInputStream(zipInputStream);

        while ((zipEntry = zipInputStream.getNextEntry()) != null) {
            FileOutputStream fos = new FileOutputStream(newFile(destDir, zipEntry));
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fos);
            int b;
            while ((b = bufferedInputStream.read()) != -1)
                bufferedOutputStream.write(b);
            bufferedOutputStream.close();
        }

        zipInputStream.closeEntry();
        zipInputStream.close();
        bufferedInputStream.close();
    }

    public static File newFile(File destDir, ZipEntry zipEntry) throws IOException {

        File destFile = new File(destDir, zipEntry.getName());

        if (!destFile.getParentFile().exists() && !destFile.getParentFile().mkdirs())
            throw new IOException(
                    "No se pudo crear el directorio: " + destFile.getParentFile().getAbsolutePath()
            );

        if (!destFile.getCanonicalPath().startsWith(destDir.getCanonicalPath() + File.separator))
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());

        return destFile;
    }

    public static void zipDirectory(File sourceDirectory, File target) throws IOException {

        FileOutputStream fileOutputStream = new FileOutputStream(target);
        ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream);
        try {

            // El primer archivo a comprimir debe ser mimetype
            File mimetype = new File(sourceDirectory, "mimetype");
            addFileToZipArchive(zipOutputStream, mimetype, null);

            if (!mimetype.delete())
                return;

            for (File file : Objects.requireNonNull(sourceDirectory.listFiles()))
                addFileToZipArchive(zipOutputStream, file, null);

        } catch (Exception e) {
            e.printStackTrace();
        }
        zipOutputStream.flush();
        fileOutputStream.flush();
        zipOutputStream.close();
        fileOutputStream.close();

    }

    private static void addFileToZipArchive(
            ZipOutputStream zipOutputStream, File fileToZip, String parentDirectoryName
    ) throws Exception {

        if (fileToZip == null || !fileToZip.exists())
            return;

        String zipEntryName = fileToZip.getName();
        if (parentDirectoryName != null && !parentDirectoryName.isEmpty())
            zipEntryName = parentDirectoryName + "/" + fileToZip.getName();

        if (fileToZip.isDirectory()) {
            for (File file : Objects.requireNonNull(fileToZip.listFiles()))
                addFileToZipArchive(zipOutputStream, file, zipEntryName);
        } else {
            System.out.println("   " + zipEntryName);
            byte[] buffer = new byte[1024];
            FileInputStream fileInputStream = new FileInputStream(fileToZip);
            ZipEntry zipEntry = new ZipEntry(zipEntryName);

            // El archivo mimetype no debe estar comprimido
            if (zipEntry.getName().equals("mimetype")) {

                RandomAccessFile f = new RandomAccessFile(fileToZip, "r");
                byte[] entryData = new byte[(int)f.length()];
                f.readFully(entryData);

                zipEntry.setMethod(ZipEntry.STORED);
                zipEntry.setSize(entryData.length);
                zipEntry.setCompressedSize(entryData.length);

                CRC32 crc = new CRC32();
                crc.update(entryData);
                zipEntry.setCrc(crc.getValue());

            }

            zipOutputStream.putNextEntry(zipEntry);

            int length;
            while ((length = fileInputStream.read(buffer)) > 0) {
                zipOutputStream.write(buffer, 0, length);
            }
            zipOutputStream.closeEntry();
            fileInputStream.close();
        }
    }

}
