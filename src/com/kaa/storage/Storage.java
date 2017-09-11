package com.kaa.storage;

import com.kaa.model.RealSpace;
import com.kaa.utils.IOUtils;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Gwirggiddug on 08.02.2015.
 */
public class Storage extends AbstractStorage<RealSpace> {

    protected long backlogLimit;
    protected long serializationPackSize;
    private long fileIndex = 0;

    private List<String> files = new LinkedList<>();
    private ExecutorService reader = Executors.newSingleThreadExecutor();
    private ExecutorService writer = Executors.newSingleThreadExecutor();
    private int processCount=0;//количество активных процессов сохранения
    private List<String> filesInProcess = new LinkedList<>();

    @Override
    public boolean add(RealSpace realSpace) {
        boolean add = super.add(realSpace);
        checkSize();
        return add;
    }

    @Override
    public boolean addAll(Collection<RealSpace> units) {
        boolean add = super.addAll(units);
        checkSize();
        return add;
    }

    @Override
    protected boolean checkSize() {
        if (size()> backlogLimit) {
            try {
                serialize();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else if(size()<serializationPackSize){
            try {
                deserialize();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * добавляет записанный файл в список досутпных
     * @param fileName
     */
    private synchronized void addFileName(String fileName) {
        if (filesInProcess.contains(fileName)) {
            filesInProcess.remove(fileName);
        }
        files.add(fileName);
    }

    /**
     * генерирует имя файла с пятизначным числовым суффиксом
     * @return имя файла сохранения вариантов
     */
    private synchronized String getFileNameForWrite() {
        String index = String.valueOf(++fileIndex);
        while (index.length()<5) {
            index = "0" + index;
        }

        String fileName = "solutions#" + ++fileIndex + ".page";
        filesInProcess.add(fileName);
        return fileName;
    }

    /**
     * @return файл для загрузки вариантов
     */
    private synchronized String getFileNameForRead() {
        if (files.size()>0) {
            String file = files.get(0);
            files.remove(0);
            return file;
        }
        return null;
    }


    /**
     * сохраняет избыточные варианты на диск
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void serialize() throws IOException, ClassNotFoundException {

        if (size()<= serializationPackSize) return;

        String fileForWrite = getFileNameForWrite();

        List<RealSpace> solutions = new LinkedList<>();
        while(size()> serializationPackSize) {
            RealSpace space = getLast();
            solutions.add(space);
        }

        processCount++;
        Runnable write = () -> {
            System.out.println("\nStart write:" + fileForWrite + "\n");
            IOUtils.saveVariants(solutions, fileForWrite);
            addFileName(fileForWrite);
            processCount--;
            System.out.println("\nStop write:" + fileForWrite + "\n");
        };
        writer.execute(write);
    }

    /**
     * загружает варианты с диска
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void deserialize() throws IOException, ClassNotFoundException {
        if (!hasSerialized()) return;

        List<RealSpace> spaces = IOUtils.loadVariants(getFileNameForRead());

        int i = 0;
        List<RealSpace> solutions = new LinkedList<>();
        for (RealSpace space : spaces) {
            if (size() < backlogLimit) {
                add(space);
                i++;
            } else {
                solutions.add(space);
            }
        }

        System.out.println("\n" + (spaces.size()-solutions.size())+"/"+size() + " get:" + i);

        if (solutions.size() > 0) {
//            executor.execute(() -> IOUtils.saveVariants(solutions, getFileNameForWrite()));
            IOUtils.saveVariants(solutions, getFileNameForWrite());
        }
    }

    /**
     * Показывает, есть ли сохранённые на диске объекты
     * @return true, если есть
     */
    private boolean hasSerialized() {
        if (files.size() > 0) {
            return true;
        }else{
            while (isSavingInProcess()){
            }
            return files.size() > 0;
        }
    }

    private boolean isSavingInProcess() {
        return processCount > 0;
    }


}
