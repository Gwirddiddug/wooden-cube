package org.kaa.storage;

import org.kaa.model.RealSpace;
import org.kaa.utils.IOUtils;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Gwirggiddug on 08.02.2015.
 */
public class Storage extends AbstractStorage<RealSpace> {

	protected RealSpace space;
	protected long backlogLimit;
	protected long serializationPackSize;
	private long fileIndex = 0;

	private ConcurrentSkipListSet<String> filesInProcess = new ConcurrentSkipListSet<>();
	private ConcurrentSkipListSet<String> files = new ConcurrentSkipListSet<>();

	private ExecutorService reader = Executors.newSingleThreadExecutor();
	private ExecutorService writer = Executors.newSingleThreadExecutor();
	private AtomicInteger processCount = new AtomicInteger(0);//количество активных процессов сохранения

	@Override
	public boolean add(RealSpace realSpace) {
        //		checkSize();
		return super.add(realSpace);
	}

	@Override
	public boolean addAll(Collection<RealSpace> units) {
        //		checkSize();
		return super.addAll(units);
	}

	@Override
	protected void checkSize() {
		if (size() > backlogLimit) {
			serialize();
		} else if (size() < backlogLimit - serializationPackSize) {
			deserialize();
		}
	}

	/**
	 * добавляет записанный файл в список досутпных
	 *
	 * @param fileName
	 */
	private synchronized void addFileName(String fileName) {
		if (filesInProcess.remove(fileName)) {
			files.add(fileName);
		}
	}

	/**
	 * генерирует имя файла с шестизначным числовым суффиксом
	 *
	 * @return имя файла сохранения вариантов
	 */
	private synchronized String getFileNameForWrite() {
		String index = String.valueOf(++fileIndex);
		while (index.length() < 6) {
			index = "0" + index;
		}

		String fileName = "solutions#" + index + ".page";
		filesInProcess.add(fileName);
		return fileName;
	}

	/**
	 * @return файл для загрузки вариантов
	 */
	private synchronized String getFileNameForRead() {
		if (!files.isEmpty()) {
			String file = files.first();
			files.remove(file);
			return file;
		}
		return null;
	}


	/**
	 * сохраняет избыточные варианты на диск
	 *
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private void serialize() {
		if (size() <= serializationPackSize) return;

		String fileForWrite = getFileNameForWrite();

		List<RealSpace> solutions = new LinkedList<>();
		while (solutions.size() < serializationPackSize && size() > 0) {
			RealSpace space = getLast();
			solutions.add(space);
		}

		processCount.incrementAndGet();
		Runnable write = () -> {
//            System.out.println("Start write:" + fileForWrite);
			IOUtils.saveVariantsOptimized(solutions, fileForWrite);
			addFileName(fileForWrite);
			processCount.decrementAndGet();
//            System.out.println("Stop write:" + fileForWrite);
		};
		writer.execute(write);
	}

	/**
	 * загружает варианты с диска
	 *
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private void deserialize() {
		if (!hasSerialized()) return;

		List<RealSpace> spaces = IOUtils.loadVariantsOptimized(getFileNameForRead(), this.space);
		addAll(spaces);

/*        List<RealSpace> solutions = new LinkedList<>();
        for (RealSpace space : spaces) {
            if (size() < backlogLimit) {
                add(space);
            } else {
                solutions.add(space);
            }
        }
//        System.out.println("\n" + (spaces.size() - solutions.size()) + "/" + size() + " get:" + i);
        if (solutions.size() > 0) {
            IOUtils.saveVariantsOptimized(solutions, getFileNameForWrite());
        }*/
	}

	/**
	 * Показывает, есть ли сохранённые на диске объекты
	 *
	 * @return true, если есть
	 */
	private boolean hasSerialized() {
		if (!files.isEmpty()) {
			return true;
		} else {
			while (isSavingInProcess()) {
				try {
					Thread.yield();
					Thread.sleep(500);
				} catch (InterruptedException e) {
					System.err.println("Waiting interrupted");
				}
			}
			return !files.isEmpty();
		}
	}

	private boolean isSavingInProcess() {
		return processCount.get() > 0;
	}
}
