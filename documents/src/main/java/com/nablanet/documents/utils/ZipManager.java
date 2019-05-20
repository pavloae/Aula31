package com.nablanet.documents.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipManager {

    /**
     * @param file Un archivo comprimido
     * @param location La ubicación donde se descomprimirá el archivo. Si no se especifica una
     *                 ubicación se utilizará el nombre del archivo comprimido para generar el
     *                 directorio.
     *                 Si ya existe un directorio con el mismo nombre se borrará.
     *                 Si existe un archivo con ese nombre se lanzará una exepción.
     * @return Un archivo representando el directorio donde se descomprimió
     * @throws IOException Cuando no se pueda crear un directorio donde descomprimir los archivos
     * o cuando falle la descompresión.
     */
    public static File unzip(File file, String location) throws IOException {

        File destDir;
        if (location == null)
            if (FileManager.getExtension(file) == null)
                throw new IllegalArgumentException("El nombre del archivo no puede ser descomprimido");
            else
                destDir = new File(file.getParent(), FileManager.getPrefix(file));
        else
            destDir = new File(location);

        if (destDir.exists() && destDir.isDirectory())
            FileManager.delete(destDir, true);

        if (destDir.exists() && destDir.isFile())
            throw new IOException("Existe un archivo con el mismo nombre de destino");

        if(!destDir.mkdirs())
            throw new IOException("No se pudo crear el directorio");

        ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(file));
        BufferedInputStream bufferedInputStream = new BufferedInputStream(zipInputStream);

        ZipEntry zipEntry;
        BufferedOutputStream bufferedOutputStream;
        while ((zipEntry = zipInputStream.getNextEntry()) != null) {
            if (zipEntry.isDirectory()){
                if (!new File(destDir, zipEntry.getName()).mkdirs())
                    throw new ZipException("No se pudo crear un directorio");
            } else {
                bufferedOutputStream = new BufferedOutputStream(
                        new FileOutputStream(newFile(destDir, zipEntry))
                );
                int b;
                while ((b = bufferedInputStream.read()) != -1)
                    bufferedOutputStream.write(b);
                bufferedOutputStream.close();
            }


        }

        zipInputStream.closeEntry();
        zipInputStream.close();
        bufferedInputStream.close();

        return destDir;
    }

    public static void zipODF(File sourceDirectory, File targetFile) throws IOException {

        FileOutputStream fileOutputStream = new FileOutputStream(targetFile);
        ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream);

        List<File> fileList = new ArrayList<>();

        // El primer archivo a comprimir debe ser mimetype
        fileList.add(new File(sourceDirectory, "mimetype"));
        for (File file : Objects.requireNonNull(sourceDirectory.listFiles()))
            if (!file.getName().equals("mimetype"))
                fileList.add(file);

        for (File file : fileList)
            addFileToODF(zipOutputStream, file, null);

        zipOutputStream.flush();
        fileOutputStream.flush();
        zipOutputStream.close();
        fileOutputStream.close();

    }

    private static void addFileToODF(
            ZipOutputStream zos, File fileToZip, String parentDirectoryName
    ) throws IOException {

        String zipEntryName = fileToZip.getName();
        if (parentDirectoryName != null && !parentDirectoryName.isEmpty())
            zipEntryName = parentDirectoryName + "/" + fileToZip.getName();

        if (fileToZip.isDirectory()) {
            for (File file : Objects.requireNonNull(fileToZip.listFiles()))
                addFileToODF(zos, file, zipEntryName);
        } else {

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

            zos.putNextEntry(zipEntry);

            int length;
            while ((length = fileInputStream.read(buffer)) > 0) {
                zos.write(buffer, 0, length);
            }
            zos.closeEntry();
            fileInputStream.close();
        }
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
}
